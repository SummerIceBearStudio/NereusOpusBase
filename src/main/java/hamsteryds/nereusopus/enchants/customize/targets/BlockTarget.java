package hamsteryds.nereusopus.enchants.customize.targets;

import hamsteryds.nereusopus.enchants.customize.internal.Mutable;
import hamsteryds.nereusopus.enchants.customize.internal.ParamType;
import hamsteryds.nereusopus.enchants.customize.internal.Target;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class BlockTarget extends Target implements Mutable {
    public BlockTarget() {
        super("BLOCK");
    }

    @Override
    public Pair<ParamType, Object> get(Object obj, String name) {
        Block block = (Block) obj;
        return switch (name) {
            case "biome" -> new Pair<>(ParamType.STRING, block.getBiome().toString());
            case "block_data" -> new Pair<>(ParamType.BLOCKDATA, block.getBlockData());
            case "block_power" -> new Pair<>(ParamType.INTEGER, block.getBlockPower());
            case "chunk" -> new Pair<>(ParamType.CHUNK, block.getChunk());
            case "humidity" -> new Pair<>(ParamType.DOUBLE, block.getHumidity());
            case "light_level" -> new Pair<>(ParamType.INTEGER, block.getLightLevel());
            case "light_sky" -> new Pair<>(ParamType.INTEGER, block.getLightFromSky());
            case "light_block" -> new Pair<>(ParamType.INTEGER, block.getLightFromBlocks());
            case "location" -> new Pair<>(ParamType.LOCATION, block.getLocation());
            case "temperature" -> new Pair<>(ParamType.DOUBLE, block.getTemperature());
            case "type" -> new Pair<>(ParamType.MATERIAL, block.getType());
            case "world" -> new Pair<>(ParamType.WORLD, block.getWorld());
            case "x" -> new Pair<>(ParamType.INTEGER, block.getX());
            case "y" -> new Pair<>(ParamType.INTEGER, block.getY());
            case "z" -> new Pair<>(ParamType.INTEGER, block.getZ());
            case "is_indirectly_powered" -> new Pair<>(ParamType.BOOLEAN, block.isBlockIndirectlyPowered());
            case "is_powered" -> new Pair<>(ParamType.BOOLEAN, block.isBlockPowered());
            case "is_empty" -> new Pair<>(ParamType.BOOLEAN, block.isEmpty());
            case "is_liquid" -> new Pair<>(ParamType.BOOLEAN, block.isLiquid());
            case "is_passable" -> new Pair<>(ParamType.BOOLEAN, block.isPassable());
            default -> super.get(obj, name);
        };
    }

    @Override
    public void set(Object obj, String name, Object value) {
        Block block = (Block) obj;
        switch (name) {
            case "apply_bone_meal" -> block.applyBoneMeal(BlockFace.valueOf(value.toString()));
            case "break_naturally" -> block.breakNaturally();
            case "break_naturally_by_tool" -> block.breakNaturally((ItemStack) value);
            case "biome" -> block.setBiome(Biome.valueOf(value.toString()));
            case "block_data" -> block.setBlockData((BlockData) value);
            case "type" -> block.setType((Material) value);
            default -> {
            }
        }
    }
}
