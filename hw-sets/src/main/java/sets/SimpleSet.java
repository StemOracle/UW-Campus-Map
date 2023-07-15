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

import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {

  // RI: valSet != null and this != null
  // AF: If isComplement is false, this = valSet
  //     If isComplement is true,  this = R \ valSet

  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    this(FiniteSet.of(vals), false);
  }

  public SimpleSet(FiniteSet valSet, boolean isComplement) {
    this.valSet = valSet;
    this.isComplement = isComplement;
  }
  
  // The FiniteSet of all points either in or NOT in the set.
  private final FiniteSet valSet;

  // True iff FiniteSet represents all points NOT in the set.
  private final boolean isComplement;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;
    return this.valSet.equals(other.valSet) && this.isComplement == other.isComplement;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    // If true, this is a finite complement of the set of all real numbers.
    // If false, this is a finite set.
    if(this.isComplement) {
      return Float.POSITIVE_INFINITY;
    } else {
      return this.valSet.size();
    }
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers. These
   *     floats will be turned into strings in the standard manner (the same as
   *     done by, e.g., String.valueOf(float)).
   */
  public String toString() {
    if(this.isComplement && this.valSet.size() == 0) {
      return "R";
    } else if(this.valSet.size() == 0) {
      return "{}";
    }

    // From here, we now the val set isn't empty.
    StringBuilder buf = new StringBuilder();
    List<Float> vals = this.valSet.getPoints();
    // If a complement, we do R \ FiniteSet.
    if(isComplement) {
      buf.append("R \\ ");
    }
    buf.append("{");
    buf.append(vals.get(0));
    int i = 1;

    // Inv: buf = "{vals.get(0), vals.get(1), ..., vals.get(i-1)"
    while(i < vals.size()) {
      buf.append(", ");
      buf.append(vals.get(i));
      i = i+1;
    }

    buf.append("}");
    // Post: Returns "{vals.get(0), vals.get(1), ..., vals.get(n-1)}"
    return buf.toString();
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    // If a finite set, taking the complement,would make this a finite complement of all real numbers.
    // If a finite complement of all real numbers, taking the complement would make this a finite set.
    return new SimpleSet(this.valSet, !this.isComplement);
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {
    FiniteSet Tvals = this.valSet;
    FiniteSet Svals = other.valSet;

    boolean isTC = this.isComplement;
    boolean isSC = other.isComplement;
    // If both finite sets, use the finite set union method.
    if(!isTC && !isSC) {
      return new SimpleSet(Tvals.union(Svals), false);
    // If both finite complement sets, then the only elements that should remain in the
    // resulting set should be in both complement sets. In other words, in order for an
    // element to NOT be in the unioned SimpleSet, it cannot be in either S or T.
    } else if(isTC && isSC) {
      return new SimpleSet(Tvals.intersection(Svals), true);
    // Elements appearing in the complement set do not exist in the infinite set. If the element
    // appears in the finite set, it no longer exists in the complement set as it's now part of
    // the infinite set. We're essentially subtracting elements from the finite complement set.
    } else if(isTC && !isSC) {
      return new SimpleSet(Tvals.difference(Svals), true);
    // Same logic as before, as one of the sets is infinite and the other is finite. We subtract
    // elements in the finite set from the finite complement set.
    } else {
      return new SimpleSet(Svals.difference(Tvals), true);
    }
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersected with other
   */
  public SimpleSet intersection(SimpleSet other) {
    // Using set theory, let +, *, and ^C be shorthand for union, intersection, and complement, respectively.
    // T * S = ((T^C) + (S^C))^C
    SimpleSet TC = this.complement();
    SimpleSet SC = other.complement();
    return (TC.union(SC)).complement();
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    // Using set theory, let *, ^C, and \ be shorthand for intersection, complement, and difference, respectively.
    // T \ S = T * (S^C)
    SimpleSet T = this;
    SimpleSet SC = other.complement();
    return T.intersection(SC);
  }
}
