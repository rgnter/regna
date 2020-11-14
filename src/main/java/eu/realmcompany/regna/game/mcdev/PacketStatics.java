package eu.realmcompany.regna.game.mcdev;

import eu.realmcompany.regna.game.mcdev.network.RealmPlayerConnection;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PacketStatics {

    /**
     * Set's player connection to realmconnection
     * @param player Player
     */
    public static void setRealmConnection(@NotNull Player player) {
        EntityPlayer nmsPlayer = getNmsPlayer(player);

        nmsPlayer.playerConnection.networkManager.setPacketListener(new RealmPlayerConnection(
                nmsPlayer.server,
                nmsPlayer.playerConnection.networkManager,
                nmsPlayer));
    }
    /**
     * Set's player connection to realmconnection
     * @param player Player
     */
    public static RealmPlayerConnection getRealmConnection(@NotNull Player player) {
        return (RealmPlayerConnection) getNmsPlayer(player).playerConnection;
    }

    /**
     * Get nms player from Bukkit player
     * @param player Bukkit player
     * @return Nms Player
     */
    public static @NotNull EntityPlayer getNmsPlayer(@NotNull Player player) {
        return  ((CraftPlayer) player).getHandle();
    }


}
