import React, {Component} from 'react';
import Edge from './Edge';

interface EdgeListProps {
  /**
   * Called when a new Edgep[] is ready and 'Draw' is clicked.
   * Replaces state in App component.
   */ 
  onChange(lines: Edge[]): void;
}

interface EdgeListState {
  /**
   * Text currently stored in the text field.
   */
  text: string;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps, EdgeListState> {

  constructor(props: any) {
    // Well defined onChange function expected as props.
    super(props);
    this.state = {
      text: ""
    };
  }


  /**
   * Parses text to give an array of Edges. 
   * @param text Currently in the text box.
   * @spec.requires Each line of text must be well-formatted as x1 y1 x2 y2 COLOR.
   * Badly formatted lines aren't parsed into an Edge.
   * @return Array of Edge, where each line of text is parsed into an Edge.
   */
  private parse(text: string): Edge[] {
    const lines: string[] = text.split("\n");
    let edges: Edge[] = [];

    let i = 0;
    // Inv: Up to current, all lines are parsed into an Edge object and added.
    // Badly formatted lines are rejected.
    while(i < lines.length) {
      // To be more forgiving.
      const parsed: string = lines[i].trim();
      const tokens: string[] = parsed.split(" ");

      if(tokens.length !== 5) {
        alert("Line: '" + parsed + "' has incorrect number of tokens.\n"
              + "Each line should have 5 tokens but this line has "
              + tokens.length + ".");
        i++;
        continue;
      }

      // To avoid errors but will probably change to something else.
      const x1: number = parseFloat(tokens[0]);
      const y1: number = parseFloat(tokens[1]);
      const x2: number = parseFloat(tokens[2]);
      const y2: number = parseFloat(tokens[3]);
      // To be more forgiving.
      const col: string = tokens[4].toLowerCase();

      if(isNaN(x1) || isNaN(y1) || isNaN(x2) || isNaN(y2)) {
        alert("Line: '" + parsed + "' has incorrect token type(s).\n"
              + "Each line should be formatted as 'num num num num color'.");
      } else if((x1 < 0) || (x1 > 4000) || (y1 < 0) || (y1 > 4000)
                                        || (x2 < 0) || (x2 > 4000)
                                        || (y2 < 0) || (y2 > 4000)) {
        alert("Line: '" + parsed + "' has a start or end coordinate out of bounds.\n"
              + "The start and end coordinates must be within (0, 0) and "
              + "(4000, 4000) but this line starts at (" + x1 + ", " + y1 + ")"
              + " and ends at (" + x2 +  ", " + y2 + ").");
      } else {
        edges.push(new Edge(x1, y1, x2, y2, col));
      }

      i++;
    }

    return edges;
  }


  render() {
    return ( 
      <div id="edge-list">
        Edges <br/>
          <textarea
            rows={5}
            cols={30}
            // Changes the text area in the text box.
            onChange={(event: any) => {this.setState({text: event.target.value});}}
            value={this.state.text}
          /> <br/>
        <button onClick={() => {this.props.onChange(this.parse(this.state.text));}}>Draw</button>
        <button onClick={() => {this.props.onChange([]);}}>Clear</button>
      </div>
    );
  }
}

export default EdgeList;
