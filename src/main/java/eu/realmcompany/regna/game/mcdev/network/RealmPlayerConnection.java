package eu.realmcompany.regna.game.mcdev.network;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.v1_16_R2.*;

import java.util.Map;
import java.util.UUID;

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

    }
}
