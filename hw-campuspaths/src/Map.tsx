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
import { Point, Segment, Path } from "./Interfaces";

// Defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];
// Default zoom of map
const defZoom: number = 15;

/** Properties of this component */
interface MapProps {
  /** Path between two select buildings */
  path: Path;
  /** Point coordinate of starting building */
  start: Point | null;
  /** Point coordinate of destination building */
  dest: Point | null;
  /** Color of marked starting, dest building, and path between them */
  col: string;
  /** True iff pathfinding and must zoom to fit found path */
  zoomFlag: number;
}

/** If both points non-null, zooms map to tightly fit both
 * @param props two possibly null points
 * @return null; produces potential side effects but benign return */
function Zoom(props: {p1: Point | null, p2: Point | null}): null {
  const map = useMap();
  // This should never be the case when pathfinding.
  if(props.p1 === null || props.p2 === null) { return null; }
  // Once we know which is less and more, can pad bounds out slightly.
  let x1: number = props.p1.x, y1: number = props.p1.y;
  let x2: number = props.p2.x, y2: number = props.p2.y;
  if(x1 > x2) { let swap: number = x1; x1 = x2; x2 = swap; }
  if(y1 > y2) { let swap: number = y1; y1 = y2; y2 = swap; }
  map.fitBounds([[yToLat(y1-42.5), xToLon(x1-42.5)],
                 [yToLat(y2+42.5), xToLon(x2+42.5)]]);
  return null;
}

/** Unzooms map back to default state
 * @return null; produces side effects but benign return */
function UnZoom() {
  const map = useMap();
  map.setView(position, defZoom);
  return null;
}

/** React Leaflet map focused on UW Seattle campus. Campus buildings can be
 * selected (and marked) on it and a path can be drawn between them. */
class Map extends Component<MapProps, {}> {

  /** Returns MapCircle centered at select building in given color, null if null center
   * @param center center of circle to be drawn, draws nothing if null
   * @return MapCircle centered at select building in given color, null if null center */
  makeCircle(center: Point | null): JSX.Element | null {
    let col: string = this.props.col;
    if(center === null) { return null; }
    else if (col === "") { col = "red"; }
    return <MapCircle x={center.x} y={center.y} radius={42.5} color={col} />;
  }

  /** Draws path between two select buildings with provided color */
  makeLines(): JSX.Element[] {
    let col: string = this.props.col;
    if(col === "") { col = "red"; }
    let lines: JSX.Element[] = [];
    let segs: Segment[] = this.props.path.path;
    for(let i: number = 0; i < segs.length; i += 1) {
      let seg: Segment = segs[i];
      lines.push(
        <MapLine x1={seg.start.x} y1={seg.start.y} x2={seg.end.x} y2={seg.end.y} color={col} />);
    }
    // If zoomFlag isn't binary, neither zoom nor unzoom
    if(this.props.zoomFlag === 1) {
      lines.push(<Zoom p1={this.props.start} p2={this.props.dest} />);
    } else if(this.props.zoomFlag === 0) {
      lines.push(<UnZoom />);
    }
    return lines;
  }

  render() {
    return (
        <div id="map">
          <MapContainer center={position} zoom={defZoom} scrollWheelZoom={true}>
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
