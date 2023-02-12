package io.github.vooiid.skyminigames.bukkit.server.types;

import io.github.vooiid.skyminigames.bukkit.BukkitMain;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServerType {

    LOGIN, LOBBY,
    
    SW_SOLO, SW_TEAM, SW_SQUAD,

    BW_SOLO, BW_TEAM, BW_SQUAD,

    NETWORK, NONE;

    public boolean isLobby() {
        return this.name().contains("LOBBY");
    }

    public ServerType getServerLobby() {
        switch (this) {
            case SW_SOLO:
                return LOBBY;
            default:
                return LOBBY;
        }
    }

    public boolean canSendData() {
        return true;
    }

}
