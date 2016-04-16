package com.unimelb.swen30006.metromadness.routers;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class SimpleRouter implements PassengerRouter {

	/*
	 * This function determines whether the passenger should disembark the
	 * station or not
	 */
	public boolean shouldDisembark(Passenger p, Train t, Station currentStation)
			throws Exception {
		Station destination = p.getDestination();
		Line trainLine = t.getTrainLine();

		// check if the current train can take you to your destination
		if (trainLine.getStations().contains(destination)) {
			if (currentStation.equals(destination)) {
				p.setReachedDestination(true);
				return true;
			} else {
				// train can take the passenger to destination
				// but won't disembark until reached
				return false;
			}
		}

		// if code reach here: this train won't take you to your destination
		// have to transfer

		// check if the current station can be a transfer station
		if (onSameLine(currentStation, destination)) {
			currentStation.addWaiting(p);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * This function determines whether the passenger should embark the station
	 * or not
	 */
	public boolean shouldEmbark(Passenger p, Train t, Station currentStation)
			throws Exception {

		Station destination = p.getDestination();
		Line trainLine = t.getTrainLine();
		Station checkStation = currentStation;
		boolean shouldEmbark = false;

		// if the currentStation is the end then change the direction
		// for checks
		boolean forward = t.getForward();
		if (trainLine.endOfLine(currentStation)) {
			forward = !forward;
		}

		//
		do {
			// check if destination is on this trainLine
			if (trainLine.getStations().contains(destination)) {
				if (checkStation.equals(destination)) {
					// destination is on the path therefore embark
					shouldEmbark = true;
					break;
				}
			} else if (onSameLine(checkStation, destination)) {
				// if destination and checkStation are on the same line
				// checkStation would be a transfer station
				if (checkStation.equals(currentStation)) {
					// if currentStation is already a transfer station
					// don't embark, just wait for the train with same
					// trainLine as destination
					shouldEmbark = false;
					break;
				} else {
					shouldEmbark = true;
					break;
				}
			}
			// check the nextStation on the line
			checkStation = trainLine.nextStation(checkStation, forward);
		} while (!trainLine.endOfLine(checkStation));

		// handle the last station
		if (trainLine.getStations().contains(destination)) {
			if (checkStation.equals(destination)) {
				shouldEmbark = true;
			}
		} else if (onSameLine(checkStation, destination)) {
			if (checkStation.equals(currentStation)) {
				shouldEmbark = false;
			} else {
				shouldEmbark = true;
			}
		}

		// no transfer station on the path
		return shouldEmbark;
	}

	/*
	 * Check if station1 and station2 shares a same line
	 */
	private boolean onSameLine(Station station1, Station station2) {
		for (Line line : station2.getLines()) {
			if (station1.getLines().contains(line)) {
				return true;
			}
		}
		return false;
	}

}
