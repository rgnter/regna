package eu.realmcompany.regna.abstraction.game;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.abstraction.AAbstract;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public abstract class AGameMechanic extends AAbstract {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    public String mechanicName;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    public boolean doesTick;

    /**
     * Default constructor
     * @param mechanics Mechanics instance
     */
    public AGameMechanic(@NotNull RegnaMechanics mechanics) {
        super(mechanics.getRegna().getKaryon());
    }

}
