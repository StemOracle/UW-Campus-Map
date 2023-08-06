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

package pathfinder;

import pathfinder.datastructures.*;
import pathfinder.parser.*;
import graph.Graph;

import java.util.*;


/**
 * An immutable CampusMap that allows pathfinding between campus buildings!
 *
 */
public class CampusMap implements ModelAPI {
    
    // AF: buildCords -> coordinate position of each building.
    // campus -> graph with building positions as nodes and distances as edges.
   
    // RI: No nulls anywhere, except potentially the return of findShortestPath.
    // Keys of buildCords must equal short name of corresponding CampusBuilding.
   
    /**
     * Maps a campus building's short name to its corresponding CampusBuilding.
     */	
    private Map<String, CampusBuilding> buildCords;


    /**
     * Graph with CampusPaths as nodes and path distances as edges.
     */
    private Graph<Point, Double> campus;


    /**
     * For debug purposes only!
     */
    private static final boolean DEBUG = false;


    /**
     * Creates new instance of a CampusMap!
     * @throws IllegalArgumentException if any provided path's parent or child
     * doesn't exist in the campus map.
     *
     */
    public CampusMap() {
        // Prepare Graph object
        this.campus = new Graph<Point, Double>();

        // Set of all campus buildings
        List<CampusBuilding> buildings = (CampusPathsParser.parseCampusBuildings("campus_buildings.csv"));
	Map<String, String> shortToLong = new HashMap<String, String>();

	// Inv: All CampusBuildings up to current are extracted for coordinates,
	// which are added as nodes of this.compass.
        for(CampusBuilding building : buildings) {
           this.campus.addNode(new Point(building.getX(), building.getY()));
	   this.buildCords.put(building.getShortName(), building);
        }

        // Set of all campus paths
        Set<CampusPath> paths = new HashSet<CampusPath>();
	paths.addAll(CampusPathsParser.parseCampusPaths("campus_paths.csv"));

	// Inv: All CampusPaths are matched with their Point nodes,
	// then added as edges to this.compass.
	for(CampusPath path : paths) {
	    Point start = null;
	    Point end = null;
	    // No invarient here just looking for start and end buildings of current CampusPath.
	    for(CampusBuilding building : buildings) {
		if((path.getX1() == building.getX()) && (path.getY1() == building.getY())) {
		    start = new Point(path.getX1(), path.getY1());
		}
		if((path.getX2() == building.getX()) && (path.getY1() == building.getY())) {
		    end = new Point(path.getX2(), path.getY2());
		}
	    }
	    if(start == null || end == null) {
		String errorMsg = "Path pointed from (" + String.format("%.3f", path.getX1());
		errorMsg += ", " + String.format("%.3f", path.getY1()) + ") to (";
		errorMsg += String.format("%.3f", path.getX2()) + ", " + String.format("%.3f", path.getY2());
		errorMsg += "). Start or end coordinate does not correspond to existing building.";
	        throw new IllegalArgumentException(errorMsg);
	    }
	    this.campus.addEdge(start, end, path.getDistance());
	}
    }


    @Override
    public boolean shortNameExists(String shortName) {
        return (this.buildCords.keySet()).contains(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
	if(!shortNameExists(shortName)) {
	    throw new IllegalArgumentException("Short building name " + shortName + " not found.");
	}
        return (this.buildCords.get(shortName)).getLongName();
    }

    @Override
    public Map<String, String> buildingNames() {
        Map<String, String> toLong = new HashMap<String, String>();

	// Inv: All shortName keys up to current are put into new map,
	// with corresponding longNames as values.
	for(String shortName : this.buildCords.keySet()) {
	    toLong.put(shortName, (this.buildCords.get(shortName)).getLongName());
	}
	return toLong;
    }

    @Override 
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
	if(startShortName == null || endShortName == null) {
	    throw new IllegalArgumentException(startShortName + " or " + endShortName + " was null.");
	} else if((this.buildCords.keySet()).contains(startShortName)
		  || (this.buildCords.keySet()).contains(endShortName)) {
	    throw new IllegalArgumentException(startShortName + " or " + endShortName + " not found.");
	} else {
	    CampusBuilding start = this.buildCords.get(startShortName);
	    CampusBuilding end = this.buildCords.get(endShortName);
	    return Algorithm.findShortestDistance(this.campus,
			                                 new Point(start.getX(), start.getY()),
			                                 new Point(end.getX(), end.getY()));
	}
    }

    
    /**
     * Checks if RI has been violated.
     * Executes smoothly if not.
     * @throws AssertionError if RI violated.
     */
    private void checkRep() {
        assert this.buildCords != null;
	assert this.campus != null;
	if(DEBUG) {
	    // No need to check for nulls in graph object; its RI checks for that!
	    // Inv: All keys and values up to current are non-null and key value
	    // matches corresponding CampusBuilding shortName.
	    for(String name : this.buildCords.keySet()) {
	        assert name != null;
		CampusBuilding building = this.buildCords.get(name);
		assert building != null;
		assert building.getShortName() != null;
		assert building.getLongName() != null;
	    }
	}
    }
}
