package eu.realmcompany.regna.services.chat;

import eu.realmcompany.regna.abstraction.services.AService;
import eu.realmcompany.regna.abstraction.services.Service;
import eu.realmcompany.regna.services.RegnaServices;
import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.*;

@Service("Chat")
public class ChatService extends AService {

    /**
     * Super constructor
     * @param services Services instance
     */
    public ChatService(@NotNull RegnaServices services) {
        super(services);
    }

    @Override
    public void initialize() throws Exception {
        registerAsListener();
    }

    @Override
    public void terminate() throws Exception {

    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        String jsonFormat = "{\"text\":\"%s%s %s\\u2022 %s%s\"}";

        String playerName = event.getPlayer().getName();

        String message = event.getMessage();
        for(Player player : getOnlinePlayers()) {
            if(message.toLowerCase().contains("@" + player.getName().toLowerCase())) {
                message = message.replaceAll("(?i)@" + player.getName(), "§e@" + StringEscapeUtils.escapeJava(player.getName()) + "§r");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
            }
        }

        message = message.replaceAll("\\\\what\\\\", "( •_•)");
        message = message.replaceAll("\\\\ohyeah\\\\", "(￢‿￢ )");
        message = message.replaceAll("\\\\yaay\\\\", "＼(￣▽￣)／");
        message = message.replaceAll("\\\\blush\\\\", "(≧ω≦)");

        message = message.replaceAll("o/", "( ﾟ◡ﾟ)/");
        message = StringEscapeUtils.escapeJava(message);

        String formatted = String.format(jsonFormat, "§7", playerName, "§8", "§f", ChatColor.translateAlternateColorCodes('&', message));

        try {
            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.jsonToComponent(formatted), ChatMessageType.CHAT, event.getPlayer().getUniqueId());
            System.out.printf("§8(§7%s§8) §7%s §8-§f %s\n", event.getPlayer().getClientBrandName(), playerName, StringEscapeUtils.unescapeJava(message));
            Bukkit.getOnlinePlayers().stream().map(player -> ((CraftPlayer) player)).forEach(player -> player.getHandle().playerConnection.sendPacket(packet));

        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
