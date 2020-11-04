package eu.realmcompany.regna.abstraction;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.services.RegnaServices;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class AService implements Listener {

    @Getter
    private final RegnaKaryon instance;
    @Getter
    private final RegnaServices services;


    /**
     * Service constructor
     * @param services Service instance
     */
    public AService(@NotNull RegnaServices services) {
        this.services = services;
        this.instance = services.getInstance();
    }



    /**
     * Called on service construction
     * @throws Exception Thrown when Service couldn't handle Exception.
     */
    public void construct() throws Exception {}

    /**
     * Called on service initialization
     * @throws Exception Thrown when Service couldn't handle Exception.
     */
    abstract public void initialize() throws Exception;

    /**
     * Called on service termination
     * @throws Exception Thrown when Service couldn't handle Exception.
     */
    abstract public void terminate()  throws Exception;

    /**
     * Sexier way of registering listeners
     */
    public void registerAsListener() {
        Bukkit.getPluginManager().registerEvents((Listener) this, getInstance());
    }

    /**
     * Sexier way of unregistering listeners
     */
    public void unregisterAsListener() {
        HandlerList.unregisterAll(this);
    }

}
