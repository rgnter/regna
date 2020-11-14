package eu.realmcompany.regna.abstraction.game;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.abstraction.Abstracted;
import eu.realmcompany.regna.abstraction.services.Service;
import eu.realmcompany.regna.game.RegnaGame;
import eu.realmcompany.regna.services.RegnaServices;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Objects;

public abstract class AGameMechanic extends Abstracted {

    /**
     * Default constructor from superclass
     * @param instance Instance of Karyon
     */
    public AGameMechanic(@NotNull RegnaKaryon instance) {
        super(instance);
    }

    /**
     * Default constructor
     * @param game Instance of Game
     */
    public AGameMechanic(@NotNull RegnaGame game) {
        super(game.getInstance());
    }

    /**
     * Called once every tick
     */
    public void tick() {

    }

    /**
     * @return Returns annotation data of this Game Mechanic
     */
    public @NotNull GameMechanic getAnnotationData() {
        GameMechanic fromClass = this.getClass().getAnnotation(GameMechanic.class);
        return Objects.requireNonNullElseGet(fromClass, () -> new GameMechanic() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return GameMechanic.class;
            }

            @Override
            public @NotNull String value() {
                return getClass().getSimpleName();
            }

            @Override
            public boolean tickable() {
                return false;
            }
        });
    }
}
