package hamsteryds.nereusopus.utils.api;

import dev.lone.itemsadder.api.CustomBlock;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.listeners.executors.entries.BlockListener;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * <p>世界工具类</p>
 */
public class WorldUtils {
    /**
     * <p>生成随机粒子</p>
     * <p>默认 {@code factor} 为 10，{@code circle} 为 1</p>
     *
     * @param particle 粒子
     * @param loc      中心位置
     * @param amount   数量
     * @param option   生成选项
     * @param height   高度
     * @param range    范围
     *
     */
    public static <T> void spawnRNAParticles(Particle particle, Location loc, int amount, T option, double height, double range) {
        spawnRNAParticles(particle, loc, amount, option, height, range, 10, 1);
    }

    /**
     * 生成随机粒子
     *
     * @param particle 粒子
     * @param loc      中心位置
     * @param amount   数量
     * @param option   生成选项
     * @param height   高度
     * @param range    范围
     * @param factor   每一圈生成数量
     * @param circle   生成粒子的圈数
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/World.html#spawnParticle(org.bukkit.Particle,org.bukkit.Location,int,T)">{@code spawnParticle}</a>
     */
    public static <T> void spawnRNAParticles(Particle particle, Location loc, int amount, T option, double height, double range, int factor, int circle) {
        World world = loc.getWorld();
        for (double i = 0, currentH = -height; i <= 360.0 * circle; i += 360.0 / factor, currentH += 2.0 * height / factor / circle) {
            double rad = Math.toRadians(i);
            double sin = Math.sin(rad) * range;
            double cos = Math.cos(rad) * range;
            world.spawnParticle(particle, loc.clone().add(sin, currentH, cos), amount, option);
        }
    }

    /**
     * <p>生成圆形粒子</p>
     * <p>默认factor为10</p>
     *
     * @param particle 粒子
     * @param loc      圆心位置
     * @param amount   单次粒子数量
     * @param option   生成选项
     * @param range    范围
     *
     */
    public static <T> void spawnCircleParticles(Particle particle, Location loc, int amount, T option, double range) {
        spawnCircleParticles(particle, loc, amount, option, range, 10);
    }

    /**
     * 生成圆形粒子
     *
     * @param particle 粒子
     * @param loc      圆心位置
     * @param amount   单次粒子数量
     * @param option   生成选项
     * @param range    范围
     * @param factor   圆周上生成位置数量
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/World.html#spawnParticle(org.bukkit.Particle,org.bukkit.Location,int,T)">{@code spawnParticle}</a>
     *
     */
    public static <T> void spawnCircleParticles(Particle particle, Location loc, int amount, T option, double range, int factor) {
        World world = loc.getWorld();
        for (double i = 0; i <= 360.0; i += 360.0 / factor) {
            double rad = Math.toRadians(i);
            double sin = Math.sin(rad) * range;
            double cos = Math.cos(rad) * range;
            world.spawnParticle(particle, loc.clone().add(sin, 0, cos), amount, option);
        }
    }

    /**
     * 产生简单粒子
     *
     * @param particle 粒子
     * @param loc      位置
     * @param amount   数量
     * @param option   生成选项
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/World.html#spawnParticle(org.bukkit.Particle,org.bukkit.Location,int,T)">{@code spawnParticle}</a>
     */
    public static <T> void spawnSimpleParticle(Particle particle, Location loc, int amount, T option) {
        Class<?> clazz = particle.getDataType();
        if (clazz != null && option != null) {
            loc.getWorld().spawnParticle(particle, loc, amount, clazz.cast(option));
        } else {
            loc.getWorld().spawnParticle(particle, loc, amount);
        }
    }

    /**
     * 让玩家破坏方块
     *
     * @param player 玩家
     * @param block  方块
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Player.html#breakBlock(org.bukkit.block.Block)">{@code breakBlock}</a>
     */
    public static void breakExtraBlock(Player player, Block block) {
        try {
            BlockListener.breakExtra(block);
            player.breakBlock(block);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (block.getType() != Material.AIR) {
                if (NereusOpus.itemsadder) {
                    if (CustomBlock.byAlreadyPlaced(block) != null) {
                        CustomBlock.getLoot(block, player.getInventory().getItemInMainHand(), true).forEach((itemStack -> {
                            block.getLocation().getWorld().dropItem(block.getLocation(), (ItemStack) itemStack);
                        }));
                        CustomBlock.remove(block.getLocation());
                    } else {
                        block.breakNaturally(player.getInventory().getItemInMainHand());
                    }
                } else {
                    block.breakNaturally(player.getInventory().getItemInMainHand());
                }
            }
        }
    }

    /**
     * 获取粒子
     *
     * @param name 粒子名字
     * @return 粒子 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html">{@link Particle}</a>
     */
    public static Particle getParticle(String name) {
        try {
            return Particle.valueOf(name);
        } catch (Exception exception) {
            return Particle.ASH;
        }
    }

    /**
     * <p>将位置转化为字符串</p>
     * <p>字符串格式：世界名字,x坐标,y坐标,z坐标</p>
     *
     * @param loc 位置
     * @return 对应的字符串{@link String}
     */
    public static String locToString(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    /**
     * <p>将字符串转化为位置</p>
     * <p>字符串格式：世界名字,x坐标,y坐标,z坐标</p>
     *
     * @param string 字符串
     * @return 位置 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Location.html">{@link Location}</a>
     */
    public static Location stringToLoc(String string) {
        String[] splited = string.split(",");
        return new Location(
                Bukkit.getWorld(splited[0]),
                Integer.parseInt(splited[1]),
                Integer.parseInt(splited[2]),
                Integer.parseInt(splited[3]));
    }

    /**
     * 速度是否不安全
     *
     * @param vector 速度向量 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/util/Vector.html">{@code vector}</a>
     * @return boolean 是否不安全
     */
    public static boolean isUnsafeVelocity(Vector vector) {
        final double x = vector.getBlockX();
        final double y = vector.getBlockY();
        final double z = vector.getBlockZ();
        return x > 4.0 || y > 4.0 || z > 4.0 || x < -4.0 || y < -4.0 || z < -4.0;
    }

    private static double getSafeVelocity(double x) {
        return x > 4.0 ? 4.0 : (x < -4.0 ? -4.0 : x);
    }

    /**
     * 转换为安全速度
     *
     * @param vector 速度向量 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/util/Vector.html">{@code vector}</a>
     * @return 安全的速度向量 速度向量 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/util/Vector.html">{@code vector}</a>
     */
    public static Vector convertToSafeVelocity(Vector vector) {
        final double x = vector.getX();
        final double y = vector.getY();
        final double z = vector.getZ();
        return new Vector(getSafeVelocity(x), getSafeVelocity(y), getSafeVelocity(z));
    }

    /**
     * 增加实体的速度
     *
     * @param entity         实体
     * @param vector         速度向量 <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/util/Vector.html">{@code vector}</a>
     * @param checkKnockback 是否检查 {@code Knockback}
     *
     * @see <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html#GENERIC_KNOCKBACK_RESISTANCE">{@code GENERIC_KNOCKBACK_RESISTANCE}</a>
     */
    public static void addVelocity(Entity entity, Vector vector, boolean checkKnockback) {
        if (checkKnockback && entity instanceof LivingEntity creature) {
            AttributeInstance instance = creature.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if (instance != null) {
                double value = instance.getValue();
                if (value >= 1) {
                    return;
                }
                if (value > 0) {
                    vector.multiply(1 - value);
                }
            }
        }
        Vector newVelocity = vector;
        if (isUnsafeVelocity(vector)) {
            newVelocity = convertToSafeVelocity(vector);
        }
        try {
            entity.setVelocity(newVelocity);
        } catch (Exception | Error exception) {

        }
    }
}
