package com.howie.snake;

/**
 * Simple class containing two integer values and a comparison function.
 * There's probably something I should use instead, but this was quick and
 * easy to build.
 * 
 */
public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int newX, int newY) {
        x = newX;
        y = newY;
    }
    public Coordinate(Coordinate p) {
        x = p.x;
        y = p.y;
    }
    public boolean equals(Coordinate other) {
        if (x == other.x && y == other.y) {
            return true;
        }
        return false;
    }

    public void move(Dir dir)
    {
	    switch(dir)
	    {
	    case UP:
	        y--;
	        break;
	    case RIGHT:
	        x++;
	        break;
	    case LEFT:
	        x--;
	        break;
	    case DOWN:
	        y++;
	        break;
	    default:
	        break;
	    }
	}
	
    @Override
    public String toString() {
        return "Coordinate: [" + x + "," + y + "]";
    }
}