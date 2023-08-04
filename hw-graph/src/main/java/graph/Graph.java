package graph;
import java.util.*;


/** 
 * Represents a mutable generically labeled graph, where nodes are labeled with generic type T1.
 * Edges point from nodes to other nodes and are labeled with generic type T2.
 * No two nodes can have the same label, nor can two edges have the same label
 * if they're arranged identically (same parent and child).
 */
public class Graph <T1, T2> {
  // AF: this.nodes -> map of labeled nodes with keys as labels
  // and entries as nodes
  // this.edges -> set of labeled edges that point from edge.parent
  // to edge.child
  //
  // RI: this != null, this.nodes != null, this.edges != null,
  // no entries in this.nodes and this.edges are null,
  // all edges most point from and to nodes contained in this.nodes
  // For each entry of this.nodes, string key must be equal to corresponding node label

  private HashMap<T1, GraphNode> nodes;
  private Set<GraphEdge> edges;
  private static final boolean DEBUG = false;

  /**
   * Creates a graph object with no nodes
   */
  public Graph() {
    this.nodes = new HashMap<T1, GraphNode>();
    this.edges = new HashSet<GraphEdge>();
    this.checkRep();
  }


  /**
   * Creates a graph object with nodes and edges from the provided collections
   * For each element of parents, children, and edgelabels, edge is added that
   * points from parent entry to child entry and labeled as edgeLabel
   * @param nodes collection of node labels of type T1
   * @param parents List of node labels with type T1, will serve as parent nodes
   * @param children List of node labels with type T1, will serve as child nodes
   * @param edgeLabels List of edge labels with type T2, will serve as edges that
   * point from parents to children
   * @throws IllegalArgumentException if any nulls found, if parents, children,
   * and edgeLabels are different sizes, and if labels in parents or children
   * are not found in nodes collection
   */
  public Graph(Collection<T1> nodes, List<T1> parents,
	       List<T1> children, List<T2> edgeLabels) {
    this();
    if(nodes == null || parents == null || children == null || edgeLabels == null) {
      throw new IllegalArgumentException("Error: Null collection found");
    } else if(parents.size() != children.size() || children.size() != edgeLabels.size()) {
      throw new IllegalArgumentException
	        ("Error: parents, children, and edgeLabels are not all the same size");
    }

    // Inv: All nodeLabels up to current are turned into nodes, put into this.nodes, and non-null
    for(T1 nodeLabel : nodes) {
      if(nodeLabel == null) {
        throw new IllegalArgumentException("Error: Null node label found");
      }
      this.nodes.putIfAbsent(nodeLabel, new GraphNode(nodeLabel));
    }

    int i = 0;
    // Inv: Up to the ith element, all elements of parents, children, and edgeLabels are non-null
    // parents and children exist in this.nodes
    // Elements up to i converted to an edge and added to this.edges
    while(i < parents.size()) {
      T1 parent = parents.get(i);
      T1 child = children.get(i);
      T2 edgeLabel = edgeLabels.get(i);
      if(parent == null || child == null || edgeLabel == null) {
        throw new IllegalArgumentException
		  ("Error: Null parent, child, or edge label found");
      } else if(!(this.nodes.keySet().contains(parent)
		  && this.nodes.keySet().contains(child))) {
	throw new IllegalArgumentException
		  ("Error: Parent or child not found in given node collection");
      } else {
        this.edges.add(new GraphEdge(this.nodes.get(parent),
		                     this.nodes.get(child), edgeLabel));
	i++;
      }
    }

    this.checkRep();
  }


  /**
   * Adds labeled node to this
   * @param label label of new node
   * @spec.requires labeled node is non-null and can't already exist,
   * no behavior otherwise
   * @spec.modifies this
   * @spec.effects adds node to this
   */
  public void addNode(T1 label) {
    this.checkRep();
    if(label != null) {
      this.nodes.putIfAbsent(label, new GraphNode(label));
    }
    this.checkRep();
  }

 
  /**
   * Removes labeled node from this
   * @param label of node to remove
   * @spec.requires labeled node must exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects removes node from this, all incoming/outgoing edges removed too
   */
  public void removeNode(T1 label) {
    this.checkRep();
    if(label != null && this.nodes.keySet().contains(label)) {
      GraphNode toRemove = this.nodes.get(label);
      // Inv: All edges up to current don't point to or away from labeled node
      for(GraphEdge edge : this.edges) {
	// Reference equality is stronger here and still correct
        if(edge.parent == toRemove || edge.child == toRemove) {
          this.edges.remove(edge);
        }
      }
      this.nodes.remove(label);
    }
    this.checkRep();
  }


  /**
   * Adds edge labeled edgeLabel, points from parentNode to childNode
   * @param parentNode new edge points from parentNode
   * @param childNode new edge points to childNode
   * @param edgeLabel label of new edge
   * @spec.requires labeled nodes must exist,
   * identical edge can't already exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects adds edge to this, pointing from parentNode to childNode
   */
  public void addEdge(T1 parentNode, T1 childNode, T2 edgeLabel) {
    this.checkRep();
    if((this.nodes.keySet()).contains(parentNode)
       && (this.nodes.keySet()).contains(childNode)
       && parentNode != null && childNode != null && edgeLabel != null) {

      this.edges.add(new GraphEdge(this.nodes.get(parentNode),
		                   this.nodes.get(childNode), edgeLabel));
    }
    this.checkRep();
  }


  /**
   * Removes labeled edge that points from parentNode to childNode from this
   * @param parentNode edge to remove points from parentNode
   * @param childNode edge to remove points to childNode
   * @param edgeLabel label of edge to remove
   * @spec.requires labeled nodes must exist, labeled edge must exist,
   * and all must be non-null, no behavior otherwise
   * @spec.modifies this
   * @spec.effects removes labeled edge from this,
   * formerly pointed from parentNode to childNode and labeled edgeLabel
   */
  public void removeEdge(T1 parentNode, T1 childNode, T2 edgeLabel) {
    if(parentNode != null && childNode != null && edgeLabel != null) {
      this.checkRep();
      GraphNode parent = this.nodes.get(parentNode);
      GraphNode child = this.nodes.get(childNode);
      GraphEdge edgeCopy = new GraphEdge(parent, child, edgeLabel);
      this.edges.remove(edgeCopy);
      this.checkRep();
    }
  }


  /**
   * Maps labels of outgoing edges to labels of corresponding child nodes
   * @param label label of parent node
   * @spec.requires labeled node be non-null and be exist in graph,
   * empty map returned otherwise
   * @return Map of labeled node's outgoing edges as keys and corresponding
   * child node labels as values
   */
  public Map<T2, T1> listChildren(T1 label) {
    this.checkRep();
    Map<T2, T1> kids = new HashMap<T2, T1>();
   if(label == null || !this.nodes.keySet().contains(label)) {
     this.checkRep();
     return kids;
   }

    // Inv: If current edge points from labeled parent, add child(edge) to kids
    // All edges before current are contained in kids or don't point from labeled parent
    for(GraphEdge edge : this.edges) {
      if((edge.parent.label).equals(label)) {
	kids.putIfAbsent(edge.label, edge.child.label);
      }
    }

    this.checkRep();
    return kids;
  }


  /**
   * Lists all node labels
   * @return List of all node labels
   */
  public List<T1> listNodes() {
    this.checkRep();
    List<T1> theNodes = new ArrayList<T1>();
    theNodes.addAll(this.nodes.keySet());
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
    if(!(o instanceof Graph<?, ?>)) {
      return false;
    }
    Graph<?, ?> casted = (Graph<?, ?>)o;

    // Not nearly as simple to do, since we did away with solely checking for
    // observational equality
    
    if(this.nodes.size() != casted.nodes.size()
       || this.edges.size() != casted.edges.size()) {
      return false;
    } else if(!(this.nodes.keySet()).equals(casted.nodes.keySet())) {
      return false;
    }

    List<GraphEdge> thisEdges = new ArrayList<GraphEdge>();
    thisEdges.addAll(this.edges);
    List<Graph<?, ?>.GraphEdge> oEdges = new ArrayList<Graph<?, ?>.GraphEdge>();
    oEdges.addAll(casted.edges);

    // Inv: All edges of this and casted up to current are observationally equal
    int i = 0;
    while(i < thisEdges.size()) {
      GraphEdge ours = thisEdges.get(i);
      Graph<?, ?>.GraphEdge theirs = oEdges.get(i);
      if(!(ours.parent.label).equals(theirs.parent.label)
	 || !(ours.child.label).equals(theirs.child.label)
	 || !(ours.label.equals(theirs.label))) {

	this.checkRep();
        return false;
      }
      i++;
    }
    
    this.checkRep();
    return true;
  }


  /**
   * Gives hash value of this
   * @return hash value of this
   */
  @Override
  public int hashCode() {
    this.checkRep();
    int hash = 0;

    for(T1 nodeLabel : this.nodes.keySet()) {
      // Same value as corresponding node's hash
      hash += nodeLabel.hashCode();
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
      // and entry labels match graph labels
      for(T1 nodeLabel : this.nodes.keySet()) {
        assert (nodeLabel != null);
	assert (this.nodes.get(nodeLabel) != null);
	assert (nodeLabel.equals((this.nodes.get(nodeLabel)).label));
      }
      // Inv: All edges up to current are non-null
      // and point to nodes contained in this.nodes
      for(GraphEdge edge : this.edges) {
	assert (edge != null);
	GraphNode parent = edge.parent;
	GraphNode child = edge.child;
        assert (this.nodes.keySet()).contains(parent.label);
	assert (this.nodes.keySet()).contains(child.label);
	// Observational equality is not enough. Edge must REFERENCE
	// nodes that exist in the graph
	assert (this.nodes.get(parent.label) == parent);
	assert (this.nodes.get(child.label) == child);
      }
    }
  }




  /**
   * Represents an immutable node of a labeled graph.
   * Labeled with generic type T1
   */
  private class GraphNode {
    
    // AF: this.label -> label of this node
    // RI: this != null and this.label != null
	  
    private T1 label;


    /**
     * Creates a labeled node
     * @param label chosen label of node
     * @throws IllegalArgumentException if label == null
     */
    public GraphNode(T1 label) {
      if(label == null) {
        throw new IllegalArgumentException();
      }
      this.label = label;
      this.checkRep();
    }


    /**
     * Gives hash value of this
     * @return hash value of this
     */
    @Override
    public int hashCode() {
      this.checkRep();
      return this.label.hashCode();
    }


    /**
     * Checks if the RI has been violated, successfuly completes if not
     * @throws assertion error if RI has been violated
     */
    private void checkRep() {
      assert this != null;
      assert this.label != null;
    }
  }




  /**
   * Represents an immutable edge of a labeled graph.
   * Labeled with generic type T2
   */
  private class GraphEdge {

    // AF: Edge points from this.parent to this.child and is Labeled this.label
    // RI: this != null, parent != null, child != null, label != null

    private GraphNode parent;
    private GraphNode child;
    private T2 label;


    /**
     * Creates a labeled edge
     * @param parent parent node; edge points away from
     * @param child child node; edge points toward
     * @param label label of edge
     * @throws IllegalArgumentException if parent, child, or label are null
     */
    public GraphEdge(GraphNode parent, GraphNode child, T2 label) {
      if(parent == null || child == null || label == null) {
        throw new IllegalArgumentException();
      }
      this.parent = parent;
      this.child = child;
      this.label = label;
      this.checkRep();
    }


    /**
     * Returns true iff o is a GraphEdge and has the same parent, child, and label
     * @param o object to check equality with
     * @return true iff o is a GraphEdge and has the same parent, child, and label
     */
    @Override
    public boolean equals(Object o) {
      this.checkRep();
      // Checks for nulls!
      if(!(o instanceof Graph<?, ?>.GraphEdge)) {
        return false;
      }
      Graph<?, ?>.GraphEdge casted = (Graph<?, ?>.GraphEdge)o;
      this.checkRep();
      // They must REFERENCE same parents and children
      return (this.parent) == casted.parent && this.child == casted.child
	                                    && this.label.equals(casted.label);
    }


    /**
     * Gives hash value of this
     * @return hash value of this
     */
    @Override
    public int hashCode() {
      this.checkRep();
      return (this.parent.hashCode()) + (this.label.hashCode())
	      + (this.label.hashCode());
    }

  /**
   * Checks if the RI has been violated, successfuly completes if not
   * @throws assertion error if RI has been violated
   */
    private void checkRep() {
      assert this.parent != null;
      assert this.child != null;
      assert this.label != null;
    }
  }
}
