package io.github.vooiid.skyminigames.bukkit.lobby.menus;

import io.github.vooiid.skyminigames.bukkit.BukkitMain;
import io.github.vooiid.skyminigames.bukkit.item.ItemBuilder;
import io.github.vooiid.skyminigames.bukkit.item.MenuInventory;
import io.github.vooiid.skyminigames.bukkit.item.MenuUpdateHandler;
import io.github.vooiid.skyminigames.bukkit.item.click.ClickType;
import io.github.vooiid.skyminigames.bukkit.item.click.MenuClickHandler;
import io.github.vooiid.skyminigames.bukkit.server.types.ServerType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.RequiredArgsConstructor;

public class ServersInventory {

    public ServersInventory(Player player) {
        MenuInventory menuInventory = new MenuInventory("§8Select game mode", 3);

        createItens(player, menuInventory);

        menuInventory.setUpdateHandler(new MenuUpdateHandler() {

            @Override
            public void onUpdate(Player player, MenuInventory menu) {
                createItens(player, menuInventory);
            }
        });

        menuInventory.open(player);
    }

    public void createItens(Player player, MenuInventory menuInventory) {

        menuInventory.setItem(10,
                new ItemBuilder().name("§aSky Wars").type(Material.EYE_OF_ENDER)
                        .lore("", "§e0 playing ago",
                                "")
                        .build(),
                (p, inv, type, stack, slot) -> {
                    // ação
                    player.sendMessage("§eCreate enum ServerType to Connect\nBy vooiid");
                });
    }

    @RequiredArgsConstructor
    public static class SendClick implements MenuClickHandler {

        private final String serverId;

        @Override
        public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
            BukkitMain.getInstance().sendPlayer(p, serverId);
        }

    }
}
