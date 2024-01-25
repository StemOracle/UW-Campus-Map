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
import {MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import MapCircle from "./MapCircle";
import { UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER } from "./Constants";
import { Edge, Point } from "./Interfaces";

// Defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

/** Properties of this component */
interface MapProps {
  /** Makes up path between buildings */
  edges: Edge[];
  /** Point coordinate of starting building */
  startPoint: Point | null;
  /** Point coordinate of destination building */
  destPoint: Point | null;
  /** Color of marked starting, dest building, and path between them */
  drawColor: string;
}

class Map extends Component<MapProps, {}> {

  makeCircle(center: Point | null) {
    let col: string = this.props.drawColor;
    if(center === null) {
      return null;
    } else if (this.props.drawColor === "") {
      col = "red";
    }
    return <MapCircle x={center.x} y={center.y} radius={42.5} color={col} />;
  }

  // Benign so far
  makeLines() {
    let col: string = this.props.drawColor;
  }

  render() {
    return (
      <div id="map">
        <MapContainer center={position} zoom={15} scrollWheelZoom={false}>
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {this.props.edges.map(
            (edge: Edge, i: number) => (
              <MapLine
                color={this.props.drawColor}
                x1={edge.x1}
                y1={edge.y1}
                x2={edge.x2}
                y2={edge.y2}
                key={i}
              />
            )
          )}
          {this.makeCircle(this.props.startPoint)}
          {this.makeCircle(this.props.destPoint)}
        </MapContainer>
      </div>
    );
  }
}
export default Map;
