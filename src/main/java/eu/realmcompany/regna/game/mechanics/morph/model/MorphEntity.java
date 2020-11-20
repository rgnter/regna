package eu.realmcompany.regna.game.mechanics.morph.model;

import eu.realmcompany.regna.game.mcdev.PktStatics;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MorphEntity {

    @Getter
    private final UUID         owner;
    @Getter
    private final EntityInsentient morph;

    private final List<UUID> shownTo = new ArrayList<>();

    @Getter
    private int maxDistance = 50;

    /**
     * Creates morph entity
     * @param owner Owner of morph
     * @param morph Morph
     */
    public MorphEntity(@NotNull UUID owner, @NotNull EntityInsentient morph) {
        this.owner = owner;
        this.morph = morph;

    }

    public void showFor(@NotNull Player player) {
        EntityPlayer owner = PktStatics.getNmsPlayer(getOwner());

        EntityPlayer nmsPlayer = PktStatics.getNmsPlayer(player);
        nmsPlayer.playerConnection.sendPacket(getCreatePacket());
        nmsPlayer.c((Entity) owner);
    }

    public void hideFor(@NotNull Player player) {
        EntityPlayer owner = PktStatics.getNmsPlayer(getOwner());

        EntityPlayer nmsPlayer = PktStatics.getNmsPlayer(player);
        nmsPlayer.playerConnection.sendPacket(getDestroyPacket());
        nmsPlayer.playerConnection.sendPacket(owner.P());
    }

    /**
     * Handles movement of this Morph
     * @param from From
     * @param to   To
     */
    public void handleMovement(Vector from, Vector to, float yaw, float pitch, float headRotation, boolean isOnGround) {
        short netX = (short) ((to.getX() * 32 - from.getX() * 32) * 128);
        short netY = (short) ((to.getY() * 32 - from.getY() * 32) * 128);
        short netZ = (short) ((to.getZ() * 32 - from.getZ() * 32) * 128);
        byte netYaw  = (byte) ((int) (yaw * 256.0F / 360.0F));
        byte netPitch  = (byte) ((int) (pitch * 256.0F / 360.0F));
        byte headRot  = (byte) ((int) (headRotation * 256.0F / 360.0F));
        this.morph.setPositionRotation(to.getX(), to.getY(), to.getZ(), yaw, pitch);


        var movPkt = getMovementPacket(netX, netY, netZ, netYaw, netPitch, isOnGround);
        var headPkt = new PacketPlayOutEntityHeadRotation(this.morph, headRot);

        PktStatics.sendPacketToAll(movPkt, owner);
        PktStatics.sendPacketToAll(headPkt, owner);
    }

    /**
     * Handles teleport of this Morph
     * @param location Location
     */
    public void handleTeleport(Location location) {
        this.handleTeleport(location.toVector(), location.getYaw(), location.getPitch(), location.getWorld());
    }

    /**
     * Handles teleport of this Morph
     * @param location Location
     */
    public void handleTeleport(Vector location, float yaw, float pitch, World world) {
        this.morph.setPositionRotation(location.getX(), location.getY(), location.getZ(), yaw, pitch);
        this.morph.world = PktStatics.getNmsWorldServer(world);

        PktStatics.sendPacketToAll(new PacketPlayOutEntityTeleport(this.morph), owner);
    }

    /**
     * Handles metadata update of this Morph
     */
    public void handleUpdate(){
        PktStatics.sendPacketToAll(getUpdatePacket(), owner);
    }

    /**
     * @return Spawn packet of this morph
     */
    public @NotNull PacketPlayOutSpawnEntityLiving getCreatePacket() {
        return (PacketPlayOutSpawnEntityLiving) this.morph.P();
    }

    /**
     * @return Spawn packet of this morph
     */
    public @NotNull PacketPlayOutEntityDestroy getDestroyPacket() {
        return new PacketPlayOutEntityDestroy(this.morph.getId());
    }

    /**
     * @return Spawn packet of this morph
     */
    public @NotNull PacketPlayOutEntityMetadata getUpdatePacket() {
        return new PacketPlayOutEntityMetadata(this.morph.getId(), this.morph.getDataWatcher(), false);
    }

    /**
     * @return Spawn packet of this morph
     */
    public @NotNull PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook getMovementPacket(short netX, short netY, short netZ, byte netYaw, byte netPitch, boolean isOnGround) {
        return new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(this.morph.getId(), netX, netY, netZ, netYaw, netPitch, isOnGround);
    }
}
