package eu.realmcompany.regna.abstraction.services;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.abstraction.Abstracted;
import eu.realmcompany.regna.game.RegnaGame;
import eu.realmcompany.regna.services.RegnaServices;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Objects;

public abstract class AService extends Abstracted implements Listener {

    /**
     * Default constructor from superclass
     * @param instance Instance of Karyon
     */
    public AService(@NotNull RegnaKaryon instance) {
        super(instance);
    }

    /**
     * Default constructor
     * @param services Instance of Services
     */
    public AService(@NotNull RegnaServices services) {
        super(services.getInstance());
    }

    /**
     * @return Returns annotation data of this Service
     */
    public @NotNull Service getAnnotationData() {
        Service fromClass = this.getClass().getAnnotation(Service.class);
        return Objects.requireNonNullElseGet(fromClass, () -> new Service() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Service.class;
            }

            @Override
            public @NotNull String value() {
                return getClass().getSimpleName();
            }
        });
    }



}
