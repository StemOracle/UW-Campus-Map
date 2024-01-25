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
import "./App.css";

/** State of this component */
interface AppState {
  /** Makes up path between buildings */
  edges: Edge[];
  /** Point coordinate of starting building */
  startPoint: Point | null;
  /** Point coordinate of destination building */
  destPoint: Point | null;
  /** Color of marked starting, dest building, and path between them */
  drawColor: string;
}

/** Visualizes UW Seattle Campus finds shortest path between two select buildings */
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
            onChange={(startPoint: Point | null | undefined,
                       destPoint: Point | null | undefined,
                       edges: Edge[] | undefined,
                       drawColor: string | undefined) => {
              if(startPoint !== undefined) {this.setState({startPoint: startPoint});}
              if(destPoint !== undefined) {this.setState({destPoint: destPoint});}
              if(edges !== undefined) {this.setState({edges: edges});}
              if(drawColor !== undefined) {this.setState({drawColor: drawColor});}}}
          />
        </div>
      </div>
    );
  }
}

export default App;
