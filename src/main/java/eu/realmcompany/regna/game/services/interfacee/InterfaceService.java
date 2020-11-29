package eu.realmcompany.regna.game.services.interfacee;

import eu.realmcompany.regna.abstraction.game.AGameService;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import eu.realmcompany.regna.game.services.RegnaServices;
import eu.realmcompany.regna.providers.storage.data.FriendlyData;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class InterfaceService extends AGameService {

    private String coordsFormat;
    private boolean coordsEnabled;

    private FriendlyData config;

    @Getter
    private List<UUID> disabledPlayer = new ArrayList<>();

    public InterfaceService(@NotNull RegnaServices services) {
        super(services);
        setServiceName("Interface Service");
    }

    @Override
    public void initialize() throws Exception {
        registerAsListener();

        this.config = this.getKaryon().getStorage().provideYaml("configurations/services/interface.yaml", true).getData();

        if (this.config.getBool("root.action-bar-coords.enabled", false)) {
            this.coordsEnabled = true;
            this.coordsFormat = this.config.getString("root.action-bar-coords.format");
        }

        Bukkit.getServer().getCommandMap().register("regna", new Command("coordinates") {
            {
                setAliases(Arrays.asList("coords", "position"));
            }

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
                if (!(sender instanceof Player))
                    return true;
                var player = ((Player) sender);

                if (!disabledPlayer.remove(player.getUniqueId())) {
                    sender.sendMessage("§7Coordinates disabled.");
                    disabledPlayer.add(player.getUniqueId());
                } else
                    sender.sendMessage("§7Coordinates enabled.");
                return true;
            }
        });
    }

    @Override
    public void terminate() throws Exception {

    }

    @EventHandler
    public void onPlayerMove(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final EntityPlayer nmsPlayer = PktStatics.getNmsPlayer(player);

        Executors.newSingleThreadExecutor().submit(() -> {
            while (player.isOnline()) {
                if(player.isDead())
                    return;
                if (!coordsEnabled)
                    break;
                if (this.disabledPlayer.contains(event.getPlayer().getUniqueId()))
                    break;
                final var pos = player.getLocation().toVector();

                String jsonFormat = ChatColor.translateAlternateColorCodes('&', this.coordsFormat);
                jsonFormat = jsonFormat.replaceAll("\\{coords_x}", pos.getBlockX() + "")
                        .replaceAll("\\{coords_y}", pos.getBlockY() + "")
                        .replaceAll("\\{coords_z}", pos.getBlockZ() + "");

                var coords =
                        new PacketPlayOutTitle(
                                PacketPlayOutTitle.EnumTitleAction.ACTIONBAR,
                                IChatBaseComponent.ChatSerializer.jsonToComponent(jsonFormat), 0, Integer.MAX_VALUE, 0);

                nmsPlayer.playerConnection.sendPacket(coords);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
