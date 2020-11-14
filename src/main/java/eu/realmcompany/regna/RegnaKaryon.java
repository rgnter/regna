package eu.realmcompany.regna;

import eu.realmcompany.regna.game.RegnaGame;
import eu.realmcompany.regna.game.mcdev.PacketStatics;
import eu.realmcompany.regna.services.RegnaServices;
import eu.realmcompany.regna.storage.StorageManager;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Log4j2(topic = "RegnaKaryon")
public class RegnaKaryon extends JavaPlugin implements Listener {

    public static RegnaKaryon instance;

    @Getter
    private final StorageManager storageManager;

    @Getter
    private final RegnaServices services;
    @Getter
    private final RegnaGame game;

    {
        log.info("Instancing...");
        instance = this;
        storageManager = new StorageManager(this);

        services = new RegnaServices(this);
        game = new RegnaGame(this);
    }


    @Override
    public void onLoad() {
        super.onLoad();
        log.info("Constructing Karyon...");

        this.services.construct();
        this.game.construct();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        log.info("Initializing Karyon...");

        Bukkit.getPluginManager().registerEvents(this, this);

        this.services.initialize();
        this.game.initialize();

        try {
            this.storageManager.provideJson("test.json", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.storageManager.provideYaml("test.yaml", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        log.info("Terminating Karyon...");
        this.game.terminate();
        this.services.terminate();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        PacketStatics.setRealmConnection(event.getPlayer());
    }


}
