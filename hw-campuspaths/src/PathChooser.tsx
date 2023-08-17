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
}

interface Path {
  /**
   * Total weight of Path.
   */
  cost: number;

  /**
   * Starting coordinates of Path.
   */
  start: Point;

  /**
   * All straight-line Segments of Path.
   */
  path: Segment[];
}

interface Segment {
  /**
   * Starting coordinates of Segment.
   */
  start: Point;

  /**
   * Ending coordinates of Segment.
   */
  end: Point;

  /**
   * Weight of Segment.
   */
  cost: number;
}

interface Point {
  /**
   * X-coordinate in cartesian plane.
   */
  x: number;

  /**
   * Y-coordinate in cartesian plane.
   */
  y: number;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class PathChooser extends Component<PathChooserProps, PathChooserState> {

  private readonly HOSTNAME: string = "http://attu1.cs.washington.edu:";
  private readonly PORT: string | number = 4567;

  constructor(props: PathChooserProps) {
    // Well defined onChange function expected as props.
    super(props);
    this.state = {
      start: "",
      end: "",
      color: "",
      buildMap: {}
    };
  }

  componentDidMount() {
    this.getBuildings();
  }

  async getBuildings() {
    let buildsPromise = fetch(this.HOSTNAME + this.PORT + "/listBuildings");
    let buildsString = await buildsPromise;
    let parsedBuilds = await buildsString.json();
    this.setState({buildMap: parsedBuilds});
  }

  async pathBetweenBuildings(start: string, end: string) {
    if(this.state.start === "" || this.state.end === "") {
      alert("Please select a starting building and destination building.");
      return;
    } else if(this.state.color === "") {
      alert("Please specify a color.");
      return;
    }

    let pathPromise = fetch(this.HOSTNAME + this.PORT + "/findPath?start="
                                          + start + "&end=" + end);
    let pathString = await pathPromise;
    let parsedPath = await pathString.json();
    
    if(parsedPath === null) {
      alert("No path found between buildings " + start + " and " + end);
      return;
    }
    let directions: Path = parsedPath as Path;
    let edges: Edge[] = [];

    // Inv?
    let i: number = 0;
    while(i < directions.path.length) {
      const seg: Segment = directions.path[i];
      edges.push(new Edge(seg.start.x, seg.start.y, seg.end.x,
                          seg.end.y, this.state.color));
      i++;
    }
    this.props.onChange(edges);
  }

  buildingSelection(): JSX.Element[] {
    const fields: string[] = Object.keys(this.state.buildMap);
    const vals: string[] = Object.values(this.state.buildMap);
    let valids: JSX.Element[] = [];  

    // Inv?
    let i: number = 0;
    while(i < fields.length) {
      valids.push(<option value={fields[i]}>{vals[i]}</option>);
      i++;
    }
    return valids;
  }

  render() {
    return (
      <div id="path-chooser">
        <div id="select-start">
          <label htmlFor="start-select">Start:</label><br/>
            <select id={"start-select"} value={this.state.start} onChange={(event: any) => {this.setState({start: event.target.value});}}>
              <option value={""}>Starting Building</option>
              {this.buildingSelection()}
            </select>
        </div>
        <div id="select-destination">
          <label htmlFor="dest-select">Destination:</label><br/>
            <select id={"dest-select"} value={this.state.end} onChange={(event: any) => {this.setState({end: event.target.value});}}>
              <option value={""}>Destination Building</option>
              {this.buildingSelection()}
            </select>
        </div>
        <div id="enter-color">
          <label htmlFor="color-text">Color:</label><br/>
            <textarea id={"color-text"} rows={1} cols={10}
              onChange={(event: any) => {this.setState({color: event.target.value});}}
              value={this.state.color}
            />
        </div>
        <div id="options"> 
          <button onClick={() => {
            this.pathBetweenBuildings(this.state.start, this.state.end);}}>
            Find Path
          </button>
          <button onClick={() => {
            this.props.onChange([]);
            this.setState({start: "", end: "", color: ""});}}>
            Reset
          </button>
        </div>
      </div>
    );
  }
}

export default PathChooser;
