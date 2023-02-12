package io.github.vooiid.skyminigames.bukkit.lobby.player;

import io.github.vooiid.skyminigames.bukkit.lobby.menus.ServersInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuPlayerListener implements Listener
{

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickListener(final InventoryClickEvent event) {
	    final Player p = (Player)event.getWhoClicked();
	    if (event.getInventory().getName().startsWith("§8Select")) {
	        event.setCancelled(true);
	        if (event.getCurrentItem().getType() == Material.EYE_OF_ENDER) {
	            p.closeInventory();
                p.sendMessage("§ePlease set connect server or type on MenuPlayerListener.java\n\n§9By: vooiid (github.com/vooiid)");
            }
			event.setCancelled(true);
        }
		event.setCancelled(true);
    }

}
