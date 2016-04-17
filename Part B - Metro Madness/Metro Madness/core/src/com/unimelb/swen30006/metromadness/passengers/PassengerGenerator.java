package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Collection;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public abstract class PassengerGenerator {
	protected static ArrayList<Line> allLines;
	// The station that passengers are getting on
	protected Station s;
	// The line they are travelling on
	// The max volume
	protected float maxVolume;
	public PassengerGenerator(Station s, float max) {
		this.s = s;
		this.maxVolume = max;
	}
	public abstract Passenger[] generatePassengers();
	public abstract Passenger generatePassenger();
	public static void setAllLines(Collection<Line> values) {
		allLines = new ArrayList<Line>(values);
	}
}
