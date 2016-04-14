package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Collection;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerGenerator {
	private static ArrayList<Line> allLines;
	// The station that passengers are getting on
	private Station s;
	// The line they are travelling on
	private ArrayList<Line> lines;
	
	// The max volume
	private float maxVolume;
	
	public PassengerGenerator(Station s, ArrayList<Line> lines, float max){
		this.s = s;
		this.lines = lines;
		this.maxVolume = max;
	}
	
	public Passenger[] generatePassengers(){
		int count = (int) (Math.random()*maxVolume);
		Passenger[] passengers = new Passenger[count];
		for(int i=0; i<count; i++){
			passengers[i] = generatePassenger();
		}
		return passengers;
	}
	
	public Passenger generatePassenger(){
		// Pick a random station from the line
		
		Line l = allLines.get((int) (Math.random()*(allLines.size()-1)));
		Station s = l.getStations().get((int) (Math.random()*(l.getStations().size()-1)));
		
		if (s.getName().equals(this.s.getName())) {
			generatePassenger();
		}
		return this.s.generatePassenger(s);
	}
	
	public static void setAllLines(Collection<Line> collection) {
		allLines = new ArrayList<Line>(collection);
	}
	
}
