import React, { Component } from 'react';
import { Point, Path } from './Interfaces'

/** Properties of this component */
interface PathChooserProps {
  /** Marks selected buildings */
  markBuild(pt: Point, isStart: boolean): void;
  /** Draws path between selected buildings */
  pathfind(path: Path, col: string): void;
  /** Changes color of path and marked buildings */
  setCol(col: string): void;
  /** Resets entire application */
  reset(): void;
}

/** State of this component */
interface PathChooserState {
  /** Text stored in text field labeled 'Start' */
  start: string;
  /** Text stored in field labeled 'End' */
  end: string;
  /** Text stored in field labeled 'Color' */
  color: string;
  /** Map of campus buildings' short names to their long names */
  buildMap: object;
}

/** Slew of options including select menues for campus buildings;
 * button to circle two select buildings and find path between them;
 * text field to specify color of path and circled buildings, and button
 * that resets the entire app. */
class PathChooser extends Component<PathChooserProps, PathChooserState> {
  /** URL of Spark backend */
  private readonly HOSTNAME: string = "http://localhost:";
  private readonly PORT: string = "4567";

  constructor(props: PathChooserProps) {
    // Well-defined onChange, pathfind, setCol, and reset functions expected as props
    super(props);
    this.state = {start: "", end: "", color: "", buildMap: {}};
  }

  /** Sets up this component */
  componentDidMount(): void {
    this.getBuildings();
  }

  /** Fetches object that maps all UW campus building short names to long names */
  async getBuildings() {
    // If try fails, our server isn't running
    try {
      let buildsString = await fetch(this.HOSTNAME + this.PORT + "/listBuildings");
      // Something went wrong if this check fails.
      if(!buildsString.ok) { alert("Error retrieving buildings."); return; }
      let parsedBuilds = await buildsString.json();
      this.setState({buildMap: parsedBuilds});
    } catch (e) {
      alert("Error: Server down for maintenance.");
    }
  }

  /** Finds shortest path between building labeled start and building labeled end
   * @spec.requires valid buildings must be chosen, color must be specified,
   * and a path must exist between buildings. No behavior otherwise */
  async pathBetweenBuildings() {
    let col: string = this.state.color;
    // If not enough info given, benignly return
    if(this.state.start === "" || this.state.end === "") {
      alert("Please select a starting building and destination building."); return;
    } else if(this.state.color === "") {
      col = "red";
    }

    // If try fails, our server isn't running
    try {
      let pathString = await fetch(this.HOSTNAME + this.PORT + "/findPath?start="
        + this.state.start + "&end=" + this.state.end);
      // Something went wrong if this check fails.
      if(!pathString.ok) { alert("Error finding path between select buildings."); return; }
      let parsedPath = await pathString.json();
      // If no path found, benignly return
      if (parsedPath === null) {
        alert("No path found between buildings " + this.state.start + " and " + this.state.end);
        return;
      }
      this.props.pathfind(parsedPath as Path, col);
    } catch (e) { alert("Error: Server down for maintenance."); }
  }

  /** Draws circle around select building in select color
   * @param buildName name of select building
   * @param isStart true iff select building is start building (not destination) */
  async markBuilding(buildName: string, isStart: boolean) {
    // If try fails, our server isn't running
    try {
      let pointString: Response = await fetch(this.HOSTNAME + this.PORT
        + "/lookupBuilding?shortName=" + buildName);
      // Something went wrong if this check fails.
      if(!pointString.ok) { alert("Error finding select building."); return; }
      let parsedPoint = await pointString.json();
      let castedPoint: Point = parsedPoint as Point;
      this.props.markBuild(castedPoint, isStart);
    } catch (e) { alert("Error: Server down for maintenance."); }
  }

  /** Gives select component options for each building of UW campus
   * @return array of select component options for each building of UW campus */
  buildingSelection(): JSX.Element[] {
    const fields: string[] = Object.keys(this.state.buildMap);
    const vals: string[] = Object.values(this.state.buildMap);
    let valids: JSX.Element[] = [];  

    // Inv: All entries of fields and vals up to current are converted to
    // select component options and pushed to valids.
    for(let i: number = 0; i < fields.length; i += 1) {
      valids.push(<option value={fields[i]}>{vals[i]}</option>);
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
                this.markBuilding(event.target.value, true);
              }}>
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
                this.markBuilding(event.target.value, false);}}>
              <option value={""}>Destination Building</option>
              {this.buildingSelection()}
            </select>
        </div>
        <div id="enter-color">
          <label htmlFor="color-text">Color:</label><br/>
          <textarea id={"color-text"} rows={1} cols={10} value={this.state.color}
            onChange={(event: any) => {
              this.setState({color: event.target.value});}}/>
          <button onClick={() => {
            this.props.setCol(this.state.color);}}>
            ✔</button>
        </div>
        <div id="options">
          <button onClick={() => {
            this.pathBetweenBuildings();}}>
            Find Path</button>
          <button onClick={() => {
              this.props.reset();
              this.setState({start: "", end: "", color: ""});}}>
            Reset</button>
        </div>
      </div>
    );
  }
}

export default PathChooser;
