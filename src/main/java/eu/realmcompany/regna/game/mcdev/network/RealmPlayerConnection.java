package eu.realmcompany.regna.game.mcdev.network;

import net.minecraft.server.v1_16_R2.*;

public class RealmPlayerConnection extends PlayerConnection {

    /**
     * Default constructor from super class
     *
     * @param minecraftServer Minecraft server
     * @param networkManager  Network manger
     * @param entityPlayer    Entity player
     */
    public RealmPlayerConnection(MinecraftServer minecraftServer, NetworkManager networkManager, EntityPlayer entityPlayer) {
        super(minecraftServer, networkManager, entityPlayer);
    }

    @Override
    public void a(PacketPlayInFlying packet) {
        super.a(packet);
    }
}
