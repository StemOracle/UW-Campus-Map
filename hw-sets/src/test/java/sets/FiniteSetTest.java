/*
 * Copyright (C) 2023 Soham Pardeshi.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package sets;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 * FiniteSetTest is a glassbox test of the FiniteSet class.
 */
public class FiniteSetTest {

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.FiniteSet() Tests
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Test creating basic sets.
   */
  @Test
  public void testCreationEmptySet() {
    assertEquals(Arrays.asList(),
        FiniteSet.of(new float[0]).getPoints());
  }

  /**
   * Test creating basic sets.
   */
  @Test
  public void testCreationBasic() {
    assertEquals(Arrays.asList(1f),
        FiniteSet.of(new float[] {1}).getPoints());      // one item
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {1, 2}).getPoints());   // two items
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {2, 1}).getPoints());   // two out-of-order
  }

  /**
   * Test creating a set that contains a negative point.
   */
  @Test
  public void testCreationNegative() {
    assertEquals(Arrays.asList(-2f, 2f),
        FiniteSet.of(new float[] {2, -2}).getPoints());
  }

  // Note:
  // The following are sets used throughout the rest of the tests.

  /** An empty set. */
  private static FiniteSet S0 = FiniteSet.of(new float[0]);

  /** A "singleton" set. */
  private static FiniteSet S1 = FiniteSet.of(new float[] {1});

  /** A "complex" set. Or, a set that contains more than one value. */
  private static FiniteSet S12 = FiniteSet.of(new float[] {1, 2});

  // TODO: Feel free to initialize additional (private static) FiniteSet
  //       objects here if you plan to use more of them for the tests you
  //       need to implement below.

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.equals() Tests
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Test set equality on an empty set.
   */
  @Test
  public void testEqualsEmptySet() {
    assertTrue(S0.equals(S0));
    assertFalse(S0.equals(S1));
    assertFalse(S0.equals(S12));
  }

  /**
   * Test set equality on a set containing one point.
   */
  @Test
  public void testEqualsSingleton() {
    assertFalse(S1.equals(S0));
    assertTrue(S1.equals(S1));
    assertFalse(S1.equals(S12));
  }

  /**
   * Test set equality on a set of multiple points.
   */
  @Test
  public void testEqualsComplexSet() {
    assertFalse(S12.equals(S0));
    assertFalse(S12.equals(S1));
    assertTrue(S12.equals(S12));
  }

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.size() Test
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Test set size.
   */
  @Test
  public void testSize() {
    assertEquals(S0.size(), 0);
    assertEquals(S1.size(), 1);
    assertEquals(S12.size(), 2);
  }

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.union() Tests
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Tests forming the union of two finite sets.
   */
  @Test
  public void testUnion() {
    // TODO: implement tests for FiniteSet.union()

  }

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.intersection() Tests
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Tests forming the intersection of two finite sets.
   */
  @Test
  public void testIntersection() {
    // TODO: implement tests for FiniteSet.intersection()

  }

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.difference() Tests
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Tests forming the difference of two finite sets.
   */
  @Test
  public void testDifference() {
    // TODO: implement tests for FiniteSet.difference()

  }

}
