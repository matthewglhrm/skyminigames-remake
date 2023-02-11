package io.github.vooiid.skyminigames.bukkit.api.vanish;

import io.github.vooiid.skyminigames.bukkit.api.PlayerCancellableEvent;
import org.bukkit.entity.Player;
import lombok.Getter;

@Getter
public class PlayerHideToPlayerEvent extends PlayerCancellableEvent {

    private Player toPlayer;

    public PlayerHideToPlayerEvent(Player player, Player toPlayer) {
        super(player);
        this.toPlayer = toPlayer;
    }
}

