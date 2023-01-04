package hamsteryds.nereusopus.utils.internal.data;

import hamsteryds.nereusopus.utils.internal.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class EnchantmentsInfoDataType implements PersistentDataType<byte[], EnchantmentsInfo> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<EnchantmentsInfo> getComplexType() {
        return EnchantmentsInfo.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull EnchantmentsInfo info, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
//        Serialize
        return SerializationUtils.serialize(info);
    }

    @Override
    public @NotNull EnchantmentsInfo fromPrimitive(byte @NotNull [] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            InputStream is = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(is);
            return (EnchantmentsInfo) o.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
