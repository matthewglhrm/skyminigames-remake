package io.github.vooiid.skyminigames.bukkit.item;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ItemLore {

    public static List<String> formatForLore(String text) {
        return getLore(30, text);
    }

    public static List<String> getLore(int max, String text) {
        List<String> lore = new ArrayList<>();
        text = ChatColor.translateAlternateColorCodes('&', text);
        String[] split = text.split(" ");
        String color = "";
        text = "";

        for (int i = 0; i < split.length; i++) {
            if (ChatColor.stripColor(text).length() >= max || ChatColor.stripColor(text).endsWith(".")
                    || ChatColor.stripColor(text).endsWith("!")) {
                lore.add(text);
                if (text.endsWith(".") || text.endsWith("!"))
                    lore.add("");
                text = color;
            }

            String toAdd = split[i];

            if (toAdd.contains("§"))
                color = ChatColor.getLastColors(toAdd.toLowerCase());

            if (toAdd.contains("\n")) {
                toAdd = toAdd.substring(0, toAdd.indexOf("\n"));
                split[i] = split[i].substring(toAdd.length() + 1);
                lore.add(text + (text.length() == 0 ? "" : " ") + toAdd);
                text = color;
                i--;
            } else {
                text += (ChatColor.stripColor(text).length() == 0 ? "" : " ") + toAdd;
            }
        }
        lore.add(text);
        return lore;
    }

}
