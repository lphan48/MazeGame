import java.util.ArrayDeque;
import java.util.Deque;

// Represents a queue that processes newly added items last (bfs)
class Queue implements ICollection {
    // All cells in this
    Deque<Cell> contents;

    Queue() {
        this.contents = new ArrayDeque<Cell>();
    }

    // Is this queue empty?
    public boolean isEmpty() {
        return this.contents.isEmpty();
    }

    // EFFECT: adds the given item to this queue
    public void add(Cell item) {
        this.contents.addLast(item);
    }

    // Returns the first item in this queue
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