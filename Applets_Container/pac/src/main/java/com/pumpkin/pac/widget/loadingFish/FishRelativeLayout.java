package com.pumpkin.pac.widget.loadingFish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pumpkin.pac.R;
import com.pumpkin.ui.util.ExtensionHelperKt;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FishRelativeLayout extends FrameLayout {
    private static final String TAG = "FRL";
    private Path mPath;
    //    private Paint mPaint;
    private View food;
    private final int foodLength = ExtensionHelperKt.getDpToPx(10);
    private final int middleP = foodLength / 2;
    private ImageView iv_fish;
    private FishDrawable fishDrawable;
    private float touchX;
    private float touchY;
    private ObjectAnimator animator;

    protected AtomicInteger runningNum = new AtomicInteger(0);
    private final StartState startState = new StartState(this);
    private final EndState endState = new EndState(this);
    private State currentState = endState;

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    private void start(float x, float y, boolean isForce) {
        currentState.start(x, y, isForce);
    }

    private void end() {
        currentState.end();
    }

    @Override
    public float getAlpha() {
        return alpha;
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    private float alpha;

    public FishRelativeLayout(Context context) {
        this(context, null);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        iv_fish = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iv_fish.setLayoutParams(layoutParams);
        fishDrawable = new FishDrawable();
        iv_fish.setImageDrawable(fishDrawable);
        addView(iv_fish);

        food = new View(context);
        food.setLayoutParams(new LayoutParams(foodLength, foodLength));
        food.setBackgroundResource(R.drawable.food_bg);
        foodP(fishDrawable.getMiddlePoint().x - middleP, fishDrawable.getMiddlePoint().y - middleP);
        addView(food);

        mPath = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        start(event.getX(), event.getY(), true);
        return super.onTouchEvent(event);
    }

    private void next() {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();

        final Random random = new Random();

        // TODO: 2023/8/7 随机延迟多少秒之后 进行相关的启动

        start(random.nextInt((int) width), random.nextInt((int) height), false);

    }

    private void go(float x, float y) {
        touchX = x;
        touchY = y;
        foodP(touchX - middleP, touchY - middleP);
        makeTrail();
    }

    private void foodP(float x, float y) {
        food.setX(x);
        food.setY(y);
    }

    private void makeTrail() {
        //鱼的重心相对坐标
        PointF fishRelativeMiddle = fishDrawable.getMiddlePoint();

        //鱼的绝对坐标 --- 起始点
        PointF fishMiddle = new PointF(fishRelativeMiddle.x + iv_fish.getX(), fishRelativeMiddle.y + iv_fish.getY());

        // 鱼头坐标 ---- 控制点一
        final PointF fishHead = new PointF(fishDrawable.getHeadPoint().x + iv_fish.getX(),
                fishDrawable.getHeadPoint().y + iv_fish.getY());

        //点击坐标 ----结束点
        PointF touch = new PointF(touchX, touchY);

        /**
         * 先用cos公式向量+三角函数算出AOB的度数
         * 控制点2在AOB的角平分线上（人为规定的）
         */
        //控制点2的坐标
        float angle = includeAngle(fishMiddle, fishHead, touch) / 2;
        float delta = includeAngle(fishMiddle, new PointF(fishMiddle.x + 1, fishMiddle.y), fishHead);
        PointF control2 = fishDrawable.calculatePoint(fishMiddle, fishDrawable.getHEAD_RADIUS() * 1.6f, Math.abs(delta - angle));
        mPath.reset();
        mPath.moveTo(fishMiddle.x - fishRelativeMiddle.x, fishMiddle.y - fishRelativeMiddle.y);
        mPath.cubicTo(fishHead.x - fishRelativeMiddle.x, fishHead.y - fishRelativeMiddle.y, control2.x, control2.y, touch.x - fishRelativeMiddle.x, touch.y - fishRelativeMiddle.y);
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        animator = ObjectAnimator.ofFloat(iv_fish, "x", "y", mPath);
        animator.setDuration(2500);
        //鱼游动时胃部加快摆动
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fishDrawable.setFrequance(1);
                end();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                fishDrawable.setFrequance(4);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            final PathMeasure pathMeasure = new PathMeasure(mPath, false);
            final float[] tan = new float[2];

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //执行了周期的多少百分比
                float fraction = animation.getAnimatedFraction();
                //得到路径的切线
                pathMeasure.getPosTan(pathMeasure.getLength() * fraction, null, tan);
                float angle = (float) Math.toDegrees(Math.atan2(-tan[1], tan[0]));
                fishDrawable.setFishMainAngle(angle);
            }
        });
        animator.start();
    }


    /**
     * @param A
     * @param O
     * @param B
     * @return
     */
    public float includeAngle(PointF A, PointF O, PointF B) {
        //0A*0B向量积
        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
        //OA*OB绝对值
        float OALength = (float) Math.sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y));
        float OBLength = (float) Math.sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y));
        float cosAOB = AOB / (OALength * OBLength);
        float angleAOB = (float) Math.toDegrees(Math.acos(cosAOB));

        float direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x);
        if (direction == 0) {
            if (AOB >= 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (direction > 0) {
                return -angleAOB;
            } else {
                return angleAOB;
            }
        }
    }

    /**
     * 运行状态机
     */
    abstract static class State {
        FishRelativeLayout fl;

        State(FishRelativeLayout fl) {
            this.fl = fl;
        }

        abstract void start(float x, float y, boolean isForce);

        abstract void end();
    }

    static class StartState extends State {
        StartState(FishRelativeLayout fl) {
            super(fl);
        }

        @Override
        void start(float x, float y, boolean isForce) {

            if (isForce || fl.runningNum.get() == 0) {
                fl.runningNum.incrementAndGet();
                fl.go(x, y);
            }
        }

        @Override
        void end() {
            fl.setCurrentState(fl.endState);
            fl.end();
        }
    }

    static class EndState extends State {
        EndState(FishRelativeLayout fl) {
            super(fl);
        }

        @Override
        void start(float x, float y, boolean isForce) {
            fl.setCurrentState(fl.startState);
            fl.start(x, y, isForce);
        }

        @Override
        void end() {
            int currentNum = fl.runningNum.decrementAndGet();
            Log.d(TAG, "end: current num is " + currentNum);
            if (currentNum == 0) {
                //自动 计算下标 下一个
                fl.next();
            }
        }
    }
}