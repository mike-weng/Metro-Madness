package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.trains.Train;

public abstract class Track {
	public static final float DRAW_RADIUS = 10f;
	public static final int LINE_WIDTH = 6;
	protected Point2D.Float startPos;
	protected Point2D.Float endPos;
	protected Color trackColour;

	public Track(Point2D.Float start, Point2D.Float end, Color trackCol) {
		this.startPos = start;
		this.endPos = end;
		this.trackColour = trackCol;
	}

	abstract public void render(ShapeRenderer renderer);

	abstract public boolean canEnter(boolean forward);

	abstract public void enter(Train t);

	abstract public String toString();

	abstract public void leave(Train t);
}