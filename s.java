import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

class PhoneDirectory {
    private Queue<Integer> available;
    private HashSet<Integer> used;
    private int maxNumbers;

    public PhoneDirectory(int maxNumbers) {
        this.maxNumbers = maxNumbers;
        available = new LinkedList<>();
        used = new HashSet<>();
        for (int i = 0; i < maxNumbers; i++) {
            available.offer(i);
        }
    }

    public int get() {
        if (available.isEmpty()) {
            return -1;
        }
        int number = available.poll();
        used.add(number);
        return number;
    }

    public boolean check(int number) {
        if (number < 0 || number >= maxNumbers) {
            return false;
        }
        return !used.contains(number);
    }

    public void release(int number) {
        if (used.remove(number)) {
            available.offer(number);
        }
    }
}
//
import java.util.*;

class AutocompleteSystem {
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
        boolean isWord = false;
    }

    private TrieNode root;
    private StringBuilder currentInput;
    private TrieNode currentNode;

    public AutocompleteSystem(String[] sentences, int[] times) {
        root = new TrieNode();
        currentInput = new StringBuilder();
        currentNode = root;
        for (int i = 0; i < sentences.length; i++) {
            addSentence(sentences[i], times[i]);
        }
    }

    private void addSentence(String sentence, int count) {
        TrieNode node = root;
        for (char c : sentence.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
            node.counts.put(sentence, node.counts.getOrDefault(sentence, 0) + count);
        }
        node.isWord = true;
    }

    public List<String> input(char c) {
        if (c == '#') {
            String sentence = currentInput.toString();
            addSentence(sentence, 1);
            currentInput = new StringBuilder();
            currentNode = root;
            return new ArrayList<>();
        }
        currentInput.append(c);
        if (currentNode != null) {
            currentNode = currentNode.children.get(c);
        }
        if (currentNode == null)
            return new ArrayList<>();
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(
                (a, b) -> a.getValue().equals(b.getValue()) ? a.getKey().compareTo(b.getKey())
                        : b.getValue() - a.getValue());
        pq.addAll(currentNode.counts.entrySet());
        List<String> result = new ArrayList<>();
        int k = 3;
        while (!pq.isEmpty() && k-- > 0) {
            result.add(pq.poll().getKey());
        }
        return result;
    }
}