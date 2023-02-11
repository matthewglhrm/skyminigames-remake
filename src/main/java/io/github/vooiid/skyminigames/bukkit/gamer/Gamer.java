package io.github.vooiid.skyminigames.bukkit.gamer;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class Gamer {

    private String name;
    private UUID uniqueId;

    @Setter
    private boolean flying = false;

    public Gamer(Player player) {
        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
    }
}
