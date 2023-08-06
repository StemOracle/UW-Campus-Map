package pathfinder;

import pathfinder.datastructures.Path;
import java.util.*;
import graph.Graph;


/**
 * Given a Graph with nodes labeled as generic type T edges labeled as doubles,
 * Algorithm will treat edge labels as weights and find path leading from one
 * node to another with minimal total weight.
 * Algorithm is immutable.
 */
public class Algorithm {


  /**
   * Returns path from start to dest with least total weight
   * @param <T> generic type that serve as nodes of path and compass
   * @param compass graph with edges labeled/weighted with doubles
   * @param start node to start from
   * @param dest destination node
   * @spec.requires start and dest nodes must exist in given graph,
   * IllegalArgumentException thrown otherwise
   * @throws IllegalArgumentException if either node doesn't exist in
   * given graph
   * @throws NullPointerException if compass, start, or dest is null
   * @return path from start to dest with least total weight
   * or null if no such path exists
   */
  public static <T> Path<T> findShortestDistance(Graph<T, Double> compass, T start, T dest) {
    if(compass == null || start == null || dest == null) {
        throw new NullPointerException("null compass, start node, or dest node found.");
    }
    List<T> nodes = compass.listNodes();
    if(!nodes.contains(start) || !nodes.contains(dest)) {
	throw new IllegalArgumentException("node " + start + " or " + dest + "not found in compass.");
    }
    Comparator<Path<T>> comp = new Comparator<Path<T>>() {
      public int compare(Path<T> p1, Path<T> p2) { return Double.compare(p1.getCost(), p2.getCost());}
    };

    PriorityQueue<Path<T>> active = new PriorityQueue<Path<T>>(comp);
    Set<T> finished = new HashSet<T>();
    active.add(new Path<T>(start));
   
    // Inv: All paths in active haven't been tried yet.
    // All nodes in finished we have exploerd or have prepared to explore its
    // outgoing segments. 
    while(!active.isEmpty()) {
      Path<T> minPath = active.remove();
      T minDest = minPath.getEnd();
      if(minDest.equals(dest)) {
        return minPath;
      }
      if(finished.contains(minDest)) {
        continue;
      }
      // No major invarient here just extending outgoing segments to current path.
      // Branching out paths!
      for(Graph<T, Double>.GraphEdge edge : compass.listChildren(minDest)) {
        T child = edge.getChild();
	if(!finished.contains(child)) {
          Path<T> newPath = minPath.extend(child, edge.getLabel());
	  active.add(newPath);
	}
      }
      finished.add(minDest);
    }
    return null;
  }
}


