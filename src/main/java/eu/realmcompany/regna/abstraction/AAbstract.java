package eu.realmcompany.regna.abstraction;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.game.Regna;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import eu.realmcompany.regna.game.services.RegnaServices;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;


public abstract class AAbstract implements Listener {

    @Getter
    private final RegnaKaryon karyon;

    @Getter
    private final Regna regna;
    @Getter
    private final RegnaMechanics mechanics;
    @Getter
    private final RegnaServices services;

    /**
     * Default constructor
     *
     * @param karyon Karyon instance
     */
    public AAbstract(RegnaKaryon karyon) {
        this.karyon = karyon;
        this.regna = karyon.getRegna();

        this.services = regna.getKaryon().getRegna().getServices();
        this.mechanics = regna.getKaryon().getRegna().getMechanics();
    }

    /**
     * Called on construction
     *
     * @throws Exception Thrown when couldn't handle Exception.
     */
    public void construct() throws Exception {
    }

    /**
     * Called on initialization
     *
     * @throws Exception Thrown when couldn't handle Exception.
     */
    abstract public void initialize() throws Exception;

    /**
     * Called on termination
     *
     * @throws Exception Thrown when couldn't handle Exception.
     */
    abstract public void terminate() throws Exception;

    /**
     * Sexier way of registering listeners
     */
    public void registerAsListener() {
        Bukkit.getPluginManager().registerEvents((Listener) this, getKaryon());
    }

    /**
     * Sexier way of unregistering listeners
     */
    public void unregisterAsListener() {
        HandlerList.unregisterAll(this);
    }

}
