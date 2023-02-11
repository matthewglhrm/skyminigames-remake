package io.github.vooiid.skyminigames.bukkit.lobby;

import io.github.vooiid.skyminigames.bukkit.group.Group;
import io.github.vooiid.skyminigames.bukkit.packets.system.ScoreboardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardLobby implements Listener
{

    @EventHandler
    public void onScoreboard(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        setScoreboarding(p);
    }

    public static void setScoreboarding(Player p) {
        ScoreboardAPI score = new ScoreboardAPI("§6§lSKYMINIGAMES");

        Group role = Group.getRole(p);

        score.addLine("§3", 8);
        score.addLine("  §fJogadores: §a" + Bukkit.getOnlinePlayers().size(), 7);
        score.addLine("§2", 6);
        score.addLine("  §fHub: §a#1", 5);
        score.addLine("  §fCreditos: §a", 4);
        score.addLine("  §fCash: §a", 3);
        score.addLine("§1", 2);
        score.addLine("  §eskyminigames.com  ", 1);

        score.setScoreboard();

        p.setScoreboard(score.getScoreboard());

    }

}
