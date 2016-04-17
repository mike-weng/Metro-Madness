/*
 * Group 44
 */
package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.tracks.Track;

public abstract class Train {

	// The state that a train can be in
	public enum State {
		IN_STATION, READY_DEPART, ON_ROUTE, WAITING_ENTRY, FROM_DEPOT
	}

	// Constants
	public static final int MAX_TRIPS = 4;
	public static final Color FORWARD_COLOUR = Color.ORANGE;
	public static final Color BACKWARD_COLOUR = Color.VIOLET;
	public static final float TRAIN_WIDTH = 4;
	public static final float TRAIN_LENGTH = 6;
	public static final float TRAIN_SPEED = 50f;

	// The line that this is traveling on
	protected Line trainLine;

	// Passenger Information
	protected int trainSize;
	protected ArrayList<Passenger> passengers;
	protected float departureTimer;

	// Station and track and position information
	protected Station station;
	protected Track track;
	protected Point2D.Float pos;

	// Direction and direction
	protected boolean forward;
	protected State state;

	// State variables
	protected int numTrips;
	protected boolean disembarked;

	public Train(Line trainLine, Station start, boolean forward,
			int trainSize) {
		this.trainLine = trainLine;
		this.station = start;
		this.state = State.FROM_DEPOT;
		this.trainSize = trainSize;
		this.forward = forward;
		this.passengers = new ArrayList<Passenger>();
	}

	public abstract void update(float delta);

	public void embark(Passenger p) throws Exception {
		if (this.passengers.size() > this.trainSize) {
			throw new Exception();
		}
		this.passengers.add(p);
	}
	
	protected ArrayList<Passenger> disembark() throws Exception {
		ArrayList<Passenger> disembarking = new ArrayList<Passenger>();
		Iterator<Passenger> iterator = this.passengers.iterator();
		while (iterator.hasNext()) {
			Passenger p = iterator.next();
			if (this.station.shouldDisembark(p, this)) {
				disembarking.add(p);
				iterator.remove();
			}
		}
		return disembarking;
	}
	
	protected void move(float delta) {
		// Work out where we're going
		float angle = angleAlongLine(this.pos.x, this.pos.y,
				this.station.getPosition().x, this.station.getPosition().y);
		float newX = this.pos.x
				+ (float) (Math.cos(angle) * delta * TRAIN_SPEED);
		float newY = this.pos.y
				+ (float) (Math.sin(angle) * delta * TRAIN_SPEED);
		this.pos.setLocation(newX, newY);
	}
	
	@Override
	public String toString() {
		return "Train [line=" + this.trainLine.getName() + ", departureTimer="
				+ departureTimer + ", pos=" + pos + ", forward=" + forward
				+ ", state=" + state + ", numTrips=" + numTrips
				+ ", disembarked=" + disembarked + "]";
	}

	protected boolean inStation() {
		return (this.state == State.IN_STATION
				|| this.state == State.READY_DEPART);
	}

	private float angleAlongLine(float x1, float y1, float x2, float y2) {
		return (float) Math.atan2((y2 - y1), (x2 - x1));
	}

	public void render(ShapeRenderer renderer) {
		if (!this.inStation()) {
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			float percentage = this.passengers.size() / 20f;
			renderer.setColor(col.cpy().lerp(Color.DARK_GRAY, percentage));
			renderer.circle(this.pos.x, this.pos.y,
					TRAIN_WIDTH * (1 + percentage));
		}
	}

	public Line getTrainLine() {
		return this.trainLine;
	}

	public boolean getForward() {
		return this.forward;
	}

}
