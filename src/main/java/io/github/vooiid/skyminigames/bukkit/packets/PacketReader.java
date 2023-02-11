package io.github.vooiid.skyminigames.bukkit.packets;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.Packet;

public class PacketReader
{

    public static void registerPacket(Player p, Packet<?> packetType) {
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetType);
    }

    public static int getPing(Player p) {
        return ((CraftPlayer) p).getHandle().ping;
    }

}
