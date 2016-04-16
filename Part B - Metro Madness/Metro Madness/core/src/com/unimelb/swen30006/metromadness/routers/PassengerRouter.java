/**
 * 
 */
package com.unimelb.swen30006.metromadness.routers;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

public interface PassengerRouter {
	public boolean shouldDisembark(Passenger p, Train train,
			Station currentStation) throws Exception;

	public boolean shouldEmbark(Passenger p, Train train,
			Station currentStation) throws Exception;

}
