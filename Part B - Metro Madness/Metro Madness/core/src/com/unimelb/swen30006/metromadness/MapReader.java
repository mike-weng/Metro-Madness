/*
 * Group 44
 */
package com.unimelb.swen30006.metromadness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// Imports for parsing XML files
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
// The things we are generating
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.routers.SimpleRouter;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class MapReader {

	private ArrayList<Train> trains;
	private HashMap<String, Station> stations;
	private HashMap<String, Line> lines;

	private boolean processed;
	private String filename;

	public MapReader(String filename) {
		this.trains = new ArrayList<Train>();
		this.stations = new HashMap<String, Station>();
		this.lines = new HashMap<String, Line>();
		this.filename = filename;
		this.processed = false;
	}

	public void process() {
		try {
			// Build the doc factory
			FileHandle file = Gdx.files.internal("maps/melbourne.xml");
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);

			// Process stations
			Element stations = root.getChildByName("stations");
			Array<Element> stationList = stations.getChildrenByName("station");
			for (Element e : stationList) {
				Station s = processStation(e);
				this.stations.put(s.getName(), s);
			}

			// Process Lines
			Element lines = root.getChildByName("lines");
			Array<Element> lineList = lines.getChildrenByName("line");
			for (Element e : lineList) {
				Line l = processLine(e);
				this.lines.put(l.getName(), l);
			}

			PassengerGenerator.setAllLines(this.lines.values());

			// Process Trains
			Element trains = root.getChildByName("trains");
			Array<Element> trainList = trains.getChildrenByName("train");
			for (Element e : trainList) {
				Train t = processTrain(e);
				this.trains.add(t);
			}

			this.processed = true;

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public Collection<Train> getTrains() {
		if (!this.processed) {
			this.process();
		}
		return this.trains;
	}

	public Collection<Line> getLines() {
		if (!this.processed) {
			this.process();
		}
		return this.lines.values();
	}

	public Collection<Station> getStations() {
		if (!this.processed) {
			this.process();
		}
		return this.stations.values();
	}

	private Train processTrain(Element e) {
		// Retrieve the values
		String line = e.get("line");
		String start = e.get("start");
		int size = e.getInt("size");
		boolean dir = e.getBoolean("direction");

		// Retrieve the lines and stations
		Line l = this.lines.get(line);
		Station s = this.stations.get(start);

		// Make the train
		return new Train(l, s, dir, size);
	}

	private Station processStation(Element e) {
		String name = e.get("name");
		int x_loc = e.getInt("x_loc") / 8;
		int y_loc = e.getInt("y_loc") / 8;
		int maxPax = e.getInt("max_passengers");
		boolean canEnter = e.getBoolean("canEnter");
		boolean canLeave = e.getBoolean("canLeave");
		String router = e.get("router");
		PassengerRouter r = createRouter(router);

		return new Station(x_loc, y_loc, r, name, maxPax, canEnter, canLeave);
	}

	private Line processLine(Element e) {
		Color stationCol = extractColour(e.getChildByName("station_colour"));
		Color lineCol = extractColour(e.getChildByName("line_colour"));
		String name = e.get("name");
		Line l = new Line(stationCol, lineCol, name);

		Array<Element> stations = e.getChildrenByNameRecursively("station");
		for (Element s : stations) {
			Station station = this.stations.get(s.get("name"));
			boolean twoWay = s.getBoolean("double");
			l.addStation(station, twoWay);
		}

		return l;
	}

	private PassengerRouter createRouter(String type) {
		if (type.equals("simple")) {
			return new SimpleRouter();
		}
		return null;
	}

	private Color extractColour(Element e) {
		float red = e.getFloat("red") / 255f;
		float green = e.getFloat("green") / 255f;
		float blue = e.getFloat("blue") / 255f;
		return new Color(red, green, blue, 1f);
	}

}
