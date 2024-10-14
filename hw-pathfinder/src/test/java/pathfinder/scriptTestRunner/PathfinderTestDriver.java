package pathfinder.scriptTestRunner;

import pathfinder.datastructures.Path;
import pathfinder.Algorithm;
import graph.Graph;

import java.util.*;
import java.io.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    private final Map<String, Graph<String, Double>> graphs = new HashMap<String, Graph<String, Double>>();
    private final PrintWriter output;
    private final BufferedReader input;


    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
	this.input = new BufferedReader(r);
        this.output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
               (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }


    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
		case "FindPath":
		    findPath(arguments);
		    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new IllegalArgumentException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<String, Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new IllegalArgumentException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> giraffe = graphs.get(graphName);
	giraffe.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new IllegalArgumentException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        Graph<String, Double> giraffe = graphs.get(graphName);
	double weight = Double.parseDouble(edgeLabel);
	giraffe.addEdge(parentName, childName, weight);
	String val = String.format("%.3f", weight).trim();
        output.println("added edge " + val + " from " + parentName + " to " + childName + " in " + graphName);
    }
    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new IllegalArgumentException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, Double> giraffe = graphs.get(graphName);
	List<String> nodes = giraffe.listNodes();
	Collections.sort(nodes);
	String toGo = graphName + " contains:";
	if(!nodes.isEmpty()) {	
            int i = 0;
            while(i < nodes.size()) {
	        toGo += " " + nodes.get(i);
		i++;
	    }	
	}
	output.println(toGo);
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new IllegalArgumentException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> giraffe = graphs.get(graphName);
        List<Graph<String, Double>.GraphEdge> edges;
	edges = giraffe.listChildren(parentName);
	
	List<String> kids = new ArrayList<String>();

        for(Graph<String, Double>.GraphEdge edge : edges) {
	  kids.add(edge.getChild() + "(" + String.format("%.3f", edge.getLabel()).trim() + ")");
	}

	Collections.sort(kids);

        String toGo = "the children of " + parentName + " in " + graphName + " are:";
	if(!kids.isEmpty()) {
            int i = 0;
            while(i < kids.size()) {
                toGo += " " + kids.get(i);
		i++;
            }
	}
	output.println(toGo);
    }

    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
	    throw new IllegalArgumentException("Bad arguments to FindChildren: " + arguments);
	}

        String graphName = arguments.get(0);
	String startNode = arguments.get(1);
	String destNode = arguments.get(2);
	findPath(graphName, startNode, destNode);
    }

    private void findPath(String graphName, String startNode, String destNode) {
	Graph<String, Double> giraffe = graphs.get(graphName);
        List<String> giraffeNodes = giraffe.listNodes();

        if(!giraffeNodes.contains(startNode) || !giraffeNodes.contains(destNode)) {
	    if(!giraffeNodes.contains(startNode)) {
	        output.println("unknown: " + startNode);
	    }
	    if(!giraffeNodes.contains(destNode)) {
	        output.println("unknown: " + destNode);
	    }
	} else {
	    Path<String> journey = Algorithm.findShortestDistance(giraffe, startNode, destNode);
	    output.println("path from " + startNode + " to " + destNode + ":");
	    if(journey == null) {
	        output.println("no path found");
	    } else {
		Iterator<Path<String>.Segment> segs = journey.iterator();

	        while(segs.hasNext()) {
		    Path<String>.Segment seg = segs.next();
	            output.println(seg.getStart() + " to " + seg.getEnd() + " with weight " + String.format("%.3f", seg.getCost()));
		}

	        output.println(String.format("total cost: %.3f", journey.getCost()));	
	    }
	}
    }


}

