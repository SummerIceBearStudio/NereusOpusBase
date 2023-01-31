package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.utils.api.StringUtils;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * <p>信息工具类</p>
 */
public class InformationUtils {
    /**
     * 发送消息
     *
     * @param entity 实体
     * @param msg    消息
     */
    public static void sendMsg(Entity entity, String msg) {
        entity.sendMessage(msg);
    }

    /**
     * 广播消息
     *
     * @param msg 消息
     */
    public static void broadcastMsg(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text(msg));
        }
    }

    /**
     * 发送标题消息
     *
     * @param entity   实体
     * @param title    标题
     * @param subTitle 子标题
     */
    public static void sendTitle(Entity entity, String title, String subTitle) {
        if (entity instanceof Player player) {
            player.sendTitle(title, subTitle, 10, 70, 20);
        }
    }

    /**
     * 广播标题消息
     *
     * @param title    标题
     * @param subTitle 子标题
     */
    public static void broadcastTitle(String title, String subTitle) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subTitle, 10, 70, 20);
        }
    }

    /**
     * 发送标题消息
     *
     * @param entity   实体
     * @param title    标题
     * @param subTitle 子标题
     * @param in       fadeIn
     * @param stay     stay
     * @param out      fadeOut
     *
     * @see <a href = "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Player.html#sendTitle(java.lang.String,java.lang.String,int,int,int)">{@code sendTitle(String, String, int, int, int)}</a>
     */
    public static void sendTitle(Entity entity, String title, String subTitle, int in, int stay, int out) {
        if (entity instanceof Player player) {
            player.sendTitle(title, subTitle, in, stay, out);
        }
    }

    /**
     * 发送状态栏消息
     *
     * @param entity 实体
     * @param msg    消息
     */
    public static void sendActionBar(Entity entity, String msg) {
        if (entity instanceof Player player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        }
    }

    /**
     * 向玩家发送信息
     *
     * @param who    玩家
     * @param notice 信息
     * @param params 参数
     */
    @SafeVarargs
    public static <E> void sendInfo(Entity who, String notice, E... params) {
        String form = notice.split(":")[0];
        String content = StringUtils.replace(notice.split(":")[1], params);
        if (content.length() <= 0) {
            return;
        }
        if (form.equals("@msg") || form.equals("@message") || form.equals("@聊天栏") || form.equals("@聊天") || form.equals("@聊天信息")) {
            sendMsg(who, content);
        } else if (form.equals("@actionbar") || form.equals("@行为栏")) {
            sendActionBar(who, content);
        } else if (form.equals("@title") || form.equals("@大标题")) {
            sendTitle(who, content, "");
        } else if (form.equals("@subtitle") || form.equals("@小标题")) {
            sendTitle(who, "", content);
        }
    }
}
