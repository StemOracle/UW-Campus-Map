import React, { Component } from "react";
import { Circle } from "react-leaflet";
import {
    xToLon,
    yToLat
} from "./Constants";

interface MapCircleProps {
    color: string
    x: number
    y: number
    radius: number
}

class MapCircle extends Component<MapCircleProps, {}> {
  constructor(props: any) {
    super(props);
  }

  render() {
    return (
      <Circle
        color={this.props.color}
        center={[yToLat(this.props.y), xToLon(this.props.x)]}
        radius={this.props.radius}
      />
    );
  }
}
export default MapCircle;