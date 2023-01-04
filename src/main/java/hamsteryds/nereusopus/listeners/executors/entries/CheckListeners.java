package hamsteryds.nereusopus.listeners.executors.entries;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashSet;

public class CheckListeners implements Listener {
    public static HashSet<Event> events = new HashSet<>();

    public static void addCheckEvent(Event event) {
        events.add(event);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBreak(BlockBreakEvent event) {
        if (events.contains(event) && !event.isCancelled()) {
            event.setCancelled(true);
            events.remove(event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (events.contains(event) && !event.isCancelled()) {
            event.setCancelled(true);
            events.remove(event);
        }
    }
}
