package eu.realmcompany.regna.game.mechanics.magic.morph;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import eu.realmcompany.regna.abstraction.game.AGameMechanic;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import eu.realmcompany.regna.game.mechanics.magic.morph.model.MorphEntity;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2(topic = "Morph Mechanics")
public class MorphMechanics extends AGameMechanic {

    private final Map<UUID, MorphEntity> morphedPlayers = new HashMap<>();

    public MorphMechanics(@NotNull RegnaMechanics mechanics) {
        super(mechanics);

        setMechanicName("Morphing");
        setTickable(true);
    }

    @Override
    public void initialize() throws Exception {
        registerAsListener();
    }

    @Override
    public void terminate() throws Exception {

    }

    @Override
    public void tick() {

    }


    /**
     * Morphs player
     *
     * @param player Player to morph
     * @param type   Type of mob to morph
     * @return Success/Failure
     */
    public MorphEntity morphPlayer(@NotNull Player player, @NotNull String type) {
        if (this.morphedPlayers.containsKey(player.getUniqueId()))
            demorphPlayer(player);

        EntityPlayer nmsPlayer = PktStatics.getNmsPlayer(player);

        var opt = EntityTypes.getByName(type);
        if (opt.isEmpty()) {
            log.warn("Selected entity doesn't exist ({}).", type);
            return null;
        }
        Entity selected = opt.get().create(nmsPlayer.getWorld());
        if (!(selected instanceof EntityInsentient)) {
            log.warn("Selected entity is not Living Entity ({}).", type);
            return null;
        }
        EntityInsentient insentient = (EntityInsentient) selected;
        insentient.setNoAI(true);

        var morphed = new MorphEntity(nmsPlayer.getUniqueID(), nmsPlayer, (EntityInsentient) selected, getKaryon());
        morphed.syncPosition(player.getLocation(), nmsPlayer.getHeadRotation());


        Bukkit.getOnlinePlayers().stream().filter(onlinePlayer -> !onlinePlayer.getUniqueId().equals(player.getUniqueId())).forEach(morphed::showFor);
        morphed.handleUpdate();
        morphed.handleTeleport(player.getLocation(), nmsPlayer.getHeadRotation());
        morphed.syncEquipment();

        this.morphedPlayers.put(nmsPlayer.getUniqueID(), morphed);
        return morphed;
    }

    /**
     * Demorphs player
     *
     * @param player Player to demorph
     * @return Success/Failure
     */
    public boolean demorphPlayer(@NotNull Player player) {
        if (!this.morphedPlayers.containsKey(player.getUniqueId()))
            return false;
        MorphEntity morph = this.morphedPlayers.remove(player.getUniqueId());
        Bukkit.getOnlinePlayers().stream().filter(onlinePlayer -> !onlinePlayer.getUniqueId().equals(player.getUniqueId())).forEach(morph::hideFor);

        return true;
    }

    public boolean isMorphed(@NotNull Player player) {
        return this.morphedPlayers.containsKey(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // show all existing morphs
        this.morphedPlayers.forEach((player, morph) -> {
            if (!player.equals(event.getPlayer().getUniqueId())) {
                morph.showFor(event.getPlayer());
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isMorphed(event.getPlayer()))
            demorphPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        EntityPlayer player = PktStatics.getNmsPlayer(event.getPlayer());
        if (!this.morphedPlayers.containsKey(owner))
            return;

        MorphEntity morphed = this.morphedPlayers.get(owner);
        morphed.handleMovement(event.getFrom().toVector(), event.getTo().toVector(), event.getTo().getYaw(), event.getTo().getPitch(), player.getHeadRotation(), event.getPlayer().isOnGround());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;
        EntityPlayer nmsOwner = PktStatics.getNmsPlayer(owner);
        if (nmsOwner == null)
            return;

        MorphEntity morphed = this.morphedPlayers.get(owner);
        morphed.handleTeleport(event.getTo(), nmsOwner.getHeadRotation());
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        // call local method
        onPlayerTeleport(new PlayerTeleportEvent(event.getPlayer(), event.getPlayer().getLocation(), event.getPlayer().getLocation()));
    }

    @EventHandler
    public void onPlayerCrouch(PlayerToggleSneakEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;

        MorphEntity morphed = this.morphedPlayers.get(owner);
        System.out.println(event.getPlayer().isSneaking());
        morphed.getMorph().setPose(event.getPlayer().isSneaking() ? EntityPose.CROUCHING : EntityPose.STANDING);
        morphed.handleUpdate();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getClickedBlock() != null) {
            MorphEntity morphed = this.morphedPlayers.get(owner);
            PacketPlayOutAnimation anm = new PacketPlayOutAnimation(morphed.getMorph(), 0);
            PktStatics.sendPacketToAll(anm, owner);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;

        MorphEntity morphed = this.morphedPlayers.get(owner);
        if (event.getNewItem() != null)
            morphed.handleArmorItems(event.getSlotType(), event.getNewItem());

    }

    @EventHandler
    public void onPlayerItemSwap(PlayerSwapHandItemsEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;
        MorphEntity morphed = this.morphedPlayers.get(owner);
        if (event.getMainHandItem() != null && event.getOffHandItem() != null)
            morphed.handleHandItems(event.getMainHandItem(), event.getOffHandItem());

    }

    @EventHandler
    public void onPlayerItemSwap(PlayerItemHeldEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;

        MorphEntity morphed = this.morphedPlayers.get(owner);
        morphed.handleHandItems(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer().getInventory().getItemInOffHand());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        UUID owner = event.getEntity().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;


        MorphEntity morphed = this.morphedPlayers.get(owner);
        PacketPlayOutAnimation anm = new PacketPlayOutAnimation(morphed.getMorph(), 1);
        PktStatics.sendPacketToAll(anm, owner);

    }


    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        if (!event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK))
            return;

        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;

        MorphEntity morphed = this.morphedPlayers.get(owner);
        morphed.getMorph().setPose(EntityPose.SLEEPING);
        morphed.handleTeleport(event.getBed().getLocation(), morphed.getOwnerPlayer().getHeadRotation());
        morphed.handleUpdate();
    }

    @EventHandler
    public void onPlayerSleep(PlayerBedLeaveEvent event) {
        UUID owner = event.getPlayer().getUniqueId();
        if (!this.morphedPlayers.containsKey(owner))
            return;

        MorphEntity morphed = this.morphedPlayers.get(owner);
        EntityPlayer nmsOwner = morphed.getOwnerPlayer();

        morphed.getMorph().setPose(EntityPose.STANDING);
        morphed.handleTeleport(event.getPlayer().getLocation(), nmsOwner.getHeadRotation());
        morphed.handleUpdate();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (isMorphed(event.getEntity()))
            demorphPlayer(event.getEntity());
    }

    /**
     * @return Immutable map of all morphed palyers
     */
    public @NotNull Map<UUID, MorphEntity> getMorphedPlayers() {
        return Collections.unmodifiableMap(this.morphedPlayers);
    }

}
