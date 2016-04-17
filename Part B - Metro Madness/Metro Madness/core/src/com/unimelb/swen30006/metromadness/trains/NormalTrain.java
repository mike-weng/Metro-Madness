/*
 * Group 44
 */
package com.unimelb.swen30006.metromadness.trains;

import java.awt.geom.Point2D;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class NormalTrain extends Train {

	public NormalTrain(Line trainLine, Station start, boolean forward,
			int trainSize) {
		super(trainLine, start, forward, trainSize);
	}

	public void update(float delta) {
		// Update all passengers
		for (Passenger p : this.passengers) {
			p.update(delta);
		}

		// Update the state
		switch (this.state) {
		case FROM_DEPOT:
			// We have our station initialized we just need to retrieve the next
			// track, enter the
			// current station offically and mark as in station
			try {
				if (this.station.canEnter()) {
					this.station.enter(this);
					this.pos = (Point2D.Float) this.station.getPosition()
							.clone();
					this.state = State.IN_STATION;
					this.disembarked = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		case IN_STATION:
			// When in station we want to disembark passengers
			// and wait 10 seconds for incoming passgengers
			if (!this.disembarked) {
				try {
					this.disembark();
					this.departureTimer = this.station.getDepartureTime();
					this.disembarked = true;
				} catch (Exception e) {
					// Massive error.
					return;
				}
			} else {
				// Count down if departure timer.
				if (this.departureTimer > 0) {
					this.departureTimer -= delta;
				} else {
					// We are ready to depart, find the next track and wait
					// until we can enter
					try {
						boolean endOfLine = this.trainLine
								.endOfLine(this.station);
						if (endOfLine) {
							this.forward = !this.forward;
						}
						this.track = this.trainLine.nextTrack(this.station,
								this.forward);
						this.state = State.READY_DEPART;
						break;
					} catch (Exception e) {
						// Massive error.
						return;
					}
				}
			}
			break;
		case READY_DEPART:

			// When ready to depart, check that the track is clear and if
			// so, then occupy it if possible.
			if (this.track.canEnter(this.forward)) {
				try {
					// Find the next
					Station next = this.trainLine.nextStation(this.station,
							this.forward);
					// Depart our current station
					this.station.depart(this);
					this.station = next;

				} catch (Exception e) {
					// e.printStackTrace();
				}
				this.track.enter(this);
				this.state = State.ON_ROUTE;
			}
			break;
		case ON_ROUTE:

			// Checkout if we have reached the new station
			if (this.pos.distance(this.station.getPosition()) < 10) {
				this.state = State.WAITING_ENTRY;
			} else {
				move(delta);
			}
			break;
		case WAITING_ENTRY:

			// Waiting to enter, we need to check the station has room and if so
			// then we need to enter, otherwise we just wait
			try {
				if (this.station.canEnter()) {
					this.track.leave(this);
					this.pos = (Point2D.Float) this.station.getPosition()
							.clone();
					this.station.enter(this);
					this.state = State.IN_STATION;
					this.disembarked = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}
}
