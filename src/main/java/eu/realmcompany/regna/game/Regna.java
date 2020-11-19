package eu.realmcompany.regna.game;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.diagnostics.timings.Timer;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import eu.realmcompany.regna.game.services.RegnaServices;
import eu.realmcompany.regna.providers.storage.data.FriendlyData;
import eu.realmcompany.regna.providers.storage.store.AStore;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2(topic = "Regna")
public class Regna {

    @Getter
    private final @NotNull RegnaKaryon karyon;

    @Getter
    private RegnaServices services;
    @Getter
    private RegnaMechanics mechanics;

    @Getter
    private AStore regnaConfig;

    @Getter
    private boolean servicesEnabled = true;
    @Getter
    private boolean mechanicsEnabled = true;

    /**
     * Default constructor
     *
     * @param instance Instance of karyon
     */
    public Regna(@NotNull RegnaKaryon instance) {
        this.karyon = instance;
        log.info("Instancing §eRegna§r");
        Timer timer = Timer.timings().start();
        try {
            this.regnaConfig = karyon.getStorage().provideYaml("configurations/regna.yaml", true);

            FriendlyData config = this.regnaConfig.getData();
            this.servicesEnabled  = config.getBool("game.services.enabled" , true);
            this.mechanicsEnabled = config.getBool("game.mechanics.enabled", true);
        } catch (Exception e) {
            log.warn("Couldn't load regna configuration!");
        }

        if (this.servicesEnabled) {
            log.info("Instancing Regna Services...");
            try {
                this.services = new RegnaServices(this);
            } catch (Exception x) {
                log.error("Caught exception when instancing  RegnaServices", x);
                this.servicesEnabled = false;
            }
        } else
            log.warn("Services disabled!");

        if (this.mechanicsEnabled) {
            log.info("Instancing Regna Mechanics...");
            try {
            this.mechanics = new RegnaMechanics(this);
            } catch (Exception x) {
                log.error("Caught exception when instancing RegnaMechanics", x);
                this.mechanicsEnabled = false;
            }
        } else
            log.warn("Services disabled!");


        timer.stop();
        log.info("Instancing §eRegna§r took §a{}ms", timer.resultMilli());
    }

    public void construct() {
        Timer timer = Timer.timings().start();
        log.info("Constructing §eRegna§r");
        if (this.servicesEnabled)
            this.services.construct();
        if (this.mechanicsEnabled)
            this.mechanics.construct();

        timer.stop();
        log.info("Constructing §eRegna§r took §a{}ms", timer.resultMilli());
    }

    public void initialize() {
        Timer timer = Timer.timings().start();
        log.info("Initializing §eRegna§r");
        if (this.servicesEnabled)
            this.services.initialize();
        if (this.mechanicsEnabled)
            this.mechanics.initialize();

        timer.stop();
        log.info("Initializing §eRegna§r took §a{}ms", timer.resultMilli());
    }

    public void terminate() {
        Timer timer = Timer.timings().start();
        log.info("Terminating §eRegna§r");
        this.services.initialize();
        if (this.mechanicsEnabled)
            this.mechanics.terminate();
        if (this.servicesEnabled)
            this.services.terminate();

        timer.stop();
        log.info("Terminating §eRegna§r took §a{}ms", timer.resultMilli());
    }


}
