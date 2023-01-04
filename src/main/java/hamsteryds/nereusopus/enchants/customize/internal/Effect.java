package hamsteryds.nereusopus.enchants.customize.internal;

import hamsteryds.nereusopus.utils.api.StringUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Effect {
    public String name;
    public String target;
    public ParamType type;
    public List<Effect> chains = new ArrayList<>();
    public String expression = null;

    public Effect(String name, @Nullable ConfigurationSection section, Map<String, String> params) {
        assert section != null;
        this.name = name;
        this.target = section.getString("target");
        this.type = ParamType.valueOf(section.getString("type"));
        if (section.contains("expression")) {
            this.expression = StringUtils.replace(section.getString("expression"), params);
        } else {
            section = section.getConfigurationSection("chains");
            assert section != null;
            for (String path : section.getKeys(false)) {
                chains.add(new Effect(path, section.getConfigurationSection(path), params));
            }
        }
    }

    public void effect(int level, Event event, Trigger trigger, Pair<ParamType, Object> object, Map<String, String> variables) {
        Pair<ParamType, Object> directed;
        if (object.getFirst() == null) {
            directed = trigger.get(event, target);
        } else {
            directed = object.getFirst().target.get(object.getSecond(), target);
        }
        Map<String, String> params = new HashMap<>();
        params.put("level", "" + level);
        params.put("target", directed.getSecond().toString());
        params.putAll(variables);
        if (directed.getFirst().target instanceof Generateable generateable) {
            directed.setSecond(generateable.generate(expression, params));
        } else {
            if (object.getFirst() == null) {
                directed = trigger.get(event, target);
            } else {
                directed = object.getFirst().target.get(object.getSecond(), target);
            }
        }
        Object value = directed.getSecond();
        if (chains.size() <= 0) {
            if (object.getFirst() == null) {
                trigger.set(event, target, value);
            } else {
                if (object.getFirst().target instanceof Mutable mutable) {
                    mutable.set(object.getSecond(), target, value);
                }
            }
        } else {
            for (Effect effect : chains) {
                effect.effect(level, event, trigger, new Pair<>(type, directed.getSecond()), variables);
            }
        }
    }
}
