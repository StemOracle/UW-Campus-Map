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
import { Point, Path } from "./Interfaces";
import "./App.css";

const emptyPath: Path = {cost: 0, start: {x: 0, y: 0}, path: []};

/** State of this component */
interface AppState {
  /** Path between two select buildings */
  path: Path;
  /** Point coordinate of starting building */
  start: Point | null;
  /** Point coordinate of destination building */
  dest: Point | null;
  /** Color of marked starting, dest building, and path between them */
  col: string;
  /** 0 if pathfinding and must zoom, 1 if selecting buildings and must unzoom
   * Neither if neither */
  zoomFlag: number;
}

/** Visualizes UW Seattle Campus and allows pathfinding between two select
 * buildings. Path and building markers drawn in color of user's choice. */
class App extends Component<{}, AppState> {

  constructor(props: any) {
    // No props
    super(props);
    this.state = {
      // Empty path
      path: emptyPath,
      start: null,
      dest: null,
      col: "",
      zoomFlag: 0
    };
  }

  render() {
    return (
      <div id="app">
        <Map
          path={this.state.path}
          start={this.state.start}
          dest={this.state.dest}
          col={this.state.col}
          zoomFlag={this.state.zoomFlag}
        />
        <div>
          <h1 id="app-title">Find Path Between Buildings</h1>
          <p>
            Choose a starting building and a destination building on the UW campus. <br/>
            This will display the shortest path between the two buildings.
          </p>
          <PathChooser
            markBuild={(pt: Point, isStart: boolean) => {
              if(isStart) {this.setState({start: pt, zoomFlag: 0});}
              else {this.setState({dest: pt, zoomFlag: 0});}}}
            pathfind={(path: Path, col: string) => {
              this.setState({path: path, col: col, zoomFlag: 1});}}
            setCol={(col: string) => {
              this.setState({col: col, zoomFlag: 2})}}
            reset={() => {
              this.setState({start: null, dest: null, path: emptyPath, col: "", zoomFlag: 0});}}
          />
        </div>
      </div>
    );
  }
}

export default App;
