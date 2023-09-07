// Represents a mutable collection of cells
interface ICollection {
    // Is this collection empty?
    boolean isEmpty();

    // EFFECT: adds the given item to this collection
    void add(Cell item);

    // Returns the first item in this collection
    // EFFECT: Removes the first item
    Cell remove();

    // EFFECT: empties this collection
    void clear();
}
