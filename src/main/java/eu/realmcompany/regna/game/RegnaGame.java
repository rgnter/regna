package eu.realmcompany.regna.game;

import eu.realmcompany.regna.RegnaKaryon;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class RegnaGame {

    @Getter
    private RegnaKaryon instance;

    /**
     * Default constructor
     * @param instance Instance
     */
    public RegnaGame(@NotNull RegnaKaryon instance) {
        this.instance = instance;
    }

    public void construct() {

    }

    public void initialize() {

    }

    public void terminate() {

    }

}
