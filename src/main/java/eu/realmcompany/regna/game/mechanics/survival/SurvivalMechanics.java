package eu.realmcompany.regna.game.mechanics.survival;

import eu.realmcompany.regna.abstraction.game.AGameMechanic;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class SurvivalMechanics extends AGameMechanic {

    public SurvivalMechanics(@NotNull RegnaMechanics mechanics) {
        super(mechanics);
        setMechanicName("Survival Mechanics");
        setTickable(true);
    }

    @Override
    public void tick() {

    }

    @Override
    public void initialize() throws Exception {
        registerAsListener();
    }

    @Override
    public void terminate() throws Exception {

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

    }
}
