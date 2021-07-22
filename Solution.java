import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class Solution {

    /*
    Key points to observe:
    
    1. A greedy search in the Trie will not do the job.
       There might be some tricky overlaps, so all possible combinations must be checked.
    
       Example: 
       s = "codexzyone";  wordDict = ["code", "one", "codexzy"].
    
       In the above case, a greedy approach will finish the search after the word "code" 
       and will return "false". But, in fact, the correct answer is "true".
        
    2. On the other hand, trying all possible combinations might lead to repeating the same search, 
        so the algorithm should avoid the other extreme, namely: starting the search from one and the same 
        index, multiple times.
    
        Example:        
        s = "xxy";  wordDict = ["x", "xx"]. 
    
        Without such safeguards, in the above case, the algorithm will start the search from index 2, 
        the letter "y", twice. With large input, the time complexity will be prohibitive 
        and at some point will exceed the time limits.
     */
    LinkedList<Integer> queue_startIndexes;// start indexes for the substring to be searched.
    Set<Integer> visited_startIndexes;
    int inputStringLength;
    int lastIndexOfCurrentWord;
    TrieNode root;

    public boolean wordBreak(String s, List<String> wordDict) {
        inputStringLength = s.length();
        root = new TrieNode();
        addAllWordsToTrie(wordDict);
        return stringCanBeSegmented_intoDictionaryWords(s);
    }

    public boolean stringCanBeSegmented_intoDictionaryWords(String s) {

        queue_startIndexes = new LinkedList<>();
        queue_startIndexes.add(0);

        visited_startIndexes = new HashSet<>();
        visited_startIndexes.add(0);

        while (!queue_startIndexes.isEmpty()) {
            int current = queue_startIndexes.removeFirst();
            isWord_inDictionary(s.substring(current));
            if (lastIndexOfCurrentWord == s.length()) {
                return true;
            }
        }
        return false;
    }

    public void isWord_inDictionary(String word) {

        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);

            if (current.branch[ch - 'a'] == null) {
                break;
            }

            current = current.branch[ch - 'a'];
            if (current.isEndOfWord && visited_startIndexes.add(inputStringLength - word.length() + i + 1)) {

                queue_startIndexes.add(inputStringLength - word.length() + i + 1);
                lastIndexOfCurrentWord = inputStringLength - word.length() + i + 1;

                if (lastIndexOfCurrentWord == inputStringLength) {
                    break;
                }
            }
        }
    }

    public void addAllWordsToTrie(List<String> wordDict) {
        for (int i = 0; i < wordDict.size(); i++) {
            addWord(wordDict.get(i));
        }
    }

    public void addWord(String word) {

        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);

            if (current.branch[ch - 'a'] == null) {
                current.branch[ch - 'a'] = new TrieNode();
            }
            current = current.branch[ch - 'a'];
            if (i == word.length() - 1) {
                current.isEndOfWord = true;
            }
        }
    }

}

class TrieNode {

    boolean isEndOfWord;
    TrieNode[] branch;
    final int CHARS_IN_ALPHABET = 26;

    public TrieNode() {
        branch = new TrieNode[CHARS_IN_ALPHABET];
    }
}
