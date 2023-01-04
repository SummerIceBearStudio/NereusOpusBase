package hamsteryds.nereusopus.utils.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface GuiEntry {
    default void open(InventoryOpenEvent event) {
    }

    default void close(InventoryCloseEvent event) {
    }

    default void closeAll(InventoryCloseEvent event) {
    }

    default void clickTop(InventoryClickEvent event) {
    }

    default void clickDown(InventoryClickEvent event) {
    }

    default void click(InventoryClickEvent event) {
    }
}
