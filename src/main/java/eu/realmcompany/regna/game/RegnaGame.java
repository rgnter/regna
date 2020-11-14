package eu.realmcompany.regna.game;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.game.mechanics.magic.alchemy.AlchemyMechanics;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class RegnaGame {

    @Getter
    private RegnaKaryon instance;

    @Getter
    private AlchemyMechanics alchemyMechanics;

    /**
     * Default constructor
     * @param instance Instance
     */
    public RegnaGame(@NotNull RegnaKaryon instance) {
        this.instance = instance;

        this.alchemyMechanics = new AlchemyMechanics(this);
    }

    public void construct() {

    }

    public void initialize() {

    }

    public void terminate() {

    }


}
