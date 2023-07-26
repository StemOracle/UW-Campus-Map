/*
 * My testing strategy was to use the 0, 1, 2 heuristic as the base of my tests (if applicable)
 * and then look for any wonky special cases to try out.
 */
package graph.junitTests;
import graph.Graph;

import org.junit.Test;
import static org.junit.Assert.*;

public class GraphTest {
  /**
   * Tests that illegal inputs don't change the graph.
   */
  @Test
  public void testBenignInputs() {
    Graph giraffe = new Graph();
    giraffe.addNode("leg");

    Graph hippo = new Graph();
    hippo.addNode("leg");

    Graph spider = new Graph();
    spider.addNode("eye");
    spider.addNode("fang");

    Graph flea = new Graph();
    flea.addNode("eye");
    flea.addNode("fang");

    Graph frog = new Graph();
    frog.addNode("tongue");
    frog.addNode("scales");
    frog.addEdge("tongue", "scales", "gorf");

    Graph turtle = new Graph();
    turtle.addNode("tongue");
    turtle.addNode("scales");
    turtle.addEdge("tongue", "scales", "gorf");

    Graph turtles = new Graph();
    turtles.addNode("Splinter");
    turtles.addNode("Leonardo");
    turtles.addNode("Donatello");
    turtles.addEdge("Splinter", "Leonardo", "sensei");
    turtles.addEdge("Splinter", "Donatello", "father");

    Graph ninjas = new Graph();
    ninjas.addNode("Splinter");
    ninjas.addNode("Leonardo");
    ninjas.addNode("Donatello");
    ninjas.addEdge("Splinter", "Leonardo", "sensei");
    ninjas.addEdge("Splinter", "Donatello", "father");

    Graph amoeba = new Graph();
    amoeba.addNode("blob");
    amoeba.addEdge("blob", "blob", "self");

    Graph plankton = new Graph();
    plankton.addNode("blob");
    plankton.addEdge("blob", "blob", "self");

    ////////////////////////////////////////
    // Add nodes/edges that already exist //
    ////////////////////////////////////////
    // Tests if no new nodes exist when adding a node that exists
    // 0, 1, 2 heuristic (1)
    hippo.addNode("leg");
    assertEquals(giraffe.listNodes(), hippo.listNodes());

    // Tests if no new nodes exist when adding a node that exists
    // 0, 1, 2 heuristic (2)
    flea.addNode("fang");
    assertEquals(spider.listNodes(), flea.listNodes());

    // Ditto, but tests on children nodes
    // 0, 1, 2 heurisitc (1)
    turtle.addNode("scales");
    assertEquals(frog.listChildren("tongue"), turtle.listChildren("tongue"));

    // Ditto, but tests on children nodes
    // 0, 1, 2 heuristic (2)
    ninjas.addNode("Donatello");
    assertEquals(turtles.listChildren("Splinter"), ninjas.listChildren("Splinter"));
    
    // Ditto, but a node as its own child
    // Special case
    plankton.addNode("blob");
    assertEquals(amoeba.listChildren("blob"), plankton.listChildren("blob"));

    // Adding edge that already exists
    // 0, 1, 2 heuristic (1)
    turtle.addEdge("tongue", "scales", "gorf");
    assertEquals(frog.listChildren("tongue"), turtle.listChildren("tongue"));

    // Adding edge that already exists among another
    // 0, 1, 2 heuristic (2)
    ninjas.addEdge("Splinter", "Donatello", "father");
    assertEquals(turtles.listChildren("Splinter"), ninjas.listChildren("Splinter"));

    // Adding edge that points from node to itself
    // Special case
    plankton.addEdge("blob", "blob", "self");
    assertEquals(amoeba.listChildren("blob"), plankton.listChildren("blob"));

    // Add edge between nodes that don't exist
    // Special case
    ninjas.addEdge("Splinter", "nada", "edgy");
    assertEquals(turtles.listChildren("Splinter"), ninjas.listChildren("Splinter"));


    /////////////////////////////////////////
    // Remove nodes/edges that don't exist //
    /////////////////////////////////////////

    // Remove non-existent node
    hippo.removeNode("snout");
    assertEquals(giraffe.listNodes(), hippo.listNodes());

    // Remove non-existent edge
    turtle.removeEdge("tongue", "scales", "lick");
    assertEquals(frog.listChildren("tongue"), turtle.listChildren("tongue"));
  }

  
  /**
   * Tests the removeNode() method.
   */
  @Test
  public void testRemoveNode() {
    Graph university = new Graph();
    university.addNode("library");

    Graph school = new Graph();

    Graph fish = new Graph();
    fish.addNode("bubble");
    fish.addNode("gills");

    Graph soap = new Graph();
    soap.addNode("bubble");

    Graph college = new Graph();
    college.addNode("classroom");
    college.addNode("lunchroom");
    college.addEdge("classroom", "lunchroom", "hallway");

    Graph k12 = new Graph();
    k12.addNode("classroom");

    Graph calendar = new Graph();
    calendar.addNode("week");
    calendar.addNode("Monday");
    calendar.addNode("Tuesday");
    calendar.addEdge("week", "Monday", "first");
    calendar.addEdge("week", "Tuesday", "second");

    Graph weekly = new Graph();
    weekly.addNode("week");
    weekly.addNode("Monday");
    weekly.addEdge("week", "Monday", "first");
	
    // Remove only node
    // 0, 1, 2 heuristic (1)
    university.removeNode("library");
    assertEquals(university.listNodes(), school.listNodes());

    // Remove node along other node
    // 0, 1, 2 heuristic (2)
    fish.removeNode("bubble");
    assertEquals(fish.listNodes(), soap.listNodes());
    
    // Remove an only child
    // 0, 1, 2 heuristic (1)
    college.removeNode("lunchroom");
    assertEquals(college.listChildren("classroom"), k12.listChildren("classroom"));
    
    // Remove a child with a sibling
    // 0, 1, 2 heuristic (2)
    calendar.removeNode("Tuesday");
    assertEquals(calendar.listChildren("week"), weekly.listChildren("week"));
  }


  /**
   * Tests the removeEdge() method.
   */
  @Test
  public void testRemoveEdge() {
    Graph college = new Graph();
    college.addNode("classroom");
    college.addNode("lunchroom");
    college.addEdge("classroom", "lunchroom", "hallway");

    Graph k12 = new Graph();
    k12.addNode("classroom");

    Graph calendar = new Graph();
    calendar.addNode("week");
    calendar.addNode("Monday");
    calendar.addNode("Tuesday");
    calendar.addEdge("week", "Monday", "first");
    calendar.addEdge("week", "Tuesday", "second");

    Graph weekly = new Graph();
    weekly.addNode("week");
    weekly.addNode("Monday");
    weekly.addEdge("week", "Monday", "first");

    Graph alabama = new Graph();
    alabama.addNode("child");
    alabama.addEdge("child", "child", "parent");

    Graph washington = new Graph();
    washington.addNode("child");

    // Remove lonely edge
    // 0, 1, 2 heuristic (1)
    college.removeEdge("classroom", "lunchroom", "hallway");
    assertEquals(college.listChildren("classroom"), k12.listChildren("classroom"));
    
    // Remove edge neighbored with another edge
    // 0, 1, 2 heuristic (2)
    calendar.removeEdge("week", "Tuesday", "second");
    assertEquals(calendar.listChildren("week"), weekly.listChildren("week"));
    
    // Remove edge from node pointing to itself
    // Special case
    alabama.removeEdge("child", "child", "parent");
    assertEquals(alabama.listChildren("child"), washington.listChildren("child"));
  }
}
