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
import MapLine from './MapLine';
import Edge from './Edge';

interface EdgeListProps {
  onChange(lines: Edge[]): void;  // called when a new edge list is ready
}

interface EdgeListState {
  text: string; // Text stored in the text box
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeListState> {

  constructor(props: any) {
    super(props);
    this.state = {
      text: ""
    };
  }

  parse(text: string): Edge[] {
    const lines: string[] = text.split("\n");
    let edges: Edge[] = [];

    let i = 0;
    // Inv: Up to current, all lines are parsed into an Edge object and added.
    // Alternatievly, if badly formatted, they are rejected.
    while(i < lines.length) {
      const parsed: string = lines[i];
      const tokens: string[] = parsed.split(" ");
      if(tokens.length !== 5) {
        console.log("Line: " + parsed + " was improperly formatted. Too many tokens.");
        continue;
      }
      // To avoid errors but will probably change to something else.
      const x1: any = +lines[0]; const y1: any = +lines[1];
      const x2: any = +lines[2]; const y2: any = +lines[3];
      // To be more forgiving with casing.
      const col: any = lines[4].toLowerCase();
      if(typeof x1 !== 'number' || typeof y1 !== 'number' || typeof x2 !== 'number'
                              || typeof y2 !== 'number' || typeof col !== 'string') {
        console.log("Line: " + parsed + " was improperly formatted. Incorrect token type.");
        continue;
      } else if((x1 < 0) || (x1 > 4000) || (y1 < 0) || (y1 > 4000) || (x2 < 0) || (x2 > 4000)
                                        || (y2 < 0) || (y2 > 4000)) {
        console.log("Line: " + parsed + " has a start or end coordinate out of bounds.");
        continue;
      }
      edges.push(new Edge(x1, y1, x2, y2, col));
    }
    return edges;
  }

  render() {
    return ( 
      <div id="edge-list">
        Edges <br/>
          <MapLine color="red" x1={0} y1={0} x2={0} y2={0} />
          <textarea
            rows={5}
            cols={30}
            // Can try ChangeEventHandler<HTMLTextAreaElement>
            // Changes the text area in the text box.
            onChange={(event: any) => {this.setState({text: event.target.value});}}
            value={this.state.text}
          /> <br/>
        <button onClick={() => {this.props.onChange(this.parse(this.state.text));}}>Draw</button>
        <button onClick={() => {console.log('Clear onClick was called');}}>Clear</button>
      </div>
      );
  }
}

export default EdgeList;
