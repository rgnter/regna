package eu.realmcompany.regna.game.services.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import eu.realmcompany.regna.abstraction.game.AGameService;
import eu.realmcompany.regna.game.services.RegnaServices;
import eu.realmcompany.regna.providers.storage.data.FriendlyData;
import eu.realmcompany.regna.providers.storage.store.AStore;
import eu.realmcompany.regna.statics.PlaceholderStatics;
import eu.realmcompany.regna.statics.PermissionStatics;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.apache.commons.text.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.mth.tuples.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import static org.bukkit.Bukkit.getOnlinePlayers;

@Log4j2(topic = "Regna::ChatService")
public class ChatService extends AGameService {

    private AStore configStore;
    @Getter
    private FriendlyData config = FriendlyData.fromEmptyYaml();

    private String chatFormat;
    private String deathFormat;
    private String joinFormat;
    private String quitFormat;

    private boolean emotesEnabled = true;
    private Permission emotesPermission = null;

    private boolean mentionEnabled = true;
    private String mentionIndicator = "@";
    private String mentionColor = "&e";
    private Permission mentionPermission = null;
    private Sound mentionSound = Sound.BLOCK_NOTE_BLOCK_BELL;
    private float mentionVolume = 1f;
    private float mentionPitch = 1f;

    private final Map<String, Pair<String, Permission>> emotes = new HashMap<>();

    /**
     * Super constructor
     *
     * @param services Services instance
     */
    public ChatService(@NotNull RegnaServices services) {
        super(services);
        setServiceName("ChatService");
    }

    @Override
    public void initialize() throws Exception {
        registerAsListener();

        this.configStore = getKaryon().getStorage().provideYaml("configurations/services/chat.yaml", true);
        this.config = this.configStore.getData();

        loadConfiguration();
    }

    @Override
    public void terminate() throws Exception {

    }

    /**
     * Loads emotes from config
     */
    protected void loadConfiguration() {
        this.chatFormat  = this.config.getString("root.chat.message-format", "{\"text\":\"§7{player_name} §8\\u2022 §f{message}\"}");
        this.deathFormat = this.config.getString("root.chat.death-format", "{\"text\":\"&c\\u2620 &7{message}\"}");
        //
        this.joinFormat  = this.config.getString("root.chat.join-format", "{\"text\":\"&a+ §f{player_name}\"}");
        this.quitFormat = this.config.getString("root.chat.quit-format", "{\"text\":\"&c- &f{player_name}\"}");

        // emotes
        this.emotesEnabled = this.config.getBool("root.emotes.enabled", true);
        if (this.emotesEnabled) {
            this.emotesPermission = PermissionStatics.permissionFromString(this.config.getString("root.emotes.permission"));
            if (this.emotesPermission != null)
                log.debug("Permission for emotes: " + this.emotesPermission.getName());


            String defaultEmojiIndicator = this.config.getString("root.emotes.indicator", "\\");
            for (String configName : this.config.getKeys("root.emotes.listed")) {
                String key = this.config.getString("root.emotes.listed." + configName + ".key");
                String emote = this.config.getString("root.emotes.listed." + configName + ".emote");

                // permission
                Permission perm = PermissionStatics.permissionFromString(this.config.getString("root.emotes.listed." + configName + ".permission"));

                // override indicator if set
                String indicator = defaultEmojiIndicator;
                if (this.config.isSet("root.emotes.listed." + configName + ".indicator"))
                    indicator = this.config.getString("root.emotes.listed." + configName + ".indicator", "this_will_never_happen");

                if (key != null && emote != null)
                    this.emotes.put(indicator + key, Pair.of(emote, perm));
            }

            log.debug("Loaded {} emotes.", this.emotes.size());
        } else
            log.info("Emotes are disabled.");

        // mention
        this.mentionEnabled = this.config.getBool("root.mention.enabled", true);
        if (this.mentionEnabled) {
            this.mentionPermission = PermissionStatics.permissionFromString(this.config.getString("root.mention.permission", "this_will_never_happen"));
            if (this.mentionPermission != null)
                log.debug("Permission for mentions: " + this.mentionPermission.getName());

            this.mentionIndicator = this.config.getString("root.mention.indicator", "@");
            this.mentionColor = this.config.getString("root.mention.color", "&a");

            try {
                this.mentionSound = Sound.valueOf(this.config.getString("root.mention.sound.name", "BLOCK_NOTE_BLOCK_BELL").toUpperCase());
            } catch (Exception x) {
                log.error("Couldn't parse SOUND for mention. Using default.");
            }
            this.mentionVolume = this.config.getFloat("root.mention.sound.volume", 1f);
            this.mentionPitch = this.config.getFloat("root.mention.sound.pitch", 1f);

        } else
            log.info("Pings are disabled.");

    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player sender = event.getPlayer();
        String jsonFormat = ChatColor.translateAlternateColorCodes('&', this.chatFormat);
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();

        // PING
        if (mentionEnabled)
            if (mentionPermission == null || sender.hasPermission(mentionPermission))
                for (Player player : getOnlinePlayers()) {
                    if (message.toLowerCase().contains(mentionIndicator + player.getName().toLowerCase())) {
                        message = message.replaceAll("(?i)" + mentionIndicator + player.getName(), mentionColor + mentionIndicator +  player.getName().toLowerCase() + "&r");
                        player.playSound(player.getLocation(), mentionSound, mentionVolume, mentionPitch);
                    }
                }
        // EMOTES
        if (emotesEnabled)
            // check global emote permission
            if (emotesPermission == null || sender.hasPermission(emotesPermission))
                for (String key : this.emotes.keySet()) {
                    if (message.contains(key)) {
                        var data = this.emotes.get(key);

                        // check emote permission
                        if (data.getSecond() == null || sender.hasPermission(data.getSecond())) {
                            message = message.replaceAll(Matcher.quoteReplacement(key), data.getFirst());
                        }
                    }
                }
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = StringEscapeUtils.escapeJava(message);

        try {
            String formatted = jsonFormat.replaceAll("(?i)\\{message}", Matcher.quoteReplacement(message)).replaceAll("(?i)\\{player_name}", playerName);
            formatted = PlaceholderStatics.askPapiForPlaceholders(formatted, sender);

            try {
                JsonElement element = new JsonParser().parse(formatted);

                PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(element), ChatMessageType.CHAT, sender.getUniqueId());
                Bukkit.getOnlinePlayers().stream().map(player -> ((CraftPlayer) player)).forEach(player -> player.getHandle().playerConnection.sendPacket(packet));
                Bukkit.getConsoleSender().sendMessage(String.format("§8{§fChat§8} §8(§7%s§8) §7%s §8-§f %s", sender.getClientBrandName(), playerName, StringEscapeUtils.unescapeJava(message)));
            } catch (JsonSyntaxException x) {
                log.error("Couldn't parse chat", x);
            }
        } catch (Exception x) {
            log.error("Couldn't process chat", x);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player killed = event.getEntity();
        Location loc = killed.getLocation();
        String playerName = event.getEntity().getName();
        String message = event.getDeathMessage();
        if(message == null)
            message = "";

        String jsonFormat = ChatColor.translateAlternateColorCodes('&', this.deathFormat);

        String formatted = jsonFormat
                .replaceAll("(?i)\\{player_name}", playerName)
                .replaceAll("(?i)\\{message}", Matcher.quoteReplacement(message))
                .replaceAll("(?i)\\{x}\"", "" + loc.getBlockX())
                .replaceAll("(?i)\\{y}\"", "" + loc.getBlockY())
                .replaceAll("(?i)\\{z}\"", "" + loc.getBlockZ())
                .replaceAll("(?i)\\{lost_xp}\"", "" + event.getDroppedExp());
        formatted = PlaceholderStatics.askPapiForPlaceholders(formatted, killed);

        try {
            JsonElement element = new JsonParser().parse(formatted);
            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(element), ChatMessageType.CHAT, killed.getUniqueId());
            Bukkit.getOnlinePlayers().stream().map(player -> ((CraftPlayer) player)).forEach(player -> player.getHandle().playerConnection.sendPacket(packet));
            event.setDeathMessage("");
        } catch (Exception x ){
            log.error("Couldn't send death message", x);
        } finally {
            Bukkit.getConsoleSender().sendMessage(
                    String.format("§8{§4Death§8}§7 %s %s\n\t\t\t§7Lost xp §e%d§r, §7Death location §c%d §a%d §9%d§8(§cx§7,§ay§7,§9z§8)", message, killed.getKiller() != null ? "§f" + playerName + "§7 killed by §f" + killed.getKiller().getName() + "§r" : "",
                            event.getDroppedExp(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())
            );
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();
        String playerName = event.getPlayer().getName();

        String jsonFormat = ChatColor.translateAlternateColorCodes('&', this.joinFormat);
        String formatted = jsonFormat
                .replaceAll("(?i)\\{player_name}", playerName);
        formatted = PlaceholderStatics.askPapiForPlaceholders(formatted, joined);

        try {
            JsonElement element = new JsonParser().parse(formatted);
            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(element), ChatMessageType.CHAT, joined.getUniqueId());
            Bukkit.getOnlinePlayers().stream().map(player -> ((CraftPlayer) player)).forEach(player -> player.getHandle().playerConnection.sendPacket(packet));
        } catch (Exception x ){
            log.error("Couldn't send join message", x);
        } finally {
            Bukkit.getConsoleSender().sendMessage(
                    String.format("§8{§aJoin§8} §f%s", playerName)
            );
        }
        event.setJoinMessage("");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player quitter = event.getPlayer();
        String playerName = event.getPlayer().getName();

        String jsonFormat = ChatColor.translateAlternateColorCodes('&', this.quitFormat);
        String formatted = jsonFormat
                .replaceAll("(?i)\\{player_name}", playerName);
        formatted = PlaceholderStatics.askPapiForPlaceholders(formatted, quitter);

        try {
            JsonElement element = new JsonParser().parse(formatted);
            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(element), ChatMessageType.CHAT, quitter.getUniqueId());
            Bukkit.getOnlinePlayers().stream().map(player -> ((CraftPlayer) player)).forEach(player -> player.getHandle().playerConnection.sendPacket(packet));
        } catch (Exception x ){
            log.error("Couldn't send quit message", x);
        } finally {
            Bukkit.getConsoleSender().sendMessage(
                    String.format("§8{§cQuit§8} §f%s", playerName)
            );
        }
        event.setQuitMessage("");
    }


}
