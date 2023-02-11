package io.github.vooiid.skyminigames.bukkit.server.counter;

import io.github.vooiid.skyminigames.bukkit.server.types.ServerType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class ServerCounter implements PluginMessageListener {

    public static int all, swsolo, lobby;

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg) {

        if (channel.equals("BungeeCord")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(msg);

            String sub = in.readUTF();

            if (sub.equals("PlayerCount")) {

                all = in.readInt();

            } try {

                String sv = in.readUTF();

                if (sv.equals(ServerType.LOBBY.getName())) {

                    lobby = in.readInt();

                } else if (sv.equals(ServerType.ATLANTIDA.getName())) {

                    swsolo = in.readInt();

                }

            } catch (Exception e) {

            }
        } else if (channel.equals("WDL|INIT")) {

            p.kickPlayer(
                    "§c§lERRO!\n\n§cDetectamos o uso de clients com sistema de WORLDDOWNLOADER.\n§cUtilize outro client e tente novamente.");

        }

    }

}
