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
    Graph<String, String> giraffe = new Graph<String, String>();
    giraffe.addNode("leg");

    Graph<String, String> hippo = new Graph<String, String>();
    hippo.addNode("leg");

    Graph<String, String> spider = new Graph<String, String>();
    spider.addNode("eye");
    spider.addNode("fang");

    Graph<String, String> flea = new Graph<String, String>();
    flea.addNode("eye");
    flea.addNode("fang");

    Graph<String, String> frog = new Graph<String, String>();
    frog.addNode("tongue");
    frog.addNode("scales");
    frog.addEdge("tongue", "scales", "gorf");

    Graph<String, String> turtle = new Graph<String, String>();
    turtle.addNode("tongue");
    turtle.addNode("scales");
    turtle.addEdge("tongue", "scales", "gorf");

    Graph<String, String> turtles = new Graph<String, String>();
    turtles.addNode("Splinter");
    turtles.addNode("Leonardo");
    turtles.addNode("Donatello");
    turtles.addEdge("Splinter", "Leonardo", "sensei");
    turtles.addEdge("Splinter", "Donatello", "father");

    Graph<String, String> ninjas = new Graph<String, String>();
    ninjas.addNode("Splinter");
    ninjas.addNode("Leonardo");
    ninjas.addNode("Donatello");
    ninjas.addEdge("Splinter", "Leonardo", "sensei");
    ninjas.addEdge("Splinter", "Donatello", "father");

    Graph<String, String> amoeba = new Graph<String, String>();
    amoeba.addNode("blob");
    amoeba.addEdge("blob", "blob", "self");

    Graph<String, String> plankton = new Graph<String, String>();
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
    Graph<String, String> university = new Graph<String, String>();
    university.addNode("library");

    Graph<String, String> school = new Graph<String, String>();

    Graph<String, String> fish = new Graph<String, String>();
    fish.addNode("bubble");
    fish.addNode("gills");

    Graph<String, String> soap = new Graph<String, String>();
    soap.addNode("bubble");

    Graph<String, String> college = new Graph<String, String>();
    college.addNode("classroom");
    college.addNode("lunchroom");
    college.addEdge("classroom", "lunchroom", "hallway");

    Graph<String, String> k12 = new Graph<String, String>();
    k12.addNode("classroom");

    Graph<String, String> calendar = new Graph<String, String>();
    calendar.addNode("week");
    calendar.addNode("Monday");
    calendar.addNode("Tuesday");
    calendar.addEdge("week", "Monday", "first");
    calendar.addEdge("week", "Tuesday", "second");

    Graph<String, String> weekly = new Graph<String, String>();
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
    Graph<String, String> college = new Graph<String, String>();
    college.addNode("classroom");
    college.addNode("lunchroom");
    college.addEdge("classroom", "lunchroom", "hallway");

    Graph<String, String> k12 = new Graph<String, String>();
    k12.addNode("classroom");
    k12.addNode("lunchroom");

    Graph<String, String> calendar = new Graph<String, String>();
    calendar.addNode("week");
    calendar.addNode("Monday");
    calendar.addNode("Tuesday");
    calendar.addEdge("week", "Monday", "first");
    calendar.addEdge("week", "Tuesday", "second");

    Graph<String, String> weekly = new Graph<String, String>();
    weekly.addNode("week");
    weekly.addNode("Monday");
    weekly.addNode("Tuesday");
    weekly.addEdge("week", "Monday", "first");

    Graph<String, String> alabama = new Graph<String, String>();
    alabama.addNode("child");
    alabama.addEdge("child", "child", "parent");

    Graph<String, String> washington = new Graph<String, String>();
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
    Graph<String, String> scarecrow = new Graph<String, String>();
    Graph<String, String> gnome = new Graph<String, String>();
    assertTrue(scarecrow.equals(gnome));

    // Simple test no edges
    Graph<String, String> hitman = new Graph<String, String>();
    Graph<String, String> assassin = new Graph<String, String>();
    assassin.addNode("trained");
    assertFalse(assassin.equals(hitman));

    // Simple test no edges
    Graph<String, String> computer = new Graph<String, String>();
    computer.addNode("electronic");
    Graph<String, String> calculator = new Graph<String, String>();
    calculator.addNode("electronic");
    assertTrue(computer.equals(calculator));

    // Complex test no edges
    Graph<String, String> gov = new Graph<String, String>();
    gov.addNode("corrupt");
    gov.addNode("powerful");
    gov.addNode("corporate");
    Graph<String, String> politician = new Graph<String, String>();
    politician.addNode("corrupt");
    politician.addNode("powerful");
    politician.addNode("only sometimes corporate");
    assertFalse(politician.equals(gov));

    // Simple test one edge
    Graph<String, String> myself = new Graph<String, String>();
    myself.addNode("charles");
    myself.addNode("spoiled");
    myself.addEdge("charles", "spoiled", "nature");
    Graph<String, String> sister = new Graph<String, String>();
    sister.addNode("redacted");
    sister.addNode("spoiled");
    sister.addEdge("redacted", "spoiled", "nature");
    assertFalse(myself.equals(sister));

    // Complex test multiple edges
    Graph<String, String> bankRobbery = new Graph<String, String>();
    bankRobbery.addNode("vault");
    bankRobbery.addNode("loot");
    bankRobbery.addNode("getaway");
    bankRobbery.addEdge("vault", "loot", "contains");
    bankRobbery.addEdge("loot", "getaway", "run");
    Graph<String, String> jewelHeist = new Graph<String, String>();
    jewelHeist.addNode("vault");
    jewelHeist.addNode("loot");
    jewelHeist.addNode("getaway");
    jewelHeist.addEdge("vault", "loot", "contains");
    jewelHeist.addEdge("loot", "getaway", "run");
    assertTrue(bankRobbery.equals(jewelHeist));


    //////////////
    // hashCode //
    //////////////

    // Equal graph? Equal hash!
    assertTrue(scarecrow.hashCode() == gnome.hashCode());
    assertTrue(computer.hashCode() == calculator.hashCode());
    assertTrue(bankRobbery.hashCode() == jewelHeist.hashCode());

    // Hash values better be consistent!
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
  @Test
  public void testConstructor() {
    // Simple empty test
    Graph<String, String> empty1 = new Graph<String, String>();
    Graph<String, String> empty2 = new Graph<String, String>
            (new HashSet<String>(), new HashSet<Graph<String, String>.GraphEdge>());
    assertEquals(empty2, empty1);


    // Intermediate test
    Graph<String, String> national = new Graph<String, String>();
    national.addNode("roman");
    national.addNode("canadian");
    national.addNode("american");

    HashSet<String> setty = new HashSet<String>();
    setty.add("roman"); setty.add("canadian"); setty.add("american");
    
    Graph<String, String> person = new Graph<String, String>
            (setty, new HashSet<Graph<String, String>.GraphEdge>());
    assertEquals(person, national);


    // Complex test with edges
    Graph<String, String> triangle = new Graph<String, String>();
    triangle.addNode("acute"); triangle.addNode("right"); triangle.addNode("obtuse");
    triangle.addEdge("acute", "right", "lower than");
    triangle.addEdge("right", "obtuse", "lower than");
    triangle.addEdge("obtuse", "acute", "greater than");

    HashSet<String> triangleNodes = new HashSet<String>();
    angles.add("acute"); angles.add("right"); angles.add("obtuse");

    HashSet<Graph<String, String>.GraphEdge> triangleEdges;
    triangleEdges = new HashSet<Graph<String, String>.GraphEdge>();
    triangleEdges.add(Graph<String, String>.new GraphEdge("acute", "right", "lower than"));
    triangleEdges.add(Graph<String, String>.new GraphEdge("right", "obtuse", "lower than"));
    triangleEdges.add(Graph<String, String>.new GraphEdge("obtuse", "acute", "greater than"));
    
    Graph<String, String> pointy = new Graph<String, String>(triangleNodes, triangleEdges);
    assertEquals(pointy, triangle);


    // Special test
    Graph<String, String> childParent = new Graph<String, String>();
    childParent.addNode("routine");
    childParent.addEdge("routine", "routine", "sleep");

    HashSet<String> daily = new HashSet<String>();
    daily.add("routine");
    
    HashSet<Graph<String, String>.GraphEdge> sleepy;
    sleepy = new HashSet<Graph<String, String>.GraphEdge>();
    sleepy.add(Graph<String, String>.new GraphEdge("routine", "routine", "sleep"));

    Graph<String, String> cycle = new Graph<String, String>(daily, sleepy);
    assertEquals(cycle, childParent);
  }


  Graph<String, String> pic = new Graph<String, String>();
  pic.addNode("alaska");
  pic.addNode("washington");
  pic.addNode("florida");

  Graph<String, String>.GraphEdge edge1;
  edge1 = Graph<String, String>.new GraphEdge("alaska", "washington", "colder than");

  Graph<String, String>.GraphEdge edge2;
  edge2 = Graph<String, String>.new GraphEdge("florida", "florida", "Donald Trump");

  Graph<String, String>.GraphEdge edge3;
  edge3 = Graph<String, String>.new GraphEdge("alaska", "washington", "less inflation than");

  Graph<String, String>.GraphEdge edge4;
  edge4 = Graph<String, String>.new GraphEdge("florida", "florida", "Donald Trump");

  Graph<String, String>.GraphEdge edge5;
  edge5 = Graph<String, String>.new GraphEdge("alaska", "washington", "colder than");

  Graph<String, String>.GraphEdge edge6;
  edge6 = Graph<String, String>.new GraphEdge("florida", "florida", "Ron DeSantis");

  Graph<String, String>.GraphEdge edge7;
  edge7 = Graph<String, String>.new GraphEdge("washington", "florida", "colder than");

  Graph<String, String>.GraphEdge edge8;
  edge8 = Graph<String, String>.new GraphEdge("washington", "alaska", "less inflation than");

  @Test
  public void testEdgeGetters() {
    /////////////////
    // getParent() //
    /////////////////

    // Test normal edge
    assertEquals(edge1.parent, "alaska");
    // Test special edge
    assertEquals(edge2.parent, "florida");
	

    ////////////////
    // getChild() //
    ////////////////

    // Test normal edge
    assertEquals(edge1.child, "washington");
    // Test special edge
    assertEquals(edge2.child, "florida");

    
    ////////////////
    // getLabel() //
    ////////////////
    
    // Test normal edge
    assertEquals(edge1.label, "colder than");
    // Test special edge
    assertEquals(edge2.label, "Donald Trump");
  }


  @Test
  public void testEdgeEqualsAndHash() {
  ////////////////////
  // equals(Object) //
  ////////////////////

  // Test normal edges false
  assertFalse(edge1.equals(edge3));
  // Test normal edges true
  assertTrue(edge1.equals(edge5));
  // Test special edges false
  assertFalse(edge4.equals(edge6));
  // Test special edges true
  assertTrue(edge2.equals(edge4));
  // Reference same normal edge
  assertTrue(edge3.equals(edge3));
  // Reference same special edge
  assertTrue(edge4.equals(edge4));
  // Same label but different parents/children
  assertFalse(edge5.equals(edge7));
  // Flip flop the parents
  assertFalse(edge3.equals(edge8));


  ////////////////
  // hashCode() //
  ////////////////

  // Equal edge? Equal hash!
  assertEquals(edge1.hashCode(), edge5.hashCode());
  assertEquals(edge2.hashCode(), edge4.hashCode());

  // Hash values better be consistent!
  int hash1 = edge1.hashCode(), hash2 = edge2.hashCode();
  int hash3 = edge3.hashCode(), hash4 = edge4.hashCode();
  int hash5 = edge5.hashCode(), hash6 = edge6.hashCode();
  int hash7 = edge7.hashCode(), hash8 = edge8.hashCode();
  assertEquals(edge1.hashCode, hash1);
  assertEquals(edge2.hashCode, hash2);
  assertEquals(edge3.hashCode, hash3);
  assertEquals(edge4.hashCode, hash4);
  assertEquals(edge5.hashCode, hash5);
  assertEquals(edge6.hashCode, hash6);
  assertEquals(edge7.hashCode, hash7);
  assertEquals(edge8.hashCode, hash8);
  }
}
