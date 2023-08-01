package graph;
import java.util.*;


/** 
 * Represents a mutable labeled graph, where nodes are labeled with strings
 * and edges point from nodes to other nodes, also labeled with strings.
 * No two nodes can have the same label, nor can two edges have the same label
 * if they're arranged identically (same parent and child).
 */
public class Graph {
  // AF: this.nodes -> collection of labeled nodes with keys as labels
  // and entries as nodes
  // this.edges -> set of labeled edges that point from edge.parent
  // to edge.child
  //
  // RI: this != null, this.nodes != null, this.edges != null,
  // no entries in this.nodes and this.edges are null,
  // all edges most point from and to nodes contained in this.nodes
  // For each entry of this.nodes, string key must be equal to corresponding node label

  private HashMap<String, GraphNode> nodes;
  private Set<GraphEdge> edges;
  private static final boolean DEBUG = true;

  /**
   * Creates a graph object with no nodes
   */
  public Graph() {
    this.nodes = new HashMap<String, GraphNode>();
    this.edges = new HashSet<GraphEdge>();
    checkRep();
  }


  /**
   * Creates a graph object with nodes and edges from provided sets
   * @param nodes set of node labels
   * @param edges set of edges formatted as [parentLabel, childLabel, edgeLabel]
   * @throws IllegalArgumentException if edges entries aren't properly formatted,
   * if parent/child nodes don't exist, if nodes or edges are null, or if any of
   * their elements are null
   */
  public Graph(HashSet<String> nodes, HashSet<String[]> edges) {
    this();
    if(nodes == null || edges == null) {
      throw new IllegalArgumentException();
    }

    // Inv: All nodeLabels up to current are turned into nodes, put into this.nodes, and non-null
    for(String nodeLabel : nodes) {
      if(nodeLabel == null) {
        throw new IllegalArgumentException();
      }

      this.nodes.put(nodeLabel, new GraphNode(nodeLabel));
    }

    // Inv: All edgeArrs up to current are turned into edges, added to this.edges, and non-null
    for(String[] edgeArr : edges) {
      if(edgeArr == null || edgeArr[0] == null || edgeArr[1] == null || edgeArr[2] == null) {
        throw new IllegalArgumentException();
      } else if(!(this.nodes.keySet()).contains(edgeArr[0])
	        || !(this.nodes.keySet().contains(edgeArr[1]))) {
	throw new IllegalArgumentException();
      } else {
	this.edges.add(new GraphEdge(this.nodes.get(edgeArr[0]), this.nodes.get(edgeArr[1]), edgeArr[2]));
      }
    }
    this.checkRep();
  }


  /**
   * Adds labeled node to this
   * @param label label of new node
   * @spec.requires labeled node can't already exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects adds node to this
   */
  public void addNode(String label) {
    this.checkRep();
    GraphNode newNode = new GraphNode(label);
    this.nodes.put(label, newNode);
    this.checkRep();
  }

 
  /**
   * Removes labeled node from this
   * @param label of node to remove
   * @spec.requires labeled node must exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects removes node from this, all associated edges are removed as well
   */
  public void removeNode(String label) {
    this.checkRep();
    this.nodes.remove(label);

    // Inv: All edges up to current don't point to or away from labeled node
    for(GraphEdge edge : this.edges) {
      if((edge.parent.label).equals(label) || (edge.child.label).equals(label)) {
        this.edges.remove(edge);
      }
    }

    this.checkRep();
  }


  /**
   * Adds edge labeled edgeLabel, points from parentNode to childNode
   * @param parentNode new edge points from parentNode
   * @param childNode new edge points to childNode
   * @param edgeLabel label of new edge
   * @spec.requires labeled nodes must exist, identical edge can't already exist no behavior otherwise
   * @spec.modifies this
   * @spec.effects adds edge to this, pointing from parentNode to childNode
   */
  public void addEdge(String parentNode, String childNode, String edgeLabel) {
    this.checkRep();

    if((this.nodes.keySet()).contains(parentNode) && (this.nodes.keySet()).contains(childNode)) {
      this.edges.add(new GraphEdge(this.nodes.get(parentNode), this.nodes.get(childNode), edgeLabel));
    }

    this.checkRep();
  }


  /**
   * Removes labeled edge that points from parentNode to childNode from this
   * @param parentNode edge to remove points from parentNode
   * @param childNode edge to remove points to childNode
   * @param edgeLabel label of edge to remove
   * @spec.requires labeled nodes must exist, labeled edge must exist, no behavior otherwise
   * @spec.modifies this
   * @spec.effects removes edge from this, formerly pointed from parentNode to childNode
   */
  public void removeEdge(String parentNode, String childNode, String edgeLabel) {
    this.checkRep();
    GraphNode parent = this.nodes.get(parentNode);
    GraphNode child = this.nodes.get(childNode);
    GraphEdge edgeCopy = new GraphEdge(parent, child, edgeLabel);
    this.edges.remove(edgeCopy);
    this.checkRep();
  }


  /**
   * Lists labeled node's children's labels formatted as childLabel(edgeLabel)
   * in alphabetical format
   * @param label label of parent node
   * @spec.requires labeled node must exist, no behavior otherwise
   * @return List of labeled node's children's labels formatted as childLabel(edgeLabel)
   * in alphabetical format
   */
  public List<String> listChildren(String label) {
    this.checkRep();
    List<String> kids = new ArrayList<String>();

    // Inv: If edge points from labeled parent, add child(edge) to kids
    // All edges up to current either don't point from parent or are contained in kids
    for(GraphEdge edge : this.edges) {
      if((edge.parent.label).equals(label)) {
	kids.add(edge.child.label + "(" + edge.label + ")");
      }
    }

    kids.sort(Comparator.naturalOrder());
    this.checkRep();
    return kids;
  }


  /**
   * Lists all nodes' labels in alphabetical order
   * @return List of all node's labels in alphabetical order
   */
  public List<String> listNodes() {
    this.checkRep();
    List<String> theNodes = new ArrayList<String>();
    theNodes.addAll(this.nodes.keySet());
    theNodes.sort(Comparator.naturalOrder());
    this.checkRep();
    return theNodes;
  }


  /**
   * Returns iff o is a Graph and has the same nodes and edges as this
   * @param o object to check equality with
   * @return iff o is a Graph and has the same nodes and edges as this
   */
  @Override
  public boolean equals(Object o) {
    this.checkRep();
    if(!(o instanceof Graph)) {
      return false;
    }
    Graph casted = (Graph)o;
    this.checkRep();
    return ((this.nodes).equals(casted.nodes)) && ((this.edges).equals(casted.edges));
  }


  /**
   * Gives hash value of this
   * @return hash value of this
   */
  @Override
  public int hashCode() {
    this.checkRep();
    int hash = 0;

    for(String nodeLabel : this.nodes.keySet()) {
      hash += (this.nodes.get(nodeLabel)).hashCode();
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
      // Inv: All nodes up to current are non-null and entry labels match graph labels
      for(String nodeLabel : this.nodes.keySet()) {
        assert (nodeLabel != null);
	assert (this.nodes.get(nodeLabel) != null);
	assert (nodeLabel.equals((this.nodes.get(nodeLabel)).label));
      }
      // Inv: All edges up to current are non-null and point to nodes contained in this.nodes
      for(GraphEdge edge : this.edges) {
	assert (edge != null);
        assert (this.nodes.keySet()).contains(edge.parent.label);
	assert (this.nodes.keySet()).contains(edge.child.label);
      }
    }
  }




  /**
   * Represents an immutable node of a labeled graph
   */
  private class GraphNode {
    
    // AF: this.label -> label of this node
    // RI: this != null and this.label != null
	  
    String label;


    /**
     * Creates a labeled node
     * @param label chosen label of node
     * @throws IllegalArgumentException if label == null
     */
    public GraphNode(String label) {
      if(label == null) {
        throw new IllegalArgumentException();
      }
      this.label = label;
      this.checkRep();
    }


    /**
     * Returns true iff o is a GraphNode and has the same label as this
     * @param o object to check equality with
     * @return true iff o is a GraphNode and has the same label as this
     */
    @Override
    public boolean equals(Object o) {
      this.checkRep();
      if(!(o instanceof GraphNode)) {
        return false;
      }
      GraphNode casted = (GraphNode)o;
      this.checkRep();
      return (this.label).equals(casted.label);
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
   * Represents an immutable edge of a labeled graph
   */
  private class GraphEdge {

    // AF: Edge points from this.parent to this.child and is Labeled this.label
    // RI: this != null, parent != null, child != null, label != null

    GraphNode parent;
    GraphNode child;
    String label;


    /**
     * Creates a labeled edge
     * @param parent parent node; edge points away from
     * @param child child node; edge points toward
     * @param label label of edge
     * @throws IllegalArgumentException if parent, child, or label are null
     */
    public GraphEdge(GraphNode parent, GraphNode child, String label) {
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
      if(!(o instanceof GraphEdge)) {
        return false;
      }
      GraphEdge casted = (GraphEdge)o;
      this.checkRep();
      return (this.parent.label).equals(casted.parent.label)
	  && (this.child.label).equals(casted.child.label)
	  && (this.label).equals(casted.label);
    }


    /**
     * Gives hash value of this
     * @return hash value of this
     */
    @Override
    public int hashCode() {
      this.checkRep();
      return (this.parent.hashCode()) + (this.label.hashCode()) + (this.label.hashCode());
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
