/*
 * Group 44
 */
package com.unimelb.swen30006.metromadness.passengers;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class RandomGenerator extends PassengerGenerator {

	public RandomGenerator(Station s, float max) {
		super(s, max);
	}

	public Passenger[] generatePassengers() {
		int count = (int) (Math.random() * maxVolume);
		Passenger[] passengers = new Passenger[count];
		for (int i = 0; i < count; i++) {
			passengers[i] = generatePassenger();
		}
		return passengers;
	}

	public Passenger generatePassenger() {
		// Pick a random line from all lines
		Line l = allLines.get((int) (Math.random() * (allLines.size() - 1)));
		// Pick a random station from the line
		Station destination = l.getStations()
				.get((int) (Math.random() * (l.getStations().size() - 1)));

		// re-generate if the station is the currentStation or station does not
		// allow passengers to leave
		if (destination.equals(this.s) || !destination.passengersCanLeave()) {
			return generatePassenger();
		}
		return this.s.generatePassenger(destination);
	}

}
