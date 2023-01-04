package hamsteryds.nereusopus.enchants.customize.internal;

import hamsteryds.nereusopus.utils.api.StringUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Condition {
    public String name;
    public String target;
    public ParamType type;
    public List<Condition> chains = new ArrayList<>();
    public String expression = null;
    public String assignment;

    public Condition(String name, @Nullable ConfigurationSection section, Map<String, String> params) {
        assert section != null;
        this.name = name;
        this.target = section.getString("target");
        this.type = ParamType.valueOf(section.getString("type"));
        this.assignment = section.getString("assignment", null);
        if (section.contains("expression")) {
            this.expression = StringUtils.replace(section.getString("expression"), params);
        } else {
            section = section.getConfigurationSection("chains");
            assert section != null;
            for (String path : section.getKeys(false)) {
                chains.add(new Condition(path, section.getConfigurationSection(path), params));
            }
        }
    }

    public boolean isTrue(int level, Event event, Trigger trigger, Pair<ParamType, Object> object, Map<String, String> variables) {
        Pair<ParamType, Object> directed;
        if (object.getFirst() == null) {
            directed = trigger.get(event, target);
        } else {
            directed = object.getFirst().target.get(object.getSecond(), target);
        }
        if (assignment != null) {
            variables.put(assignment, directed.getSecond().toString());
        }
        if (chains.size() <= 0 && type.target instanceof Checkable checkable) {
            String tmp = expression;
            tmp = StringUtils.replace(tmp, variables);
            tmp = tmp.replace("target", directed.getSecond().toString());
            tmp = tmp.replace("level", "" + level);
            return checkable.check(directed.getSecond(), tmp);
        } else {
            for (Condition condition : chains) {
                if (!condition.isTrue(level, event, trigger, new Pair<>(type, directed.getSecond()), variables)) {
                    return false;
                }
            }
            return true;
        }
    }
}
