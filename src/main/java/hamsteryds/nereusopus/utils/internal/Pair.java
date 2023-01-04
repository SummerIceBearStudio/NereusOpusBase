package hamsteryds.nereusopus.utils.internal;

public class Pair<E, F> {
    private E first;
    private F second;

    public Pair(E key, F value) {
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