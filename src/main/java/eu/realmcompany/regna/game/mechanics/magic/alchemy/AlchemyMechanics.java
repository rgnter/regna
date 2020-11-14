package eu.realmcompany.regna.game.mechanics.magic.alchemy;

import eu.realmcompany.regna.abstraction.game.AGameMechanic;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

public class AlchemyMechanics extends AGameMechanic {

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


    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        System.out.println(event.getItemDrop().getLocation());
    }
}
