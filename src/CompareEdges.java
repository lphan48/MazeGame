import java.util.Comparator;

// Represents a comparator for edges
class CompareEdges implements Comparator<Edge> {
    // Compares edges by weight
    public int compare(Edge one, Edge two) {
        return one.weight - two.weight;
    }
}