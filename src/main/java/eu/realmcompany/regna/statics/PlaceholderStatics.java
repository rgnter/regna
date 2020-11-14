package eu.realmcompany.regna.statics;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Placeholder API Statics
 */
public class PlaceholderStatics {

    /**
     *
     * @param message Message to set placeholders
     * @param player  Player
     * @return If PAPI is present, returns message with set placeholders, otherwise unchanged message will be returned
     */
    public static @NotNull String askPapiForPlaceholders(@NotNull String message, @NotNull Player player) {
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }



}
