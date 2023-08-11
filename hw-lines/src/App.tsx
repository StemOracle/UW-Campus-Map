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

import React, { Component } from "react";
import EdgeList from "./EdgeList";
import Edge from "./Edge";
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
  /**
   * Array of Edges that will be parsed into MapLines.
   */
  edges: Edge[];
}

/**
 * An application that visualizes the UW Campus and allows you to draw lines.
 * Draw lines by typing in starting coordinates, ending coordinates, and colors.
 */
class App extends Component<{}, AppState> { // <- {} means no props.

  constructor(props: any) {
    // No props here!
    super(props);
    this.state = {
      edges: []
    };
  }

  render() {
    return (
      <div id="app">
        <Map
          edges={this.state.edges}
        />
        <div>
          <h1 id="app-title">Line Mapper!</h1>
          <EdgeList
            onChange={(edges: Edge[]) => {this.setState({edges: edges});}}
          />
        </div>
    </div>
    );
  }
}

export default App;
