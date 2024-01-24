export interface Point { x: number, y: number }

export interface Edge { x1: number, y1: number, x2: number, y2: number, color: String }

export interface Path {
    /**
     * Total weight of Path
     */
    cost: number;

    /**
     * Starting coordinates of Path
     */
    start: Point;

    /**
     * All straight-line Segments of Path
     */
    path: Segment[];
}

/**
 * Vector with a starting Point and an ending Point
 * Also given a weight that doesn't necessarily mean pythagorean length
 */
export interface Segment {
    /**
     * Starting coordinates of Segment
     */
    start: Point;

    /**
     * Ending coordinates of Segment
     */
    end: Point;

    /**
     * Weight of Segment
     */
    cost: number;
}