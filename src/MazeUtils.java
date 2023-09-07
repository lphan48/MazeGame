import java.util.HashMap;

// Represents utility functions
class MazeUtils {
    // Finds the final representative of the given cell in the given hashmap
    Cell find(HashMap<Cell, Cell> reps, Cell looking) {
        if (looking == reps.get(looking)) {
            return looking;
        } else {
            return find(reps, reps.get(looking));
        }
    }
}