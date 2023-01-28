package hamsteryds.nereusopus.listeners.display.chat;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import hamsteryds.nereusopus.NereusOpus;
import hamsteryds.nereusopus.utils.api.display.DisplayUtils;
import hamsteryds.nereusopus.utils.api.NMSUtils;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ChatPacket1_19 extends PacketAdapter {
    private static final Class<?> CraftItemStack;
    public static Method setTag;
    public static Method parse;
    public static Method getOrCreateTag;
    private static Method asNMSCopy;
    private static Method asBukkitCopy;

    static {
        CraftItemStack = NMSUtils.getClass("inventory.CraftItemStack");
        if (CraftItemStack == null) {
            throw new NoClassDefFoundError();
        }
        try {
            asNMSCopy = CraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            asBukkitCopy = CraftItemStack.getDeclaredMethod("asBukkitCopy", net.minecraft.world.item.ItemStack.class);
            setTag = net.minecraft.world.item.ItemStack.class.getDeclaredMethod("c", NBTTagCompound.class);
            parse = MojangsonParser.class.getDeclaredMethod("a", String.class);
            getOrCreateTag = net.minecraft.world.item.ItemStack.class.getDeclaredMethod("u");
        } catch (NoSuchMethodException exception) {
            exception.printStackTrace();
        }
    }

    public ChatPacket1_19() {
        super(NereusOpus.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT);
    }

    public static Component modify(Component baseComponnet) throws InvocationTargetException, IllegalAccessException {
        if (baseComponnet instanceof TranslatableComponent translatableComponent) {
            List<Component> args = new ArrayList<>();
            for (Component component : translatableComponent.args()) {
                args.add(modify(component));
            }
            baseComponnet = translatableComponent.args(args);
        }
//        System.out.println(baseComponnet);

        List<Component> children = new ArrayList<>();
        for (Component child : baseComponnet.children()) {
            children.add(modify(child));
        }
        baseComponnet = baseComponnet.children(children);
//        System.out.println(baseComponnet);

        HoverEvent<?> tmp = baseComponnet.style().hoverEvent();
        if (tmp == null) {
            return baseComponnet;
        }
//        System.out.println(baseComponnet);

        if (!(tmp.value() instanceof HoverEvent.ShowItem showItem)) {
            return baseComponnet;
        }
        //        System.out.println(showItem);

        if (showItem.nbt() == null) {
            return baseComponnet;
        }

        HoverEvent<HoverEvent.ShowItem> hoverEvent = (HoverEvent<HoverEvent.ShowItem>) tmp;

        Material material = Material.matchMaterial(showItem.item().toString());
        if (material == null) {
            return baseComponnet;
        }
        net.minecraft.world.item.ItemStack rawItem = (net.minecraft.world.item.ItemStack)
                asNMSCopy.invoke(null, new ItemStack(material, showItem.count()));

        try {
            setTag.invoke(rawItem, parse.invoke(null, showItem.nbt().string()));
        } catch (Exception e) {
            e.printStackTrace();
            return baseComponnet;
        }


        HoverEvent.ShowItem newShowItem = showItem.nbt(
                BinaryTagHolder.binaryTagHolder(
                        getOrCreateTag.invoke(asNMSCopy.invoke(null,
                                DisplayUtils.toDisplayMode((ItemStack) asBukkitCopy.invoke(null, rawItem))
                        )).toString()));

        HoverEvent<?> newHover = hoverEvent.value(newShowItem);
        Style style = baseComponnet.style().hoverEvent(newHover);
        return baseComponnet.style(style);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        for (int i = 0; i < packet.getChatComponents().size(); i++) {
            WrappedChatComponent component = packet.getChatComponents().read(0);
//            System.out.println(component);
            if (component == null) {
                continue;
            }

//            System.out.println(component);
            if (!(component.getHandle() instanceof IChatBaseComponent components)) {
                continue;
            }
            Component tmp;
            try {
                tmp = modify(GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(components)));
            } catch (InvocationTargetException | IllegalAccessException exception) {
                exception.printStackTrace();
                return;
            }
            WrappedChatComponent newComponent = WrappedChatComponent.fromHandle(
                    IChatBaseComponent.ChatSerializer.a(
                            GsonComponentSerializer.gson().serialize(tmp.asComponent())));
            packet.getChatComponents().write(i, newComponent);
        }
    }
}
