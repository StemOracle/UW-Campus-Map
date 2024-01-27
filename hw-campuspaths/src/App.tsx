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

import React, {Component} from 'react';
import Map from "./Map";
import PathChooser from "./PathChooser";
import { Point, Segment } from "./Interfaces";
import "./App.css";

/** State of this component */
interface AppState {
  /** Makes up path between buildings */
  segs: Segment[];
  /** Point coordinate of starting building */
  startPoint: Point | null;
  /** Point coordinate of destination building */
  destPoint: Point | null;
  /** Color of marked starting, dest building, and path between them */
  drawColor: string;
  /** True iff pathfinding and must zoom to fit found path */
  isPathfinding: boolean;
}

/** Visualizes UW Seattle Campus finds shortest path between two select buildings */
class App extends Component<{}, AppState> {

  constructor(props: any) {
    // No props
    super(props);
    this.state = {
      segs: [],
      startPoint: null,
      destPoint: null,
      drawColor: "",
      isPathfinding: false
    };
  }

  render() {
    return (
      <div id="app">
        <Map
          segs={this.state.segs}
          startPoint={this.state.startPoint}
          destPoint={this.state.destPoint}
          drawColor={this.state.drawColor}
          isPathfinding={this.state.isPathfinding}
        />
        <div>
          <h1 id="app-title">Find Path Between Buildings</h1>
          <p>
            Choose a starting building and a destination building on the UW campus. <br/>
            This will display the shortest path between the two buildings.
          </p>
          <PathChooser
            onChange={(startPoint: Point | null | undefined,
                       destPoint: Point | null | undefined,
                       segs: Segment[] | undefined,
                       drawColor: string | undefined,
                       isPathfinding: boolean) => {
              this.setState({isPathfinding: false});
              if(startPoint !== undefined) {this.setState({startPoint: startPoint});}
              if(destPoint !== undefined) {this.setState({destPoint: destPoint});}
              if(segs !== undefined) {this.setState({segs: segs});}
              if(drawColor !== undefined) {this.setState({drawColor: drawColor});}
              this.setState({isPathfinding: isPathfinding});}}
          />
        </div>
      </div>
    );
  }
}

export default App;
