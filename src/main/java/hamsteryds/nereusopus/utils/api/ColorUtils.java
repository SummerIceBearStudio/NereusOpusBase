package hamsteryds.nereusopus.utils.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

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

    public static Team getTeamByColor(ChatColor color) {
        return coloredTeams.get(color);
    }
}
