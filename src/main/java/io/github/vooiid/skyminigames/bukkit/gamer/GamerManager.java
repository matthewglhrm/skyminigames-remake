package io.github.vooiid.skyminigames.bukkit.gamer;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GamerManager {

    private HashMap<UUID, Gamer> gamers;

    public void loadGamer(UUID uuid, Gamer gamer) {
        gamers.put(uuid, gamer);
    }

    public Gamer getGamer(UUID uuid) {
        return gamers.get(uuid);
    }

    public Collection<Gamer> getGamers() {
        if (gamers == null)
            return null;

        if (gamers.isEmpty())
            return null;
        return gamers.values();
    }

    public void unloadGamer(UUID uuid) {
        gamers.remove(uuid);
    }

}
