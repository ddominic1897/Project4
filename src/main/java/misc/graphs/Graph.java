/*
 * Diana Dominic, Anna Lim
 * CSE 373 Project 4
 */

package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    private IList<V> vertices;
    private IList<E> edges;
    IDictionary<V, ISet<E>> adjList;   
    
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {   
        this.adjList = new ChainedHashDictionary<V, ISet<E>>();
        for (V vertex : vertices) {
            adjList.put(vertex, new ChainedHashSet<E>());
        }
        for (E edge : edges) {
            if (edge.getWeight() < 0.0) {
                throw new IllegalArgumentException();
            }
            if (!(vertices.contains(edge.getVertex1()) && vertices.contains(edge.getVertex2()))) {
                throw new IllegalArgumentException();
            }
            adjList.get(edge.getVertex1()).add(edge);
            adjList.get(edge.getVertex2()).add(edge);
        }
       this.vertices = vertices;
       this.edges = edges;            
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vertices.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> minTree = new ChainedHashSet<>();
        ArrayDisjointSet<V> forest = new ArrayDisjointSet<>();
        IList<E> sortedEdges = Searcher.topKSort(numEdges(), edges);
        
        //populate "forest"
        for (V vertex: vertices) {
            forest.makeSet(vertex);
        }
        
        for (E edge : sortedEdges) {
            if (forest.findSet(edge.getVertex1()) != forest.findSet(edge.getVertex2())) {
                forest.union(edge.getVertex1(), edge.getVertex2());
                minTree.add(edge);
            }
        }        
        return minTree;
    }    

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {     
        IDictionary<V, Double> distances = new ChainedHashDictionary<V, Double>();
        IDictionary<V, IList<E>> theEdges = new ChainedHashDictionary<V, IList<E>>();
        IPriorityQueue<Vertex> minDist = new ArrayHeap<Vertex>();
        ISet<V> seen = new ChainedHashSet<V>();
        
        //set up vertex distances
        for (V vertex : this.vertices) {
            theEdges.put(vertex,  new DoubleLinkedList<>());
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);  
        minDist.insert(new Vertex(start, 0.0));
        
        while (!minDist.isEmpty()) {
            V currentVertex = minDist.removeMin().getVertex();
            //return path of edges from starting vertex
            if (currentVertex.equals(end)) {
                return theEdges.get(currentVertex);
            }
            //explore if not seen before
            if (!seen.contains(currentVertex)) {
                seen.add(currentVertex);
                
                for (E edge: adjList.get(currentVertex)) {
                    V other = edge.getOtherVertex(currentVertex);
                    //haven't seen other vertex
                    if (!seen.contains(other)) {
                        double updatedDist = distances.get(currentVertex) + edge.getWeight();
                        
                        //if dist needs to be updated
                        if (distances.get(other) >= updatedDist) {
                            minDist.insert(new Vertex(other, updatedDist));
                            distances.put(other, updatedDist);                            
                            if (currentVertex.equals(start)) {
                                theEdges.put(currentVertex, new DoubleLinkedList<E>());
                            }
                            
                            //copy over to new path
                            IList<E> updatedEdge = new DoubleLinkedList<E>();
                            for (E otherEdge : theEdges.get(currentVertex)) {
                                updatedEdge.add(otherEdge);
                            }
                            //update 
                            theEdges.put(other, updatedEdge);
                            theEdges.get(other).add(edge);                          
                        }
                    }                   
                }
            }
        }
        //no path exists
        throw new NoPathExistsException();        
    }
    
    private class Vertex implements Comparable<Vertex> {
        private V vertex;
        private Double dist;
        
        public Vertex(V vertex, double dist) {
            this.vertex = vertex;
            this.dist = dist;
        }
        
        public V getVertex() {
            return this.vertex;
        }
        
        @Override
        public int compareTo(Graph<V, E>.Vertex o) {
            return (int) (dist.compareTo(o.dist));
        }
    }
}
