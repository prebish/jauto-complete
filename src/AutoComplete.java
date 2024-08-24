package src;

/**
 * An implementation of the AutoCompleteInterface using a DLB Trie.
 */

import java.util.ArrayList;

public class AutoComplete implements AutoCompleteInterface {

  private DLBNode root; // root of the DLB Trie
  private StringBuilder currentPrefix; // running prefix
  private DLBNode currentNode; // current DLBNode
  private int offset; // how far the prefix is from the trie

  @Override
  public boolean add(String word) {
    // check for non existant word
    if (word == null || word.isEmpty()) throw new IllegalArgumentException("word null or empty");
    // initialize root
    if (root == null) root = new DLBNode('\0'); 

    DLBNode currentNode = root; // start at root
    boolean isNewWord = false; // if a new word was added, stays false if already exists

    // for each letter in word...
    for (char let : word.toCharArray()) {
      // look for child containing letter
      DLBNode child = findChild(currentNode, let); 

      // if child with current letter not isFound
      if (child == null) {
        child = new DLBNode(let); // create new node
        addChild(currentNode, child); // add child to currentNode
        isNewWord = true; // new node added
      }
      currentNode = child; // traverse trie
    }

    // if not already a word
    if (!currentNode.isWord) {
      currentNode.isWord = true;

      // travel back up to increment size
      DLBNode ancestor = currentNode; 
      while (ancestor != null) {
        ancestor.size++;
        ancestor = ancestor.parent;
      }
    }
    return isNewWord;
  }

  @Override
  public boolean advance(char c) {
    if (currentPrefix == null) currentPrefix = new StringBuilder(); // handling null currentPrefix

    // initialize currentNode if null, also check root
    if (currentNode == null) {
      if (root == null) return false; // trie empty, cant advance
      else currentNode = root;
    }

    currentPrefix.append(c); // add to prefix
    if(offset > 0) return false; // cant advance with an offset

    // search for child containing c
    DLBNode child = findChild(currentNode, c);
    if (child == null) {offset++; return false;} // no letter, cannot advance, increase offset
    else currentNode = child;
    return true;
  }

  @Override
  public void retreat() {
    // if prefix empty
    if (currentPrefix.length() == 0) throw new IllegalStateException("Running prefix is the empty string");

    // remove last let from prefix
    currentPrefix.deleteCharAt(currentPrefix.length() - 1);

    // decrement and return
    if (offset > 0) {offset--; return;}

    // if currentNode and its parent exist, backtrack
    if (currentNode != null && currentNode.parent != null) currentNode = currentNode.parent;
    return;
  }

  @Override
  public void reset() {
    currentNode = root; offset = 0; // reset currentNode and offset
    currentPrefix = new StringBuilder(); // wipe currentPrefix
  }

  @Override
  public boolean isWord() {
    // cant be word with offset or a null currentNode
    if(currentNode == null || offset > 0) return false;
    else return currentNode.isWord;
  }

  @Override
  public void add() {
    if (currentPrefix != null) add(currentPrefix.toString());
    else throw new NullPointerException("currentPrefix null");
  }

  @Override
  public int getNumberOfPredictions() {
    if(offset > 0 || currentNode == null) return 0;
    else return currentNode.size;
  }

  @Override
  public String retrievePrediction() {
    // no prediction if no node, or offset exists
    if(offset > 0 || currentNode == null) return null;

    ArrayList<String> prediction = new ArrayList<>();
    StringBuilder s = new StringBuilder(currentPrefix);

    // if is word, add prefix to list
    if(currentNode.isWord) prediction.add(s.toString());

    // returns list with a single word (fastest word found)
    // much faster than retrievePredictionS()
    retrievePredictionDFS(currentNode.child, s, prediction);
    return prediction.get(0);
  }

  @Override
  public ArrayList<String> retrievePredictions() {
    // no prediction if no node, or offset exists
    if(offset > 0 || currentNode == null) return null;

    ArrayList<String> predictions = new ArrayList<>();
    StringBuilder s = new StringBuilder(currentPrefix);

    // if is word, add prefix to list
    if(currentNode.isWord) predictions.add(s.toString());

    // similar to retrieving a single prediction
    // potentially slower, searches for all possible
    retrievePredictionsDFS(currentNode.child, predictions, s);
    return predictions;
  }

  @Override
  public boolean delete(String word) {
    if (word == null || word.isEmpty()) return false; // check for non existant word
    if (root == null) return false; // initialize root

    DLBNode currentNode = root; // start at root
    ArrayList<DLBNode> toRemove = new ArrayList<>();
    int toRemoveCount = 0;

    // for each letter in word...
    for (char let : word.toCharArray()) {
      // look for child containing letter
      currentNode = findChild(currentNode, let);

      // if child with current letter not isFound
      if (currentNode == null) return false;
      else toRemove.add(currentNode); toRemoveCount++;
    }

    // not word, false
    if(!currentNode.isWord) return false;
    currentNode.isWord = false; // otherwise mark false

    // go through toRemove list and update nodes
    for (int i = toRemoveCount-1; i >= 0; i--) {
      DLBNode deadNode = toRemove.get(i);
      deadNode.size--; // decrement size for backtrack

      // if node size is 0 (no words under it)
      // and not marking end of multiple words
      if (deadNode.size == 0 && !deadNode.isWord) removeNode(deadNode); 

      // size > 0, node has children
      // node is purposful, stop deletions
      if (deadNode.size > 0 || deadNode.isWord) break;
    }
    return true;
  }

  // The DLBNode class
  private class DLBNode {
    private char data; // letter inside the node
    private int size; // number of words in the subtrie rooted at node
    private boolean isWord; // true if the node is at the end of a word
    private DLBNode nextSibling; // doubly-linked list of siblings
    private DLBNode previousSibling;
    private DLBNode child; // child reference
    private DLBNode parent; // parent reference

    private DLBNode(char data) { // constructor
      this.data = data;
      size = 0;
      isWord = false;
    }
  }

  // Helper methods for debugging //

  // Prints the nodes in a DLB Trie for debugging. The letter inside each node is
  // followed by an asterisk if
  // the node's isWord flag is set. The size of each node is printed between
  // parentheses.
  // Siblings are printed with the same indentation, whereas child nodes are
  // printed with a deeper
  // indentation than their parents.
  public void printTrie() {
    System.out.println("==================== START: DLB Trie ====================");
    printTrie(root, 0);
    System.out.println("==================== END: DLB Trie ====================");
  }

  // a helper method for printTrie
  private void printTrie(DLBNode node, int depth) {
    if (node != null) {
      for (int i = 0; i < depth; i++) {
        System.out.print(" ");
      }
      System.out.print(node.data);
      if (node.isWord) {
        System.out.print(" *");
      }
      System.out.println(" (" + node.size + ")");
      printTrie(node.child, depth + 1);
      printTrie(node.nextSibling, depth);
    }
  }


  // HELPER METHODS

  // Helper for: retrievePrediction()
  // returns list containing ONE of the most accurate predictions under a specified root node
  public ArrayList<String> retrievePredictionDFS(DLBNode currentNode, StringBuilder s, ArrayList<String> prediction) {

    if (prediction.size() > 0 || currentNode == null) return prediction;

    s.append(currentNode.data); // add to path

    if (currentNode.isWord) prediction.add(new String(s)); // if node.isWord, add path as word to list

    // if child NOT null...
    if (currentNode.child != null) {
      retrievePredictionDFS(currentNode.child, s, prediction); // down a level
      s.deleteCharAt(s.length() - 1);
    }

    // if nextSibling NOT null...
    if (currentNode.nextSibling != null)
      s.deleteCharAt(s.length() - 1); // remove current child at same level before replacing with sibling
    retrievePredictionDFS(currentNode.nextSibling, s, prediction); // check other words on same level

    return prediction;
  }

  // Helper for: retrievePredictions()
  // returns list of ALL predictions under a specified root
  public ArrayList<String> retrievePredictionsDFS(DLBNode currentNode, ArrayList<String> predictions, StringBuilder s) {
    if (currentNode == null) return predictions;

    s.append(currentNode.data); // add to path

    if (currentNode.isWord) predictions.add(new String(s)); // if node.isWord, add path as word to list

    // if child NOT null...
    if (currentNode.child != null) {
      retrievePredictionsDFS(currentNode.child, predictions, s); // down a level
      s.deleteCharAt(s.length() - 1);
    }

    // if nextSibling NOT null...
    if (currentNode.nextSibling != null)
      s.deleteCharAt(s.length() - 1); // remove current child at same level before replacing with sibling
    retrievePredictionsDFS(currentNode.nextSibling, predictions, s); // check other words on same level

    return predictions;
  }

  // Helper
  // searches siblings for matching data
  private DLBNode findChild(DLBNode parent, char let) {

    if (parent == null)
      return null;

    DLBNode child = parent.child;
    while (child != null) {
      // node with char isFound
      if (child.data == let)
        return child;
      else
        child = child.nextSibling; // iterate
    }
    // pre-existing node with char not isFound
    return null;
  }

  // Helper for: add(String)
  // adds a node as a child
  private void addChild(DLBNode parent, DLBNode child) {
    if (parent == null || child == null)
      return;

    if (parent.child == null)
      parent.child = child;
    else {
      // find last sibling
      DLBNode sibling = parent.child;
      while (sibling.nextSibling != null)
        sibling = sibling.nextSibling;
      sibling.nextSibling = child; // new child as last sibling
      child.previousSibling = sibling; // set previous sibling
    }
    // set parent
    child.parent = parent;
  }

  // Helper for: delete()
  // removes node if no longer in use
  private void removeNode(DLBNode child) {
    if (child == null)
      return;

    DLBNode parent = child.parent;
    DLBNode nextSibling = child.nextSibling;
    DLBNode previousSibling = child.previousSibling;

    if(parent != null && parent.child == child) parent.child = nextSibling;
    if(previousSibling != null) previousSibling.nextSibling = nextSibling;
    if(nextSibling != null) nextSibling.previousSibling = previousSibling;

    return;
  }

}

