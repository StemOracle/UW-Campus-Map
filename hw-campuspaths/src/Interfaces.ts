/** Cartesian point coordinate */
export interface Point {
    /** X-coordinate */
    x: number;
    /** Y-Coordinate */
    y: number;
}

/** Vector with a starting Point and an ending Point
 * Also given a weight that doesn't necessarily mean pythagorean length */
export interface Segment {
    /** Starting coordinates of Segment */
    start: Point;
    /** Ending coordinates of Segment */
    end: Point;
    /** Weight of Segment */
    cost: number;
}

/** Path composed of starting point and many straight-line segments */
export interface Path {
    /** Total weight of Path */
    cost: number;
    /** Starting coordinates of Path */
    start: Point;
    /** All straight-line Segments of Path */
    path: Segment[];
}