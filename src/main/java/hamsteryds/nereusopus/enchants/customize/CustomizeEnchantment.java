package hamsteryds.nereusopus.enchants.customize;

import hamsteryds.nereusopus.enchants.customize.internal.Condition;
import hamsteryds.nereusopus.enchants.customize.internal.Effect;
import hamsteryds.nereusopus.enchants.customize.internal.Trigger;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.listeners.executors.EventExecutor;
import hamsteryds.nereusopus.utils.api.DebugUtils;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomizeEnchantment extends EventExecutor {
    public Trigger trigger;
    public List<Condition> conditions = new ArrayList<>();
    public List<Effect> effects = new ArrayList<>();
    public List<String> variables;

    public CustomizeEnchantment(File file) {
        super(file);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("conditions");
        trigger = Trigger.getTrigger(ActionType.valueOf(config.getString("triggerType")));
        variables = config.getStringList("variables");
        for (String path : section.getKeys(false)) {
            conditions.add(new Condition(path, section.getConfigurationSection(path), paramHolders()));
        }
        section = config.getConfigurationSection("effects");
        for (String path : section.getKeys(false)) {
            effects.add(new Effect(path, section.getConfigurationSection(path), paramHolders()));
        }
    }

    @Override
    public void trigger(int level, ActionType type, Event event, LivingEntity who) {
        if (event == null) {
            //TODO TICK
            return;
        }
        DebugUtils.debug("----------调试信息----------", who);
        DebugUtils.debug("调试者: " + who.getName(), who);
        DebugUtils.debug("当前动作: " + type + " (触发匹配" + trigger.actionType.toString() + ")", who);
        if (!type.equals(trigger.actionType)) {
            DebugUtils.debug("触发匹配失败！", who);
            return;
        }
        DebugUtils.debug("触发匹配成功，进入条件检验", who);
        Map<String, String> tmp = new HashMap<>();
        variables.forEach((String variable) -> tmp.put(variable, variable));
        params().forEach((String param, String expression) -> tmp.put(param, expression));
        DebugUtils.debug("变量扫描完成：" + tmp, who);
        for (Condition condition : conditions) {
            DebugUtils.debug("检验条件：" + condition.name, who);
            if (!condition.isTrue(level, event, Trigger.getTrigger(type), new Pair<>(null, event), tmp)) {
                DebugUtils.debug("条件检验失败！", who);
                return;
            }
            DebugUtils.debug("条件检验成功！", who);
        }
        DebugUtils.debug("条件检验全部通过，开始生效", who);
        for (Effect effect : effects) {
            DebugUtils.debug("触发效果：" + effect.name, who);
            effect.effect(level, event, trigger, new Pair<>(null, event), tmp);
        }
        DebugUtils.debug("触发效果完成！", who);
    }
}
