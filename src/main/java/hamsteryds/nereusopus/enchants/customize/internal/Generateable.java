package hamsteryds.nereusopus.enchants.customize.internal;

import java.util.Map;

public interface Generateable {
    Object generate(Object obj, Map<String, String> params);
}
