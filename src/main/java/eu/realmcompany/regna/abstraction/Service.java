package eu.realmcompany.regna.abstraction;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    /**
     * Service name
     */
    @NotNull String value();

}
