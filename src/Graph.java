import java.util.ArrayList;

// Represents a graph of cells 
class Graph {
    // All cells in this
    ArrayList<Cell> cells;
    // Tracks the path from one cell (odd indeces) to its neighbor (even indeces)
    ArrayList<Cell> cameFromEdge;
    // Cells that have already been fully processed
    ArrayList<Cell> alreadySeen;

    Graph() {
        this.cells = new ArrayList<Cell>();
        this.cameFromEdge = new ArrayList<Cell>();
        this.alreadySeen = new ArrayList<Cell>();
    }

    // EFFECT: adds a new cell to the cells of this
    void addCell(int horLength, int verLength, int idx) {
        this.cells.add(new Cell(horLength, verLength, idx));
    }

    // Returns the cell at the given index of this cells
    Cell get(int idx) {
        return this.cells.get(idx);
    }

    // Traverses this graph via breadth first search
    ArrayList<Cell> bfs(Cell from, Cell to, ArrayList<Edge> edges) {
        return searchHelp(from, to, new Queue(), edges);
    }

    // Traverses this graph via depth first search
    ArrayList<Cell> dfs(Cell from, Cell to, ArrayList<Edge> edges) {
        return searchHelp(from, to, new Stack(), edges);
    }

    // Abstraction to traverse this graph, where worklist is the cells to be
    // processed next, and edges are the edges of the maze that can be crossed
    ArrayList<Cell> searchHelp(Cell from, Cell to, ICollection worklist,
            ArrayList<Edge> edges) {
        worklist.add(from);
        while (!worklist.isEmpty()) {
            Cell next = worklist.remove();
            // If next is the final cell, add it and clear the worklist
            if (next.equals(to)) {
                this.alreadySeen.add(next);
                worklist.clear();
            }
            // If next isn't the ending cell and it hasnt been processed
            else if (!next.equals(to) && !alreadySeen.contains(next)) {
                this.alreadySeen.add(next);
                // Find its neighbors valid neighbors
                for (Cell n : next.getNeighbors(edges, this.cells)) {
                    worklist.add(n);
                    this.cameFromEdge.add(next);
                    this.cameFromEdge.add(n);
                }
            }
        }
        return this.alreadySeen;
    }

    // Traces the correct path of this graph if it wasnt manually solved
    ArrayList<Cell> reconstruct(Cell from, Cell to) {
        Cell lookingFor = to;
        ArrayList<Cell> correctPath = new ArrayList<Cell>();
        correctPath.add(to);
        // A boolean to track when we've reached the beginning cell
        boolean atBeginning = false;
        int idx = this.cameFromEdge.size() - 1;
        while (!atBeginning) {
            Cell current = this.cameFromEdge.get(idx);
            // If current is the right one, add it to correctPath and
            // backtrack to the neighbor current came from
            if (current == lookingFor && this.cameFromEdge.indexOf(current) % 2 == 1) {
                lookingFor = this.cameFromEdge.get(this.cameFromEdge.indexOf(current) - 1);
                correctPath.add(lookingFor);
            }
            // If current is the beginning cell, end the loop
            else if (current == from) {
                correctPath.add(current);
                atBeginning = true;
            }
            idx = idx - 1;
        }
        return correctPath;
    }

    // Traces the correct path of this graph if it was manually solved
    ArrayList<Cell> reconstruct(Cell from, Cell to, ArrayList<Cell> cameFromEdge) {
        Cell lookingFor = to;
        ArrayList<Cell> correctPath = new ArrayList<Cell>();
        correctPath.add(to);
        // A boolean to track when we've reached the beginning cell
        boolean atBeginning = false;
        int idx = cameFromEdge.size() - 1;
        while (!atBeginning) {
            Cell current = cameFromEdge.get(idx);
            // If current is the right one, add it to correctPath and
            // backtrack to the neighbor current came from
            if (current == lookingFor && cameFromEdge.indexOf(current) % 2 == 1) {
                lookingFor = cameFromEdge.get(cameFromEdge.indexOf(current) - 1);
                correctPath.add(lookingFor);
            }
            // If current is the beginning cell, end the loop
            else if (current == from) {
                correctPath.add(current);
                atBeginning = true;
            }
            idx = idx - 1;
        }
        return correctPath;
    }
}