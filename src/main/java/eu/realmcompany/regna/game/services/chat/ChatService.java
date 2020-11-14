package eu.realmcompany.regna.game.services.chat;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import eu.realmcompany.regna.abstraction.game.AGameService;
import eu.realmcompany.regna.game.services.RegnaServices;
import eu.realmcompany.regna.providers.storage.data.FriendlyData;
import eu.realmcompany.regna.providers.storage.store.AStore;
import eu.realmcompany.regna.statics.PapiStatics;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import org.apache.commons.text.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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

    private boolean emotesEnabled = true;
    private Permission emotesPermission = null;

    private boolean pingEnabled = true;
    private String pingIndicator = "@";
    private String pingColor = "&e";
    private Permission pingPermission = null;
    private Sound pingSound = Sound.BLOCK_NOTE_BLOCK_BELL;
    private float pingVolume = 1f;
    private float pingPitch = 1f;

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
        this.chatFormat = this.config.getString("root.chat.format", "{\"text\":\"§7{player_name} §8\\u2022 §f{message}\"}");

        // emotes
        this.emotesEnabled = this.config.getBool("root.emotes.enabled", true);
        if (this.emotesEnabled) {
            if (this.config.isSet("root.emotes.permission")) {
                this.emotesPermission = new Permission(this.config.getString("root.emotes.permission", "this_will_never_happen"));
                log.debug("Permission for emotes: " + this.emotesPermission.getName());
            }

            String defaultEmojiIndicator = this.config.getString("root.emotes.indicator", "\\");
            for (String configName : this.config.getKeys("root.emotes.listed")) {
                String key = this.config.getString("root.emotes.listed." + configName + ".key");
                String emote = this.config.getString("root.emotes.listed." + configName + ".emote");

                // permission
                Permission perm = null;
                if (this.config.isSet("root.emotes.listed." + configName + ".permission"))
                    perm = new Permission(this.config.getString("root.emotes.listed." + configName + ".permission", "this_will_never_happen"));

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

        // ping
        this.pingEnabled = this.config.getBool("root.ping.enabled", true);
        if (this.pingEnabled) {
            if (this.config.isSet("root.ping.permission")) {
                this.pingPermission = new Permission(this.config.getString("root.ping.permission", "this_will_never_happen"));
                log.debug("Permission for pings: " + this.emotesPermission.getName());
            }
            this.pingIndicator = this.config.getString("root.ping.indicator", "@");
            this.pingColor = this.config.getString("root.ping.color", "&a");

            try {
                this.pingSound = Sound.valueOf(this.config.getString("root.ping.sound.name", "BLOCK_NOTE_BLOCK_BELL").toUpperCase());
            } catch (Exception x) {
                log.error("Couldn't parse SOUND for ping. Using default.");
            }
            this.pingVolume = this.config.getFloat("root.ping.sound.volume", 1f);
            this.pingPitch = this.config.getFloat("root.ping.sound.pitch", 1f);

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
        if (pingEnabled)
            if (pingPermission == null || sender.hasPermission(pingPermission))
                for (Player player : getOnlinePlayers()) {
                    if (message.toLowerCase().contains(pingIndicator + player.getName().toLowerCase())) {
                        message = message.replaceAll("(?i)" + pingIndicator + player.getName(), pingColor + pingIndicator + playerName + "&r");
                        player.playSound(player.getLocation(), pingSound, pingVolume, pingPitch);
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
            formatted = PapiStatics.askPapiForPlaceholders(formatted, sender);

            try {
                JsonElement element = new JsonParser().parse(formatted);

                PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(element), ChatMessageType.CHAT, sender.getUniqueId());
                Bukkit.getOnlinePlayers().stream().map(player -> ((CraftPlayer) player)).forEach(player -> player.getHandle().playerConnection.sendPacket(packet));
                Bukkit.getConsoleSender().sendMessage(String.format("§8(§7%s§8) §7%s §8-§f %s", sender.getClientBrandName(), playerName, StringEscapeUtils.unescapeJava(message)));
            } catch (JsonSyntaxException x) {
                log.error("Couldn't parse chat", x);
            }
        } catch (Exception x) {
            log.error("Couldn't process chat", x);
        }
    }
}
