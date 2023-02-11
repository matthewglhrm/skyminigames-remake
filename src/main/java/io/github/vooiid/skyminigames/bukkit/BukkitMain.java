package io.github.vooiid.skyminigames.bukkit;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.vooiid.skyminigames.bukkit.command.Fly;
import io.github.vooiid.skyminigames.bukkit.gamer.Gamer;
import io.github.vooiid.skyminigames.bukkit.gamer.GamerManager;
import io.github.vooiid.skyminigames.bukkit.lobby.ScoreboardLobby;
import io.github.vooiid.skyminigames.bukkit.lobby.player.PlayerListener;
import io.github.vooiid.skyminigames.bukkit.server.counter.ServerCounter;
import io.github.vooiid.skyminigames.string.GeneralString;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin
{

    private static BukkitMain instance;
    PluginManager pm = Bukkit.getPluginManager();

    @Getter
    private GamerManager gamerManager;

    @Getter
    private ProtocolManager protocolManager;
    public static BukkitMain getAPIManager() {
        return getPlugin(BukkitMain.class);
    }

    public static BukkitMain getInstance() {
        return instance;
    }


    public void setup() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ServerCounter());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "WDL|CONTROL");
        getServer().getMessenger().registerIncomingPluginChannel(this, "WDL|INIT", new ServerCounter());



        instance = this;
    }

    @Override
    public void onEnable() {
        setup();
        registerEvents();
        Bukkit.getConsoleSender().sendMessage(Color.GREEN + GeneralString.PREFIX_CONSOLE + "Kibe iniciado com sucesso!");

    }

    public void registerEvents() {
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new ScoreboardLobby(), this);
        getCommand("fly").setExecutor(new Fly());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Color.RED + GeneralString.PREFIX_CONSOLE + "Kibe desligado com sucesso!");

    }

}
