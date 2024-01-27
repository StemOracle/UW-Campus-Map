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
  start: Point | null;
  /** Point coordinate of destination building */
  dest: Point | null;
  /** Color of marked starting, dest building, and path between them */
  col: string;
  /** True iff pathfinding and must zoom to fit found path */
  willDraw: boolean;
}

/** Visualizes UW Seattle Campus finds shortest path between two select buildings */
class App extends Component<{}, AppState> {

  constructor(props: any) {
    // No props
    super(props);
    this.state = {
      segs: [],
      start: null,
      dest: null,
      col: "",
      willDraw: false
    };
  }

  render() {
    return (
      <div id="app">
        <Map
          segs={this.state.segs}
          start={this.state.start}
          dest={this.state.dest}
          col={this.state.col}
          willDraw={this.state.willDraw}
        />
        <div>
          <h1 id="app-title">Find Path Between Buildings</h1>
          <p>
            Choose a starting building and a destination building on the UW campus. <br/>
            This will display the shortest path between the two buildings.
          </p>
          <PathChooser
            markBuild={(pt: Point, isStart: boolean) => {
              if(isStart) {this.setState({start: pt, willDraw: false});}
              else {this.setState({dest: pt, willDraw: false});}}}
            pathfind={(segs: Segment[], col: string) => {
              this.setState({segs: segs, col: col, willDraw: true});}}
            setCol={(col: string) => {
              this.setState({col: col, willDraw: false})}}
            reset={() => {
              this.setState({start: null, dest: null, segs: [], col: "", willDraw: false});}}
          />
        </div>
      </div>
    );
  }
}

export default App;
