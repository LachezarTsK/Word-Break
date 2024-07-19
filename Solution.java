
import java.util.List;
import java.util.LinkedList;

public class Solution {

    private LinkedList<Integer> queueStartIndexes;
    private boolean[] visitedStartIndexes;
    private final Trie trie = new Trie();

    public boolean wordBreak(String input, List<String> wordDict) {
        visitedStartIndexes = new boolean[input.length() + 1];
        addAllWordsToTrie(wordDict);
        return stringCanBeSegmentedIntoDictionaryWords(input);
    }

    public void addAllWordsToTrie(List<String> wordDict) {
        for (int i = 0; i < wordDict.size(); i++) {
            trie.addWord(wordDict.get(i));
        }
    }

    public boolean stringCanBeSegmentedIntoDictionaryWords(String input) {
        queueStartIndexes = new LinkedList<>();
        queueStartIndexes.add(0);
        visitedStartIndexes[0] = true;

        while (!queueStartIndexes.isEmpty()) {
            if (queueStartIndexes.getLast() == input.length()) {
                return true;
            }
            int startIndex = queueStartIndexes.removeFirst();
            trie.addStartIndexesSubstringsToQueue(input.substring(startIndex), input.length(), queueStartIndexes, visitedStartIndexes);
        }
        return false;
    }
}

class Trie {

    private class TrieNode {

        private static final int ALPHABET_SIZE = 26;
        TrieNode[] branches = new TrieNode[ALPHABET_SIZE];
        boolean isEndOfWord;
    }

    private final TrieNode root = new TrieNode();

    public void addWord(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (current.branches[ch - 'a'] == null) {
                current.branches[ch - 'a'] = new TrieNode();
            }
            current = current.branches[ch - 'a'];
        }
        current.isEndOfWord = true;
    }

    public void addStartIndexesSubstringsToQueue(String word, int inputLength, LinkedList<Integer> queueStartIndexes, boolean[] visitedStartIndexes) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {

            int index = word.charAt(i) - 'a';
            if (current.branches[index] == null) {
                break;
            }

            current = current.branches[index];
            if (!current.isEndOfWord || visitedStartIndexes[inputLength - word.length() + i + 1]) {
                continue;
            }

            queueStartIndexes.add(inputLength - word.length() + i + 1);
            visitedStartIndexes[inputLength - word.length() + i + 1] = true;
            if (queueStartIndexes.peekLast() == inputLength) {
                break;
            }
        }
    }
}
