package hamsteryds.nereusopus.utils.internal;

import java.io.Serializable;

public class SerializablePair<E, F> implements Serializable {
    private E first;
    private F second;

    public SerializablePair(E key, F value) {
        this.first = key;
        this.second = value;
    }

    public E getFirst() {
        return first;
    }

    public void setFirst(E first) {
        this.first = first;
    }

    public F getSecond() {
        return second;
    }

    public void setSecond(F second) {
        this.second = second;
    }
}