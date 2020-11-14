package eu.realmcompany.regna.abstraction;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.game.RegnaGame;
import eu.realmcompany.regna.services.RegnaServices;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public abstract class Abstracted implements Listener {

    @Getter
    private final RegnaKaryon instance;
    @Getter
    private final RegnaServices services;
    @Getter
    private final RegnaGame game;

    /**
     * Abstractable constructor
     * @param instance Karyon instance
     */
    public Abstracted(@NotNull RegnaKaryon instance) {
        this.instance = instance;
        this.game     = instance.getGame();
        this.services = instance.getServices();
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
