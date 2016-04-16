/*
 * Group 44
 */
package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

public class OneTrack extends Track {
	private boolean occupied;

	public OneTrack(Point2D.Float start, Point2D.Float end, Color trackCol) {
		super(start, end, trackCol);
		this.occupied = false;
	}

	public void render(ShapeRenderer renderer) {
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y,
				LINE_WIDTH);
	}

	public boolean canEnter(boolean forward) {
		return !this.occupied;
	}

	public void enter(Train t) {
		this.occupied = true;
	}

	public String toString() {
		return "Track [startPos=" + startPos + ", endPos=" + endPos
				+ ", trackColour=" + trackColour + ", occupied=" + occupied
				+ "]";
	}

	public void leave(Train t) {
		this.occupied = false;
	}
}