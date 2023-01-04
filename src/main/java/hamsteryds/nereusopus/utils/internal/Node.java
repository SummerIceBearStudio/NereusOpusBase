package hamsteryds.nereusopus.utils.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node {
    private final Set<String> words;
    private boolean isEnd;
    private final Map<Character, Node> son;

    public Node() {
        this.words = new HashSet<>();
        this.isEnd = false;
        this.son = new HashMap<>();
    }

    public Node(Set<String> words) {
        this.words = words;
        this.isEnd = false;
        this.son = new HashMap<>();
    }

    public Set<String> getWords() {
        return words;
    }

    public boolean getIsEnd() {
        return this.isEnd;
    }

    public void setEnd() {
        this.isEnd = true;
    }

    public Map<Character, Node> getSonMap() {
        return this.son;
    }

    public void addSon(Character key, Node node) {
        son.put(key, node);
    }

    public Node getSon(Character key) {
        return this.son.get(key);
    }

    public void addWord(String word) {
        this.words.add(word);
    }

    public void removeWord(String word) {
        this.words.remove(word);
    }
}
