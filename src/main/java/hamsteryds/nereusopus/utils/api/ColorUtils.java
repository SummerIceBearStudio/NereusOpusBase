package hamsteryds.nereusopus.utils.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;


/**
 * 颜色工具类
 */
public class ColorUtils {
    private static final HashMap<ChatColor, Team> coloredTeams = new HashMap<>();

    static {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        for (ChatColor color : ChatColor.values()) {
            if (board.getTeam("NO-" + color.name()) == null) {
                Team team = board.registerNewTeam("NO-" + color.name());
                team.setColor(color);
            }
            coloredTeams.put(color, board.getTeam("NO-" + color.name()));
        }
    }

    /**
     * 通过颜色获得Team类
     *
     * @param color 颜色
     * @return <a href="https://hub.spigotmc.org/javadocs/spigot/org/bukkit/scoreboard/Team.html">{@code Team}</a> - 通过颜色获取的Team类
     */

    public static Team getTeamByColor(ChatColor color) {
        return coloredTeams.get(color);
    }
}
