package eu.realmcompany.regna.game.mechanics;

import eu.realmcompany.regna.game.Regna;
import eu.realmcompany.regna.game.mechanics.magic.alchemy.AlchemyMechanics;
import eu.realmcompany.regna.game.mechanics.magic.morph.MorphMechanics;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.jetbrains.annotations.NotNull;

@Log4j2(topic = "Regna Mechanics")
public class RegnaMechanics {

    @Getter
    private final @NotNull Regna regna;

    @Getter
    private final MinecraftServer nmsServer;

    @Getter
    private AlchemyMechanics alchemy;
    @Getter
    private MorphMechanics morph;
    /**
     * Default constructor
     * @param regna Regna instance
     */
    public RegnaMechanics(@NotNull Regna regna) {
        this.regna = regna;
        this.nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
    }


    public void construct() {
        this.alchemy = new AlchemyMechanics(this);
        this.morph = new MorphMechanics(this);
        log.info("Constructing Regna Mechanics");

        try {
            this.alchemy.construct();

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.morph.construct();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {
            this.alchemy.initialize();
            // register tickable
            nmsServer.b(() -> this.alchemy.tick());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.morph.initialize();
            // register tickable
            nmsServer.b(() -> this.morph.tick());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        try {
            this.morph.terminate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.alchemy.terminate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
