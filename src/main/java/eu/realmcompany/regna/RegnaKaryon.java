package eu.realmcompany.regna;

import eu.realmcompany.regna.services.RegnaServices;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.rgnt.recrd.Logger;

public class RegnaKaryon extends JavaPlugin {

    public Logger logger;
    {
        logger = Logger.Builder.make().withPrefix("Regna").build();
    }

    @Getter
    private RegnaServices services;


    @Override
    public void onLoad() {
        super.onLoad();
        this.logger.info("Constructing Karyon...");
        this.services = new RegnaServices(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.logger.info("Initializing Karyon...");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.logger.info("Terminating Karyon...");
    }

    public @NotNull Logger logger() {
        return logger;
    }

}
