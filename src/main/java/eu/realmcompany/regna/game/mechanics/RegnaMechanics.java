package eu.realmcompany.regna.game.mechanics;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.diagnostics.timings.Timer;
import eu.realmcompany.regna.game.Regna;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class RegnaMechanics {

    @Getter
    private final @NotNull Regna regna;

    /**
     * Default constructor
     * @param karyon Karyon instance
     */
    public RegnaMechanics(@NotNull Regna regna) {
        this.regna = regna;
    }
}
