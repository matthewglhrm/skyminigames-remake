package io.github.vooiid.skyminigames.bukkit.command;

import io.github.vooiid.skyminigames.bukkit.BukkitMain;
import io.github.vooiid.skyminigames.bukkit.gamer.Gamer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fly implements CommandExecutor
{
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) {
                return true;
            } else {
                Player p = (Player) sender;

                Gamer gamer = BukkitMain.getInstance().getGamerManager().getGamer(p.getUniqueId());
                if (!gamer.isFlying()) {
                    gamer.setFlying(true);

                    while(!p.getAllowFlight()) {
                        p.setAllowFlight(true);
                    }

                    p.setFlying(true);
                    p.sendMessage("§aYou've activated fly mode");
                } else {
                    gamer.setFlying(false);

                    while(p.getAllowFlight()) {
                        p.setAllowFlight(false);
                    }

                    p.setFlying(false);
                    p.sendMessage("§cYou've disabled fly mode");
                }

                gamer = null;
                p = null;
                }


            return false;
        }

    }

