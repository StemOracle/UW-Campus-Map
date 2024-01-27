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
// Default zoom of map
const defZoom: number = 15;

/** Properties of this component */
interface MapProps {
  /** Makes up path between buildings */
  segs: Segment[];
  /** Point coordinate of starting building */
  start: Point | null;
  /** Point coordinate of destination building */
  dest: Point | null;
  /** Color of marked starting, dest building, and path between them */
  col: string;
  /** True iff pathfinding and must zoom to fit found path */
  willDraw: boolean;
}

function Zoom(props: {p1: Point | null, p2: Point | null}) {
  const map = useMap();
  if(props.p1 === null || props.p2 === null) { return null; }
  let x1: number = xToLon(props.p1.x), y1: number = yToLat(props.p1.y);
  let x2: number = xToLon(props.p2.x), y2: number = yToLat(props.p2.y);
  if(x1 > x2) { let swap: number = x1; x1 = x2; x2 = swap }
  if(y1 > y2) { let swap: number = y1; y1 = y2; y2 = swap }
  map.fitBounds([[y1, x1], [y2, x2]]);
  return null;
}

class Map extends Component<MapProps, {}> {
  makeCircle(center: Point | null): JSX.Element | null {
    let col: string = this.props.col;
    if(center === null) { return null; }
    else if (col === "") { col = "red"; }
    return <MapCircle x={center.x} y={center.y} radius={42.5} color={col} />;
  }

  makeLines(): JSX.Element[] {
    let col: string = this.props.col;
    if(col === "") { col = "red"; }
    let lines: JSX.Element[] = [];
    for(let i: number = 0; i < this.props.segs.length; i += 1) {
      let seg: Segment = this.props.segs[i];
      lines.push(
        <MapLine x1={seg.start.x} y1={seg.start.y} x2={seg.end.x} y2={seg.end.y} color={col} />);
    }
    if(this.props.willDraw) {
      lines.push(<Zoom p1={this.props.start} p2={this.props.dest} />);
    }
    return lines;
  }

  render() {
    return (
        <div id="map">
          <MapContainer center={position} zoom={defZoom} scrollWheelZoom={false}>
            <TileLayer
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
            {this.makeLines()}
            {this.makeCircle(this.props.start)}
            {this.makeCircle(this.props.dest)}
          </MapContainer>
        </div>
    );
  }
}
export default Map;
