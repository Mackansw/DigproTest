package se.digpro.gui;

import se.digpro.model.Point;

import java.awt.geom.Ellipse2D;

public class VertexPoint extends Ellipse2D.Double {

    private Point point;

    /**
     * Constructor with point parameter
     * @param point the to represent
     */
    public VertexPoint(Point point) {
        super(point.getX() - 5, point.getY() - 5, 10, 10);
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }
}