package eu.realmcompany.regna;

import eu.realmcompany.regna.game.Regna;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import eu.realmcompany.regna.providers.database.DatabaseProvider;
import eu.realmcompany.regna.providers.storage.StorageProvider;
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
    private final DatabaseProvider database;
    @Getter
    private final StorageProvider storage;

    @Getter
    private final Regna regna;

    {
        log.info("Instancing providers....");
        this.storage = new StorageProvider(this);
        this.database = new DatabaseProvider(this);

        log.info("Instancing game...");
        this.regna = new Regna(this);
    }


    @Override
    public void onLoad() {
        super.onLoad();
        log.info("Constructing Karyon...");

        this.regna.construct();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        log.info("Initializing Karyon...");

        Bukkit.getPluginManager().registerEvents(this, this);

        this.regna.initialize();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        this.regna.terminate();
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
       PktStatics.setRealmConnection(event.getPlayer());
    }


}
