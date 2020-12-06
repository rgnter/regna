package eu.realmcompany.regna.game.mechanics.magic.particles.model;

import eu.realmcompany.regna.game.mcdev.PktStatics;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.ParticleType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class ParticleModel {

    @Getter @Setter
    private ParticleType particleType;
    private Set<Vector> vertices = new HashSet<>();

    public ParticleModel(Set<Vector> vertices) {
        this.vertices = vertices;
    }

    public static @NotNull ParticleModel fromOBJ(@NotNull InputStream stream) {
        StringBuilder data = new StringBuilder();
        int b;
        do {
            try {
                b = stream.read();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            if(b!=-1)
                data.append((char) b);
        } while (b != -1);
        return fromOBJ(data.toString());
    }
    public static @NotNull ParticleModel fromOBJ(@NotNull String data) {
        final Set<Vector> vertices = new HashSet<>();

        final AtomicInteger lineNum = new AtomicInteger(0);
        Arrays.stream(data.split("\n")).forEach(line -> {
            lineNum.addAndGet(1);

            if(line.startsWith("v")) {
                String[] coords = line.substring(1).split(" ");
                if(coords.length != 3) {
                    log.error("§cBad particle vertex at line {}; XYZ component missing or damaged.", lineNum);
                    return;
                }
                try {
                    vertices.add(new Vector(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2])));
                } catch (Exception x) {
                    log.error("§cBad particle vertex at line {}; Parsing XYZ component went wrong.", lineNum, x);
                }
            }
        });
        log.info("Processed {} vertices.", vertices.size());
        return new ParticleModel(vertices);
    }

    public @NotNull Set<PacketPlayOutWorldParticles> getPackets(@NotNull Vector origin){
        Set<PacketPlayOutWorldParticles> particles = new HashSet<>();
        for (Vector vertex : this.vertices) {
            Vector real = vertex.clone().add(origin);
            particles.add(PktStatics.makeParticlePacket(this.particleType, false, real, new Vector(), 1, 1));
        }
        return particles;
    }

    /**
     * @return Set of all vertex vectors
     */
    public @NotNull Set<Vector> getVertices() {
        return Collections.unmodifiableSet(this.vertices);
    }

}
