// Represents an edge between two cells

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.RectangleImage;

public class Edge {
    public int weight;
    // The left or top cell
    public Cell from;
    // The right or bottom cell
    public Cell to;
    // Whether this edge is horizontal (t) or vertical (f)
    private boolean hor;

    public Edge(Cell from, Cell to, int edgeAmount, boolean hor) {
        this.from = from;
        this.to = to;
        this.hor = hor;
        // Gives this edge a random weight
        this.weight = new Random().nextInt(edgeAmount);
    }

    // EFFECT: places this edge onto the given WorldScene
    public void drawEdge(WorldScene scene) {
        if (this.hor) {
            scene.placeImageXY(new RectangleImage(this.from.dimen(true), 2, "solid",
                    Color.darkGray), this.from.coord(true),
                    this.from.coord(false) + this.from.dimen(false) / 2);
        } else {
            scene.placeImageXY(new RectangleImage(2, this.from.dimen(false), "solid",
                    Color.darkGray),
                    this.from.coord(true)
                            + this.from.dimen(true) / 2,
                    this.from.coord(false));
        }
    }

    // EFFECT: changes the weight of this to the given (for testing)
    public void changeWeight(int weight) {
        this.weight = weight;
    }

    // Abstracted helper that adds the neighbor of this if it is reachable in the
    // maze
    // EFFECT: Adds the from cell of this edge to the given list
    public void fromNeighbor(Cell from, Cell to, ArrayList<Cell> neighbors) {
        if (this.from == from && this.to == to) {
            neighbors.add(from);
        }
    }

    // Abstracted helper that adds the neighbor of this if it is reachable in the
    // maze
    // EFFECT: Adds the to cell of this edge to the given list
    public void toNeighbor(Cell from, Cell to, ArrayList<Cell> neighbors) {
        if (this.from == from && this.to == to) {
            neighbors.add(to);
        }
    }
}