package io.github.vooiid.skyminigames.bukkit.group;

import org.bukkit.entity.Player;

/*

 * System roles forked for: Kotleyxz (Mardok)

*/

public enum Group {
    ADM("ADM", "§9[ADM] §f", "§9", "§9ADM", "role.adm"),
    MEMBRO("Membro", "§7", "§7", "§7Membro", "");

    public String name, prefix, coloredName, coloredTag, permission;

    Group(String name, String prefix, String coloredName, String coloredTag, String permission){
        this.name = name;
        this.prefix = prefix;
        this.coloredName = coloredName;
        this.coloredTag = coloredTag;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getColoredName() {
        return coloredName;
    }

    public String getColoredTag() {
        return coloredTag;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasRolePermission(Player p) {
        return permission.isEmpty() || p.hasPermission(permission);
    }

    public static Group getRole(Player p) {
        for (Group role : values()) {
            if (role.hasRolePermission(p)) {
                return role;
            }
        }
        return MEMBRO;
    }

}
