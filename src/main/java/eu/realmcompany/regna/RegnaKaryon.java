package eu.realmcompany.regna;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.miniatures.MiniatureAPI;
import xyz.rgnt.miniatures.model.Miniature;
import xyz.rgnt.recrd.Logger;

public class RegnaKaryon extends JavaPlugin {

    public Logger logger;
    {
        logger = Logger.Builder.make().withPrefix("Regna").build();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.logger.info("Loading {0}", "Karyon");

        Bukkit.getServer().getPluginManager().addPermission(new Permission("karyon.admin", PermissionDefault.OP));
        Bukkit.getServer().getCommandMap().register("realm", new Command("miniature") {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
                if(!sender.hasPermission("karyon.admin")) {
                    return true;
                }
                Player player = (Player) sender;
                World world = player.getWorld();

                Miniature.Builder builder = MiniatureAPI.areaToMiniature(world, player.getLocation().toVector(),
                        player.getLocation().toVector().add(new Vector(-10f, -10f, -10f)), new Miniature.Options(){{
                            randomizeWater = true;
                            randomizeLava = true;
                        }});

                Miniature miniature = builder.get();
                sender.sendMessage("§aminiature done");

                miniature.getPositions().forEach((index, part) -> {
                    Vector location = part.getCalculatedPos().add(player.getLocation().toVector());
                    System.out.println(part.getCalculatedPos());
                    sender.sendMessage("§7" + location.toBlockVector());
                    Block block = world.getBlockAt(new Location(world, location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                    block.setType(part.getMaterial());
                });

                return true;
            }
        });
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public @NotNull Logger logger() {
        return logger;
    }

}
