import java.util.ArrayDeque;
import java.util.Deque;

// Represents a stack that processes newly added items first (dfs)
class Stack implements ICollection {
    // All cells in this
    Deque<Cell> contents;

    Stack() {
        this.contents = new ArrayDeque<Cell>();
    }

    // Is this stack empty?
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    // EFFECT: adds the given item to this stack
    public void add(Cell item) {
        this.contents.addFirst(item);
    }

    // Returns the first item in this stack
    // EFFECT: Removes the first item
    public Cell remove() {
        return this.contents.removeFirst();
    }

    // EFFECT: empties this collection
    public void clear() {
        for (Cell cell : this.contents) {
            this.remove();
        }
    }
}