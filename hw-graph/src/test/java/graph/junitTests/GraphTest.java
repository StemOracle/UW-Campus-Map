/*
 * My testing strategy was to use the 0, 1, 2 heuristic as the base of my tests (if applicable)
 * and then look for any wonky special cases to try out.
 */
package graph.junitTests;
import graph.Graph;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.*;

public class GraphTest {
  @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested
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
    assertEquals(giraffe, hippo);

    // Tests if no new nodes exist when adding a node that exists
    // 0, 1, 2 heuristic (2)
    flea.addNode("fang");
    assertEquals(spider, flea);

    // Ditto, but tests on children nodes
    // 0, 1, 2 heurisitc (1)
    turtle.addNode("scales");
    assertEquals(frog, turtle);

    // Ditto, but tests on children nodes
    // 0, 1, 2 heuristic (2)
    ninjas.addNode("Donatello");
    assertEquals(turtles, ninjas);
    
    // Ditto, but a node as its own child
    // Special case
    plankton.addNode("blob");
    assertEquals(amoeba, plankton);

    // Adding edge that already exists
    // 0, 1, 2 heuristic (1)
    turtle.addEdge("tongue", "scales", "gorf");
    assertEquals(frog, turtle);

    // Adding edge that already exists among another
    // 0, 1, 2 heuristic (2)
    ninjas.addEdge("Splinter", "Donatello", "father");
    assertEquals(turtles, ninjas);

    // Adding edge that points from node to itself
    // Special case
    plankton.addEdge("blob", "blob", "self");
    assertEquals(amoeba, plankton);

    // Add edge between nodes that don't exist
    // Special case
    ninjas.addEdge("Splinter", "nada", "edgy");
    assertEquals(turtles, ninjas);


    /////////////////////////////////////////
    // Remove nodes/edges that don't exist //
    /////////////////////////////////////////

    // Remove non-existent node
    hippo.removeNode("snout");
    assertEquals(giraffe, hippo);

    // Remove non-existent edge
    turtle.removeEdge("tongue", "scales", "lick");
    assertEquals(frog, turtle);
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
    assertEquals(university, school);

    // Remove node along other node
    // 0, 1, 2 heuristic (2)
    fish.removeNode("gills");
    assertEquals(fish, soap);
    
    // Remove an only child
    // 0, 1, 2 heuristic (1)
    college.removeNode("lunchroom");
    assertEquals(college, k12);
    
    // Remove a child with a sibling
    // 0, 1, 2 heuristic (2)
    calendar.removeNode("Tuesday");
    assertEquals(calendar, weekly);
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
    k12.addNode("lunchroom");

    Graph calendar = new Graph();
    calendar.addNode("week");
    calendar.addNode("Monday");
    calendar.addNode("Tuesday");
    calendar.addEdge("week", "Monday", "first");
    calendar.addEdge("week", "Tuesday", "second");

    Graph weekly = new Graph();
    weekly.addNode("week");
    weekly.addNode("Monday");
    weekly.addNode("Tuesday");
    weekly.addEdge("week", "Monday", "first");

    Graph alabama = new Graph();
    alabama.addNode("child");
    alabama.addEdge("child", "child", "parent");

    Graph washington = new Graph();
    washington.addNode("child");

    // Remove lonely edge
    // 0, 1, 2 heuristic (1)
    college.removeEdge("classroom", "lunchroom", "hallway");
    assertEquals(college, k12);
    
    // Remove edge neighbored with another edge
    // 0, 1, 2 heuristic (2)
    calendar.removeEdge("week", "Tuesday", "second");
    assertEquals(calendar, weekly);
    
    // Remove edge from node pointing to itself
    // Special case
    alabama.removeEdge("child", "child", "parent");
    assertEquals(alabama, washington);
  }

  /**
   * Tests the equals() method
   */
  @Test
  public void equalsTestAndHashCodeTest() {
    ////////////
    // Equals //
    ////////////

    // Vacuous test
    Graph scarecrow = new Graph();
    Graph gnome = new Graph();
    assertTrue(scarecrow.equals(gnome));

    // Simple test no edges
    Graph hitman = new Graph();
    Graph assassin = new Graph();
    assassin.addNode("trained");
    assertFalse(assassin.equals(hitman));

    // Simple test no edges
    Graph computer = new Graph();
    computer.addNode("electronic");
    Graph calculator = new Graph();
    calculator.addNode("electronic");
    assertTrue(computer.equals(calculator));

    // Complex test no edges
    Graph gov = new Graph();
    gov.addNode("corrupt");
    gov.addNode("powerful");
    gov.addNode("corporate");
    Graph politician = new Graph();
    politician.addNode("corrupt");
    politician.addNode("powerful");
    politician.addNode("only sometimes corporate");
    assertFalse(politician.equals(gov));

    // Simple test one edge
    Graph myself = new Graph();
    myself.addNode("charles");
    myself.addNode("spoiled");
    myself.addEdge("charles", "spoiled", "nature");
    Graph sister = new Graph();
    sister.addNode("redacted");
    sister.addNode("spoiled");
    sister.addEdge("redacted", "spoiled", "nature");
    assertFalse(myself.equals(sister));

    // Complex test multiple edges
    Graph bankRobbery = new Graph();
    bankRobbery.addNode("vault");
    bankRobbery.addNode("loot");
    bankRobbery.addNode("getaway");
    bankRobbery.addEdge("vault", "loot", "contains");
    bankRobbery.addEdge("loot", "getaway", "run");
    Graph jewelHeist = new Graph();
    jewelHeist.addNode("vault");
    jewelHeist.addNode("loot");
    jewelHeist.addNode("getaway");
    jewelHeist.addEdge("vault", "loot", "contains");
    jewelHeist.addEdge("loot", "getaway", "run");
    assertTrue(bankRobbery.equals(jewelHeist));

    //////////////
    // hashCode //
    //////////////

    // Equal object? Equal hash
    assertTrue(scarecrow.hashCode() == gnome.hashCode());
    assertTrue(computer.hashCode() == calculator.hashCode());
    assertTrue(bankRobbery.hashCode() == jewelHeist.hashCode());

    // Should return consistent values
    int scarecrowHash = scarecrow.hashCode();
    assertEquals(scarecrow.hashCode(), scarecrowHash);
    int computerHash = computer.hashCode();
    assertEquals(computer.hashCode(), computerHash);
    int jewelHash = jewelHeist.hashCode();
    assertEquals(jewelHeist.hashCode(), jewelHash);
  }

  /**
   * Tests the constructor
   */
  public void testConstructor() {
    // Simple empty test
    Graph empty1 = new Graph();
    Graph empty2 = new Graph(new HashSet<String>(), new HashSet<String[]>());
    assertEquals(empty2, empty1);

    // Intermediate test
    Graph national = new Graph();
    national.addNode("roman");
    national.addNode("canadian");
    national.addNode("american");
    HashSet<String> setty = new HashSet<String>();
    setty.add("roman"); setty.add("canadian"); setty.add("american");
    Graph person = new Graph(setty, new HashSet<String[]>());
    assertEquals(person, national);

    // Complex test
    Graph triangle = new Graph();
    triangle.addNode("acute");
    triangle.addNode("right");
    triangle.addNode("obtuse");
    triangle.addEdge("acute", "right", "lower than");
    triangle.addEdge("right", "obtuse", "lower than");
    triangle.addEdge("obtuse", "acute", "greater than");
    HashSet<String> angles = new HashSet<String>();
    angles.add("acute"); angles.add("right"); angles.add("obtuse");
    HashSet<String[]> edges = new HashSet<String[]>();
    edges.add(new String[]{"acute", "right", "lower than"});
    edges.add(new String[]{"right", "obtuse", "lower than"});
    edges.add(new String[]{"obtuse", "acute", "greater than"});
    Graph anglez = new Graph(angles, edges);

    // Special test
    Graph childParent = new Graph();
    childParent.addNode("routine");
    childParent.addEdge("routine", "routine", "sleep");
    HashSet<String> routine = new HashSet<String>();
    routine.add("routine");
    HashSet<String[]> repeat = new HashSet<String[]>();
    repeat.add(new String[]{"routine", "routine", "sleep"});
    Graph cycle = new Graph(routine, repeat);
  }
}
