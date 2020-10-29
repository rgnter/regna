package eu.realmcompany.regna.services;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.abstraction.AService;
import eu.realmcompany.regna.services.chat.ChatService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.rgnt.recrd.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegnaServices {

    @Getter
    private RegnaKaryon instance;
    @Getter
    private Logger logger;

    private final Map<Class<? extends AService>, AService> services = new HashMap<>();

    /**
     * Default constructor
     *
     * @param instance Instance of Karyon
     */
    public RegnaServices(RegnaKaryon instance) {
        this.instance = instance;
        this.logger = instance.logger();

        registerService(ChatService.class);
    }

    /**
     * Checks whether service is registered.
     *
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns boolean true, if service is registered.
     */
    public <A extends AService> boolean isServiceRegistered(@NotNull Class<A> service) {
        return this.services.containsKey(service);
    }

    /**
     * Registers service
     *
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns boolean true, if service was successfully registered.
     */
    public <A extends AService> boolean registerService(@NotNull Class<A> service) {
        if(service == AService.class) {
            this.logger.warn("Tried to register abstract service");
            return false;
        }
        if (isServiceRegistered(service)) {
            this.logger.warn("Tried to register service '{0}', which is already registered!", service.getName());
            return false;
        }

        try {
            final Constructor<A> constructor = service.getConstructor(RegnaServices.class);
            final AService serviceInst = constructor.newInstance(this);
            this.services.put(service, serviceInst);
            this.logger.info("Registered service §e{0}", serviceInst.getServiceName());
            return true;
        } catch (NoSuchMethodException e) {
            logger.error("Couldn't find default constructor for '{0}'", e, service);
        } catch (IllegalAccessException e) {
            logger.error("Couldn't access default constructor for '{0}'", e, service);
        } catch (InstantiationException e) {
            logger.error("Couldn't create instance from default constructor for '{0}'", e, service);
        } catch (InvocationTargetException e) {
            logger.error("Service '{0}' thrown exception", e.getTargetException(), service);
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
    public <A extends AService> boolean unregisterService(@NotNull Class<A> service) {
        if(service == AService.class) {
            this.logger.warn("Tried to unregister abstract service");
            return false;
        }
        if (isServiceRegistered(service)) {
            this.logger.warn("Tried to unregister service '{0}', which is not registered!", service.getName());
            return false;
        }

        this.services.remove(service);

        return true;
    }

    /**
     *
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns optional with value of service instance.
     */
    public <A extends AService> @NotNull Optional<A> getServiceOptional(@NotNull Class<A> service) {
        return Optional.ofNullable(getService(service));
    }

    /**
     *
     * @param service Service class
     * @param <A>     Type of Service class
     * @return Returns service instance.
     */
    public <A extends AService> @Nullable A getService(@NotNull Class<A> service) {
        if(service == AService.class) {
            this.logger.warn("Tried to get abstract service");
            return null;
        }
        if (!isServiceRegistered(service)) {
            this.logger.error("Tried to get service '{0}', which is not registered!", service.getName());
            return null;
        }

        return (A)this.services.get(service);
    }

    /**
     * @return Immutable map of services
     */
    public @NotNull Map<Class<? extends AService>, AService> getServices() {
        return Collections.unmodifiableMap(this.services);
    }
}
