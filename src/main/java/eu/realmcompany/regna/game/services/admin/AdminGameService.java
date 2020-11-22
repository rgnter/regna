package eu.realmcompany.regna.game.services.admin;

import eu.realmcompany.regna.abstraction.game.AGameService;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import eu.realmcompany.regna.game.mechanics.magic.morph.model.MorphEntity;
import eu.realmcompany.regna.game.services.RegnaServices;
import net.minecraft.server.v1_16_R3.*;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Arrays;
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

        Bukkit.getServer().getCommandMap().register("regna", new Command("gamemode") {
            {
                setAliases(Arrays.asList("gm", "gmc", "gms", "gma", "gmsp"));
            }
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
                if(!sender.hasPermission("regna.gamemode")) {
                    sender.sendMessage("§cNie.");
                    return true;
                }

                GameMode mode = null;

                if (alias.equalsIgnoreCase("gmc")) mode = GameMode.CREATIVE;
                else if (alias.equalsIgnoreCase("gms")) mode = GameMode.SURVIVAL;
                else if (alias.equalsIgnoreCase("gma")) mode = GameMode.ADVENTURE;
                else if (alias.equalsIgnoreCase("gmsp")) mode = GameMode.SPECTATOR;

                // read from arguments if already not specified
                if(mode == null)
                if (args.length > 0) {
                        try {
                            String modeName = args[0].toUpperCase();
                            args = (String[]) ArrayUtils.remove(args, 0);

                            // maybe its shortcut? try
                            switch (modeName) {
                                case "C":
                                case "1":
                                case "CREATIVE":
                                    mode = GameMode.CREATIVE;
                                    break;
                                case "A":
                                case "2":
                                case "ADVENTURE":
                                    mode = GameMode.ADVENTURE;
                                    break;
                                case "S":
                                case "0":
                                case "SURVIVAL":
                                    mode = GameMode.SURVIVAL;
                                    break;
                                case "SP":
                                case "3":
                                case "SPECTATOR":
                                case "SPEC":
                                    mode = GameMode.SPECTATOR;
                                    break;
                            }

                            // well what if not
                            if (mode == null)
                                mode = GameMode.valueOf(modeName);

                        } catch (IllegalArgumentException x) {
                            sender.sendMessage("§c# §fUnknown Gamemode '" + args[0] + "'");
                            return true;
                        }
                }

                if (mode == null) {
                    sender.sendMessage("§c# §fYou must specify gamemode");
                    return true;

                }

                if(args.length > 0) {
                    String targetName = args[0];
                    Player target = Bukkit.getPlayer(targetName);

                    if(!sender.hasPermission("regna.gamemode.others")) {
                        sender.sendMessage("§c# §fYou don't have enough permissions.");
                        return true;
                    }

                    if(target != null) {

                        if (target.getGameMode().equals(mode)) {
                            sender.sendMessage("§c# §fThey already have this gamemode! Chumaj:D");
                            return true;
                        }

                        Bukkit.getConsoleSender().sendMessage("§8{§fAdmin§8} §7Player '§a" + sender.getName() + "§r' has changed §a" + targetName + "'s§r gamemode to '§e" + mode.name().toLowerCase() + "§r' from '§e" + target.getGameMode().name().toLowerCase() + "§r'");
                        target.setGameMode(mode);
                        sender.sendMessage("§a# §f Gamemode of §e" + targetName + "§r changed to '§e" + mode.name().toLowerCase() + "§r'");
                        return true;
                    } else {
                        sender.sendMessage("§c# §fInvalid player!");
                        return true;
                    }
                }
                Player p = (Player) sender;

                if (p.getGameMode().equals(mode)) {
                    sender.sendMessage("§c# §fYou already are in this gamemode! Chumaj:D");
                    return true;
                }

                Bukkit.getConsoleSender().sendMessage("§8{§fAdmin§8}§7 Player '§e" + p.getName() + "§r' has changed his gamemode to '§e" + mode.name().toLowerCase() + "§r' from '§e" + p.getGameMode().name().toLowerCase() + "§r'");
                p.setGameMode(mode);
                sender.sendMessage("§a# §fGamemode changed to '§e" + mode.name().toLowerCase() + "§f'");

                return true;
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("posemymorph") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                Player executor = ((Player) commandSender);
                EntityPlayer nmsPlayer = PktStatics.getNmsPlayer(executor);
                EntityPose pose = null;
                try {
                   pose = EntityPose.valueOf(args[0].toUpperCase());
                } catch (Exception x) {
                    commandSender.sendMessage("§cBad pose");
                }
                if(pose == null)
                    return true;

                MorphEntity morphed = getKaryon().getRegna().getMechanics().getMorph().getMorphedPlayers().get(executor.getUniqueId());
                morphed.getMorph().setPose(pose);
                morphed.handleUpdate();
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                return Arrays.stream(EntityPose.values()).map(EntityPose::name).collect(Collectors.toList());
            }
        });

        Bukkit.getServer().getCommandMap().register("regna", new Command("test") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                getKaryon().getRegna().getMechanics().getMorph().demorphPlayer(((Player) commandSender));
                return true;
            }
        });


    }

    @Override
    public void terminate() throws Exception {

    }
}
