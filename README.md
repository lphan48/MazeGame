<h3 align="center">Maze Game</h3>
Completed Spring 2023

## About The Project
This project utilizes Kruskal's algorithm to generate random mazes that can be played either manually by the user, or automatically using depth first and breadth first algorithms. The game tracks user movement and displays the shortest possible path upon completion.

## Dependences

This project supports Java class file versions 61.0 or higher.

### Installation

1. Clone the repo:
   ```sh
   git clone https://github.com/lphan48/MazeGame
   ```
2. Navigate to project directory:
   ```sh
   cd MazeGame
   ```
3. Run the game by executing the main JAR file, located in the project's root directory:
   ```sh
   java -jar MazeGame.jar
   ```


## Usage
#### The game begins by animating the generation of a random maze using Kruskal's MST Algorithm:
<img width="1000" alt="Screenshot 2023-09-07 at 11 54 05 AM" src="https://github.com/lphan48/MazeGame/assets/116211528/96a1aa06-fa67-4f4e-8075-d3575b218736">

### Once the maze is fully generated, the user can select one of the three ways to solve the maze:
<img width="1000" alt="Screenshot 2023-09-07 at 11 54 21 AM" src="https://github.com/lphan48/MazeGame/assets/116211528/6fb558a3-6fbb-4f85-b759-f80737773724">

### Selecting 'DFS' (depth first) or 'BFS' (breadth first) will animate the maze solving using the selected algorithm. Once the maze is solved, the game will highlight the quickest possible path:
<img width="1000" alt="Screenshot 2023-09-07 at 11 54 40 AM" src="https://github.com/lphan48/MazeGame/assets/116211528/6921ec5a-c4b2-4530-bc16-540d31932329">
<img width="1000" alt="Screenshot 2023-09-07 at 11 55 04 AM" src="https://github.com/lphan48/MazeGame/assets/116211528/75d6b79e-b00d-4cdc-a06f-799025928ae6">

### Selecting 'Manual' will allow the player to solve the maze individually:
<img width="1000" alt="Screenshot 2023-09-07 at 11 56 48 AM" src="https://github.com/lphan48/MazeGame/assets/116211528/03119c67-4703-470f-b700-0a8cb0ada40b">

### After completion, clicking anywhere on the board will restart the game and create a new randomly generated maze:
<img width="1000" alt="Screenshot 2023-09-07 at 11 54 21 AM" src="https://github.com/lphan48/MazeGame/assets/116211528/f97c2749-8890-48d0-bc28-a32c0f41f9fe">








