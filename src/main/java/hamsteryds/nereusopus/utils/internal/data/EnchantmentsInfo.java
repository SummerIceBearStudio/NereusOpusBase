package hamsteryds.nereusopus.utils.internal.data;

import hamsteryds.nereusopus.utils.internal.SerializablePair;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EnchantmentsInfo implements Serializable {
    private Set<SerializablePair<String, Integer>> infos;

    public EnchantmentsInfo() {
        this.infos = new HashSet<>();
    }

    public EnchantmentsInfo(Set<SerializablePair<String, Integer>> set) {
        this.infos = set;
    }

    public Set<SerializablePair<String, Integer>> get() {
        return this.infos;
    }

    public void set(Set<SerializablePair<String, Integer>> set) {
        this.infos = set;
    }

    public void add(SerializablePair<String, Integer> p) {
        this.infos.add(p);
    }

    public void remove(SerializablePair<String, Integer> p) {
        this.infos.remove(p);
    }
}
