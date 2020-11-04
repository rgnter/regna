package eu.realmcompany.regna;

import eu.realmcompany.regna.game.RegnaGame;
import eu.realmcompany.regna.services.RegnaServices;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;

@Log4j2(topic = "RegnaKaryon")
public class RegnaKaryon extends JavaPlugin {

    public static RegnaKaryon instance;

    @Getter
    private final RegnaServices services;
    @Getter
    private final RegnaGame   game;
    {
        log.info("Instancing...");
        services = new RegnaServices(this);
        game     = new RegnaGame(this);
    }


    @Override
    public void onLoad() {
        instance = this;
        super.onLoad();
        log.info("Constructing Karyon...");

        this.services.construct();
        this.game.construct();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        log.info("Initializing Karyon...");

        this.services.initialize();
        this.game.initialize();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        log.info("Terminating Karyon...");
        this.game.terminate();
        this.services.terminate();
    }


}
