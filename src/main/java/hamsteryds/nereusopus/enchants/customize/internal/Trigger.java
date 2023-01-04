package hamsteryds.nereusopus.enchants.customize.internal;

import hamsteryds.nereusopus.enchants.customize.triggers.player.*;
import hamsteryds.nereusopus.listeners.executors.ActionType;
import hamsteryds.nereusopus.utils.internal.Pair;
import org.bukkit.event.Event;

import java.util.HashMap;

public abstract class Trigger {
    public static HashMap<ActionType, Trigger> triggers = new HashMap<>();
    public ActionType actionType;

    public Trigger(ActionType actionType) {
        this.actionType = actionType;
        triggers.put(actionType, this);
    }

    public static void initialize() {
        new AdvancementDone();
        new ArmorStandManipulate();
        new AttackEntity();
        new BedEnter();
        new BedLeave();
        new BucketEmpty();
        new BucketEntity();
        new BucketFill();
        new Chat();
        new Damaged();
        new DamagedByBlock();
        new DamagedByEntity();
        new DropItem();
        new ExpChange();
        new Fish();
        new HarvestBlock();
        new InteractEntity();
        new InteractLeft();
        new InteractLeftAir();
        new InteractLeftBlock();
        new InteractRight();
        new InteractRightAir();
        new InteractRightBlock();
        new ItemBreak();
        new ItemConsume();
        new ItemDamage();
        new ItemHeld();
        new ItemMend();
        new LevelChange();
        new Move();
        new PickUpArrow();
        new PickUpItem();
        new Portal();
        new RecipeDiscover();
        new Respawn();
        new Riptide();
        new ShearEntity();
        new SwapHandItems();
        new TakeLecternBook();
        new Teleport();
        new ToggleFlight();
        new ToggleSneak();
        new ToggleSprint();
        new UnleashEntity();
    }

    public static Trigger getTrigger(ActionType actionType) {
        return triggers.get(actionType);
    }

    public abstract Pair<ParamType, Object> get(Event evt, String name);

    public abstract void set(Event evt, String name, Object value);
}
