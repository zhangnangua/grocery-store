/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.howie.snake;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;

/**
 * SnakeView: implementation of a simple game of Snake
 */
@SuppressLint("NewApi")
public class SnakeView extends TileView implements SurfaceHolder.Callback,
        Runnable {

    private static final String TAG = "SnakeView";

    /**
     * Current mode of application: READY to run, RUNNING, or you have already
     * lost. static final ints are used instead of an enum for performance
     * reasons.
     */
    public int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    public static boolean g_autorun = true;

    private int mScreenWidth;
    private int mScreenHeight;

    /**
     * Current direction the snake is headed.
     */
    private Dir mDirection = Dir.UP;
    private Dir mNextDirection = Dir.UP;

    public Dir getDirection() {
        return mDirection;
    }

    public Dir getNextDirection() {
        return mNextDirection;
    }

    public void setNextDirection(Dir dir) {
        mNextDirection = dir;
    }

    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    private static final int RED_STAR = 1;
    private static final int YELLOW_STAR = 2;
    private static final int GREEN_STAR = 3;
    private static final int GRAY_STAR = 4;

    /**
     * mScore: used to track the number of apples captured mMoveDelay: number of
     * milliseconds between snake movements. This will decrease as apples are
     * captured.
     */
    private long mScore = 0;
    private long mMoveInterval = 1;
    private int mSpeedSwitchFrames = 5;
    /**
     * mLastMove: tracks the absolute time when the snake last moved, and is
     * used to determine if a move should be made based on mMoveDelay.
     */
    private long mFrameCounter;

    /**
     * mSnakeTrail: a list of Coordinates that make up the snake's body
     * mAppleList: the secret location of the juicy apples the snake craves.
     */
    private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();
    private ArrayList<Control> mControls = new ArrayList<Control>();
    private boolean mDpad4Way;
    private static final float DPAD_DEADZONE_VALUES[] = {0.1f, 0.14f, 0.1667f,
            0.2f, 0.25f};
    private static final int DPAD_4WAY[] = {Keycodes.GAMEPAD_LEFT,
            Keycodes.GAMEPAD_UP, Keycodes.GAMEPAD_RIGHT, Keycodes.GAMEPAD_DOWN};
    private float mDpadDeadZone = DPAD_DEADZONE_VALUES[2];

    /**
     * Everyone needs a little randomness in their life
     */
    private static final Random RNG = new Random();

    private static Commander mCommander = new Commander();
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;

    private I i;

    /**
     * Constructs a SnakeView based on inflation from XML
     *
     * @param context
     * @param attrs
     */
    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView();
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSnakeView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCommander.setRows(mYTileCount);
        mCommander.setColumns(mXTileCount);
        mScreenWidth = w;
        mScreenHeight = h;
    }

    private void initSnakeView() {
        mSurfaceHolder = getHolder();// �õ�SurfaceHolder����
        mSurfaceHolder.addCallback(this);// ע��SurfaceHolder
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);// ������Ļ����

        Resources r = this.getContext().getResources();
        mScreenWidth = r.getDisplayMetrics().widthPixels;
        mScreenHeight = r.getDisplayMetrics().heightPixels;

        resetTiles(5);
        loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
        loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
        loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
        loadTile(GRAY_STAR, r.getDrawable(R.drawable.graystar));

        Control dpadControl = createControl(R.drawable.dpad);
        // SharedPreferences prefs =
        // PreferenceManager.getDefaultSharedPreferences(this.getContext());
        for (Control c : mControls)
            c.load(r, 3f, 3f);

        dpadControl.move((mScreenWidth - dpadControl.getWidth()) / 2, (mScreenHeight - dpadControl.getHeight()) / 2);

        mFrameCounter = 0;
        mCommander.setRows(mYTileCount);
        mCommander.setColumns(mXTileCount);
    }

    private void initNewGame() {
        mSnakeTrail.clear();
        mAppleList.clear();

        // For now we're just going to load up a short default eastbound snake
        // that's just turned north

        mSnakeTrail.add(new Coordinate(7, 7));
        mSnakeTrail.add(new Coordinate(6, 7));
        mSnakeTrail.add(new Coordinate(5, 7));
        mSnakeTrail.add(new Coordinate(4, 7));
        mSnakeTrail.add(new Coordinate(3, 7));
        mSnakeTrail.add(new Coordinate(2, 7));
        mNextDirection = Dir.UP;

        // Two apples to start with
        for (int i = 0; i < 1; i++) {
            addRandomApple();
        }

        mScore = 0;
        mSpeedSwitchFrames = 5;
        mMoveInterval = 5;
    }

    /**
     * Given a ArrayList of coordinates, we need to flatten them into an array
     * of ints before we can stuff them into a map for flattening and storage.
     *
     * @param cvec : a ArrayList of Coordinate objects
     * @return : a simple array containing the x/y values of the coordinates as
     * [x1,y1,x2,y2,x3,y3...]
     */
    private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
        int count = cvec.size();
        int[] rawArray = new int[count * 2];
        for (int index = 0; index < count; index++) {
            Coordinate c = cvec.get(index);
            rawArray[2 * index] = c.x;
            rawArray[2 * index + 1] = c.y;
        }
        return rawArray;
    }

    /**
     * Save game state so that the user does not lose anything if the game
     * process is killed while we are in the background.
     *
     * @return a Bundle with this view's state
     */
    public Bundle saveState() {
        Bundle map = new Bundle();

        map.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
        map.putInt("mDirection", mDirection.ordinal());
        map.putInt("mNextDirection", mNextDirection.ordinal());
        map.putLong("mMoveDelay", Long.valueOf(mMoveInterval));
        map.putLong("mScore", Long.valueOf(mScore));
        map.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

        return map;
    }

    /**
     * Given a flattened array of ordinate pairs, we reconstitute them into a
     * ArrayList of Coordinate objects
     *
     * @param rawArray : [x1,y1,x2,y2,...]
     * @return a ArrayList of Coordinates
     */
    private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
        ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

        int coordCount = rawArray.length;
        for (int index = 0; index < coordCount; index += 2) {
            Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
            coordArrayList.add(c);
        }
        return coordArrayList;
    }

    /**
     * Restore game state if our process is being relaunched
     *
     * @param icicle a Bundle containing the game state
     */
    public void restoreState(Bundle icicle) {
        setMode(PAUSE);

        mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
        mDirection = Dir.values()[icicle.getInt("mDirection")];
        mNextDirection = Dir.values()[icicle.getInt("mNextDirection")];
        mMoveInterval = icicle.getLong("mMoveDelay");
        mScore = icicle.getLong("mScore");
        mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
    }

    /*
     * handles key events in the game. Update the direction our snake is
     * traveling based on the DPAD. Ignore events that would cause the snake to
     * immediately turn back on itself.
     *
     * (non-Javadoc)
     *
     * @see android.view.View#onKeyDown(int, android.os.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return super.onKeyDown(keyCode, msg);
    }

    /**
     * Sets the TextView that will be used to give information (such as "Game
     * Over" to the user.
     *
     * @param newView
     */
    public void setI(I i) {
        this.i = i;
    }

    /**
     * Updates the current mode of the application (RUNNING or PAUSED or the
     * like) as well as sets the visibility of textview for notification
     *
     * @param newMode
     */
    public void setMode(int newMode) {
        int oldMode = mMode;
        mMode = newMode;

        if (newMode == LOSE) {
            initNewGame();
        }
        if (i != null) {
            i.model(newMode);
        }
    }

    /**
     * Selects a random location within the garden that is not currently covered
     * by the snake. Currently _could_ go into an infinite loop if the snake
     * currently fills the garden, but we'll leave discovery of this prize to a
     * truly excellent snake-player.
     */
    private void addRandomApple() {
        Coordinate newCoord = new Coordinate(0, 0);
        ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();
        for (int i = 1; i < mYTileCount - 1; i += 1) {
            for (int j = 1; j < mXTileCount - 1; j += 1) {
                // Make sure it's not already under the snake
                newCoord.x = j;
                newCoord.y = i;
                boolean collision = false;
                int snakelength = mSnakeTrail.size();
                for (int index = 0; index < snakelength; index++) {
                    if (mSnakeTrail.get(index).equals(newCoord)) {
                        collision = true;
                    }
                }
                if (!collision)
                    coordinateList.add(new Coordinate(newCoord));
            }
        }

        if (coordinateList.size() > 0) {
            newCoord = coordinateList.get(RNG.nextInt(coordinateList.size()));
            if (newCoord == null) {
                Log.e(TAG, "Somehow ended up with a null newCoord!");
            }
            mAppleList.add(newCoord);
        }
    }

    /**
     * Handles the basic update loop, checking to see if we are in the running
     * state, determining if a move should be made, updating the snake's
     * location.
     */
    public void update() {
        if (mMode == RUNNING) {
            if (mFrameCounter < mMoveInterval) {
                mFrameCounter += 1;
                return;
            }

            clearTiles();
            updateWalls();
            updateSnake();
            updateApples();

            if (g_autorun) {
                SnakeView.this.setNextDirection(mCommander.getNextDir(mSnakeTrail,
                        mAppleList, SnakeView.this.getDirection()));
            }

            mFrameCounter = 0;
        }

    }

    /**
     * Draws some walls.
     */
    private void updateWalls() {
        for (int x = 0; x < mXTileCount; x++) {
            setTile(GRAY_STAR, x, 0);
            setTile(GRAY_STAR, x, mYTileCount - 1);
        }
        for (int y = 1; y < mYTileCount - 1; y++) {
            setTile(GRAY_STAR, 0, y);
            setTile(GRAY_STAR, mXTileCount - 1, y);
        }
    }

    /**
     * Draws some apples.
     */
    private void updateApples() {
        for (Coordinate c : mAppleList) {
            setTile(GREEN_STAR, c.x, c.y);
        }
    }

    private boolean isSnakeBody(Coordinate pos) {
        int snakelength = mSnakeTrail.size();
        for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
            Coordinate c = mSnakeTrail.get(snakeindex);
            if (c.equals(pos)) {

                return true;
            }
        }
        return false;
    }

    /**
     * Figure out which way the snake is going, see if he's run into anything
     * (the walls, himself, or an apple). If he's not going to die, we then add
     * to the front and subtract from the rear in order to simulate motion. If
     * we want to grow him, we don't subtract from the rear.
     */
    private void updateSnake() {
        boolean growSnake = false;

        // grab the snake by the head
        Coordinate head = mSnakeTrail.get(0);
        Coordinate newHead = new Coordinate(head);

        mDirection = mNextDirection;

        switch (mDirection) {
            case RIGHT: {
                newHead = new Coordinate(head.x + 1, head.y);
                break;
            }
            case LEFT: {
                newHead = new Coordinate(head.x - 1, head.y);
                break;
            }
            case UP: {
                newHead = new Coordinate(head.x, head.y - 1);
                break;
            }
            case DOWN: {
                newHead = new Coordinate(head.x, head.y + 1);
                break;
            }
            default:
                break;
        }

        // Collision detection
        // For now we have a 1-square wall around the entire arena
        if ((newHead.x < 1) || (newHead.y < 1) || (newHead.x > mXTileCount - 2)
                || (newHead.y > mYTileCount - 2) || isSnakeBody(newHead)) {
            setMode(LOSE);
            return;

        }

        // Look for apples
        int applecount = mAppleList.size();
        for (int appleindex = 0; appleindex < applecount; appleindex++) {
            Coordinate c = mAppleList.get(appleindex);
            if (c.equals(newHead)) {
                mAppleList.remove(c);
                addRandomApple();

                mScore++;
                if (mScore % 3 == 0) {
                    mSpeedSwitchFrames += 1;
                }
                if (mScore % mSpeedSwitchFrames == 0) {
                    // mMoveInterval -= 1;
                    // if(mMoveInterval < 1)
                    // {
                    // mMoveInterval = 1;
                    // }
                }

                growSnake = true;
            }
        }

        // push a new head onto the ArrayList and pull off the tail
        mSnakeTrail.add(0, newHead);
        // except if we want the snake to grow
        if (!growSnake) {
            mSnakeTrail.remove(mSnakeTrail.size() - 1);
        }

        int index = 0;
        for (Coordinate c : mSnakeTrail) {
            if (index == 0) {
                setTile(RED_STAR, c.x, c.y);
            } else {
                setTile(YELLOW_STAR, c.x, c.y);
            }
            index++;
        }

    }

    public void drawControl(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAlpha(128);

        for (Control c : mControls)
            c.draw(canvas, paint);
    }

    private static class Control {
        private int resId;
        private boolean hidden;
        private boolean disabled;
        private Bitmap bitmap;
        private RectF bounds = new RectF();

        Control(int r) {
            resId = r;
        }

        final float getX() {
            return bounds.left;
        }

        final float getY() {
            return bounds.top;
        }

        final int getWidth() {
            return bitmap.getWidth();
        }

        final int getHeight() {
            return bitmap.getHeight();
        }

//		final boolean isEnabled() {
//			return !disabled;
//		}
//
//		final void hide(boolean b) {
//			hidden = b;
//		}
//
//		final void disable(boolean b) {
//			disabled = b;
//		}
//
//		final boolean hitTest(float x, float y) {
//			return bounds.contains(x, y);
//		}

        final void move(float x, float y) {
            bounds.set(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
        }

        final void load(Resources res, float sx, float sy) {
            bitmap = ((BitmapDrawable) res.getDrawable(resId)).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (sx * bitmap.getWidth()),
                    (int) (sy * bitmap.getHeight()), true);
        }

//		final void reload(Resources res, int id) {
//			int w = bitmap.getWidth();
//			int h = bitmap.getHeight();
//			bitmap = ((BitmapDrawable) res.getDrawable(id)).getBitmap();
//			bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
//		}

        final void draw(Canvas canvas, Paint paint) {
            if (!hidden && !disabled)
                canvas.drawBitmap(bitmap, bounds.left, bounds.top, paint);
        }
    }

//	private static float getControlScale(SharedPreferences prefs) {
//		String value = prefs.getString("vkeypadSize", null);
//		if ("small".equals(value))
//			return 1.0f;
//		if ("large".equals(value))
//			return 1.33333f;
//		return 1.2f;
//	}

    private Control createControl(int resId) {
        Control c = new Control(resId);
        mControls.add(c);
        return c;
    }

    private int get4WayDirection(float x, float y) {
        x -= 0.5f;
        y -= 0.5f;

        if (Math.abs(x) >= Math.abs(y))
            return (x < 0.0f ? 0 : 2);
        return (y < 0.0f ? 1 : 3);
    }

    private int getDpadStates(float x, float y) {
        if (mDpad4Way)
            return DPAD_4WAY[get4WayDirection(x, y)];

        final float cx = 0.5f;
        final float cy = 0.5f;
        int states = 0;

        if (x < cx - mDpadDeadZone)
            states |= Keycodes.GAMEPAD_LEFT;
        else if (x > cx + mDpadDeadZone)
            states |= Keycodes.GAMEPAD_RIGHT;
        if (y < cy - mDpadDeadZone)
            states |= Keycodes.GAMEPAD_UP;
        else if (y > cy + mDpadDeadZone)
            states |= Keycodes.GAMEPAD_DOWN;

        return states;
    }

    public void run() {
        try {
            while (true) {
                update();

                mCanvas = mSurfaceHolder.lockCanvas();
                if (mCanvas != null) {
                    render(mCanvas);
                    drawControl(mCanvas);
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
                Thread.sleep(1000 / 60);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                try {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(this).start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
