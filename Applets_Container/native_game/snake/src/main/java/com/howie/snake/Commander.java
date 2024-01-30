package com.howie.snake;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Commander {
    private int rows;
    private int columns;
    private final int DISTANCE_STUB = 0x7ffffffe;
    private final int DISTANCE_RIGID = 0x7fffffff;
    private static final Random RNG = new Random();

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    private int getDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
    }

    private Coordinate getCloseApple(Coordinate snakeHead, ArrayList<Coordinate> apples) {
        int minD = DISTANCE_RIGID;
        Coordinate r = apples.get(0);
        for (Coordinate c : apples) {
            if (c.x == 0 || c.y == 0)
                continue;

            int d = getDistance(snakeHead, c);
            if (minD > d) {
                minD = d;
                r = c;
            }
        }
        return r;
    }

    private int[][] getTileDistanceFromPos(Coordinate pos, ArrayList<Coordinate> snake) {
        ArrayList<Coordinate> apples = new ArrayList<Coordinate>();
        return getTileDistanceFromPos(pos, snake, apples);
    }

    private int[][] getTileDistanceFromPos(Coordinate pos, ArrayList<Coordinate> snake, ArrayList<Coordinate> apples) {
        int distance[][] = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == 0 || i == (rows - 1) || j == 0 || j == (columns - 1))
                    distance[i][j] = DISTANCE_RIGID;
                else {
                    distance[i][j] = DISTANCE_STUB;
                }
            }
        }
        for (Coordinate c : snake) {
            distance[c.y][c.x] = DISTANCE_RIGID;
        }
        for (Coordinate c : apples) {
            distance[c.y][c.x] = DISTANCE_RIGID;
        }

        distance[pos.y][pos.x] = 0;
        ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
        pointList.add(pos);
        while (pointList.size() > 0) {
            Coordinate frontPos = pointList.get(0);
            pointList.remove(0);
            for (int i = 0; i < 4; i++) {
                Coordinate nextPos = new Coordinate(frontPos);
                nextPos.move(Dir.values()[i]);
                if (distance[nextPos.y][nextPos.x] == DISTANCE_STUB) {
                    // Is empty tile
                    distance[nextPos.y][nextPos.x] = distance[frontPos.y][frontPos.x] + 1;
                    pointList.add(nextPos);
                }
            }
        }
        return distance;
    }

    private class CoordinateStep {
        public Coordinate pos;
        public int distance;
        public Dir relativeDir;
    }

    private boolean canSimulate(int distanceTable[][], ArrayList<Coordinate> snake, Coordinate destination) {
        while (snake.get(0).x != destination.x || snake.get(0).y != destination.y) {
            int i;
            CoordinateStep minStep = new CoordinateStep();
            minStep.distance = DISTANCE_STUB;
            for (i = 0; i < 4; i++) {
                Coordinate nextStep = new Coordinate(snake.get(0));
                nextStep.move(Dir.values()[i]);
                if (distanceTable[nextStep.y][nextStep.x] < minStep.distance) {
                    minStep.distance = distanceTable[nextStep.y][nextStep.x];
                    minStep.pos = nextStep;
                    minStep.relativeDir = Dir.values()[i];
                }
            }
            if (minStep.distance == DISTANCE_STUB) {
                return false;
            }
            snake.remove(snake.size() - 1);
            snake.add(0, minStep.pos);
        }
        return isReachable(snake, snake.get(snake.size() - 1));
    }

    ArrayList<CoordinateStep> getNextReachablePosList(int distance[][], Coordinate pos) {
        ArrayList<CoordinateStep> csList = new ArrayList<CoordinateStep>();
        for (int i = 0; i < 4; i++) {
            Coordinate nextPos = new Coordinate(pos);
            nextPos.move(Dir.values()[i]);
            int step = distance[nextPos.y][nextPos.x];
            if (step >= 0 && step < DISTANCE_STUB) {
                CoordinateStep cs = new CoordinateStep();
                cs.distance = step;
                cs.pos = nextPos;
                cs.relativeDir = Dir.values()[i];
                csList.add(cs);
            }
        }
        return csList;
    }

    Dir findMinDistanceDir(int distanceTable[][], Coordinate pos, Dir currentDir) {
        ArrayList<CoordinateStep> csList = getNextReachablePosList(distanceTable, pos);
        if (csList.size() < 0)
            return Dir.values()[RNG.nextInt() % Dir.DIR_COUNT.ordinal()];

        int m = 0x7fffffff;
        Dir findDir = Dir.DIR_COUNT;
        for (CoordinateStep cs : csList) {
            if (m > cs.distance || (m == cs.distance && (Dir) currentDir == cs.relativeDir)) {
                m = cs.distance;
                findDir = cs.relativeDir;
            }
        }
        return findDir;
    }

    Dir findMaxDistanceDir(int distanceTable[][], Coordinate pos, Dir currentDir) {
        ArrayList<CoordinateStep> csList = getNextReachablePosList(distanceTable, pos);
        if (csList.size() < 0)
            return Dir.values()[RNG.nextInt() % Dir.DIR_COUNT.ordinal()];

        int m = 0;
        Dir findDir = Dir.DIR_COUNT;
        for (CoordinateStep cs : csList) {
            if (m < cs.distance || (m == cs.distance && currentDir == cs.relativeDir)) {
                m = cs.distance;
                findDir = cs.relativeDir;
            }
        }
        return findDir;
    }

    private boolean isReachable(ArrayList<Coordinate> snake, Coordinate destination) {
        int distance[][] = getTileDistanceFromPos(destination, snake);
        ArrayList<CoordinateStep> csList = getNextReachablePosList(distance, snake.get(0));
        for (CoordinateStep cs : csList) {
            if (cs.distance > 1)
                return true;
        }
        return false;
    }

    private Dir getRandomDir(ArrayList<Coordinate> snake, Dir currentDir) {
        int distanceTable[][] = getTileDistanceFromPos(snake.get(0), snake);
        Dir d = findMaxDistanceDir(distanceTable, snake.get(0), currentDir);
        return d;
    }

    public Dir getNextDir(ArrayList<Coordinate> snake, ArrayList<Coordinate> apples, Dir currentDir) {
        ArrayList<Coordinate> fakeSnake = new ArrayList<Coordinate>();
        fakeSnake.addAll(snake);
        Coordinate closestApple = getCloseApple(snake.get(0), apples);
        int distanceTable[][] = getTileDistanceFromPos(closestApple, snake);
        boolean canEat = canSimulate(distanceTable, fakeSnake, closestApple);
        if (!canEat) {
            canEat = canSimulate(distanceTable, fakeSnake, closestApple);
        }
        Dir nextDir = Dir.DIR_COUNT;
        if (canEat) {
            nextDir = findMinDistanceDir(distanceTable, snake.get(0), currentDir);
        } else {
            Coordinate snakeTail = snake.get(snake.size() - 1);
            boolean canFollowTrail = isReachable(snake, snakeTail);
            if (canFollowTrail) {
                // Generate distance table with snake tail position
                distanceTable = getTileDistanceFromPos(snakeTail, snake, apples);
                distanceTable[snakeTail.y][snakeTail.x] = DISTANCE_RIGID;
                nextDir = findMaxDistanceDir(distanceTable, snake.get(0), currentDir);
            } else {
                // get away from apple
                nextDir = getRandomDir(snake, currentDir);
            }
        }
        if (nextDir == Dir.DIR_COUNT) {
            // invalid dir
            nextDir = getRandomDir(snake, currentDir);
//			apples.set(0, new Coordinate(snake.get(0)));
//			snake.remove(0);
//			getNextDir(snake, apples, currentDir);
        }
        Log.i("snake", nextDir.toString());
        return nextDir;
    }
}
