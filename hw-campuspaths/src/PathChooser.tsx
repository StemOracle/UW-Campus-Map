import React, {Component} from 'react';
import Edge from './Edge';

interface PathChooserProps {
  /**
   * Called when a new Edgep[] is ready and 'Draw' is clicked.
   * Replaces state in App component.
   */ 
  onChange(lines: Edge[]): void;
}

interface PathChooserState {
  /**
   * Text currently stored in the text field.
   */
  start: string;
  end: string;
  color: string;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class PathChooser extends Component<PathChooserProps, PathChooserState> {

  constructor(props: any) {
    // Well defined onChange function expected as props.
    super(props);
    this.state = {
      start: "",
      end: "",
      color: ""
    };
  }

  render() {
    return ( 
      <div id="path-chooser">
        <div>
          Start <br/>
            <textarea
              rows={1}
              cols={15}
              // Changes the text area in the text box.
              onChange={(event: any) => {this.setState({start: event.target.value});}}
              value={this.state.start}
            /> <br/>
        </div>
        <div id="end-choice">
          Destination <br/>
            <textarea
              rows={1}
              cols={15}
              // Changes the text area in the text box.
              onChange={(event: any) => {this.setState({end: event.target.value});}}
            /> <br/>
        </div>
        <div id="color-choice">
          Color <br/>
            <textarea
              rows={1}
              cols={15}
              // Changes the text area in the text box.
              onChange={(event: any) => {this.setState({color: event.target.value});}}
            /> <br/>
        </div>
        <div id="path-and-reset"> 
          <button onClick={() => {this.props.onChange([]);}}>Show Path</button>
          <button onClick={() => {this.props.onChange([]);}}>Reset</button>
        </div>
      </div>
    );
  }
}

export default PathChooser;


