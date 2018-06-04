package com.wuruoye.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * @Created : wuruoye
 * @Date : 2018/6/2 22:36.
 * @Description : 动画箭头
 */
public class ArrowView extends View implements ValueAnimator.AnimatorUpdateListener {
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_DURATION = 300;
    public static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    public static final float DEFAULT_SCALE = 0.6F;
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    private PointF[] mPs = new PointF[12];

    private Paint mPaint;
    private Path mPath;
    private ValueAnimator mAnimator;
    private float mScale;
    private int mDirection;

    private float mLength;
    private PointF fromX;
    private PointF fromY;
    private PointF fromZ;
    private PointF toX;
    private PointF toY;
    private PointF toZ;
    private PointF x;
    private PointF y;
    private PointF z;

    public ArrowView(Context context) {
        super(context);
        init();
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void change(int direction) {
        mDirection = direction;
        toX = mPs[direction * 3];
        toY = mPs[direction * 3 + 1];
        toZ = mPs[direction * 3 + 2];
        mAnimator.start();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeMiter();
        mPaint.setColor(DEFAULT_COLOR);

        mPath = new Path();

        mAnimator = new ValueAnimator();
        mAnimator.setFloatValues(0F, 1F);
        mAnimator.setDuration(DEFAULT_DURATION);
        mAnimator.setInterpolator(DEFAULT_INTERPOLATOR);
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fromX = toX;
                fromY = toY;
                fromZ = toZ;
                mLength = computeLength(fromX, fromZ);
            }
        });

        mScale = DEFAULT_SCALE;

        x = new PointF();
        y = new PointF();
        z = new PointF();

        for (int i = 0; i < 12; i++) {
            mPs[i] = new PointF();
        }
    }

    private void initLength(int length, int centerX, int centerY) {
        float b1 = length / 2 * mScale;
        float b2 = b1 / 2;
        float offset = b2 / 4;

        for (int i = 0; i < 2; i++) {
            int coefficient = i == 0 ? 1 : -1;

            // up and down
            mPs[6 * i].set(centerX - b1, centerY + (b2 - offset) * coefficient);
            mPs[6 * i + 1].set(centerX + b1, centerY + (b2 - offset) * coefficient);
            mPs[6 * i + 2].set(centerX, centerY - (b2 + offset) * coefficient);

            // left and right
            mPs[6 * i + 3].set(centerX + (b2 - offset) * coefficient, centerY + b1);
            mPs[6 * i + 4].set(centerX + (b2 - offset) * coefficient, centerY - b1);
            mPs[6 * i + 5].set(centerX - (b2 + offset) * coefficient, centerY);
        }

        x.set(mPs[mDirection]);
        y.set(mPs[mDirection + 1]);
        z.set(mPs[mDirection + 2]);
        fromX = mPs[mDirection];
        fromY = mPs[mDirection + 1];
        fromZ = mPs[mDirection + 2];
        mLength = computeLength(fromX, fromZ);
    }

    private float compute(float from, float to, float fraction) {
        return from + (to - from) * fraction;
    }

    private float computeLength(PointF x, PointF y) {
        return (float) Math.sqrt(Math.pow(x.x - y.x, 2) + Math.pow(x.y - y.y, 2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        mPath.moveTo(x.x, x.y);
        mPath.lineTo(y.x, y.y);
        mPath.lineTo(z.x, z.y);
        mPath.lineTo(x.x, x.y);

        canvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int length = h > w ? w : h;
        initLength(length, w / 2, h / 2);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
        float scale = 1F - Math.abs(value - 0.5F) * 2;
        float add = (mLength - computeLength(x, z)) * scale;
        if (mDirection == UP || mDirection == DOWN) {
            x.set(compute(fromX.x, toX.x, value), compute(fromX.y, toX.y, value));
            y.set(compute(fromY.x, toY.x, value), compute(fromY.y, toY.y, value));
            z.set(compute(fromZ.x, toZ.x, value), compute(fromZ.y, toZ.y, value));
            x.set(x.x - add, x.y);
            y.set(y.x + add, y.y);
        }else {
            x.set(compute(fromX.x, toX.x, value), compute(fromX.y, toX.y, value));
            y.set(compute(fromY.x, toY.x, value), compute(fromY.y, toY.y, value));
            z.set(compute(fromZ.x, toZ.x, value), compute(fromZ.y, toZ.y, value));
            x.set(x.x, x.y + add);
            y.set(y.x, y.y - add);
        }
        postInvalidate();
    }
}
