package com.unimelb.swen30006.metromadness.routers;


import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.ActiveStation;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class SimpleRouter implements PassengerRouter {
	
	@Override
	public boolean shouldLeave(Station currentStation, Passenger p, Station nextStation) {
		Station destination = p.getDestination();
		boolean shouldLeave;
		boolean nextAndDestSameLine = onSameLine(nextStation, destination);
		boolean currAndDestSameLine = onSameLine(currentStation, destination);
		
		if (nextAndDestSameLine) {
			if (currentStation.equals(destination)) {
				shouldLeave = true;
				p.setReachedDestination(true);
			} else {
				shouldLeave = false;
			}
		} else if (currAndDestSameLine) {
			if (currentStation instanceof ActiveStation) {
				System.out.println("change line");			
				System.out.println(destination.getLines());
				System.out.println(currentStation.getLines());
				System.out.println(nextStation.getLines());
				((ActiveStation) currentStation).addWaiting(p);
				shouldLeave = true;
			} else {
				shouldLeave = false;
			}
		} else {
			shouldLeave = false;
		}
		return shouldLeave;
	}
	public boolean shouldEnter(Passenger p, Line trainLine) {
		Station destination = p.getDestination();
		if (trainLine.getStations().contains(destination)) {
			return true;
		}
		
		for (Station s : trainLine.getStations()) {
			if (onSameLine(s, destination)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean onSameLine(Station station1, Station station2) {
		for (Line line : station2.getLines()) {
			if (station1.getLines().contains(line)) {
				// don't leave if next 
				return true;
			}
		}
		return false;
	}

}
