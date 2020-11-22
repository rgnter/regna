package eu.realmcompany.regna.game.mechanics.magic.morph.model;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.mojang.datafixers.util.Pair;
import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MorphEntity {

    @Getter
    private final RegnaKaryon      karyon;

    @Getter
    private final UUID             ownerUniqueId;
    @Getter
    private final EntityPlayer     ownerPlayer;

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
    public MorphEntity(@NotNull UUID owner, @NotNull EntityPlayer ownerPlayer, @NotNull EntityInsentient morph, @NotNull RegnaKaryon karyon) {
        this.ownerUniqueId = owner;
        this.ownerPlayer   = ownerPlayer;
        this.morph = morph;
        this.karyon = karyon;

    }

    public void showFor(@NotNull Player player) {
        EntityPlayer nmsPlayer = PktStatics.getNmsPlayer(player);
        nmsPlayer.playerConnection.sendPacket(getCreatePacket());
        nmsPlayer.c((Entity) getOwnerPlayer());
        PktStatics.untrackPlayerFor(nmsPlayer, getOwnerPlayer());
    }

    public void hideFor(@NotNull Player player) {
        EntityPlayer nmsPlayer = PktStatics.getNmsPlayer(player);
        nmsPlayer.playerConnection.sendPacket(getDestroyPacket());
        PktStatics.trackPlayerFor(nmsPlayer, getOwnerPlayer());
        nmsPlayer.playerConnection.sendPacket(getOwnerPlayer().P());
    }

    public void syncEquipment() {
        handleHandItems(this.ownerPlayer.getItemInMainHand(), this.ownerPlayer.getItemInOffHand());
        handleArmorItems(EnumItemSlot.HEAD, this.ownerPlayer.getEquipment(EnumItemSlot.HEAD));
        handleArmorItems(EnumItemSlot.CHEST, this.ownerPlayer.getEquipment(EnumItemSlot.CHEST));
        handleArmorItems(EnumItemSlot.LEGS, this.ownerPlayer.getEquipment(EnumItemSlot.LEGS));
        handleArmorItems(EnumItemSlot.FEET, this.ownerPlayer.getEquipment(EnumItemSlot.FEET));
    }

    public void syncPosition(@NotNull Vector vector, float yaw, float pitch, float headRot) {
        this.morph.setPositionRotation(vector.getX(), vector.getY(), vector.getZ(), yaw, pitch);
        this.morph.setHeadRotation(headRot);
    }

    public void syncPosition(@NotNull Location location, float headRot) {
        Vector vector = location.toVector();
        syncPosition(vector, location.getYaw(), location.getPitch(), headRot);
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
        syncPosition(to, yaw, pitch, headRot);


        var movPkt = getMovementPacket(netX, netY, netZ, netYaw, netPitch, isOnGround);
        var headPkt = new PacketPlayOutEntityHeadRotation(this.morph, headRot);

        PktStatics.sendPacketToAll(movPkt, ownerUniqueId);
        PktStatics.sendPacketToAll(headPkt, ownerUniqueId);
    }

    /**
     * Handles teleport of this Morph
     * @param location Location
     */
    public void handleTeleport(Location location, float headRot) {
        this.handleTeleport(location.toVector(), location.getYaw(), location.getPitch(), headRot, location.getWorld());
    }

    /**
     * Handles teleport of this Morph
     * @param location Location
     */
    public void handleTeleport(Vector location, float yaw, float pitch, float headRot, World world) {
        this.morph.world = PktStatics.getNmsWorldServer(world);
        syncPosition(location, yaw, pitch, headRot);

        PktStatics.sendPacketToAll(new PacketPlayOutEntityTeleport(this.morph), ownerUniqueId);
    }

    public void handleArmorItems(PlayerArmorChangeEvent.SlotType slot, @NotNull org.bukkit.inventory.ItemStack item) {
        this.handleArmorItems(EnumItemSlot.fromName(slot.name().toLowerCase()), CraftItemStack.asNMSCopy(item));
    }

    public void handleArmorItems(@NotNull EnumItemSlot slot, @NotNull ItemStack item) {
        var armor = new ArrayList<Pair<EnumItemSlot, ItemStack>>();

        armor.add(Pair.of(slot, item));

        PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment(getMorph().getId(), armor);
        PktStatics.sendPacketToAll(entityEquipment, ownerUniqueId);
    }

    public void handleHandItems(@NotNull org.bukkit.inventory.ItemStack mainhand, org.bukkit.inventory.ItemStack offhand) {
        this.handleHandItems(CraftItemStack.asNMSCopy(mainhand),CraftItemStack.asNMSCopy(offhand));
    }

    public void handleHandItems(@NotNull ItemStack mainHand, @NotNull ItemStack offHand) {
        var equipment = new ArrayList<Pair<EnumItemSlot, ItemStack>>();

        equipment.add(Pair.of(EnumItemSlot.MAINHAND, mainHand));
        equipment.add(Pair.of(EnumItemSlot.OFFHAND, offHand));
        PacketPlayOutEntityEquipment entityEquipment = new PacketPlayOutEntityEquipment(getMorph().getId(), equipment);
        PktStatics.sendPacketToAll(entityEquipment, ownerUniqueId);
    }

    /**
     * Handles metadata update of this Morph
     */
    public void handleUpdate(){
        PktStatics.sendPacketToAll(getUpdatePacket(), ownerUniqueId);
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
