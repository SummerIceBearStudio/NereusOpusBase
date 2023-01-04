package hamsteryds.nereusopus.enchants.simple;

import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class InfernalTouch extends EventExecutor {
    public InfernalTouch(File file) {
        super(file);
    }

    @Override
    public void blockDropItem(int level, BlockDropItemEvent event) {
        int amount = 0;
        String blockName = event.getBlockState().getType().toString();
        if (!blockName.endsWith("ORE") && !blockName.equals("STONE")
                && !blockName.equals("NETHERRACK")
                && !blockName.equals("SAND")) {
            return;
        }
        boolean flag = false;
        for (Item dropItem : event.getItems()) {
            ItemStack drop = dropItem.getItemStack();
            String typeName = drop.getType().toString();
            if (typeName.contains("NETHERRACK")) {
                drop.setType(Material.NETHER_BRICK);
                amount += drop.getAmount();
                flag = true;
            }
            if (typeName.contains("COBBLESTONE")) {
                drop.setType(Material.STONE);
                amount += drop.getAmount();
                flag = true;
            }
            if (typeName.equals("SAND")) {
                drop.setType(Material.GLASS);
                amount += drop.getAmount();
                flag = true;
            }
            if (typeName.contains("IRON_ORE") || typeName.contains("RAW_IRON")) {
                drop.setType(Material.IRON_INGOT);
                amount += drop.getAmount();
            }
            if (typeName.contains("GOLD_ORE") || typeName.contains("RAW_GOLD")) {
                drop.setType(Material.GOLD_INGOT);
                amount += drop.getAmount();
            }
            if (typeName.contains("COPPER_ORE") || typeName.contains("RAW_COPPER")) {
                drop.setType(Material.COPPER_INGOT);
                amount += drop.getAmount();
            }
            dropItem.setItemStack(drop);
        }
        if (getBool("drop-xp")) {
            World world = event.getPlayer().getWorld();
            Location loc = event.getPlayer().getLocation();
            ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(loc, EntityType.EXPERIENCE_ORB);
            orb.setExperience(flag ? amount : amount * 2);
        }
    }
}
