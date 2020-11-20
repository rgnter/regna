package eu.realmcompany.regna.game.mechanics.magic.alchemy;

import eu.realmcompany.regna.abstraction.game.AGameMechanic;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.ItemBook;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.Particles;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.mth.Mth;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;

public class AlchemyMechanics extends AGameMechanic {

    private Map<UUID, CraftItem> alchemyTest = new HashMap<>();

    /**
     * Default constructor
     *
     * @param mechanics Game mechanics
     */
    public AlchemyMechanics(@NotNull RegnaMechanics mechanics) {
        super(mechanics);
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
        for (UUID uuid : alchemyTest.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline())
                this.alchemyTest.remove(uuid);
            else {
                Random ran = new Random();
                CraftPlayer craft = ((CraftPlayer) player);
                EntityPlayer nmsPlayer = craft.getHandle();
                CraftItem item = alchemyTest.get(uuid);
                if(item.isInWater()) {
                    if(item.getItemStack().getType().equals(Material.OXEYE_DAISY)) {
                        final Vector loc = player.getEyeLocation().toVector();
                        Executors.newSingleThreadExecutor().submit(() -> {
                            for(int azimuth = 0; azimuth < 360; azimuth+=+6) {
                                for(int incline = 0; incline < 180; incline+=+6) {
                                    Vector pos = Mth.sphericalToCartesian(0.5, azimuth, incline).add(loc);
                                    PacketPlayOutWorldParticles pkt = new PacketPlayOutWorldParticles(Particles.CLOUD, true, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0, 1, 1);
                                    PktStatics.sendPacketToAll(pkt);
                                }
                            }
                        });
                        getMechanics().getMorph().morphPlayer(player, "skeleton");
                    }
                    else if(item.getItemStack().getType().equals(Material.POPPY))
                        getMechanics().getMorph().morphPlayer(player, "cat");
                    else if(item.getItemStack().getType().equals(Material.GUNPOWDER))
                        getMechanics().getMorph().morphPlayer(player, "creeper");
                    else if(item.getItemStack().getType().equals(Material.ROTTEN_FLESH))
                        getMechanics().getMorph().morphPlayer(player, "zombie");
                    this.alchemyTest.remove(uuid);
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player wizard = event.getPlayer();
        ItemStack offhand = wizard.getInventory().getItemInOffHand();

        if (!offhand.getType().equals(Material.WRITTEN_BOOK))
            return;
        if (!offhand.hasItemMeta())
            return;
        if (!(offhand.getItemMeta() instanceof BookMeta))
            return;

        BookMeta meta = (BookMeta) offhand.getItemMeta();
        if(!meta.hasAuthor())
            return;
        if (!meta.getAuthor().equals("rgnter"))
            return;
        meta.setPages("§k" + RandomStringUtils.random(250), "§k" + RandomStringUtils.random(250));
        offhand.setItemMeta(meta);



        this.alchemyTest.put(wizard.getUniqueId(), ((CraftItem) event.getItemDrop()));
    }
}
