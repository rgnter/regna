package eu.realmcompany.regna.services.admin;

import eu.realmcompany.regna.abstraction.AService;
import eu.realmcompany.regna.abstraction.Service;
import eu.realmcompany.regna.services.RegnaServices;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("Admin")
public class AdminService extends AService {

    public AdminService(@NotNull RegnaServices services) {
        super(services);
    }

    @Override
    public void initialize() throws Exception {
        Bukkit.getServer().getCommandMap().register("regna", new Command("brand") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if(args.length == 0) {
                    if(commandSender instanceof Player) {
                        commandSender.sendMessage("§7Your brand is: §f" + ((Player) commandSender).getClientBrandName());
                    } else
                        commandSender.sendMessage("§cSpecify player.");
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if(player == null) {
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
                if(args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("uuid") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if(args.length == 0) {
                    if(commandSender instanceof Player) {
                        commandSender.sendMessage("§7Your uuid is: §f" + ((Player) commandSender).getUniqueId());
                    } else
                        commandSender.sendMessage("§cSpecify player.");
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if(player == null) {
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
                if(args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("invsee") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if(!(commandSender instanceof Player)) {
                    commandSender.sendMessage("§cMust be player.");
                    return true;
                }
                if(args.length == 0) {
                    commandSender.sendMessage("§cSpecify player.");
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if(player == null) {
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
                if(args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("ping") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                if(!(commandSender instanceof Player)) {
                    commandSender.sendMessage("Pings of all players: ");
                    Bukkit.getOnlinePlayers().stream().map(player-> ((CraftPlayer) player).getHandle()).forEach(player -> {
                        commandSender.sendMessage("§e"+ player.getName() + "'s §7ping is: §a" + player.ping);
                    });
                    return true;
                }

                if(args.length == 0) {
                    commandSender.sendMessage("§7Your ping is: §a" + (((CraftPlayer) commandSender).getHandle().ping));
                    return true;
                } else {
                    String target = args[0];
                    Player player = Bukkit.getPlayer(target);
                    if(player == null) {
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
                if(args.length > 0) {
                    String name = args[0].toLowerCase();
                    return Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().toLowerCase().startsWith(name)).map(Player::getName).collect(Collectors.toList());
                }
                return Collections.emptyList();
            }
        });
    }

    @Override
    public void terminate() throws Exception {

    }
}
