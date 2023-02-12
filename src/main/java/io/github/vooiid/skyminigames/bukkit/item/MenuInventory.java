package io.github.vooiid.skyminigames.bukkit.item;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.vooiid.skyminigames.bukkit.BukkitMain;
import io.github.vooiid.skyminigames.bukkit.item.api.MenuOpenEvent;
import io.github.vooiid.skyminigames.bukkit.item.click.MenuClickHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import lombok.Getter;
import lombok.Setter;

public class MenuInventory {

    private HashMap<Integer, MenuItem> slotItem;
    private int rows;
    private String title;
    private Inventory inv;
    private boolean onePerPlayer;

    @Getter
    @Setter
    private MenuUpdateHandler updateHandler;

    @Setter
    @Getter
    private boolean reopenInventory = false;

    private static Map<String, Long> openDelay = new HashMap<>();

    public MenuInventory(String title, int rows) {
        this(title, rows, false);
    }

    public MenuInventory(String title, int rows, boolean onePerPlayer) {
        this.slotItem = new HashMap<>();
        this.rows = rows;
        this.title = title;
        this.onePerPlayer = onePerPlayer;

        if (!onePerPlayer) {
            this.inv = Bukkit.createInventory(new MenuHolder(this), rows * 9, "");
        }
    }

    public void addItem(MenuItem item) {
        setItem(firstEmpty(), item);
    }

    public void addItem(ItemStack item) {
        setItem(firstEmpty(), item);
    }

    public void setItem(ItemStack item, int slot) {
        setItem(slot, new MenuItem(item));
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, new MenuItem(item));
    }

    public void setItem(int slot, ItemStack item, MenuClickHandler handler) {
        setItem(slot, new MenuItem(item, handler));
    }

    public void setItem(MenuItem item, int slot) {
        setItem(slot, item);
    }

    public void setItem(int slot, MenuItem item) {
        this.slotItem.put(slot, item);

        if (!onePerPlayer) {
            inv.setItem(slot, item.getStack());
        }
    }

    public int firstEmpty() {
        if (!onePerPlayer) {
            return inv.firstEmpty();
        } else {
            for (int i = 0; i < rows * 9; i++) {
                if (!slotItem.containsKey(i)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public boolean hasItem(int slot) {
        return this.slotItem.containsKey(slot);
    }

    public MenuItem getItem(int slot) {
        return this.slotItem.get(slot);
    }

    public void clear() {
        slotItem.clear();
        if (!onePerPlayer) {
            inv.clear();
        }
    }

    public void open(Player p) {
        if (isCooldown(p.getName()))
            return;

        if (!onePerPlayer) {
            p.openInventory(inv);
            Bukkit.getPluginManager().callEvent(new MenuOpenEvent(p, inv));
        } else {
            if (p.getOpenInventory() == null//
                    || p.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST//
                    || p.getOpenInventory().getTopInventory().getSize() != rows * 9
                    || p.getOpenInventory().getTopInventory().getHolder() == null//
                    || !(p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder)//
                    || !(((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).isOnePerPlayer())) {
                createAndOpenInventory(p);
            } else {
                Inventory topInventory = p.getOpenInventory().getTopInventory();

                for (int i = 0; i < rows * 9; i++) {
                    if (slotItem.containsKey(i)) {
                        ItemStack oldItem = p.getOpenInventory().getTopInventory().getItem(i);
                        ItemStack newItem = slotItem.get(i).getStack();

                        if (oldItem == null || newItem == null) {
                            topInventory.setItem(i, newItem);
                            continue;
                        }

                        boolean update = !(oldItem.getType() == newItem.getType()
                                && oldItem.getDurability() == newItem.getDurability()
                                && oldItem.getAmount() == newItem.getAmount());

                        if (update)
                            topInventory.setItem(i, newItem);
                    } else
                        topInventory.setItem(i, null);
                }
            }

            Bukkit.getPluginManager().callEvent(new MenuOpenEvent(p, p.getOpenInventory().getTopInventory()));
            ((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).setMenu(this);
        }

        updateTitle(p);
        setCooldown(p.getName());
    }

    public void updateSlot(Player player, int slot) {
        if (slotItem.containsKey(slot))
            player.getOpenInventory().getTopInventory().setItem(slot, slotItem.get(slot).getStack());
        else
            player.getOpenInventory().getTopInventory().setItem(slot, null);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void updateTitle(Player p) {
        try {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(title));
            Method getHandle = MinecraftReflection.getCraftPlayerClass().getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(p);
            Field activeContainerField = entityPlayer.getClass().getField("activeContainer");
            Object activeContainer = activeContainerField.get(entityPlayer);
            Field windowIdField = activeContainer.getClass().getField("windowId");
            int id = windowIdField.getInt(activeContainer);
            packet.getStrings().write(0, "minecraft:chest");
            packet.getIntegers().write(0, id);
            packet.getIntegers().write(1, rows * 9);

            BukkitMain.getInstance().getProtocolManager().sendServerPacket(p, packet);
            p.updateInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAndOpenInventory(Player p) {
        Inventory playerInventory = Bukkit.createInventory(new MenuHolder(this), rows * 9, this.title);

        for (Entry<Integer, MenuItem> entry : slotItem.entrySet()) {
            playerInventory.setItem(entry.getKey(), entry.getValue().getStack());
        }

        p.openInventory(playerInventory);
    }

    public void close(Player p) {
        if (onePerPlayer) {
            destroy(p);
            p = null;
        }
    }

    public void destroy(Player p) {
        if (p.getOpenInventory().getTopInventory().getHolder() != null
                && p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
            ((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).destroy();
        }
    }

    public boolean isOnePerPlayer() {
        return onePerPlayer;
    }

    public Inventory getInventory() {
        return inv;
    }

    public boolean isCooldown(String playerName) {
        return openDelay.containsKey(playerName) && openDelay.get(playerName) > System.currentTimeMillis();
    }

    public void setCooldown(String playerName) {
        openDelay.put(playerName, System.currentTimeMillis() + 200);
    }

}
