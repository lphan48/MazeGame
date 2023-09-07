// Represents a maze game

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;

class MazeWorld extends World {
    // The vertical length of this game board
    int horLength;
    // The horizontal length of this game board
    int verLength;
    // All cells in the game
    Graph board;
    // A map where keys are all cells in this, and
    // values are each key's representative/group/link
    HashMap<Cell, Cell> representatives;
    // Any edges that connects two cells (and won't be visualized)
    ArrayList<Edge> edgesInTree;
    // Any edges currently knocked down
    ArrayList<Edge> edgesKnocked;
    // All edges in the graph, sorted by increasing weight
    ArrayList<Edge> worklist;
    // Whether or not the user has chosen a way to solve the maze
    boolean started;
    // If the maze should be solved with dfs (t) or bfs (f)
    boolean dfs;
    // If the maze is being solved manually
    boolean manual;
    // Whether or not the maze is currently being searched
    boolean searching;
    // The round of search that the maze is currently on
    int round;
    // The round of construction of the maze edges
    int edgeRound;
    // Cells already seen
    ArrayList<Cell> alreadySeen;
    // The correct path to solve this maze
    ArrayList<Cell> correctPath;
    // The list of cells traveled and where they came from,
    // to use for tracking if the maze is manually solved
    ArrayList<Cell> cameFromEdge;
    // Whether or not the game is over
    boolean over;

    // Constructor for real, randomized games
    MazeWorld(int horLength, int verLength) {
        // Checks that both parameters are larger than one
        if (horLength < 2 || verLength < 2) {
            throw new IllegalArgumentException("Maze must be at least 2x2");
        }
        this.horLength = horLength;
        this.verLength = verLength;
        this.started = false;
        this.edgeRound = 0;
        this.searching = false;
        this.over = false;
        this.alreadySeen = new ArrayList<Cell>();
        this.edgesKnocked = new ArrayList<Edge>();

        // Create the board, worklist, and representatives for this maze game
        this.makeFields(false);

        // Sort the worklist by ascending weight
        Collections.sort(this.worklist, new CompareEdges());

        // Make randomized maze with one, non-cyclic path to any two cells
        this.makeMaze();
    }

    // Constructor for testing, where edge weights correspond to order
    // they are created (left to right, top to bottom)
    MazeWorld(int horLength, int verLength, boolean dfs) {
        // Checks that both parameters are larger than one
        if (horLength < 2 || verLength < 2) {
            throw new IllegalArgumentException("Maze must be at least 2x2");
        }
        this.horLength = horLength;
        this.verLength = verLength;
        this.started = false;
        this.manual = false;
        this.over = false;
        this.edgeRound = 0;
        this.searching = false;
        this.dfs = dfs;
        this.alreadySeen = new ArrayList<Cell>();
        this.edgesKnocked = new ArrayList<Edge>();

        // Create the board, worklist, and representatives for this maze game
        this.makeFields(true);

        // Make maze with one, non-cyclic path to any two cells
        this.makeMaze();

        // Searches the maze
        this.searchMaze();
    }

    // Helper method that sets up the fields of this
    // EFFECT: Adds items to this board, representatives, and worklist to begin maze
    void makeFields(boolean testing) {
        this.board = new Graph();
        this.worklist = new ArrayList<Edge>();
        this.representatives = new HashMap<Cell, Cell>();
        int edgeIdx = 0;
        // For all cells within the designated board size
        for (int m = 0; m < this.horLength * this.verLength; m++) {
            // Create the necessary cell
            this.board.addCell(this.horLength, this.verLength, m);

            // Add each cell to representatives, where the key is this, and the value
            // is also this by default (every cell starts only connected to itself)
            representatives.put(this.board.get(m), this.board.get(m));

            // If this cell is not in the leftmost column, add an edge to worklist
            // that separates this and the cell to the left of this
            if (m % this.horLength != 0) {
                this.worklist.add(new Edge(this.board.get(m - 1), this.board.get(m),
                        this.horLength * this.verLength * 2, false));
                // Set the weight of this edge equal to its index in the worklist
                if (testing) {
                    this.worklist.get(edgeIdx).changeWeight(edgeIdx);
                    edgeIdx = edgeIdx + 1;
                }
            }
            // If this cell is not in the top row, add an edge to worklist
            // that separates this and the cell above this
            if (m >= this.horLength) {
                this.worklist.add(new Edge(this.board.get(m - this.horLength),
                        this.board.get(m), this.horLength * this.verLength * 2, true));
                // Set the weight of this edge equal to its index in the worklist
                if (testing) {
                    this.worklist.get(edgeIdx).changeWeight(edgeIdx);
                    edgeIdx = edgeIdx + 1;
                }
            }
        }

        // Sort the worklist by ascending weight
        Collections.sort(this.worklist, new CompareEdges());
    }

    // Helper method that randomly generates this maze using kruskal's algorithm
    // EFFECT: Unions cells in representatives and adds edges to this edgesInTree
    void makeMaze() {
        this.edgesInTree = new ArrayList<Edge>();
        for (int c = 0; c < this.worklist.size(); c++) {
            Edge cheapest = this.worklist.get(c);
            // If the two cells neighboring this edge are not yet linked
            Cell repOne = new MazeUtils().find(this.representatives, cheapest.to);
            Cell repTwo = new MazeUtils().find(this.representatives, cheapest.from);
            if (repOne != repTwo) {
                // Add this edge to edgesInTree, meaning it links two cells
                this.edgesInTree.add(cheapest);
                // Union the two cells by matching their representatives
                this.representatives.replace(repOne, repTwo);
            }
        }
    }

    // Helper method that searches the maze based on the option chosen
    // EFFECT: Adds cells to alreadySeen and correctPath
    void searchMaze() {
        // Adds cells to alreadySeen based on either dfs or bfs
        if (dfs) {
            this.alreadySeen.addAll(this.board.dfs(this.board.get(0),
                    this.board.get(this.horLength * this.verLength - 1), this.edgesInTree));
        } else if (!dfs) {
            this.alreadySeen.addAll(this.board.bfs(this.board.get(0),
                    this.board.get(this.horLength * this.verLength - 1), this.edgesInTree));
        }
        // Based on the cells in alreadySeen, backtrack to reconstruct the right path
        this.correctPath = this.board.reconstruct(this.board.get(0),
                this.board.get(this.horLength * this.verLength - 1));
    }

    // Visualizes the current scene
    public WorldScene makeScene() {
        WorldScene scene = new WorldScene(1500, 900);
        // Visualizes all cells
        for (int h = 0; h < this.horLength; h++) {
            for (int v = 0; v < this.verLength; v++) {
                this.board.get(h + this.horLength * v).drawCell(scene);
            }
        }
        // Creates an outline for the maze
        this.drawOutline(scene);

        // Visualizes all edges
        for (Edge edge : this.worklist) {
            if (!edgesKnocked.contains(edge)) {
                edge.drawEdge(scene);
            }
        }

        // Displays the section of the game that allows the user to choose dfs or bfs
        this.drawButtons(scene);

        // Gives the option to design a new random maze if the current was solved
        if (this.over) {
            scene.placeImageXY(new TextImage("You solved it! Click "
                    + "the board to play again :)", 30, Color.black), 500, 650);
        }
        return scene;
    }

    // Helper for makeScene to draw the maze outline
    // EFFECT: Places an outline for the maze on the given scene
    void drawOutline(WorldScene scene) {
        scene.placeImageXY(new RectangleImage(2, this.verLength
                * this.board.get(0).dimen(false), "solid", Color.darkGray), 1, this.verLength
                        * this.board.get(0).dimen(false) / 2);
        scene.placeImageXY(new RectangleImage(2, this.verLength * this.board.get(0).dimen(false),
                "solid", Color.darkGray), this.horLength * this.board.get(0).dimen(true),
                this.verLength * this.board.get(0).dimen(false) / 2);
        scene.placeImageXY(new RectangleImage(this.horLength * this.board.get(0).dimen(true), 2,
                "solid", Color.darkGray), this.horLength * this.board.get(0).dimen(true) / 2, 1);
        scene.placeImageXY(new RectangleImage(this.horLength * this.board.get(0).dimen(true), 2,
                "solid", Color.darkGray), this.horLength * this.board.get(0).dimen(true) / 2,
                this.verLength * this.board.get(0).dimen(false));
    }

    // Helper for makeScene to draw the game buttons
    // EFFECT: Places images onto the given scene
    void drawButtons(WorldScene scene) {
        if (!searching && !started) {
            scene.placeImageXY(new TextImage("Maze Loading...", 23, Color.black), 1160, 100);
        } else {
            if (!searching) {
                // Before the user has clicked a button
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 160);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 230);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 300);
            }
            // Illuminates the button the user clicked
            else if (manual) {
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 160);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 230);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.cyan), 1160, 300);
            } else if (dfs && searching) {
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.cyan), 1160, 160);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 230);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 300);
            } else if (!dfs && searching) {
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 160);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.cyan), 1160, 230);
                scene.placeImageXY(new RectangleImage(130, 50, "solid", Color.lightGray), 1160, 300);
            }
            scene.placeImageXY(new TextImage("Let's play!", 25, Color.black), 1160, 80);
            scene.placeImageXY(new TextImage("Choose an option:", 23, Color.black), 1160, 110);
            scene.placeImageXY(new RectangleImage(130, 50, "outline", Color.black), 1160, 160);
            scene.placeImageXY(new RectangleImage(130, 50, "outline", Color.black), 1160, 230);
            scene.placeImageXY(new RectangleImage(130, 50, "outline", Color.black), 1160, 300);
            scene.placeImageXY(new TextImage("DFS", 23, Color.black), 1160, 160);
            scene.placeImageXY(new TextImage("BFS", 23, Color.black), 1160, 230);
            scene.placeImageXY(new TextImage("Manual", 23, Color.black), 1160, 300);
        }
    }

    // Handles the passing of time
    public void onTick() {
        // Display the edges knocking down at the beginning of the game
        if (!started && !searching && edgeRound < edgesInTree.size()) {
            edgesKnocked.add(edgesInTree.get(edgeRound));
            edgeRound = edgeRound + 1;
        } else if (edgeRound == edgesInTree.size()) {
            this.started = true;
        }
        // If the user is not solving manually
        if (searching && !manual && !over) {
            // Animate the search automatically in pink
            if (!this.board.get((this.horLength * this.verLength) - 1).searched()) {
                this.alreadySeen.get(this.round).changeColor(Color.pink);
                this.round = this.round + 1;
            }
            // Display the reconstructed path in magenta
            else {
                this.over = true;
                for (Cell cell : this.correctPath) {
                    cell.changeColor(Color.magenta);
                }
            }
        }
        // If the user finished solving manually, display the reconstructed in magenta
        else if (manual && !over && searching && started
                && alreadySeen.contains(this.board.get((this.horLength * this.verLength) - 1))) {
            this.over = true;
            this.correctPath = this.board.reconstruct(this.board.get(0),
                    this.board.get(horLength * verLength - 1), this.cameFromEdge);
            for (Cell cell : this.correctPath) {
                cell.changeColor(Color.magenta);
            }
        }
    }

    // Handles mouse clicks
    public void onMouseClicked(Posn posn) {
        // If the maze just started and the user clicked the dfs button
        if (!over && started && !searching && posn.x > 1095 && posn.x < 1225) {
            if (posn.y < 185 && posn.y > 135) {
                this.dfs = true;
                this.manual = false;
                this.searchMaze();
                this.searching = true;
            }
            // If the user clicked the bfs button
            else if (posn.y < 255 && posn.y > 205) {
                this.dfs = false;
                this.manual = false;
                this.searchMaze();
                this.searching = true;
            }
            // If the user clicks the manual button
            else if (posn.y < 325 && posn.y > 275) {
                this.searching = true;
                this.manual = true;
                this.alreadySeen.add(this.board.get(0));
                this.cameFromEdge = new ArrayList<Cell>();
            }
        }
        // If the user clicked to generate a new random maze, reset everything
        else if (over && posn.x < 1000 && posn.y < 600) {
            this.searching = false;
            this.started = false;
            this.round = 0;
            this.over = false;
            this.started = false;
            this.alreadySeen = new ArrayList<Cell>();
            this.edgesKnocked = new ArrayList<Edge>();
            this.edgeRound = 0;
            this.manual = false;

            // Create the board, worklist, and representatives for this maze game
            this.makeFields(false);

            // Sort the worklist by ascending weight
            Collections.sort(this.worklist, new CompareEdges());

            // Make randomized maze with one, non-cyclic path to any two cells
            this.makeMaze();
        }
    }

    // Handles key events if the user is solving manually
    // EFFECT: Adds cels to the fields of this
    public void onKeyEvent(String key) {
        // If the user is solving manually
        if (manual && !over && started) {
            Cell current = alreadySeen.get(alreadySeen.size() - 1);
            current.changeColor(Color.pink);
            // If the user pressed up and there's a neighbor above
            if (key.equals("up") && current.idx >= horLength) {
                this.manualNeighbors(current, current.idx - horLength);
            }
            // If the user pressed down and there's a neighbor below
            else if (key.equals("down") && current.idx < horLength * (verLength - 1)) {
                this.manualNeighbors(current, current.idx + horLength);
            }
            // If the user pressed left and there's a neighbor left
            else if (key.equals("left") && current.idx % horLength != 0) {
                this.manualNeighbors(current, current.idx - 1);
            }
            // If the user pressed right and there's a neighbor right
            else if (key.equals("right") && (current.idx + 1) % horLength != 0) {
                this.manualNeighbors(current, current.idx + 1);
            } else {
                current.changeColor(Color.magenta);
            }
        }
    }

    // Helper method that adds to the fields of this if the user is solving manually
    // EFFECT: Adds cells to the fields of this
    void manualNeighbors(Cell current, int nextIdx) {
        if (current.getNeighbors(edgesInTree, this.board.cells).contains(this.board.get(nextIdx))) {
            alreadySeen.add(this.board.get(nextIdx));
            this.board.get(nextIdx).changeColor(Color.magenta);
            this.cameFromEdge.add(current);
            this.cameFromEdge.add(board.get(nextIdx));
        } else {
            current.changeColor(Color.magenta);
        }
    }
}