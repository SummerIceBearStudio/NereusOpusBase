package hamsteryds.nereusopus.enchants.internal.data;

import hamsteryds.nereusopus.utils.api.LanguageUtils;

import java.util.Locale;

public enum EnchantmentType {
    VANILLA(),
    CURSE(),
    SIMPLE(),
    SKILL(),
    ARTIFACT();
    public final String displayName;

    EnchantmentType() {
        this.displayName = LanguageUtils.getLang("type." + this.toString().toLowerCase(Locale.ROOT));
    }

    public static EnchantmentType getType(String typeName) {
        try {
            return valueOf(typeName.toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            return null;
        }
    }
}
