package graph;
import java.util.List;

/**
 * Represents a mutable labeled graph, where nodes are labeled with strings
 * and edges point from nodes to other nodes, also labeled with strings.
 * No two nodes can have the same label, nor can two edges have the same label
 * if they're arranged identically (same parent and child).
 */
public class Graph {


  /** Creates a graph object with no nodes.
   */
  public Graph() {
    throw new RuntimeException();
  }


  /** Adds labeled node to this
   * @param label label of new node
   * @spec.requires labeled node can't already exist, no behavior otherwise
   * @spec.effects adds node to this
   */
  public void addNode(String label) {
    throw new RuntimeException();
  }

 
  /** Removes labeled node from this
   * @param label of node to remove
   * @spec.requires labeled node must exist, no behavior otherwise
   * @spec.effects removes node from this, all associated edges are removed as well
   */
  public void removeNode(String label) {
    throw new RuntimeException();
  }


  /** Adds edge labeled edgeLabel, points from parentNode to childNode
   * @param parentNode new edge points from parentNode
   * @param childNode new edge points to childNode
   * @param edgeLabel label of new edge
   * @spec.requires labeled nodes must exist, identical edge can't already exist no behavior otherwise
   * @spec.effects adds edge to this, pointing from parentNode to childNode
   */
  public void addEdge(String parentNode, String childNode, String edgeLabel) {
    throw new RuntimeException();
  }


  /** Removes labeled edge that points from parentNode to childNode from this
   * @param parentNode edge to remove points from parentNode
   * @param childNode edge to remove points to childNode
   * @param edgeLabel label of edge to remove
   * @spec.requires labeled nodes must exist, labeled edge must exist, no behavior otherwise
   * @spec.requires removes edge from this, formerly pointed from parentNode to childNode
   */
  public void removeEdge(String parentNode, String childNode, String edgeLabel) {
    throw new RuntimeException();
  }


  /**
   * Lists labeled node's children's labels
   * @param label label of parent node
   * @spec.requires labeled node must exist, no behavior otherwise
   * @return List of labeled node's children's labels
   */
  public List<String> listChildren(String label) {
    throw new RuntimeException();
  }


  /**
   * Lists all nodes' labels
   * @return List of all node's labels
   */
  public List<String> listNodes() {
    throw new RuntimeException();
  }
}
