package graph;
import java.util.*;


/** 
 * Represents a mutable generically labeled graph, where nodes are labeled with generic type T1.
 * Edges point from nodes to other nodes and are labeled with generic type T2.
 * No two nodes can have the same label, nor can two edges have the same label
 * if they're arranged identically (same parent and child).
 */
public class Graph <T1, T2> {
  // AF: this.nodes -> collection of labeled nodes
  // this.edges -> collection of labeled edges
  // pointing from a parent node to a child node

  // RI: this != null, this.nodes != null, this.edges != null,
  // no entries in this.nodes and this.edges are null,
  // all edges most point from and to nodes contained in this.nodes
  // For each entry of this.nodes, string key must be equal to corresponding node label

  /**
   * Set of labeled nodes
   */
  private HashSet<T1> nodes;

  /**
   * Set of GraphEdge objects that point to and from nodes
   */
  private HashSet<GraphEdge> edges;

  /**
   * For checkRep() purposes only
   */
  private static final boolean DEBUG = false;

  /**
   * Creates a graph object with no nodes
   */
  public Graph() {
    this.nodes = new HashSet<T1>();
    this.edges = new HashSet<GraphEdge>();
    this.checkRep();
  }


  /**
   * Creates a graph object with nodes and edges from the provided collections
   * For each element of parents, children, and edgelabels, edge is added that
   * points from parent entry to child entry and labeled as edgeLabel
   * @param nodes collection of nodes labeled with type T1
   * @param edges Collection of edges labeled with type T2
   * @throws IllegalArgumentException if an edge parent/child not found in nodes
   * @throws NullPointerException if any nulls found
   */
  public Graph(Collection<T1> nodes, Collection<GraphEdge> edges) {
    this();
    if(nodes == null || edges == null) {
      throw new NullPointerException("null node or edge found.");
    }

    // Inv: All nodes up to current are non-null and added to this.nodes
    for(T1 node : nodes) {
      if(node == null) {
        throw new NullPointerException("null node found.");
      }
      this.nodes.add(node);
    }
    
    // Inv: All edges up to current are non-null, have non-null parents,
    // have non-null children, have non-null labels, and are added to this.edges
    for(GraphEdge edge : edges) {
      T1 parent = edge.parent, child = edge.child;
      T2 label = edge.label;
      if(parent == null || child == null || label == null) {
        throw new NullPointerException
		  ("null parent node, child node, or edge label found.");
      } else if(!this.nodes.contains(parent) || !this.nodes.contains(child)) {
	throw new IllegalArgumentException
		  ("node " + parent + " or " + child + " not found in graph.");
      } else {
        this.edges.add(edge);
      }
    }

    this.checkRep();
  }


  /**
   * Adds labeled node to this
   * @param node label of new node
   * @spec.requires labeled node is non-null and can't already exist,
   * no behavior otherwise
   * @spec.modifies this
   * @spec.effects adds node to this
   */
  public void addNode(T1 node) {
    this.checkRep();
    if(node != null) {
      this.nodes.add(node);
    }
    this.checkRep();
  }

 
  /**
   * Removes labeled node from this
   * @param node label of node to remove
   * @spec.requires labeled node must exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects removes node from this, all incoming/outgoing edges removed too
   */
  public void removeNode(T1 node) {
    this.checkRep();
    if(node != null) {
      // Inv: All edges up to current don't point to or away from labeled node
      for(GraphEdge edge : this.edges) {
        if(edge.parent.equals(node) || edge.child.equals(node)) {
          this.edges.remove(edge);
        }
      }
      this.nodes.remove(node);
    }
    this.checkRep();
  }


  /**
   * Adds edge labeled edge, points from parent to child
   * @param parent new edge points from parent
   * @param child new edge points to child
   * @param label label of new edge
   * @spec.requires parent and child nodes must exist,
   * identical edge can't already exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects adds edge to this, pointing from parent to child
   */
  public void addEdge(T1 parent, T1 child, T2 label) {
    this.checkRep();
    if(this.nodes.contains(parent) && this.nodes.contains(child)
       && parent != null && child != null && label != null) {

      this.edges.add(new GraphEdge(parent, child, label));
    }
    this.checkRep();
  }


  /**
   * Adds edge labeled edge to this
   * @param edge edge to add
   * @spec.requires parent and child nodes must exist,
   * identical edge can't already exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects adds labeled edge to this, pointing from parent to child
   */
  public void addEdge(Graph<T1, T2>.GraphEdge edge) {
    this.checkRep();
    if(edge != null) {
      addEdge(edge.parent, edge.child, edge.label);
    }
  }


  /**
   * Removes labeled edge that points from parent to child from this
   * @param parent edge to remove points from parentNode
   * @param child edge to remove points to childNode
   * @param label label of edge to remove
   * @spec.requires parent and child nodes must exist, labeled edge must exist,
   * all must be non-null, no behavior otherwise
   * @spec.modifies this
   * @spec.effects removes labeled edge from this,
   * that points from parent to child and labeled label
   */
  public void removeEdge(T1 parent, T1 child, T2 label) {
    this.checkRep();
    if(parent != null && child != null && label != null) {
      GraphEdge edgeCopy = new GraphEdge(parent, child, label);
      this.edges.remove(edgeCopy);
      this.checkRep();
    }
  }


  /**
   * Removes labeled edge from this
   * @param edge edge to remove
   * @spec.requires parent and child nodes must exist, labeled edge must exist,
   * all must be non-null, no behavior otherwise
   * @spec.modifies this
   * @spec.effects removes labeled edge from this,
   * that points from parent to child and labeled label
   */
  public void removeEdge(Graph<T1, T2>.GraphEdge edge) {
    this.checkRep();
    if(edge != null) {
      removeEdge(edge.parent, edge.child, edge.label);
    }
  }


  /**
   * Lists all outgoing edges
   * @param node label of parent node
   * @spec.requires labeled node be non-null and be exist in graph,
   * empty map returned otherwise
   * @return List of all outgoing edges
   */
  public List<GraphEdge> listChildren(T1 node) {
    this.checkRep();
    List<GraphEdge> outgoing = new ArrayList<GraphEdge>();
    if(node == null || !this.nodes.contains(node)) {
      this.checkRep();
      return outgoing;
    }

    // Inv: If current edge points from labeled parent, add child(edge) to kids
    // All edges before current are contained in kids or don't point from labeled parent
    for(GraphEdge edge : this.edges) {
      if(edge.parent.equals(node)) {
	outgoing.add(edge);
      }
    }

    this.checkRep();
    return outgoing;
  }


  /**
   * Lists all node labels
   * @return List of all node labels
   */
  public List<T1> listNodes() {
    this.checkRep();
    List<T1> theNodes = new ArrayList<T1>();
    theNodes.addAll(this.nodes);
    this.checkRep();
    return theNodes;
  }


  /**
   * Returns true iff o is a Graph and has the same nodes and edges as this
   * @param o object to check equality with
   * @return true iff o is a Graph and has the same nodes and edges as this
   */
  @Override
  public boolean equals(Object o) {
    this.checkRep();
    // Checks for nulls!
    if(this == o) {
      return true;
    } else if(!(o instanceof Graph<?, ?>)) {
      // This branch also checks for nulls
      return false;
    }
    Graph<?, ?> casted = (Graph<?, ?>)o;
    this.checkRep();
    return (this.nodes).equals(casted.nodes) && (this.edges).equals(casted.edges);
  }


  /**
   * Gives hash value of this
   * @return hash value of this
   */
  @Override
  public int hashCode() {
    this.checkRep();
    int hash = 0;

    for(T1 node : this.nodes) {
      hash += node.hashCode();
    }

    for(GraphEdge edge : this.edges) {
      hash += edge.hashCode();
    }

    this.checkRep();
    return hash;
  }


  /**
   * Checks if the RI has been violated, successfuly completes if not
   * @throws AssertionError if RI has been violated
   */
  private void checkRep() {
    assert this != null;
    assert this.nodes != null;
    assert this.edges != null;
    if(DEBUG) {
      // Inv: All nodes up to current are non-null
      for(T1 node : this.nodes) {
        assert (node != null);
      }
      // Inv: All edges up to current are non-null
      // and point to nodes contained in this.nodes
      for(GraphEdge edge : this.edges) {
	assert (edge != null);
        assert this.nodes.contains(edge.parent);
	assert this.nodes.contains(edge.child);
      }
    }
  }




  /**
   * Represents an immutable edge of a labeled graph.
   * Labeled with generic type T2
   */
  public class GraphEdge {

    // AF: Edge points from this.parent to this.child and is Labeled this.label
    // RI: this != null, parent != null, child != null, label != null

    private final T1 parent;
    private final T1 child;
    private final T2 label;

    /**
     * Creates a labeled edge
     * @param parent parent node; edge points away from
     * @param child child node; edge points toward
     * @param label label of edge
     * @throws NullPointerException if parent, child, or label are null
     */
    public GraphEdge(T1 parent, T1 child, T2 label) {
      if(parent == null || child == null || label == null) {
        throw new NullPointerException
		  ("null parent node, child node, or edge label found.");
      }
      this.parent = parent;
      this.child = child;
      this.label = label;
      this.checkRep();
    }


    /**
     * Returns label of parent node
     * @return label of parent node
     */
    public T1 getParent() {
      this.checkRep();
      return this.parent;
    }


    /**
     * Returns Label of child node
     * @return label of child node
     */
    public T1 getChild() {
      this.checkRep();
      return this.child;
    }


    /**
     * Returns label of this edge
     * @return label of this edge
     */
    public T2 getLabel() {
      this.checkRep();
      return this.label;
    }


    /**
     * Returns true iff o is a GraphEdge and has the same parent, child, and label
     * @param o object to check equality with
     * @return true iff o is a GraphEdge and has the same parent, child, and label
     */
    @Override
    public boolean equals(Object o) {
      this.checkRep();
      if(this == o) {
        return true;
      } else if(!(o instanceof Graph<?, ?>.GraphEdge)) {
	// This branch also checks for nulls
        return false;
      }
      Graph<?, ?>.GraphEdge casted = (Graph<?, ?>.GraphEdge)o;
      this.checkRep();
      return (this.parent).equals(casted.parent)
	     && (this.child).equals(casted.child)
	     && (this.label).equals(casted.label);
    }


    /**
     * Gives hash value of this
     * @return hash value of this
     */
    @Override
    public int hashCode() {
      this.checkRep();
      return (this.parent.hashCode()) + (this.child.hashCode())
	                              + (this.label.hashCode());
    }

  /**
   * Checks if the RI has been violated, successfuly completes if not
   * @throws AssertionError if RI has been violated
   */
    private void checkRep() {
      assert this.parent != null;
      assert this.child != null;
      assert this.label != null;
    }
  }
}
