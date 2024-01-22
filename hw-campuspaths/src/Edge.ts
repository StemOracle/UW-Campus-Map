/**
 * An immutable Vector/Edge that points from  (x1, y1) to (x2, y2)
 * Colored with given color field
 */

class Edge {
  /**
   * Starting x-coordinate
   */
  public readonly x1: number

  /**
   * Starting y-coordinate
   */
  public readonly y1: number

  /**
   * Ending x-coordinate
   */
  public readonly x2: number

  /**
   * Ending y-coordinate
   */
  public readonly y2: number

  /**
   * What color this will appear as
   */
  public readonly color: string

  // AF: This represents a Vector/Edge that points from (x1, y1) to (x2, y2)
  // It is colored with given color

  /**
   * @param x1 starting x-coordinate
   * @param y1 starting y-coordinate
   * @param x2 ending x-coordinate
   * @param y2 ending y-coordinate
   * @param color What color this will appear as
   */
  constructor(x1: number, y1: number, x2: number, y2: number, color: string) {
    this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2; this.color = color;
  }
}

export default Edge;

