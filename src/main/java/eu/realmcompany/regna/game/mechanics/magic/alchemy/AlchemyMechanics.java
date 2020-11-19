package eu.realmcompany.regna.game.mechanics.magic.alchemy;

import eu.realmcompany.regna.abstraction.game.AGameMechanic;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
                item.setMomentum(new Vector(ran.nextInt(15) / 100d, ran.nextInt(15) / 100d, ran.nextInt(15)/100d));
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

        this.alchemyTest.put(wizard.getUniqueId(), ((CraftItem) event.getItemDrop()));
    }
}
