/**
 * 
 */
package com.unimelb.swen30006.metromadness.routers;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public interface PassengerRouter {
	
	public boolean shouldLeave(Station currentStation, Passenger p, Station nextStation);

	public boolean shouldEnter(Passenger p, Line trainLine);
	
}
