package io.github.vooiid.skyminigames.bukkit.item;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ItemBuilder {

    private Material material;
    private int amount;
    private short durability;
    private boolean useMeta;
    private boolean glow;
    private String displayName;
    private HashMap<Enchantment, Integer> enchantments;
    private List<String> lore;

    private Color color;
    private String skinOwner;
    private String skinUrl;

    private boolean hideAttributes;
    private boolean unbreakable;

    public ItemBuilder() {
        material = Material.STONE;
        amount = 1;
        durability = 0;
        hideAttributes = false;
        unbreakable = false;
        useMeta = false;
        glow = false;
    }

    public ItemBuilder type(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder amount(int amount) {
        if (amount > material.getMaxStackSize())
            amount = material.getMaxStackSize();
        if (amount <= 0)
            amount = 1;
        this.amount = amount;
        return this;
    }

    public ItemBuilder durability(short durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.durability = (short) durability;
        return this;
    }

    public ItemBuilder name(String text) {
        if (!useMeta) {
            useMeta = true;
        }
        this.displayName = text.replace("&", "§");
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        return enchantment(enchantment, 1);
    }

    public ItemBuilder enchantment(Enchantment enchantment, Integer level) {
        if (enchantments == null) {
            enchantments = new HashMap<>();
        }

        if (level == 0)
            return this;

        enchantments.put(enchantment, level);
        return this;
    }

    public ItemBuilder lore(String text) {
        if (!this.useMeta) {
            this.useMeta = true;
        }

        if (this.lore == null)
            this.lore = new ArrayList<>(ItemLore.getLore(30, text));
        else
            this.lore.addAll(ItemLore.getLore(30, text));

        return this;
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(List<String> text) {
        if (!this.useMeta) {
            this.useMeta = true;
        }
        if (this.lore == null) {
            this.lore = new ArrayList<>();
        }
        for (String str : text) {
            this.lore.add(str.replace("&", "§"));
        }
        return this;
    }

    public ItemBuilder glow() {
        glow = true;
        return this;
    }

    public ItemBuilder color(Color color) {
        this.useMeta = true;
        this.color = color;
        return this;
    }

    public ItemBuilder skin(String skin) {
        this.useMeta = true;
        this.skinOwner = skin;
        return this;
    }

    public ItemBuilder skinURL(String skinURL) {
        this.useMeta = true;
        this.skinUrl = skinURL;
        return this;
    }

    public ItemBuilder hideAttributes() {
        this.useMeta = true;
        this.hideAttributes = true;
        return this;
    }

    public ItemBuilder showAttributes() {
        this.useMeta = true;
        this.hideAttributes = false;
        return this;
    }

    public ItemBuilder unbreakable() {
        this.unbreakable = true;
        return this;
    }

    public ItemStack build() {
        ItemStack stack = new ItemStack(material, amount, durability);

        if (enchantments != null && !enchantments.isEmpty()) {
            for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                stack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
            }
        }

        if (useMeta) {
            ItemMeta meta = stack.getItemMeta();

            if (displayName != null) {
                meta.setDisplayName(displayName.replace("&", "§"));
            }

            if (lore != null && !lore.isEmpty()) {
                meta.setLore(lore);
            }

            /** Colored Leather Armor */
            if (color != null) {
                if (meta instanceof LeatherArmorMeta) {
                    ((LeatherArmorMeta) meta).setColor(color);
                }
            }

            /** Skull Heads */
            if (meta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) meta;
                if (skinUrl != null) {
                    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                    profile.getProperties().put("textures",
                            new Property("textures",
                                    Base64.getEncoder()
                                            .encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinUrl)
                                                    .getBytes(StandardCharsets.UTF_8))));
                    try {
                        Field field = skullMeta.getClass().getDeclaredField("profile");
                        field.setAccessible(true);
                        field.set(skullMeta, profile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (skinOwner != null) {
                    skullMeta.setOwner(skinOwner);
                }
            }

            meta.spigot().setUnbreakable(unbreakable);

            /** Item Flags */
            if (hideAttributes) {
                meta.addItemFlags(ItemFlag.values());
            } else {
                meta.removeItemFlags(ItemFlag.values());
            }

            stack.setItemMeta(meta);
        }

        if (glow && (enchantments == null || enchantments.isEmpty())) {
            try {
                Constructor<?> caller = MinecraftReflection.getCraftItemStackClass()
                        .getDeclaredConstructor(ItemStack.class);
                caller.setAccessible(true);
                ItemStack item = (ItemStack) caller.newInstance(stack);
                NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(item);
                compound.put(NbtFactory.ofList("ench"));
                return item;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                     | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        material = Material.STONE;
        amount = 1;
        durability = 0;

        if (useMeta) {
            useMeta = false;
        }

        if (glow) {
            glow = false;
        }

        if (hideAttributes) {
            hideAttributes = false;
        }

        if (unbreakable) {
            unbreakable = false;
        }

        if (displayName != null) {
            displayName = null;
        }

        if (enchantments != null) {
            enchantments.clear();
            enchantments = null;
        }

        if (lore != null) {
            lore.clear();
            lore = null;
        }

        skinOwner = null;
        skinUrl = null;
        color = null;
        return stack;
    }

    public static ItemStack glow(ItemStack stack) {
        try {
            Constructor<?> caller = MinecraftReflection.getCraftItemStackClass()
                    .getDeclaredConstructor(ItemStack.class);
            caller.setAccessible(true);
            ItemStack item = (ItemStack) caller.newInstance(stack);
            NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(item);
            compound.put(NbtFactory.ofList("ench"));
            return item;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                 | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return stack;
    }

    public static ItemBuilder fromStack(ItemStack stack) {
        ItemBuilder builder = new ItemBuilder().type(stack.getType()).amount(stack.getAmount())
                .durability(stack.getDurability());

        if (stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();

            if (meta.hasDisplayName())
                builder.name(meta.getDisplayName());

            if (meta.hasLore())
                builder.lore(meta.getLore());

            if (meta instanceof LeatherArmorMeta) {
                Color color = ((LeatherArmorMeta) meta).getColor();
                if (color != null)
                    builder.color(color);
            }

            if (meta instanceof SkullMeta) {
                SkullMeta sm = (SkullMeta) meta;
                if (sm.hasOwner())
                    builder.skin(sm.getOwner());
            }
        }

        return builder;
    }
}
