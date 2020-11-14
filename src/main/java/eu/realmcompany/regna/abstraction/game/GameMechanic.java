package eu.realmcompany.regna.abstraction.game;

import eu.realmcompany.regna.abstraction.services.Service;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@Retention(RetentionPolicy.RUNTIME)
public @interface GameMechanic {
    /**
     * @return Name of this game mechanic
     */
    @NotNull String value();

    /**
     * @return Whether is this game mechanic tickable or not
     */
    boolean tickable() default true;

}
