package io.github.vooiid.skyminigames.bukkit.lobby.player;

import io.github.vooiid.skyminigames.bukkit.group.Group;
import io.github.vooiid.skyminigames.bukkit.group.system.Manager;
import io.github.vooiid.skyminigames.bukkit.item.ItemBuilder;
import io.github.vooiid.skyminigames.bukkit.item.api.ActionItemStack;
import io.github.vooiid.skyminigames.bukkit.packets.system.TablistAPI;
import io.github.vooiid.skyminigames.bukkit.packets.system.TitleAPI;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.audio.AudioPlayer;

public class PlayerListener implements Listener
{

    private static ActionItemStack compass;
    private static ActionItemStack store;
    private static ActionItemStack lobbies;
    private static ActionItemStack collectable;
    private static ActionItemStack players_hide;

    public PlayerListener() {
        //compass menu
        compass = new ActionItemStack(new ItemBuilder().name("§eGames Menu").type(Material.COMPASS).build(),
                new ActionItemStack.Interact(ActionItemStack.InteractType.CLICK) {

                    @Override
                    public boolean onInteract(Player player, Entity entity, Block block, ItemStack item,
                                              ActionItemStack.ActionType action) {
                        return false;
                    }
                });

        //lobbies menu
        lobbies = new ActionItemStack(new ItemBuilder().name("§eSelect Another Hub").type(Material.NETHER_STAR).build(),
                new ActionItemStack.Interact(ActionItemStack.InteractType.CLICK) {

                    @Override
                    public boolean onInteract(Player player, Entity entity, Block block, ItemStack item,
                                              ActionItemStack.ActionType action) {
                        return false;
                    }
                });

        //collectable menu
        collectable = new ActionItemStack(new ItemBuilder().name("§eCollectables").type(Material.CHEST).build(),
                new ActionItemStack.Interact(ActionItemStack.InteractType.CLICK) {

                    @Override
                    public boolean onInteract(Player player, Entity entity, Block block, ItemStack item,
                                              ActionItemStack.ActionType action) {
                        return false;
                    }

                });

        //store games menu
        store = new ActionItemStack(new ItemBuilder().name("§eStore Games").type(Material.EMERALD).build(),
                new ActionItemStack.Interact(ActionItemStack.InteractType.CLICK) {

                    @Override
                    public boolean onInteract(Player player, Entity entity, Block block, ItemStack item,
                                              ActionItemStack.ActionType action) {
                        return false;
                    }

                });

        //players hide interact
        players_hide = new ActionItemStack(new ItemBuilder().name("§eHide Players §7(Click Here)").type(Material.INK_SACK).durability(10).build(),
                new ActionItemStack.Interact(ActionItemStack.InteractType.CLICK) {

                    @Override
                    public boolean onInteract(Player player, Entity entity, Block block, ItemStack item,
                                              ActionItemStack.ActionType action) {
                        return false;
                    }

                });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        //join message null
        event.setJoinMessage(null);

        //join events
        p.getInventory().clear();
        p.getInventory().setArmorContents(new ItemStack[4]);

        p.setHealth(20D);
        p.setFoodLevel(20);
        p.setGameMode(GameMode.ADVENTURE);
        p.teleport(p.getWorld().getSpawnLocation());

        //title player join
        TitleAPI.setTitle(p, "§6§lSKY MINIGAMES", "§fKibed by vooiid", 30, 20, 0, true);

        //Hotbar player join
        setHotbar(p);

        //Tablist player
        TablistAPI.sendTablistPlayer(p,
                "§a§lSKY MINIGAMES",
                "§6Site: §fwww.skyminigames.com\n\n§6Adquira VIP acessando: §fskyminigames.com/loja");

        //Player join set role
        Manager.setRole(p);
        Group role = Group.getRole(p);

        //On message welcome
        if (p.hasPermission("role.vip")) {
            event.setJoinMessage(role.getPrefix() + p.getName() + " §6entrou neste lobby!");
        }

    }

    public static void setHotbar(Player p) {

        //perfil menu
        p.getInventory()
                .setItem(1,
                        new ActionItemStack(
                                new ItemBuilder().name("§ePerfil").skin(p.getName()).durability(3)
                                        .type(Material.SKULL_ITEM).build(),
                                new ActionItemStack.Interact(ActionItemStack.InteractType.CLICK) {

                                    @Override
                                    public boolean onInteract(Player player, Entity entity, Block block, ItemStack item,
                                                              ActionItemStack.ActionType action) {
                                        return false;
                                    }
                                }).getItemStack());

        p.getInventory()
                .setItem(0, compass.getItemStack());

        p.getInventory()
                .setItem(3, store.getItemStack());

        p.getInventory()
                .setItem(4, collectable.getItemStack());

        p.getInventory()
                .setItem(7, players_hide.getItemStack());

        p.getInventory()
                .setItem(8, lobbies.getItemStack());

    }

    @EventHandler
    public void onInteractListener(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction().toString().contains("RIGHT")) {
            ItemStack item = event.getItem();
            if (item != null) {
                if (item.getType() == Material.INK_SACK) {
                    ItemMeta itemMeta = item.getItemMeta();
                    if (itemMeta != null) {

                    }
                }
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {

        Player p = event.getPlayer();
        String msg = event.getMessage();
        String name = p.getName();
        Group role = Group.getRole(p);

        //Async player format chat for permission
        if (p.hasPermission("role.vip")) {
            event.setFormat(role.getPrefix() + name + "&8: &f" + msg.replace("&", "§"));
        } else {
            event.setFormat("&f" + name + "&8: &7" + msg.replace("&", "§"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        //On player quit remove role system
        Player p = event.getPlayer();
        Manager.removeRole(p);
    }

}
