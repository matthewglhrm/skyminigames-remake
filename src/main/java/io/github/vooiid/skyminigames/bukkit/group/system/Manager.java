package io.github.vooiid.skyminigames.bukkit.group.system;

import io.github.vooiid.skyminigames.bukkit.group.Group;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/*

 * System roles forked for: Kotleyxz (Mardok)

 */

public class Manager {

    public static void setRole(Player p) {
        Group role = Group.getRole(p);
        Scoreboard sb = p.getScoreboard();
        p.setPlayerListName(role.getPrefix() + p.getName());
        p.setDisplayName(role.getPrefix() + p.getName());

        Team team = sb.getTeam(role.ordinal() + role.getName());
        if (team == null) {
            team = sb.registerNewTeam(role.ordinal() + role.getName());
            team.setPrefix(role.getPrefix());
            team.addEntry(p.getName());
        }

    }

    public static void removeRole(Player p) {
        Group role = Group.getRole(p);
        Scoreboard sb = p.getScoreboard();

        Team team = sb.getTeam(role.ordinal() + role.getName());
        if (team != null && team.hasEntry(p.getName())) {
            team.removeEntry(p.getName());
        }
    }

}
