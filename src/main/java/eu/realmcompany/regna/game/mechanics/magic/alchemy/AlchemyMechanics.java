package eu.realmcompany.regna.game.mechanics.magic.alchemy;

import eu.realmcompany.regna.abstraction.game.AGameMechanic;
import eu.realmcompany.regna.abstraction.game.GameMechanic;
import eu.realmcompany.regna.game.RegnaGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;

@GameMechanic("Alchemy")
public class AlchemyMechanics extends AGameMechanic {

    public AlchemyMechanics(@NotNull RegnaGame game) {
        super(game);
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
