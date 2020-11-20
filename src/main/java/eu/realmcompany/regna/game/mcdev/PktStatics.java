package eu.realmcompany.regna.game.mcdev;

import eu.realmcompany.regna.game.mcdev.network.RealmPlayerConnection;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftWolf;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PktStatics {

    /**
     * Set's player connection to realmconnection
     *
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
     *
     * @param player Player
     */
    public static RealmPlayerConnection getRealmConnection(@NotNull Player player) {
        return (RealmPlayerConnection) getNmsPlayer(player).playerConnection;
    }

    /**
     * Get nms player from Bukkit player
     *
     * @return Nms Player
     */
    public static @NotNull MinecraftServer getNmsServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }


    /**
     * Get nms player from Bukkit player
     *
     * @param player Bukkit player
     * @return Nms Player
     */
    public static @NotNull EntityPlayer getNmsPlayer(@NotNull Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Get nms player from Bukkit player
     *
     * @param player UUID of player
     * @return Nms Player
     */
    public static @Nullable EntityPlayer getNmsPlayer(@NotNull UUID player) {
        return getNmsServer().getPlayerList().getPlayer(player);
    }

    /**
     * Sends packet to all players
     *
     * @param pkt Packet to send
     */
    public static void sendPacketToAll(@NotNull Packet<PacketListenerPlayOut> pkt) {
        sendPacketToAll(pkt, Collections.emptyList());
    }

    /**
     * Sends packet to all players with filter
     *
     * @param pkt    Packet to send
     * @param except Vararg players to filter
     */
    public static void sendPacketToAll(@NotNull Packet<PacketListenerPlayOut> pkt, @NotNull UUID... except) {
        sendPacketToAll(pkt, Arrays.asList(except));
    }

    /**
     * Sends packet to all players with filter
     *
     * @param pkt    Packet to send
     * @param except List of players to filter
     */
    public static void sendPacketToAll(@NotNull Packet<PacketListenerPlayOut> pkt, @NotNull List<UUID> except) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !except.contains(player.getUniqueId()))
                .map(PktStatics::getNmsPlayer)
                .forEach(player -> {
                    player.playerConnection.sendPacket(pkt);
                });
    }

    public static @NotNull WorldServer getNmsWorldServer(@NotNull org.bukkit.World world) {
        return ((CraftWorld) world).getHandle();
    }
}
