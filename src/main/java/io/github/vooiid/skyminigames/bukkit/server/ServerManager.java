package io.github.vooiid.skyminigames.bukkit.server;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.vooiid.skyminigames.bukkit.BukkitMain;
import org.bukkit.entity.Player;

public class ServerManager {
    public static void sendPlayerToServer(Player p, String server) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(server.toString().toLowerCase());
            p.sendPluginMessage(BukkitMain.getAPIManager(), "BungeeCord", out.toByteArray());
            p.sendMessage("§aConectado ao servidor §e" + server);
        } catch(Exception e) {
            e.printStackTrace();
            p.sendMessage("§cErro ao conectar-se ao servidor §e" + server);
        }
    }

}
