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
public class Algorithm<T> {


  /**
   * Graph with Double-valued edge weights
   */
  private Graph<T, Double> compass;


  /**
   * Creates a new Algorithm object
   * @param compass Graph with Doubles as weights
   * @throws NullPointerException if compass is null
   */
  public Algorithm(Graph<T, Double> compass) {
    if(compass == null) {
      throw new NullPointerException("compass can't be null.");
    }
    this.compass = compass;
  }


  /**
   * Returns path from start to dest with least total weight
   * @param start node to start from
   * @param dest destination node
   * @spec.requires start and dest nodes must exist in given graph,
   * IllegalArgumentException thrown otherwise
   * @throws IllegalArgumentException if either node doesn't exist in
   * given graph
   * @return path from start to dest with least total weight
   * or null if no such path exists
   */
  public Path<T> findShortestDistance(T start, T dest) {
    Comparator<Path<T>> comp = new Comparator<Path<T>>() {
      public int compare(Path<T> p1, Path<T> p2) { return Double.compare(p1.getCost(), p2.getCost());}
    };
 

    PriorityQueue<Path<T>> active = new PriorityQueue<Path<T>>(comp);
    Set<T> finished = new HashSet<T>();
    active.add(new Path<T>(start));

    while(!active.isEmpty()) {
      Path<T> minPath = active.remove();
      T minDest = minPath.getEnd();
      if(minDest.equals(dest)) {
        return minPath;
      }
      if(finished.contains(minDest)) {
        continue;
      }
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


