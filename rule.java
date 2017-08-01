package com.example.fivechess;

import android.graphics.Point;

import java.util.List;

/**
 * Created by 贺建安 on 2017/1/24.
 */

public class rule
{
    private int MAX_PIECE = 5;
    public boolean checkFiveInline(List<Point> points)
    {
        for(Point p : points)
        {
            int x = p.x;
            int y = p.y;

            boolean win = checkHorizontal(x,y,points);
            if(win) return true;
            win = checkVertical(x,y,points);
            if(win) return true;
            win = checkLeftDiagonal(x,y,points);
            if(win) return true;
            win = checkRightDiagnal(x,y,points);
            if(win) return true;
        }
        return false;
    }

    //判断右斜是否五子
    private boolean checkRightDiagnal(int x, int y, List<Point> points)
    {
        int count = 1;
        //rightup
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x+i,y-i)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        //leftdown
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x-i,y+i)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        return false;
    }

    ////判断左斜是否五子
    private boolean checkLeftDiagonal(int x, int y, List<Point> points)
    {
        int count = 1;
        //leftup
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x-i,y-i)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        //rightdown
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x+i,y+i)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        return false;
    }

    //判断竖向是否五子
    private boolean checkVertical(int x, int y, List<Point> points)
    {
        int count = 1;
        //down
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x,y-i)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        //up
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x,y+i)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        return false;
    }

    //判断横向是否五子
    private boolean checkHorizontal(int x, int y, List<Point> points)
    {
        int count = 1;
        //left
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x-i,y)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        //right
        for(int i=1 ; i<MAX_PIECE ; i++)
        {
            if(points.contains(new Point(x+i,y)))
                count++;
            else
                break;
        }
        if(count == MAX_PIECE) return true;

        return false;
    }
}
