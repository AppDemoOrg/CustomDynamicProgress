package com.abt.custom_dynamic_progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/7 0007.
 */
public class CustomSeekbar extends View {

    private final String TAG = CustomSeekbar.class.getSimpleName();
    private int mWidth, mHeight, mDownX, mDownY, mUpX, mUpY, mMoveX, mMoveY, mIconX;
    private int mPerWidth, mCircleRadius, mTextSize;
    private Paint mPaint, mTextPaint, mButtonPaint;
    private Bitmap mBitmap, mThumb, mSpot, mSpotOn;
    private float mScale = 0;
    private int mHotarea = 100;//点击的热区
    private int mCurSections = 0;
    private int mBitMapHeight = 38;//第一个点的起始位置起始，图片的长宽是76，所以取一半的距离
    private int mTextMove = 70;//字与下方点的距离，因为字体字体是40px，再加上10的间隔
    private int[] mColors = new int[] {0xffdf5600, 0x33000000, 0xffffffff};//进度条的橙色,进度条的灰色,字体的灰色
    private Canvas mCanvas;
    private ArrayList<String> mSectionTitle;
    private ResponseOnTouch mResponseOnTouch;

    public CustomSeekbar(Context context) {
        super(context);
    }

    public CustomSeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCurSections = 0;
        mBitmap = Bitmap.createBitmap(900, 900, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mThumb = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_point);
        mSpot = BitmapFactory.decodeResource(getResources(),R.drawable.yellow_point);
        mSpotOn = BitmapFactory.decodeResource(getResources(),R.drawable.yellow_point);
        mBitMapHeight = mThumb.getHeight()/2;
        mTextMove = mBitMapHeight +22;
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        mCircleRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);//锯齿不显示
        mPaint.setStrokeWidth(3);
        mTextPaint = new Paint(Paint.DITHER_FLAG);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xffb5b5b4);
        mButtonPaint = new Paint(Paint.DITHER_FLAG);
        mButtonPaint.setAntiAlias(true);
        //initData(null);
        mSectionTitle = new ArrayList<String>();
    }

    /**
     * 实例化后调用，设置bar的段数和文字
     */
    public void initData(ArrayList<String> section) {
        if (section != null) {
            mSectionTitle = section;
        } else {
            String[] str = new String[] {"0.6M", "1M", "2M", "3M", "4M", "5M", "6M"};
            mSectionTitle = new ArrayList<String>();
            for (int i = 0; i < str.length; i++) {
                mSectionTitle.add(str[i]);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = widthSize;
        float scaleX = widthSize / 1080;
        float scaleY = heightSize / 1920;
        mScale = Math.max(scaleX, scaleY);
        //控件的高度
        // mHeight = 185;
        mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        setMeasuredDimension(mWidth, mHeight);
        mWidth = mWidth - mBitMapHeight /2;
        mPerWidth = (mWidth - mSectionTitle.size()* mSpot.getWidth() - mThumb.getWidth()/2) / (mSectionTitle.size()-1);
        mPerWidth = mPerWidth + 2;
        mHotarea = mPerWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(0);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        mPaint.setAlpha(255);
        mPaint.setColor(mColors[1]);
        canvas.drawLine(mBitMapHeight, mHeight * 2 / 3, mWidth - mBitMapHeight - mSpotOn.getWidth() / 2, mHeight * 2 / 3, mPaint);
        int section = 0;
        while (section < mSectionTitle.size()) {
            if (section < mCurSections) {
                mPaint.setColor(mColors[0]);
                canvas.drawLine(mThumb.getWidth()/2 + section * mPerWidth + (section+1) * mSpotOn.getWidth(), mHeight * 2 / 3,
                        mThumb.getWidth()/2 + section * mPerWidth + (section+1) * mSpotOn.getWidth() + mPerWidth, mHeight * 2 / 3,mPaint);
                //canvas.drawBitmap(mSpotOn, mThumb.getWidth()/2 + section * mPerWidth + section * mSpotOn.getWidth(), mHeight * 2 / 3 - mSpotOn.getHeight()/2,mPaint);
            } else {
                mPaint.setAlpha(255);
                /*if (section == mSectionTitle.size()-1) {
                    canvas.drawBitmap(mSpot,  mWidth - mSpotOn.getWidth() - mBitMapHeight /2, mHeight * 2 / 3 - mSpot.getHeight() / 2, mPaint);
                } else {
                    canvas.drawBitmap(mSpot, mThumb.getWidth()/2 + section * mPerWidth + section * mSpotOn.getWidth(), mHeight * 2 / 3 - mSpot.getHeight() / 2, mPaint);
                }*/
            }

            // 画坐标X轴
            int start = 0;
            int toTheEnd = mSectionTitle.size()-1;
            mPaint.setColor(mColors[2]);
            //int startX = mThumb.getWidth()/2 + start * mPerWidth + (start+1) * mSpotOn.getWidth();
            //int endX = mThumb.getWidth()/2 + toTheEnd * mPerWidth + (toTheEnd+1) * mSpotOn.getWidth() + mPerWidth;
            int startX = start * mPerWidth + (start+1) * mSpotOn.getWidth()/2;
            int endX = toTheEnd * mPerWidth + (toTheEnd+1) * mSpotOn.getWidth();
            canvas.drawLine(startX, mHeight * 2 / 3, endX, mHeight * 2 / 3,mPaint);

            // 画标尺的数字：6M
            if (section == mSectionTitle.size()-1) {
                int x = mWidth - mSpotOn.getWidth()- mBitMapHeight /4 - mTextSize / 2;
                x = x+80;
                Log.d(TAG, "X = "+x);
                canvas.drawText(mSectionTitle.get(section), x, mHeight * 2 / 3 - mTextMove, mTextPaint);
            } else { // 画标尺的数字：0.6M 1M 2M 3M 4M 5M
                //mPerWidth = mPerWidth+2;
                Log.d(TAG, "mPerWidth = "+mPerWidth);
                int x = mThumb.getWidth()/2 + section * mPerWidth + section * mSpotOn.getWidth();
                int y = mHeight * 2 / 3 - mTextMove;
                Log.d(TAG, "ICONTextX = "+x);
                Log.d(TAG, "ICONTextY = "+y);
                canvas.drawText(mSectionTitle.get(section), x, y, mTextPaint);
            }
            section++;
        }

        //if (section == 0) {
        //int iconX = mThumb.getWidth() / 2 + section * mPerWidth + section * mSpotOn.getWidth();
        Log.d(TAG, "mIconX = "+mIconX);
        int x = mWidth - mSpotOn.getWidth()- mBitMapHeight /4 - mTextSize / 2;
        if (mIconX > x) {
            mIconX = x+50;
        }// 画坐标X轴上的标点
        if (mIconX < 0) {
            mIconX = 0;
        }
        canvas.drawBitmap(mSpot, mIconX, mHeight * 2 / 3 - mSpot.getHeight() / 2, mPaint);
        //}

        /*if (mCurSections == mSectionTitle.size()-1) {
            canvas.drawBitmap(mThumb, mWidth - mSpotOn.getWidth() - mBitMapHeight /2 - mThumb.getWidth() / 2,
                    mHeight * 2 / 3 - mBitMapHeight, mButtonPaint);
        } else {
            canvas.drawBitmap(mThumb, mThumb.getWidth()/2 + mCurSections * mPerWidth + mCurSections * mSpotOn.getWidth() - mThumb.getWidth()/4 ,
                    mHeight * 2 / 3 - mBitMapHeight, mButtonPaint);
        }*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mThumb = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_point);
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mIconX = mDownX;
                responseTouch(mDownX, mDownY);
                break;
            case MotionEvent.ACTION_MOVE:
                mThumb = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_point);
                mMoveX = (int) event.getX();
                mMoveY = (int) event.getY();
                mIconX = mMoveX;
                invalidate();
                //responseTouch(mMoveX, mMoveY);
                break;
            case MotionEvent.ACTION_UP:
                mThumb = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_point);
                mUpX = (int) event.getX();
                mUpY = (int) event.getY();
                responseTouch(mUpX, mUpY);
                mIconX = mUpX;
                if (null != mResponseOnTouch) {
                    mResponseOnTouch.onTouchResponse(mCurSections);
                }
                break;
        }
        return true;
    }

    private void responseTouch(int x, int y) {
        if (x <= mWidth - mBitMapHeight /2) {
            mCurSections = (x + mPerWidth / 3) / mPerWidth;
        } else {
            mCurSections = mSectionTitle.size() - 1;
        }
        invalidate();
    }

    //设置监听
    public void setmResponseOnTouch(ResponseOnTouch response) {
        mResponseOnTouch = response;
    }

    //设置进度
    public void setProgress(int progress) {
        mCurSections = progress;
        invalidate();
    }

    public interface ResponseOnTouch {
        public void onTouchResponse(int volume);
    }

}
