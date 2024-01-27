import React, { Component } from "react";
import { Circle } from "react-leaflet";
import { xToLon, yToLat } from "./Constants";

/** Properties of this component */
interface MapCircleProps {
    /** Circle color */
    color: string
    /** Circle center's x-coordinate on cartesian plane */
    x: number
    /** Circle center's y-coordinate on cartesian plane */
    y: number
    /** Circle radius */
    radius: number
}

class MapCircle extends Component<MapCircleProps, {}> {
  render() {
    return (
      <Circle
        pathOptions={{color: this.props.color}}
        center={[yToLat(this.props.y), xToLon(this.props.x)]}
        radius={this.props.radius}
      />
    );
  }
}
export default MapCircle;