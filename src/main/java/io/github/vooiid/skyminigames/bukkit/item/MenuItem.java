package io.github.vooiid.skyminigames.bukkit.item;

import io.github.vooiid.skyminigames.bukkit.item.click.ClickType;
import io.github.vooiid.skyminigames.bukkit.item.click.MenuClickHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuItem {

    private ItemStack stack;
    private MenuClickHandler handler;

    public MenuItem(ItemStack itemstack) {
        this.stack = itemstack;
        this.handler = new MenuClickHandler() {
            @Override
            public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
            }
        };
    }

    public MenuItem(ItemStack itemstack, MenuClickHandler clickHandler) {
        this.stack = itemstack;
        this.handler = clickHandler;
    }

    public ItemStack getStack() {
        return stack;
    }

    public MenuClickHandler getHandler() {
        return handler;
    }

    public void destroy() {
        stack = null;
        handler = null;
    }
}
