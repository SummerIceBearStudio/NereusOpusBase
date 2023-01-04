package hamsteryds.nereusopus.utils.api;

import hamsteryds.nereusopus.utils.api.StringUtils;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class InformationUtils {
    public static void sendMsg(Entity entity, String msg) {
        entity.sendMessage(msg);
    }

    public static void broadcastMsg(String msg) {
        Bukkit.broadcast(Component.text(msg));
    }

    public static void sendTitle(Entity entity, String title, String subTitle) {
        if (entity instanceof Player player) {
            player.sendTitle(title, subTitle, 10, 70, 20);
        }
    }

    public static void broadcastTitle(String title, String subTitle) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subTitle, 10, 70, 20);
        }
    }

    public static void sendTitle(Entity entity, String title, String subTitle, int in, int stay, int out) {
        if (entity instanceof Player player) {
            player.sendTitle(title, subTitle, in, stay, out);
        }
    }

    public static void sendActionBar(Entity entity, String msg) {
        if (entity instanceof Player player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
        }
    }

    @SafeVarargs
    public static <E> void sendInfo(Entity who, String notice, E... params) {
        String form = notice.split(":")[0];
        String content = StringUtils.replace(notice.split(":")[1], params);
        if (content.length() <= 0) {
            return;
        }
        if (form == "@msg" || form == "@message" || form == "@聊天栏" || form == "@聊天" || form == "@聊天信息") {
            sendMsg(who, content);
        } else if (form == "@actionbar" || form == "@行为栏") {
            sendActionBar(who, content);
        } else if (form == "@title" || form == "@大标题") {
            sendTitle(who, content, "");
        } else if (form == "@subtitle" || form == "@小标题") {
            sendTitle(who, "", content);
        }
    }
}
