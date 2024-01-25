import React, {Component} from 'react';
import { Point, Segment, Path, Edge } from './Interfaces'

/**
 * Properties of this component
 */
interface PathChooserProps {
  /**
   * Called when a new Edge[] is ready and 'Draw' is clicked
   * Replaces state in App component
   */ 
  onChange(startPoint: Point | null, destPoint: Point | null, lines: Edge[], drawColor: string): void;

  markStart(startPoint: Point | null) : void;

  markDest(destPoint: Point | null) : void;
}

/**
 * State of this component
 */
interface PathChooserState {
  /**
   * Text stored in text field labeled 'Start'
   */
  start: string;

  /**
   * Text stored in text field labeled 'End'
   */
  end: string;

  /**
   * Text stored in text field labeled 'Color'
   */
  color: string;

  /**
   * Map of campus buildings' short names to their long names
   */
  buildMap: object;
}

/**
 * A text field that allows the user to enter the list of edges
 * Also contains the buttons that the user will use to interact with the app
 */
class PathChooser extends Component<PathChooserProps, PathChooserState> {
 /**
  * URL of Spark backend
  */
  private readonly HOSTNAME: string = "http://localhost:";
  private readonly PORT: string = "4567";

  constructor(props: PathChooserProps) {
    // Well-defined onChange function expected as props
    super(props);
    this.state = {
      start: "",
      end: "",
      color: "",
      buildMap: {}
    };
  }

  /**
   * Sets up this component
   */
  componentDidMount() {
    this.getBuildings();
  }

  /**
   * Fetches object that maps all UW campus building short names to long names
   */
  async getBuildings() {
    let buildsString = await fetch(this.HOSTNAME + this.PORT + "/listBuildings");
    let parsedBuilds = await buildsString.json();
    this.setState({buildMap: parsedBuilds});
  }

  /**
   * Finds shortest path between building labeled start and building labeled end
   * @param start name of starting building on the UW campus
   * @param end name of destination building on the UW campus
   * @spec.requires valid buildings must be chosen, a color must be specified,
   * and a path must exist between the buildings. No behavior otherwise
   */
  async pathBetweenBuildings(start: string, end: string) {
    if(this.state.start === "" || this.state.end === "") {
      alert("Please select a starting building and destination building.");
      return;
    } else if(this.state.color === "") {
      alert("Please specify a color.");
      return;
    }

    let pathString = await fetch(this.HOSTNAME + this.PORT + "/findPath?start="
                                               + start + "&end=" + end);
    let parsedPath = await pathString.json();
    if(parsedPath === null) {
      alert("No path found between buildings " + start + " and " + end);
      return;
    }

    let startPoint = await fetch(this.HOSTNAME + this.PORT + "/lookupBuilding?shortName=" + start);
    let parsedStart = await startPoint.json();
    let destPoint = await fetch(this.HOSTNAME + this.PORT + "/lookupBuilding?shortName=" + end);
    let parsedDest = await destPoint.json();

    let directions: Path = parsedPath as Path;
    let edges: Edge[] = [];

    // All Segments of directions are converted to an edge and pushed to edges
    let i: number = 0;
    while(i < directions.path.length) {
      const seg: Segment = directions.path[i];
      edges.push({x1: seg.start.x, y1: seg.start.y,
                  x2: seg.end.x, y2: seg.end.y});
      i++;
    }

    this.props.onChange(parsedStart as Point, parsedDest as Point, edges, this.state.color);
  }

  // Unused so far.
  async markBuilding(buildName: string, destFlag: boolean) {
    let pointString = await fetch(this.HOSTNAME + this.PORT
                                            + "/lookupBuilding?shortName=" + buildName);
    let parsedPoint = await pointString.json();
    let castedPoint = parsedPoint as Point;
    if(destFlag) {
      this.props.markDest(castedPoint);
    } else {
      this.props.markStart(castedPoint);
    }
  }

  /**
   * Gives select component options for each building of UW campus
   * @return array of select component options for each building of UW campus
   */
  buildingSelection(): JSX.Element[] {
    const fields: string[] = Object.keys(this.state.buildMap);
    const vals: string[] = Object.values(this.state.buildMap);
    let valids: JSX.Element[] = [];  

    // Inv: All entries of fields and vals up to current are converted to
    // select component options and pushed to valids.
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
            <select
              id={"start-select"}
              value={this.state.start}
              onChange={(event: any) => {
                this.setState({start: event.target.value});
                this.markBuilding(event.target.value, false);}}>
              <option value={""}>Starting Building</option>
              {this.buildingSelection()}
            </select>
        </div>
        <div id="select-destination">
          <label htmlFor="dest-select">Destination:</label><br/>
            <select 
              id={"dest-select"}
              value={this.state.end}
              onChange={(event: any) => {
                this.setState({end: event.target.value});
                this.markBuilding(event.target.value, true);}}>
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
            this.props.onChange(null, null, [], "");
            this.setState({start: "", end: "", color: ""});}}>
            Reset
          </button>
        </div>
      </div>
    );
  }
}

export default PathChooser;
