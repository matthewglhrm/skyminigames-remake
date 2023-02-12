package io.github.vooiid.skyminigames.bukkit.item.api;

import io.github.vooiid.skyminigames.bukkit.api.PlayerCancellableEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;

@Getter
public class MenuOpenEvent extends PlayerCancellableEvent {

    private Inventory inventory;

    public MenuOpenEvent(Player player, Inventory inventory) {
        super(player);
        this.inventory = inventory;
    }

}
