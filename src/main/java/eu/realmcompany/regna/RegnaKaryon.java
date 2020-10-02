package eu.realmcompany.regna;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.rgnt.recrd.Logger;

public class RegnaKaryon extends JavaPlugin {

    public Logger logger;

    {
        logger = Logger.Builder.make().withPrefix("Regna").build();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.logger.info("Loading {0}", "Karyon");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
