package io.github.vooiid.skyminigames.bukkit.item.api;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

import lombok.Getter;

public class ActionItemStack {

    private static final HashMap<Integer, Interact> HANDLERS = new HashMap<>();

    @Getter
    private Interact interactHandler;
    @Getter
    private ItemStack itemStack;

    public ActionItemStack(ItemStack stack, Interact handler) {
        itemStack = setTag(stack, registerHandler(handler));

        if (itemStack == null)
            itemStack = stack;

        interactHandler = handler;
    }

    public static int registerHandler(Interact handler) {
        if (HANDLERS.containsValue(handler))
            return HANDLERS.entrySet().stream().filter(entry -> entry.getValue() == handler).map(Entry::getKey)
                    .findFirst().orElse(-1);

        HANDLERS.put(HANDLERS.size() + 1, handler);
        return HANDLERS.size();
    }

    public static void unregisterHandler(Integer id) {
        HANDLERS.remove(id);
    }

    public static void unregisterHandler(Interact handler) {
        Iterator<Entry<Integer, Interact>> iterator = HANDLERS.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Integer, Interact> entry = iterator.next();

            if (entry.getValue() == handler) {
                iterator.remove();
                break;
            }
        }
    }

    public static Interact getHandler(Integer id) {
        return HANDLERS.get(id);
    }

    public static ItemStack setTag(ItemStack stack, int id) {
        try {
            if (stack == null || stack.getType() == Material.AIR)
                throw new Exception();
            Constructor<?> caller = MinecraftReflection.getCraftItemStackClass()
                    .getDeclaredConstructor(ItemStack.class);
            caller.setAccessible(true);
            ItemStack item = (ItemStack) caller.newInstance(stack);
            NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(item);
            compound.put("interactHandler", id);
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ActionItemStack create(ItemStack stack, Interact handler) {
        return new ActionItemStack(stack, handler);
    }

    @Getter
    public static abstract class Interact {

        private InteractType interactType;
        private boolean inventoryClick;

        public Interact() {
            this.interactType = InteractType.CLICK;
        }

        public Interact(InteractType interactType) {
            this.interactType = interactType;
        }

        public Interact setInventoryClick(boolean inventoryClick) {
            this.inventoryClick = inventoryClick;
            return this;
        }

        public abstract boolean onInteract(Player player, Entity entity, Block block, ItemStack item,
                                           ActionType action);
    }

    public enum ActionType {

        CLICK_PLAYER, RIGHT, LEFT;

    }

    public enum InteractType {

        PLAYER, CLICK;

    }

}
