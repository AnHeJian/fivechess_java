package com.example.fivechess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 贺建安 on 2017/1/23.
 */

public class FiveChessPanel extends View
{
    private int mPanelWidth;
    private float mLineHeight;
    private boolean isRenji;
    private boolean isSoundOn;
    public int MAX_LINE = 16;

    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mWhitePieceNew;
    private Bitmap mBlackPiece;
    private Bitmap mBlackPieceNew;

    private  float pieceOflineheight = 4 * 1.0f / 5;//棋子大小比例

    private boolean mIsWhite = false;//白棋先行
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();//棋子坐标

    private boolean mIsGameOver;
    private boolean mIsWhiteWin;

    private rule over = new rule();
    private Computer computer = new Computer();

    private SoundPool sp;

    public boolean getmIsGameOver(){return mIsGameOver;}

    public FiveChessPanel(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setBackgroundColor(0x88d2ae8a);
        init();
    }

    private void init()
    {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.white);
        mWhitePieceNew = BitmapFactory.decodeResource(getResources(),R.drawable.whitenew);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.black);
        mBlackPieceNew = BitmapFactory.decodeResource(getResources(),R.drawable.blacknew);

        sp = new SoundPool(2,AudioManager.STREAM_MUSIC,10);
        sp.load(getContext(),R.raw.btndown,10);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //测量
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);
        if(widthMode == MeasureSpec.UNSPECIFIED)
        {
            width = heightSize;
        }else if(heightMode == MeasureSpec.UNSPECIFIED)
        {
            width = widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * pieceOflineheight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece , pieceWidth , pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece , pieceWidth , pieceWidth, false);
        mWhitePieceNew = Bitmap.createScaledBitmap(mWhitePieceNew , pieceWidth , pieceWidth, false);
        mBlackPieceNew = Bitmap.createScaledBitmap(mBlackPieceNew , pieceWidth , pieceWidth, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(mWhiteArray.isEmpty()&&mBlackArray.isEmpty()) mIsWhite = false;
        if(mIsGameOver) return false;

        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x,y);

            if(mWhiteArray.contains(p)||mBlackArray.contains(p))
            {
                return false;
            }

            if(isRenji)
            {
                mBlackArray.add(p);
                playSound();
                mWhiteArray.add(computer.calcuMaxPoint(mWhiteArray, mBlackArray));
                mIsWhite = !mIsWhite;
            }
            if(!isRenji)
            {
                if(!mIsWhite)
                {
                    mBlackArray.add(p);
                    playSound();
                } else
                {
                    mWhiteArray.add(p);
                    playSound();
                }
            }

            invalidate();//刷新
            mIsWhite = !mIsWhite;

        }
        return true;
    }
    public void playSound()
    {
        if(isSoundOn) {
            sp.play(1,1,1,0,0,1);
        }
        else return;
    }

    private Point getValidPoint(int x, int y)
    {//使落子点合法
        return new Point((int)(x / mLineHeight) , (int) (y / mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whitewin = over.checkFiveInline(mWhiteArray);
        boolean blackwin = over.checkFiveInline(mBlackArray);

        if (whitewin || blackwin) {
            mIsGameOver = true;
            mIsWhiteWin = whitewin;

            String text = mIsWhiteWin ? "白棋胜利" : "黑棋胜利";

            new AlertDialog.Builder(getContext()).setTitle("游戏结束").setMessage(text).
                    setNegativeButton("重来", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            reStart();
                        }
                    }).
                    setPositiveButton("返回",null).
                    show();

        }

    }


    private void drawPiece(Canvas canvas)
    {
        for(int i = 0;i<mWhiteArray.size();i++)
        {
            Point whitePoint = mWhiteArray.get(i);
            if(i == mWhiteArray.size()-1)
            {
                canvas.drawBitmap(mWhitePieceNew ,
                        (whitePoint.x + (1-pieceOflineheight)/2)*mLineHeight,
                        (whitePoint.y + (1-pieceOflineheight)/2)*mLineHeight , null);
            } else if (i < mWhiteArray.size()-1){
                canvas.drawBitmap(mWhitePiece,
                        (whitePoint.x + (1 - pieceOflineheight) / 2) * mLineHeight,
                        (whitePoint.y + (1 - pieceOflineheight) / 2) * mLineHeight, null);
            }
        }

        for(int i = 0;i<mBlackArray.size();i++)
        {
            Point blackPoint = mBlackArray.get(i);
            if(i == mBlackArray.size()-1) {
                canvas.drawBitmap(mBlackPieceNew,
                        (blackPoint.x + (1 - pieceOflineheight) / 2) * mLineHeight,
                        (blackPoint.y + (1 - pieceOflineheight) / 2) * mLineHeight, null);
            }else if(i<mBlackArray.size()-1)
            {
                canvas.drawBitmap(mBlackPiece ,
                        (blackPoint.x + (1-pieceOflineheight)/2)*mLineHeight,
                        (blackPoint.y + (1-pieceOflineheight)/2)*mLineHeight , null);
            }
        }
    }

    private void drawBoard(Canvas canvas)
    {//绘制棋盘
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for(int i = 0;i<MAX_LINE;i++)
        {//绘制线
            int startX = (int)(lineHeight / 2);
            int endX = (int) (w - lineHeight/2);
            int y = (int) ((0.5 + i) * lineHeight);

            canvas.drawLine(startX , y , endX , y , mPaint);//横线
            canvas.drawLine(y , startX , y , endX , mPaint);//竖线
        }
    }

    //再来一局
    public void reStart()
    {
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver = false;
        mIsWhiteWin = false;
        invalidate();
    }

    public void regret()
    {
        if(mIsGameOver) return;
        if(!mIsWhite&&!(mWhiteArray.isEmpty()))
        {
            mWhiteArray.remove(mWhiteArray.size() - 1);
            mIsWhite = !mIsWhite;
        }
        else if(mIsWhite&&!(mBlackArray.isEmpty()))
        {
            mBlackArray.remove(mBlackArray.size() - 1);
            mIsWhite = !mIsWhite;
        }


        invalidate();
    }

    //棋子位置的存储与恢复
    private static final String INSTANCE = "instance" ;
    private static final String INSTANCE_GAME_OVER = "instance_game_over" ;
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array" ;
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array" ;

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if(state instanceof Bundle)
        {
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }


    public void setSoundSetting(int choice)
    {
        if(choice == 0) isSoundOn = true;
        else isSoundOn = false;
    }

    public void setisRenji(boolean isRenji) {
        this.isRenji = isRenji;
    }
}
