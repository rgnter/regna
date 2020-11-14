package eu.realmcompany.regna.abstraction.services;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    /**
     * @return Service name
     */
    @NotNull String value();

}
