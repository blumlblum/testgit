package com.imooc.imooc_wuziqi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.icu.util.Measure;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/8/10.
 */
public class wuziqiPanel extends View {
    private boolean misWhiteWinner;
    private boolean misBlackWinner;


    private int mWidthPanel;
    private Paint mpaint=new Paint();
    private float mLineHeight;
    private int MAX_LINE=10;
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float ratioPieceOfLineHeight=3*1.0f/4;
    private boolean mIsWhite=true;
    private ArrayList<Point>mWhiteArray=new ArrayList<>();
    private ArrayList<Point>mBlackArray=new ArrayList<>();
    private boolean isGameOver;
    private int MAX_COUNT_LINE=5;



    public wuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mpaint.setColor(0x88000000);
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStyle(Paint.Style.STROKE);
        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece=BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int Width=Math.min(widthSize,heightSize);
        if(widthMode==MeasureSpec.UNSPECIFIED){
            Width=heightSize;
        }else if(heightMode==MeasureSpec.UNSPECIFIED) {
            Width=widthSize;
        }
        setMeasuredDimension(Width,Width);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidthPanel=w;
        mLineHeight=mWidthPanel*1.0f/MAX_LINE;
        int PieceWidth=(int)(mLineHeight*ratioPieceOfLineHeight);
        mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,PieceWidth,PieceWidth,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,PieceWidth,PieceWidth,false);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean Whitewin=checkFiveInline(mWhiteArray);
        boolean Blackwin=checkFiveInline(mBlackArray);
        if(Whitewin||Blackwin){
            isGameOver=true;
            misWhiteWinner=Whitewin;
            String text=misWhiteWinner?"白棋胜利":"黑棋胜利";
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();


        }


    }

    private boolean checkFiveInline(List<Point> points) {
        for(Point p:points){
            int x=p.x;
            int y=p.y;
            boolean win=checkHorizontal(x,y,points);
            if(win)return true;
            win=checkVertical(x,y,points);
            if(win)return true;
            win=checkLeftDiagonal(x,y,points);
            if(win)return true;
            win=checkRightDiagonal(x,y,points);
            if(win)return true;
        }
        return false;

    }

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x-i,y))){
                count++;

            }else {
                break;
            }
        }
        if (count==MAX_COUNT_LINE){
            return true;
        }
        for(int i=1;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x+i,y))){
                count++;

            }else {
                break;
            }
        }

        return false;

    }
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x,y-i))){
                count++;

            }else {
                break;
            }
        }
        if (count==MAX_COUNT_LINE){
            return true;
        }
        for(int i=1;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x,y+i))){
                count++;

            }else {
                break;
            }
        }

        return false;

    }
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;

            }else {
                break;
            }
        }
        if (count==MAX_COUNT_LINE){
            return true;
        }
        for(int i=0;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;

            }else {
                break;
            }
        }

        return false;

    }
    private boolean checkRightDiagonal(int x, int y, List<Point> points) {
        int count=1;
        for(int i=1;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x-i,y-i))){
                count++;

            }else {
                break;
            }
        }
        if (count==MAX_COUNT_LINE){
            return true;
        }
        for(int i=0;i<MAX_COUNT_LINE;i++){
            if(points.contains(new Point(x+i,y+i))){
                count++;

            }else {
                break;
            }
        }

        return false;

    }

    private void drawPiece(Canvas canvas) {
        for(int i=0,n=mWhiteArray.size();i<n;i++)
        {

            Point whitePoint=mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,(whitePoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    ( whitePoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null );
        }
        for(int i=0,n=mBlackArray.size();i<n;i++)
        {

            Point blackPoint=mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,(blackPoint.x+(1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (blackPoint.y+(1-ratioPieceOfLineHeight)/2)*mLineHeight,null );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        if(isGameOver)return false;
        if(action==MotionEvent.ACTION_DOWN){
            int x=(int)event.getX();
            int y=(int)event.getY();
            Point p=getValidpoint(x,y);
           // Point p=new Point(x,y);
            if(mWhiteArray.contains(p)||mBlackArray.contains(p)){
                return false;
            }
            if(mIsWhite){
                mWhiteArray.add(p);
            }else {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite=!mIsWhite;
        }
        return  true;

       // return super.onTouchEvent(event);
    }

    private Point getValidpoint(int x, int y) {
        return new Point((int)(x/mLineHeight),(int)(y/mLineHeight));
    }

    private void drawBoard(Canvas canvas) {
        int w=mWidthPanel;
        float lineheight=mLineHeight;
        for(int i=0;i<MAX_LINE;i++){
            int startX=(int)(lineheight/2);
            int endX=(int)(w-lineheight/2);
            int Y=(int)((0.5+i)*lineheight);
            canvas.drawLine(startX,Y,endX,Y,mpaint);
            canvas.drawLine(Y,startX,Y,endX,mpaint);

        }
    }
    private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";





    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,isGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle=(Bundle)state;
            isGameOver=bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;


        }
        super.onRestoreInstanceState(state);
    }
    public void start(){
        mWhiteArray.clear();
        mBlackArray.clear();
        misWhiteWinner=false;
        invalidate();
    }

}
