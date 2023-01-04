package hamsteryds.nereusopus.utils.gui;

import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public abstract class CustomGui implements Listener, GuiEntry {
    public static HashMap<UUID, List<Inventory>> openedInvs = new HashMap<>();
    public String title;
    public int size;
    public String type;
    public HashMap<String, List<Integer>> keyedSections = new HashMap<>();
    public HashMap<String, List<String>> sectionData = new HashMap<>();
    public HashMap<String, ItemStack> sectionContents = new HashMap<>();

    public CustomGui(YamlConfiguration yaml) {
        title = yaml.getString("title");
        size = yaml.getInt("size");
        type = yaml.getString("type");
        ConfigurationSection section = yaml.getConfigurationSection("contents");
        for (String path : section.getKeys(false)) {
            keyedSections.put(path, section.getIntegerList(path + ".slots"));
            if (section.contains(path + ".data")) {
                sectionData.put(path, section.getStringList(path + ".data"));
            }
            if (section.contains(path + ".item")) {
                ItemStack item = ItemUtils.fromString(section.getString(path + ".item"));
                if (item != null) {
                    if (item.getType().toString().equalsIgnoreCase("PLAYER_HEAD") ||
                            item.getType().toString().equalsIgnoreCase("SKULL_ITEM")) {
                        if (section.contains(path + ".skull")) {
                            ItemUtils.setSkull(item, section.getString(path + ".skull"));
                        }
                    }
                }
                sectionContents.put(path, item);
            }
        }
        Bukkit.getPluginManager().registerEvents(new BasicInvListener(), NereusOpus.plugin);
    }

    public static void record(Player player, Inventory inv) {
        if (openedInvs.containsKey(player.getUniqueId())) {
            if (!openedInvs.get(player.getUniqueId()).contains(inv)) {
                openedInvs.get(player.getUniqueId()).add(inv);
            }
        } else {
            List<Inventory> list = new ArrayList<>();
            list.add(inv);
            openedInvs.put(player.getUniqueId(), list);
        }
    }

    public static void reset(Player player) {
        openedInvs.remove(player.getUniqueId());
    }

    public static void back(Player player) {
        if (openedInvs.containsKey(player.getUniqueId())) {
            List<Inventory> list = openedInvs.get(player.getUniqueId());
            if (list.size() > 1) {
                player.openInventory(list.get(list.size() - 2));
                list.remove(list.size() - 1);
                openedInvs.put(player.getUniqueId(), list);
            } else {
                player.closeInventory();
            }
        }
    }

    public static void reopen(Player player, List<Inventory> reopens) {
        if (reopens.size() > 1) {
            player.openInventory(reopens.get(reopens.size() - 1));
            openedInvs.put(player.getUniqueId(), reopens);
        }
    }

    public Inventory getInitGui(Inventory inv) {
        return getInitGui(inv, new HashMap<>());
    }

    @SafeVarargs
    public final <E> Inventory getInitGui(Inventory inv, HashMap<String, String> titleParams, E... data) {
        String title = this.title;
        for (String key : titleParams.keySet()) {
            title = title.replace(key, titleParams.get(key));
        }
        if (inv == null) {
            CustomInventoryHolder holder = new CustomInventoryHolder(type);
            for (int i = 0; i < data.length; i += 2) {
                holder.setData(String.valueOf(data[i]), data[i + 1]);
            }
            inv = Bukkit.createInventory(holder, size, title);
        } else {
            inv.clear();
        }
        for (String label : sectionContents.keySet()) {
            for (int slot : keyedSections.get(label)) {
                inv.setItem(slot, sectionContents.get(label));
            }
        }
        return inv;
    }

    public boolean matchSlot(String key, int slot) {
        if (keyedSections.containsKey(key)) {
            return keyedSections.get(key).contains(slot);
        } else {
            return false;
        }
    }

    public boolean matchType(Inventory inv) {
        if (inv == null) {
            return false;
        }
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof CustomInventoryHolder) {
            return ((CustomInventoryHolder) holder).type.equals(type);
        }
        return false;
    }

    public void setItems(Inventory inv, String key, ItemStack item) {
        for (int slot : keyedSections.get(key)) {
            inv.setItem(slot, item);
        }
    }

    private class BasicInvListener implements Listener {
        public HashSet<UUID> openers = new HashSet<>();

        @EventHandler
        public void onOpen(InventoryOpenEvent event) {
            if (matchType(event.getInventory())) {
                openers.add(event.getPlayer().getUniqueId());
                record((Player) event.getPlayer(), event.getInventory());
                open(event);
            }
        }

        @EventHandler
        public void onClose(InventoryCloseEvent event) {
            if (matchType(event.getInventory())) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (event.getPlayer().getOpenInventory() != null) {
                            if (event.getPlayer().getOpenInventory().getTopInventory() != null) {
                                if (event.getPlayer().getOpenInventory().getTopInventory().getSize() == 5) {
                                    reset((Player) event.getPlayer());
                                    closeAll(event);
                                }
                            }
                        }
                    }
                }.runTaskLater(NereusOpus.plugin, 2L);
                close(event);
            }
        }

        @EventHandler
        public void onClick(InventoryClickEvent event) {
            Inventory clicked = event.getClickedInventory();
            Inventory inv = event.getInventory();
            if (clicked == null) {
                return;
            }
            if (matchType(inv)) {
                event.setCancelled(true);
                click(event);
                if (matchType(clicked)) {
                    if (matchSlot("back", event.getSlot())) {
                        back((Player) event.getWhoClicked());
                    }
                    clickTop(event);
                } else {
                    clickDown(event);
                }
            }
        }
    }
}
