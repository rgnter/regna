package eu.realmcompany.regna.game.services.admin;

import eu.realmcompany.regna.abstraction.game.AGameService;
import eu.realmcompany.regna.game.services.RegnaServices;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.PlayerInteractManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AdminGameService extends AGameService {

    /**
     * Default constructor
     *
     * @param services Service instance
     */
    public AdminGameService(@NotNull RegnaServices services) {
        super(services);
        setServiceName("AdminService");
    }

    @Override
    public void initialize() throws Exception {
        Bukkit.getServer().getCommandMap().register("regna", new Command("brand") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if (!commandSender.hasPermission("regna.admin")) {
                    commandSender.sendMessage("§cNie.");
                    return true;
                }

                if (args.length == 0) {
                    if (commandSender instanceof Player) {
                        commandSender.sendMessage("§7Your brand is: §f" + ((Player) commandSender).getClientBrandName());
                    } else
                        commandSender.sendMessage("§cSpecify player.");
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if (player == null) {
                        commandSender.sendMessage("§cInvalid player.");
                        return true;
                    } else {
                        commandSender.sendMessage("§e" + player.getName() + "§7's brand is: §f" + player.getClientBrandName());
                    }
                }
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                if (!sender.hasPermission("regna.admin"))
                    return Collections.emptyList();

                if (args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("uuid") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if (!commandSender.hasPermission("regna.admin")) {
                    commandSender.sendMessage("§cNie.");
                    return true;
                }

                if (args.length == 0) {
                    if (commandSender instanceof Player) {
                        commandSender.sendMessage("§7Your uuid is: §f" + ((Player) commandSender).getUniqueId());
                    } else
                        commandSender.sendMessage("§cSpecify player.");
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if (player == null) {
                        commandSender.sendMessage("§cInvalid player.");
                        return true;
                    } else {
                        commandSender.sendMessage("§e" + player.getName() + "§7's uuid is: §f" + player.getUniqueId());
                    }
                }
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                if (!sender.hasPermission("regna.admin"))
                    return Collections.emptyList();

                if (args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("invsee") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if (!commandSender.hasPermission("regna.admin")) {
                    commandSender.sendMessage("§cNie.");
                    return true;
                }

                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("§cMust be player.");
                    return true;
                }
                if (args.length == 0) {
                    commandSender.sendMessage("§cSpecify player.");
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if (player == null) {
                        commandSender.sendMessage("§cInvalid player.");
                        return true;
                    } else {
                        commandSender.sendMessage("§7Opening " + "§e" + player.getName() + "§7's inventory.");
                        ((Player) commandSender).openInventory(player.getInventory());
                    }
                }
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                if (!sender.hasPermission("regna.admin"))
                    return Collections.emptyList();

                if (args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("ip") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if (!commandSender.hasPermission("regna.admin")) {
                    commandSender.sendMessage("§cNie.");
                    return true;
                }

                if (args.length == 0) {
                    if (commandSender instanceof Player) {
                        InetSocketAddress addr = ((CraftPlayer) commandSender).getAddress();
                        if (addr != null)
                            commandSender.sendMessage("§7Your ip is: §e" + addr.getHostString() + ":" + addr.getPort());
                        else
                            commandSender.sendMessage("§cHow the fuck did you manage to fuck this up.");
                    } else
                        commandSender.sendMessage("§cSpecify player.");
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if (player == null) {
                        commandSender.sendMessage("§cInvalid player.");
                        return true;
                    } else {
                        commandSender.sendMessage("§7Opening " + "§e" + player.getName() + "§7's inventory.");
                        ((Player) commandSender).openInventory(player.getInventory());
                    }
                }
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                if (!sender.hasPermission("regna.admin"))
                    return Collections.emptyList();

                if (args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("ping") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage("Pings of all players: ");
                    Bukkit.getOnlinePlayers().stream().map(player -> ((CraftPlayer) player).getHandle()).forEach(player -> {
                        commandSender.sendMessage("§e" + player.getName() + "'s §7ping is: §a" + player.ping);
                    });
                    return true;
                }

                if (args.length == 0) {
                    commandSender.sendMessage("§7Your ping is: §a" + (((CraftPlayer) commandSender).getHandle().ping));
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if (player == null) {
                        commandSender.sendMessage("§cInvalid player.");
                        return true;
                    } else {
                        commandSender.sendMessage("§e" + target + "'s §7ping is: §a" + (((CraftPlayer) player).getHandle().ping));
                    }
                }
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                if (args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });


        Bukkit.getServer().getCommandMap().register("regna", new Command("test") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {

                EntityPlayer me = ((CraftPlayer) commandSender).getHandle();

                NBTTagCompound data = new NBTTagCompound();
                me.save(data);
                EntityPlayer player = new EntityPlayer(me.server, me.getWorldServer(), me.getProfile(), new PlayerInteractManager(me.getWorldServer()));


                System.out.println(player.getPositionVector());
                me.server.getPlayerList().sendAll(player.P());

                return true;
            }
        });
    }

    @Override
    public void terminate() throws Exception {

    }
}
