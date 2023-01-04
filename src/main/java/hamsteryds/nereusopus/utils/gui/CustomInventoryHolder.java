package hamsteryds.nereusopus.utils.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public class CustomInventoryHolder implements InventoryHolder {
    public String type;
    public HashMap<String, Object> data;

    public <E> CustomInventoryHolder(String type) {
        this.type = type;
        this.data = new HashMap<>();
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public <E> CustomInventoryHolder setData(String key, E value) {
        data.put(key, value);
        return this;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
