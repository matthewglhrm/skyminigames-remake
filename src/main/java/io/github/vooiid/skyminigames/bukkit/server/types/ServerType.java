package io.github.vooiid.skyminigames.bukkit.server.types;

public enum ServerType {

    ATLANTIDA("atlantida", "135.148.6.7", ":25642 "),
    OVERPOWER("overpower", "135.148.6.7", "25631"),
    BUNGEE("bungeecord", "135.148.6.7", "25645"),
    LOBBY("lobby", "135.148.6.7", "25640");

    public String name;
    public String ip;
    public String port;

    ServerType(String name, String ip, String port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public static ServerType getServer(ServerType server) {
        for (ServerType serverType : values()) {
            if (serverType.getName().equalsIgnoreCase(server.getName())) {
                return server;
            }
        }
        return null;
    }

}
