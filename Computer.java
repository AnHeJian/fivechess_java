package com.example.fivechess;

import android.graphics.Point;

import java.util.List;

/**
 * Created by 贺建安 on 2017/1/24.
 */

class Coord
{
    int x;
    int y;
    int score;
    public Coord()
    {
        x=y=score=0;
    }
    public void setScore(int mx,int my,int ms)
    {
        x=mx;
        y=my;
        score=ms;
    }
}

class ChessStyle
{
    static int FourBlack = 200;
    static int ThreeBlackNoW = 100;
    static int FourWhite = 250;
    static int ThreeWhiteNoB = 150;
}
public class Computer
{
    private int MAX_PIECE = 5;
    private int MAX_LINE = 16;
    public Point calcuMaxPoint(List<Point> mW,List<Point> mB)
    {
        int score = 0;
        Coord maxPoint = new Coord();

        for(int i = 0 ; i<MAX_LINE ; i++)
            for(int j = 0 ; j<MAX_LINE ; j++)
            {
               if(!mW.contains(new Point(i,j))&&!mB.contains(new Point(i,j)))
                {
                    score = calcuScore(i , j , mW , mB);
                    if (score > maxPoint.score)
                        maxPoint.setScore(i, j, score);
               }
            }

        return new Point(maxPoint.x,maxPoint.y);
    }

    //计算坐标（chess_x,chess_y）的分数
    private int calcuScore(int chess_x, int chess_y , List<Point> mW,List<Point> mB)
    {
        int x = 0, y = 0;
        int scoreOfXY = 0;
        int numOfBlack = 0;		//五元组中黑棋数量
        int numOfWhite = 0;		//五元组中白棋数量
        int numOfEmpty = 0;		//五元组中空棋数量

                               //统计 “|”方向
        for( y = chess_y < MAX_PIECE-1 ? 0 : chess_y-(MAX_PIECE-1) ; y<=chess_y ; y++)
        {
            for(int i = 0 ; i < MAX_PIECE && y+i < MAX_LINE ; i++)
            {
                if(mB.contains(new Point(chess_x,y+i)))
                    numOfBlack ++;
                else if(mW.contains(new Point(chess_x,y+i)))
                    numOfWhite ++;
                else
                    numOfEmpty++;
            }
            scoreOfXY += scoreOfFive(numOfBlack,numOfWhite,numOfEmpty);
            numOfBlack = 0;
            numOfWhite = 0;
            numOfEmpty = 0;
        }

                                        //统计“——”方向
        for(x = chess_x<MAX_PIECE-1 ? 0 : chess_x-(MAX_PIECE-1) ; x<=chess_x ; x++)
        {
            for(int i = 0 ; i < MAX_PIECE && x+i < MAX_LINE ; i++)
            {
                if(mB.contains(new Point(i+x,chess_y)))
                    numOfBlack ++;
                else if(mW.contains(new Point(i+x,chess_y)))
                    numOfWhite ++;
                else
                    numOfEmpty ++;
            }
            scoreOfXY += scoreOfFive(numOfBlack,numOfWhite,numOfEmpty);
            numOfBlack = 0;
            numOfWhite = 0;
            numOfEmpty = 0;
        }

                                                //统计“\"方向
        if (chess_x <= chess_y)
        {
            x = (chess_x >= (MAX_PIECE-1)) ? chess_x - (MAX_PIECE-1) : 0;
            y = chess_y - (chess_x - x);
        }
        else
        {
            y = (chess_y >= (MAX_PIECE-1)) ? chess_y - (MAX_PIECE-1) : 0;
            x = chess_x - (chess_y - y);
        }
        for (; x <= chess_x && y <= chess_y; x++, y++)
        {
            for (int i = 0; i < MAX_PIECE && (x + i) < MAX_LINE && (y + i) < MAX_LINE; i++)
            {
                if(mB.contains(new Point(i+x,i+y)))
                    numOfBlack ++;
                else if(mW.contains(new Point(i+x,i+y)))
                    numOfWhite ++;
                else
                    numOfEmpty ++;
            }
            scoreOfXY += scoreOfFive(numOfBlack,numOfWhite,numOfEmpty);
            numOfBlack = 0;
            numOfWhite = 0;
            numOfEmpty = 0;
        }


        //统计“/”方向
        if (chess_x <= ((MAX_LINE-1) - chess_y))
        {
            x = (chess_x >= (MAX_PIECE-1)) ? chess_x - (MAX_PIECE-1) : 0;
            y = chess_y - (x - chess_x);
        }
        else
        {
            y = (chess_y <= (MAX_LINE -MAX_PIECE)) ? chess_y + (MAX_PIECE-1) : MAX_LINE-1;
            x = chess_x - (y - chess_y);
        }
        for (; x <= chess_x && y >= chess_y; x++, y--)
        {
            for (int i = 0; i < MAX_PIECE && (x + i) < MAX_LINE && (y - i) >= 0; i++)
            {
                if(mB.contains(new Point(i+x,y-i)))
                    numOfBlack ++;
                else if(mW.contains(new Point(i+x,y-i)))
                    numOfWhite ++;
                else
                    numOfEmpty ++;
            }
            scoreOfXY += scoreOfFive(numOfBlack,numOfWhite,numOfEmpty);
            numOfBlack = 0;
            numOfWhite = 0;
            numOfEmpty = 0;
        }

        return scoreOfXY;
    }

    private int scoreOfFive(int numOfBlack, int numOfWhite, int numOfEmpty)
    {
        if ((numOfBlack + numOfWhite + numOfEmpty) < 5)
            return 0;
        if (numOfBlack == 0)
        {
            switch (numOfWhite)
            {
                case 1://W
                    return 15;
                case 2://WW
                    return 400;
                case 3://WWW
                    return 1800;
                case 4://WWWW
                    return 8000000;
                default:
                    return 0;
            }
        }
        else if (numOfWhite == 0)
        {
            switch (numOfBlack)
            {
                case 1://B
                    return 35;
                case 2://BB
                    return 800;
                case 3://BBB
                    return 15000;
                case 4://BBBB
                    return 1000000;
                default:
                    return 0;
            }
        }
        return 0;
    }


}
