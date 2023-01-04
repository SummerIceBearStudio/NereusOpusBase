package hamsteryds.nereusopus.utils.api;

import net.md_5.bungee.api.ChatColor;

import java.util.Map;

public class StringUtils {
    @SafeVarargs
    public static <E> String replace(String origin, E... params) {
        for (int i = 0; i < params.length; i += 2) {
            origin = origin.replace(String.valueOf(params[i]), String.valueOf(params[i + 1]));
        }
        return origin;
    }

    public static <E> String replace(String origin, Map<String, String> params) {
        for (String key : params.keySet()) {
            origin = origin.replace(key, params.get(key));
        }
        return origin;
    }

    //首字母大写
    public static String keyToName(String key) {
        StringBuilder name = new StringBuilder();
        name.append(Character.toChars(key.charAt(0) - 32));
        for (int i = 1; i < key.length(); i++) {
            if (key.charAt(i) == '_') {
                name.append(Character.toChars(key.charAt(i++ + 1) - 32));
            } else {
                name.append(key.charAt(i));
            }
        }
        return name.toString();
    }

    public static String removeFormat(String legacy) {
        return ChatColor.stripColor(legacy);
    }

    public static String upperFirstLetter(String legacy) {
        return legacy.substring(0, 1).toLowerCase() + legacy.substring(1);
    }
}
