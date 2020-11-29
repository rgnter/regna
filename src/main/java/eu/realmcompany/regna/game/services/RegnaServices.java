package eu.realmcompany.regna.game.services;

import eu.realmcompany.regna.abstraction.game.AGameService;
import eu.realmcompany.regna.diagnostics.timings.Timer;
import eu.realmcompany.regna.game.Regna;
import eu.realmcompany.regna.game.services.admin.AdminGameService;
import eu.realmcompany.regna.game.services.chat.ChatService;
import eu.realmcompany.regna.game.services.interfacee.InterfaceService;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j2(topic = "Regna Services")
public class RegnaServices {

    @Getter
    private final Regna regna;

    private final Map<Class<? extends AGameService>, AGameService> services = new HashMap<>();

    /**
     * Default constructor
     *
     * @param regna Regna instance
     */
    public RegnaServices(@NotNull Regna regna) {
        this.regna = regna;
    }

    public void construct() {
        log.info("Registering services...");
        registerService(ChatService.class);
        registerService(AdminGameService.class);
        registerService(InterfaceService.class);

        log.info("Constructing services...");
        this.services.forEach((serviceClass, service) -> {
            Timer timer = Timer.timings().start();
            try {
                service.construct();
                log.info("Service §e{}§r constructed. Took §a{}§rms", service.getServiceName(), timer.stop().resultMilli());
            } catch (Exception x) {
                log.error("Failed to construct service §e{}§r", service.getServiceName(), x);
            }
        });
    }

    public void initialize() {
        log.info("Initializing services...");

        this.services.forEach((serviceClass, service) -> {
            Timer timer = Timer.timings().start();
            try {
                service.initialize();
                log.info("Service §e{}§r initialized. Took §a{}§rms", service.getServiceName(), timer.stop().resultMilli());
            } catch (Exception x) {
                log.error("Failed to initialize service §e{}§r", service.getServiceName(), x);
            }
        });
    }

    public void terminate() {
        log.info("Terminating services...");

        this.services.forEach((serviceClass, service) -> {
            Timer timer = Timer.timings().start().start();
            try {
                service.terminate();
                log.info("Service §e{}§r terminated. Took §a{}§rms", service.getServiceName(), timer.stop().resultMilli());
            } catch (Exception x) {
                log.error("Failed to terminate service §e{}§r", service.getServiceName(), x);
            }
        });
    }

    /**
     * Checks whether service is registered.
     *
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns boolean true, if service is registered.
     */
    public <A extends AGameService> boolean isServiceRegistered(@NotNull Class<A> service) {
        return this.services.containsKey(service);
    }

    /**
     * Registers service
     *
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns boolean true, if service was successfully registered.
     */
    public <A extends AGameService> boolean registerService(@NotNull Class<A> service) {
        if (service == AGameService.class) {
            log.warn("Tried to register abstract service");
            return false;
        }
        if (isServiceRegistered(service)) {
            log.warn("Tried to register service §e{}§r, which is already registered!", service.getName());
            return false;
        }


        try {
            final Constructor<A> constructor = service.getConstructor(RegnaServices.class);
            final AGameService serviceInst = constructor.newInstance(this);
            this.services.put(service, serviceInst);
            log.info("Registered service §e{}§r", serviceInst.getServiceName());
            return true;
        } catch (NoSuchMethodException e) {
            log.error("Couldn't find default constructor for §e{}§r", service.getName(), e);
            log.error(e);
        } catch (IllegalAccessException e) {
            log.error("Couldn't access default constructor for §e{}§r", service.getName(), e);
        } catch (InstantiationException e) {
            log.error("Couldn't create instance from default constructor for §e{}§r", service.getName(), e);
        } catch (InvocationTargetException e) {
            log.error("Service §e{}§r thrown exception", service.getName(), e.getTargetException());
        }

        return false;
    }

    /**
     * Unregisters service
     *
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns boolean true, if service was successfully unregistered.
     */
    public <A extends AGameService> boolean unregisterService(@NotNull Class<A> service) {
        if (service == AGameService.class) {
            log.warn("Tried to unregister abstract service");
            return false;
        }
        if (isServiceRegistered(service)) {
            log.warn("Tried to unregister service §e{}§r, which is not registered!", service.getName());
            return false;
        }

        this.services.remove(service);

        return true;
    }

    /**
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns optional with value of service instance.
     */
    public <A extends AGameService> @NotNull Optional<A> getServiceOptional(@NotNull Class<A> service) {
        return Optional.ofNullable(getService(service));
    }

    /**
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns service instance.
     */
    public <A extends AGameService> @Nullable A getService(@NotNull Class<A> service) {
        if (service == AGameService.class) {
            log.warn("Tried to get abstract service");
            return null;
        }
        if (!isServiceRegistered(service)) {
            log.error("Tried to get service §e{}§r, which is not registered!", service.getName());
            return null;
        }

        return (A) this.services.get(service);
    }

    /**
     * @return Immutable map of services
     */
    public @NotNull Map<Class<? extends AGameService>, AGameService> getServices() {
        return Collections.unmodifiableMap(this.services);
    }
}
