/*
 * Copyright (C) 2023 Soham Pardeshi.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, TileLayer, useMap } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import MapCircle from "./MapCircle";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER, yToLat, xToLon } from "./Constants";
import { Point, Segment } from "./Interfaces";

// Defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

/** Properties of this component */
interface MapProps {
  /** Makes up path between buildings */
  segs: Segment[];
  /** Point coordinate of starting building */
  startPoint: Point | null;
  /** Point coordinate of destination building */
  destPoint: Point | null;
  /** Color of marked starting, dest building, and path between them */
  drawColor: string;
}

function ZoomToFit(p1: Point, p2: Point) {
  /* I'll have to fix this later.
  const map = useMap();
  if(startPoint !== null && destPoint !== null) {
    map.fitBounds([[yToLat(startPoint.y), xToLon(startPoint.x)], [yToLat(destPoint.y), xToLon(destPoint.x)]]);
  } */
  return;
}

class Map extends Component<MapProps, {}> {
  makeCircle(center: Point | null): JSX.Element | null {
    let col: string = this.props.drawColor;
    if(center === null) { return null; }
    else if (this.props.drawColor === "") { col = "red"; }
    return <MapCircle x={center.x} y={center.y} radius={42.5} color={col} />;
  }

  makeLines(): JSX.Element[] {
    let col: string = this.props.drawColor;
    if(col === "") { col = "red"; }
    let lines: JSX.Element[] = [];
    for(let i: number = 0; i < this.props.segs.length; i += 1) {
      let seg: Segment = this.props.segs[i];
      lines.push(<MapLine
          x1={seg.start.x}
          y1={seg.start.y}
          x2={seg.end.x}
          y2={seg.end.y}
          color={col} />);
    }
    if(this.props.startPoint != null && this.props.destPoint != null) {
      ZoomToFit(this.props.startPoint, this.props.destPoint); }
    return lines;
  }

  render() {
    return (
      <div id="map">
        <MapContainer center={position} zoom={15} scrollWheelZoom={false}>
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {this.makeLines()}
          {this.makeCircle(this.props.startPoint)}
          {this.makeCircle(this.props.destPoint)}
        </MapContainer>
      </div>
    );
  }
}
export default Map;
