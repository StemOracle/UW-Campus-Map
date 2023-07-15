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

  /** A few of my sets! */
  private static FiniteSet S2 = FiniteSet.of(new float[] {2});
  private static FiniteSet S3 = FiniteSet.of(new float[] {3});
  private static FiniteSet S7 = FiniteSet.of(new float[] {7});
  private static FiniteSet S37 = FiniteSet.of(new float[] {3, 7});
  private static FiniteSet S38 = FiniteSet.of(new float[] {3, 8});
  private static FiniteSet S378 = FiniteSet.of(new float[] {3, 7, 8});
  private static FiniteSet S367 = FiniteSet.of(new float[] {3, 6, 7});
  private static FiniteSet Sm8m5 = FiniteSet.of(new float[] {-5, -8});
  private static FiniteSet Sm9m8 = FiniteSet.of(new float[] {-8, -9});
  private static FiniteSet Sm8 = FiniteSet.of(new float[] {-8});
  private static FiniteSet Sm838 = FiniteSet.of(new float[] {-8, 3, 8});
  private static FiniteSet S1378 = FiniteSet.of(new float[] {1, 3, 7, 8});
  private static FiniteSet S3678 = FiniteSet.of(new float[] {3, 6, 7, 8});
  private static FiniteSet Sm9m8m5 = FiniteSet.of(new float[] {-9, -8, -5});
  private static FiniteSet Sm9m838 = FiniteSet.of(new float[] {-9, -8, 3, 8});
  private static FiniteSet S123 = FiniteSet.of(new float[] {1, 2, 3});
  private static FiniteSet S6 = FiniteSet.of(new float[] {6});
  private static FiniteSet Sm9m88 = FiniteSet.of(new float[] {-9, -8, 8});
  private static FiniteSet Sm9 = FiniteSet.of(new float[] {-9});
  private static FiniteSet S78 = FiniteSet.of(new float[] {7, 8});
  private static FiniteSet Sm5 = FiniteSet.of(new float[] {-5});

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
    // Special case (both sets empty) & 0, 1, 2 (0)
    assertEquals(S0.union(S0), S0);

    // Special case (one set empty) & 0, 1, 2 (1)
    assertEquals(S38.union(S0), S38);

    // No overlap between sets
    assertEquals(S1.union(S378), S1378);

    // 0, 1, 2 (2)
    assertEquals(S378.union(S367), S3678);

    // Total overlap/subset
    assertEquals(S1.union(S12), S12);

    // Identical
    assertEquals(S378.union(S378), S378);

    // Strictly negative values
    assertEquals(Sm8m5.union(Sm9m8), Sm9m8m5);

    // Positive and negative
    assertEquals(Sm9m8.union(Sm838), Sm9m838);
  }

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.intersection() Tests
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Tests forming the intersection of two finite sets.
   */
  @Test
  public void testIntersection() {
    // Special case (both sets empty) & 0, 1, 2 (0)
    assertEquals(S0.intersection(S0), S0);

    // Special case (one set empty)
    assertEquals(S38.intersection(S0), S0);

    // No overlap between sets
    assertEquals(S1.intersection(S378), S0);

    // 0, 1, 2 (1)
    assertEquals(S37.intersection(S38), S3);

    // 0, 1, 2 (2)
    assertEquals(S378.intersection(S367), S37);

    // Total overlap/subset
    assertEquals(S1.intersection(S12), S1);

    // Identical
    assertEquals(S378.intersection(S378), S378);

    // Strictly negative values
    assertEquals(Sm8m5.intersection(Sm9m8), Sm8);

    // Positive and negative
    assertEquals(Sm9m8.intersection(Sm838), Sm8);
  }

  ///////////////////////////////////////////////////////////////////////////
  /// FiniteSet.difference() Tests
  ///////////////////////////////////////////////////////////////////////////

  /**
   * Tests forming the difference of two finite sets.
   */
  @Test
  public void testDifference() {
    // Special case (both sets empty) & 0, 1, 2 (0)
    assertEquals(S0.difference(S0), S0);

    // Special case (subtract empty)
    assertEquals(S38.difference(S0), S38);

    // Special case (subtract FROM empty)
    assertEquals(S0.difference(S38), S0);

    // No overlap between sets
    assertEquals(S1.difference(S378), S1);

    // 0, 1, 2 (1)
    assertEquals(S37.difference(S38), S7);

    // 0, 1, 2 (2)
    assertEquals(S378.difference(S123), S78);

    // Subtract subset
    assertEquals(S3678.difference(S378), S6);

    // Subract FROM subset
    assertEquals(S378.difference(S3678), S0);

    // Identical
    assertEquals(S378.difference(S378), S0);

    // Strictly negative values
    assertEquals(Sm8m5.difference(Sm9m8), Sm5);

    // Positive and negative
    assertEquals(Sm9m88.difference(Sm838), Sm9);
  }
}
