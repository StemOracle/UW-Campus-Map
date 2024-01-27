import React, {Component} from 'react';
import { Point, Segment, Path } from './Interfaces'

/** Properties of this component */
interface PathChooserProps {
  /** Called when drawing a path, changing color, or marking buildings */
  onChange(startPoint: Point | null | undefined,
           destPoint: Point | null | undefined,
           segs: Segment[] | undefined,
           drawColor: string | undefined,
           isPathfinding: boolean): void;
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

/** Text field that allows the user to enter the list of edges
 * Also buttons allowing user to interact with app */
class PathChooser extends Component<PathChooserProps, PathChooserState> {
  /** URL of Spark backend */
  private readonly HOSTNAME: string = "http://localhost:";
  private readonly PORT: string = "4567";

  constructor(props: PathChooserProps) {
    // Well-defined onChange function expected as props
    super(props);
    this.state = {start: "", end: "", color: "", buildMap: {}};
  }

  /** Sets up this component */
  componentDidMount() {
    this.getBuildings();
  }

  /** Fetches object that maps all UW campus building short names to long names */
  async getBuildings() {
    let buildsString = await fetch(this.HOSTNAME + this.PORT + "/listBuildings");
    let parsedBuilds = await buildsString.json();
    this.setState({buildMap: parsedBuilds});
  }

  /** Finds shortest path between building labeled start and building labeled end
   * @param start name of starting building on the UW campus
   * @param end name of destination building on the UW campus
   * @spec.requires valid buildings must be chosen, a color must be specified,
   * and a path must exist between the buildings. No behavior otherwise */
  async pathBetweenBuildings() {
    let col: string = this.state.color;
    // If not enough info given, benignly return
    if(this.state.start === "" || this.state.end === "") {
      alert("Please select a starting building and destination building."); return;
    } else if(this.state.color === "") {
      col = "red";
    }

    let pathString = await fetch(this.HOSTNAME + this.PORT + "/findPath?start="
                                               + this.state.start + "&end=" + this.state.end);
    let parsedPath = await pathString.json();
    // If no path found, benignly return
    if(parsedPath === null) {
      alert("No path found between buildings " + this.state.start + " and " + this.state.end); return;
    }
    let castedPath: Path = parsedPath as Path;
    let segs: Segment[] = [];

    // All Segments of directions are converted to an edge and pushed to edges
    for(let i: number = 0; i < castedPath.path.length; i+= 1) {
      segs.push(castedPath.path[i]);
    }
    this.props.onChange(undefined, undefined, segs, col, true);
  }

  async markBuilding(buildName: string, destFlag: boolean) {
    let pointString: Response = await fetch(this.HOSTNAME + this.PORT
                                            + "/lookupBuilding?shortName=" + buildName);
    let parsedPoint = await pointString.json();
    let castedPoint: Point = parsedPoint as Point;
    if(destFlag) {
      this.props.onChange(undefined, castedPoint, undefined, undefined, false);
    } else {
      this.props.onChange(castedPoint, undefined, undefined, undefined, false);
    }
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
              <textarea id={"color-text"} rows={1} cols={10} value={this.state.color}
                onChange={(event: any) => {
                  this.setState({color: event.target.value});}}/>
              <button onClick={() => {
                this.props.onChange(undefined, undefined, undefined, this.state.color, false);}}>✔</button>
          </div>
          <div id="options">
            <button onClick={() => {
              this.pathBetweenBuildings();}}>Find Path</button>
            <button onClick={() => {
              this.props.onChange(null, null, [], "", false);
              this.setState({start: "", end: "", color: ""});}}>Reset</button>
        </div>
      </div>
    );
  }
}

export default PathChooser;
