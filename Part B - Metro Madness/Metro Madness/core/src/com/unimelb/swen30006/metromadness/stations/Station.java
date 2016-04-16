package com.unimelb.swen30006.metromadness.stations;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Station {

	public static final int PLATFORMS = 2;
	public static final float RADIUS = 6;
	public static final int NUM_CIRCLE_STATMENTS = 100;
	public static final int MAX_LINES = 3;
	public static final float DEPARTURE_TIME = 2;

	private Point2D.Float position;
	private String name;
	protected ArrayList<Line> lines;
	protected ArrayList<Train> trains;
	private PassengerRouter router;
	private boolean passengersCanEnter;
	private boolean passengersCanLeave;
	private PassengerGenerator g;
	private ArrayList<Passenger> waiting;

	public Station(float x, float y, PassengerRouter router, String name,
			float maxPax, boolean canEnter, boolean canLeave) {
		this.name = name;
		this.router = router;
		this.position = new Point2D.Float(x, y);
		this.lines = new ArrayList<Line>();
		this.trains = new ArrayList<Train>();
		this.waiting = new ArrayList<Passenger>();
		this.g = new PassengerGenerator(this, this.lines, maxPax);
		this.passengersCanEnter = canEnter;
		this.passengersCanLeave = canLeave;
	}

	public void registerLine(Line l) {
		this.lines.add(l);
	}

	public void render(ShapeRenderer renderer) {
		float radius = RADIUS;
		for (int i = 0; (i < this.lines.size() && i < MAX_LINES); i++) {
			Line l = this.lines.get(i);
			renderer.setColor(l.getLineColour());
			renderer.circle(this.position.x, this.position.y, radius,
					NUM_CIRCLE_STATMENTS);
			radius = radius - 1;
		}

		// Calculate the percentage
		float t = this.trains.size() / (float) PLATFORMS;
		Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
		renderer.setColor(c);
		renderer.circle(this.position.x, this.position.y, radius,
				NUM_CIRCLE_STATMENTS);
	}

	public void enter(Train t) throws Exception {
		if (trains.size() >= PLATFORMS) {
			throw new Exception();
		} else {
			// Add the train
			this.trains.add(t);
			// Add the waiting passengers
			Iterator<Passenger> pIter = this.waiting.iterator();
			while (pIter.hasNext()) {
				Passenger p = pIter.next();
				try {
					if (this.shouldEmbark(p, t)) {
						t.embark(p);
					}
					pIter.remove();
				} catch (Exception e) {
					// Do nothing, already waiting
					break;
				}
			}

			if (this.passengersCanEnter) {
				// Add the new passenger
				Passenger[] ps = this.g.generatePassengers();
				for (Passenger p : ps) {
					try {
						if (this.shouldEmbark(p, t)) {
							t.embark(p);
						} else {
							this.waiting.add(p);
						}
					} catch (Exception e) {
						this.waiting.add(p);
					}
				}
			}
		}
	}

	public void addWaiting(Passenger p) {
		waiting.add(p);
	}

	public void depart(Train t) throws Exception {
		if (this.trains.contains(t)) {
			this.trains.remove(t);
		} else {
			throw new Exception();
		}
	}

	public boolean canEnter() throws Exception {
		return this.trains.size() < PLATFORMS;
	}

	public boolean passengersCanLeave() {
		return passengersCanLeave;
	}

	// Returns departure time in seconds
	public float getDepartureTime() {
		return DEPARTURE_TIME;
	}

	public boolean shouldDisembark(Passenger p, Train t) throws Exception {
		return this.router.shouldDisembark(p, t, this);
	}

	public boolean shouldEmbark(Passenger p, Train train) throws Exception {
		return this.router.shouldEmbark(p, train, this);
	}

	@Override
	public String toString() {
		return "Station [position=" + position + ", name=" + name + ", trains="
				+ trains.size() + ", router=" + router + "]";
	}

	public Passenger generatePassenger(Station s) {
		return new Passenger(this, s);
	}

	public Point2D.Float getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Line> getLines() {
		return lines;
	}

}
