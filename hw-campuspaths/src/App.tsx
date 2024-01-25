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
import { Point, Edge } from "./Interfaces";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

/**
 * State of this component
 */
interface AppState {
  /**
   * Array of Edges to be parsed into MapLine components
   */
  edges: Edge[];

  /**
   * Point coordinate of starting building
   */
  startPoint: Point | null;

  /**
   * Point coordinate of destination building
   */
  destPoint: Point | null;


  drawColor: string;
}

/**
 * An application that visualizes the UW Campus and allows you to draw lines
 * Draw lines by typing in starting coordinates, ending coordinates, and colors
 */
class App extends Component<{}, AppState> {

  constructor(props: any) {
    // No props here!
    super(props);
    this.state = {
      edges: [],
      startPoint: null,
      destPoint: null,
      drawColor: ""
    };
  }

  render() {
    return (
      <div id="app">
        <Map
          edges={this.state.edges}
          startPoint={this.state.startPoint}
          destPoint={this.state.destPoint}
          drawColor={this.state.drawColor}
        />
        <div>
          <h1 id="app-title">Find Path Between Buildings</h1>
          <p>
            Choose a starting building and a destination building on the UW campus. <br/>
            This will display the shortest path between the two buildings.
          </p>
          <PathChooser
            onChange={(startPoint: Point, destPoint: Point, edges: Edge[], drawColor: string) => {
              this.setState({startPoint: startPoint,
                                   destPoint: destPoint,
                                   edges: edges,
                                   drawColor: drawColor});}}
          />
        </div>
      </div>
    );
  }
}

export default App;
