package eu.realmcompany.regna.abstraction.game;

import eu.realmcompany.regna.RegnaKaryon;
import eu.realmcompany.regna.abstraction.AAbstract;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.jetbrains.annotations.NotNull;

public abstract class AGameMechanic extends AAbstract {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    public String mechanicName;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    public boolean tickable;

    protected MinecraftServer nmsServer;

    /**
     * Default constructor
     * @param mechanics Mechanics instance
     */
    public AGameMechanic(@NotNull RegnaMechanics mechanics) {
        super(mechanics.getRegna().getKaryon());
        this.nmsServer = mechanics.getNmsServer();
    }


    /**
     * Tick
     */
    public abstract void tick();



}
