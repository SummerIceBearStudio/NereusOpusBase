package hamsteryds.nereusopus.utils.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Trie {
    private Node root;

    public Trie() {
        root = new Node();
    }

    public Trie(List<String> list) {
        this.addWords(list);
    }

    public void addWord(String word) {
        Node cur = this.root;
        char[] array = word.toLowerCase().toCharArray();
        for (char c : array) {
            Map<Character, Node> son = cur.getSonMap();
            son.putIfAbsent(c, new Node());
            cur.addWord(word);
            cur = son.get(c);
        }
        cur.setEnd();
    }

    public void addWords(List<String> list) {
        root = new Node();
        for (String word : list) {
            this.addWord(word);
        }
    }

    public void removeWord(String word) {
        Node cur = this.root;
        char[] array = word.toLowerCase().toCharArray();
        for (char c : array) {
            Map<Character, Node> son = cur.getSonMap();
            if (son.get(c) == null) return;
            cur.removeWord(word);
            cur = son.get(c);
        }
    }

    public Node getNext(Node node, Character c) {
        if (node == null) {
            node = root;
        }
        return node.getSon(c);
    }

    public List<String> matchPrefix(String prefix) {
//        NereusOpusCore.plugin.getLogger().info("prefix : " + prefix);
        Node cur = this.root;
        char[] array = prefix.toLowerCase().toCharArray();
        for (char c : array) {
            Map<Character, Node> son = cur.getSonMap();
            if (son.get(c) == null) return null;
            cur = son.get(c);
        }
//        NereusOpusCore.plugin.getLogger().info("Result : " + cur.getWords().toString());
        return new ArrayList<>(cur.getWords());
    }

    public List<String> getAllValues() {
        return new ArrayList<>(this.root.getWords());
    }
}
