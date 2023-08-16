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
   * Text currently stored in the text field labeled 'Start'.
   */
  start: string;
  /**
   * Text currently stored in the text field labeled 'End'.
   */
  end: string;
  /**
   * Text currently stored in the text field labeled 'Color'.
   */
  color: string;
  /**
   * Map of campus building's short names to their long names.
   */
  buildMap: object;

  /**
   * Shortest path between buildings named start and end.
   */
  path: object;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class PathChooser extends Component<PathChooserProps, PathChooserState> {

  private readonly HOSTNAME: string = "http://localhost:";
  private readonly PORT: string | number = 4567;

  constructor(props: any) {
    // Well defined onChange function expected as props.
    super(props);
    this.state = {
      start: "",
      end: "",
      color: "",
      buildMap: new Object(),
      path: new Object()
    };
  }

  async getBuildings() {
    let buildsPromise = fetch(this.HOSTNAME + this.PORT + "/listBuildings");
    let buildsString = await buildsPromise;

    let parsedBuildsPromise = buildsString.json();
    let parsedBuilds = await parsedBuildsPromise;

    this.setState({buildMap: parsedBuilds});
  }

  async pathBetweenBuildings(start: string, end: string) {
    let pathPromise = fetch(this.HOSTNAME + this.PORT + "/findPath?start="
                                          + start + "&end=" + end);
    let pathString = await pathPromise;

    let parsedPathPromise = pathString.json();
    let parsedPath = await parsedPathPromise;

    this.setState({path: parsedPath});
  }

  pathToEdges(path: object): Edge[] {
    this.pathBetweenBuildings(this.state.start, this.state.end);
    let edges: Edge[] = [];
    
    let directions: object = this.state.path;
    
    // Inv?
    let i: number = 0;
    while(i < directions.path.length) {
      const seg: object = directions.path[i];
      edges.push(new Edge(seg.start.x, seg.start.y, seg.end.x,
                                       seg.end.y, seg.color));
    }
    return edges;
  }

  render() {
    this.getBuildings();

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


