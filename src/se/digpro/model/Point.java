package se.digpro.model;

public class Point {

    private String name;
    private int x;
    private int y;

    /**
     * Point constructor with the points X coordinate, Y coordinate and Point name as parameters.
     * @param x the points X coordinate
     * @param y the points Y coorinate
     * @param name the name of the point
     */
    public Point(int x, int y, String name) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}