package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

public class DualTrack extends Track {

	protected boolean forwardOccupied;
	protected boolean backwardOccupied;

	public DualTrack(Point2D.Float start, Point2D.Float end, Color trackCol) {
		super(start, end, trackCol);
		this.forwardOccupied = false;
		this.backwardOccupied = false;
	}

	public void render(ShapeRenderer renderer) {
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y,
				LINE_WIDTH);
		renderer.setColor(new Color(245f / 255f, 245f / 255f, 245f / 255f, 0.5f)
				.lerp(this.trackColour, 0.5f));
		renderer.rectLine(startPos.x, startPos.y, endPos.x, endPos.y,
				LINE_WIDTH / 3);
		renderer.setColor(this.trackColour);
	}

	public void enter(Train t) {
		if (t.getForward()) {
			this.forwardOccupied = true;
		} else {
			this.backwardOccupied = true;
		}
	}

	public boolean canEnter(boolean forward) {
		if (forward) {
			return !this.forwardOccupied;
		} else {
			return !this.backwardOccupied;
		}
	}

	public void leave(Train t) {
		if (t.getForward()) {
			this.forwardOccupied = false;
		} else {
			this.backwardOccupied = false;
		}
	}

	@Override
	public String toString() {
		return "DualTrack [forwardOccupied=" + forwardOccupied
				+ ", backwardOccupied=" + backwardOccupied + ", startPos="
				+ startPos + ", endPos=" + endPos + ", trackColour="
				+ trackColour + "]";
	}
}