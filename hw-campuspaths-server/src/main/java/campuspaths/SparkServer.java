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

package campuspaths;

import campuspaths.utils.CORSFilter;
import pathfinder.CampusMap;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class SparkServer {

  public static void main(String[] args) {
    CORSFilter corsFilter = new CORSFilter();
    corsFilter.apply();
    // The above two lines help set up some settings that allow the
    // React application to make requests to the Spark server, even though it
    // comes from a different server.
    // You should leave these two lines at the very beginning of main().

    CampusMap campus = new CampusMap();
    Gson gson = new Gson();

    // Get names of buildings.
    // No query params.
    Spark.get("/listBuildings", new Route() {
      @Override
      public Object handle(Request req, Response resp) throws Exception {
	    return gson.toJson(campus.buildingNames());
      }
    });


    // Get path between two campus buildings.
    // Expected query params: "/findPath?start=string&end=string",
    // where start and end are short names of campus buildings.
    Spark.get("/findPath", new Route() {
      @Override
      public Object handle(Request req, Response resp) throws Exception {
	    String start = req.queryParams("start");
	    String end = req.queryParams("end");
	    return gson.toJson(campus.findShortestPath(start, end));
      }
    });

    // Get Point coordinate of campus building.
    // Expected query param: "/lookupBuilding?shortName=string"
    // shortName is short name of campus building in question
    Spark.get("/lookupBuilding", new Route() {
      @Override
      public Object handle(Request req, Response resp) throws Exception {
        String shortName = req.queryParams("shortName");
        return gson.toJson(campus.lookupBuilding(shortName));
      }
    });
  }
}
