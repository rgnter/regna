package eu.realmcompany.regna.game.mechanics.magic.particles;

import eu.realmcompany.regna.abstraction.game.AGameMechanic;
import eu.realmcompany.regna.game.mcdev.PktStatics;
import eu.realmcompany.regna.game.mechanics.RegnaMechanics;
import eu.realmcompany.regna.game.mechanics.magic.particles.model.ParticleModel;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.Particles;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jline.reader.impl.ReaderUtils;

import java.io.InputStreamReader;
import java.util.concurrent.Executors;

public class ParticleMechanics extends AGameMechanic {

    public ParticleMechanics(@NotNull RegnaMechanics mechanics) {
        super(mechanics);
        setMechanicName("Particle Mechanics");
    }

    @Override
    public void initialize() throws Exception {
        Bukkit.getCommandMap().register("regna", new Command("modeltest") {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                if(!(commandSender instanceof Player))
                    return true;
                ParticleModel model = ParticleModel.fromOBJ(getKaryon().getResource("configurations/mechanics/models/particle.obj"));
                model.setParticleType(Particles.SMOKE);

                final var packets = model.getPackets(((Player) commandSender).getLocation().toVector());
                final EntityPlayer player = PktStatics.getNmsPlayer((Player) commandSender);
                Executors.newSingleThreadExecutor().submit(()->{

                });
                return true;
            }
        });
    }

    @Override
    public void terminate() throws Exception {

    }


    @Override
    public void tick() {

    }

}
