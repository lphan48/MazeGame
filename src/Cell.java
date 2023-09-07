// Represents a cell in the maze

import java.awt.Color;
import java.util.ArrayList;

import javalib.impworld.WorldScene;
import javalib.worldimages.RectangleImage;

public class Cell {
    // The dimensions of the maze this cell is in
    private int verLength;
    private int horLength;
    // This cells index in the maze board
    public int idx;
    // The dimensions of this
    private int xSize;
    private int ySize;
    // The center coordinates of this
    private int x;
    private int y;
    private Color color;

    // Constructor that calculates correct dimensions and coordinates of this
    public Cell(int horLength, int verLength, int idx) {
        // Calculates dimensions based on 1000x600 total boardsize
        this.horLength = horLength;
        this.verLength = verLength;
        this.idx = idx;
        this.xSize = 1000 / horLength;
        this.ySize = 600 / verLength;
        // If this is the top left cell
        if (idx == 0) {
            this.x = this.xSize / 2;
            this.y = this.ySize / 2;
            this.color = Color.pink;
        } else {
            this.x = idx % horLength * this.xSize + this.xSize / 2;
            this.y = idx / horLength * this.ySize + this.ySize / 2;
            // If this is the bottom right cell
            if (idx == horLength * verLength - 1) {
                this.color = Color.cyan;
            }
            // If this is any other cell
            else {
                this.color = Color.lightGray;
            }
        }
    }

    // EFFECT: places this cell onto the given WorldScene
    public void drawCell(WorldScene scene) {
        scene.placeImageXY(new RectangleImage(this.xSize, this.ySize, "solid",
                this.color), this.x, this.y);
    }

    // Returns a dimension of this (x is true, y is false)
    public int dimen(boolean x) {
        if (x) {
            return this.xSize;
        } else {
            return this.ySize;
        }
    }

    // Returns a coordinate of this (x is true, y is false)
    public int coord(boolean x) {
        if (x) {
            return this.x;
        } else {
            return this.y;
        }
    }

    // EFFECT: changes the color of this to the given
    public void changeColor(Color col) {
        this.color = col;
    }

    // Returns a list of cells that are reachable neighbors of this
    public ArrayList<Cell> getNeighbors(ArrayList<Edge> edges, ArrayList<Cell> board) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();
        // The amount of cells there are in one row
        for (Edge edge : edges) {
            // If this is a top left cell
            if (this.idx == 0) {
                edge.toNeighbor(board.get(idx), board.get(idx + 1), neighbors);
                edge.toNeighbor(board.get(idx), board.get(idx + horLength), neighbors);
            }
            // If this is a top right cell
            else if (idx == horLength - 1) {
                edge.fromNeighbor(board.get(idx - 1), board.get(idx), neighbors);
                edge.toNeighbor(board.get(idx), board.get(idx + horLength), neighbors);
            }
            // If this is in the right column
            else if ((idx + 1) % horLength == 0) {
                edge.fromNeighbor(board.get(idx - 1), board.get(idx), neighbors);
                edge.toNeighbor(board.get(idx), board.get(idx + horLength), neighbors);
                edge.fromNeighbor(board.get(idx - horLength), board.get(idx), neighbors);
            }
            // If this is in the top row
            else if (idx < horLength) {
                edge.toNeighbor(board.get(idx), board.get(idx + 1), neighbors);
                edge.fromNeighbor(board.get(idx - 1), board.get(idx), neighbors);
                edge.toNeighbor(board.get(idx), board.get(idx + horLength), neighbors);
            }
            // If this is the bottom left cell
            else if (idx == horLength * (verLength - 1)) {
                edge.toNeighbor(board.get(idx), board.get(idx + 1), neighbors);
                edge.fromNeighbor(board.get(idx - horLength), board.get(idx), neighbors);
            }
            // If this is in the left column
            else if ((idx + 1) % horLength == 0 && idx > horLength * (600 / this.ySize - 1)) {
                edge.toNeighbor(board.get(idx), board.get(idx + 1), neighbors);
                edge.toNeighbor(board.get(idx), board.get(idx + horLength), neighbors);
                edge.fromNeighbor(board.get(idx - horLength), board.get(idx), neighbors);
            }
            // If this is in the bottom row
            else if (idx > horLength * (verLength - 1) && idx != horLength * verLength - 1) {
                edge.toNeighbor(board.get(idx), board.get(idx + 1), neighbors);
                edge.fromNeighbor(board.get(idx - horLength), board.get(idx), neighbors);
                edge.fromNeighbor(board.get(idx - 1), board.get(idx), neighbors);
            }
            // If this is any middle cell
            else {
                edge.toNeighbor(board.get(idx), board.get(idx + 1), neighbors);
                edge.toNeighbor(board.get(idx), board.get(idx + horLength), neighbors);
                edge.fromNeighbor(board.get(idx - horLength), board.get(idx), neighbors);
                edge.fromNeighbor(board.get(idx - 1), board.get(idx), neighbors);
            }
        }
        return neighbors;
    }

    // Determines if this cell has been searched or not
    public boolean searched() {
        return this.color == Color.pink;
    }
}