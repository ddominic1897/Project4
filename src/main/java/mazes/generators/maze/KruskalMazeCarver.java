/*
 * Diana Dominic, Anna Lim
 * CSE 373 Project 4
 */

package mazes.generators.maze;

import java.util.Random;

import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import misc.graphs.Graph;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.
        ISet<Wall> walls = maze.getWalls();
        ISet<Room> rooms = maze.getRooms();
        Random rand = new Random();
        
        // set random weights
        for (Wall wall : walls) {
            wall.setDistance(rand.nextDouble());
        }
        
        Graph<Room, Wall> graph = new Graph<>(rooms, walls);
        ISet<Wall> result = graph.findMinimumSpanningTree();
        
        //reset to original
        for (Wall wall: walls) {
            wall.resetDistanceToOriginal();
        }
        return result;
    }
}
