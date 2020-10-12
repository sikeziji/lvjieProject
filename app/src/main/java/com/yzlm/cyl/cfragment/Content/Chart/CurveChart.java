package com.yzlm.cyl.cfragment.Content.Chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


public class CurveChart extends DrawLineChart {
    public CurveChart(Context context) {
        super(context);
    }

    public CurveChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void DrawLineCircle(Canvas canvas) {

    }

    @Override
    public void DrawLine(Canvas canvas) {
        Path mPath=new Path();
        for(int j = 0; j < mPoints.length; j++) {
            Point startp = mPoints[j];
            Point endp;
            if (j != mPoints.length - 1) {
                endp = mPoints[j + 1];
                int wt = (startp.x + endp.x) / 2;
                Point p3 = new Point();
                Point p4 = new Point();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;
                if (j == 0) {
                    mPath.moveTo(startp.x, startp.y);
                }
                /**
                 *添加一个立方贝塞尔曲线的最后一点,接近控制点
                 *(x1,y1)和(x2,y2),到最后(x3,y3)。如果没有移至()调用
                 *为这个轮廓,第一点是自动设置为(0,0)。
                 *
                 * @param x1的坐标1立方曲线控制点
                 * @param y1第一控制点的坐标一立方曲线
                 * @param x2上第二个控制点的坐标一立方曲线
                 * @param y2第二控制点的坐标一立方曲线
                 * @param x3的坐标三次曲线的终点
                 * @param y3终点坐标的三次曲线
                 *
                 */
                mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            }

        }
        /**这里直接使用了折线的画笔，你也可以在此类在定义一个画笔*/
        canvas.drawPath(mPath,getBrokenLinePaint());
    }

    //填充的曲线

    /*@Override
    public void DrawLine(Canvas canvas) {
        Path mPath = new Path();
        for (int j = 0; j < mPoints.length; j++) {
            Point startp = mPoints[j];
            Point endp;
            if (j != mPoints.length - 1) {
                endp = mPoints[j + 1];
                int wt = (startp.x + endp.x) / 2;
                Point p3 = new Point();
                Point p4 = new Point();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;
                if (j == 0) {
                    mPath.moveTo(startp.x, startp.y);
                }
                *//**
                 *添加一个立方贝塞尔曲线的最后一点,接近控制点
                 *(x1,y1)和(x2,y2),到最后(x3,y3)。如果没有移至()调用
                 *为这个轮廓,第一点是自动设置为(0,0)。
                 *
                 * @param x1的坐标1立方曲线控制点
                 * @param y1第一控制点的坐标一立方曲线
                 * @param x2上第二个控制点的坐标一立方曲线
                 * @param y2第二控制点的坐标一立方曲线
                 * @param x3的坐标三次曲线的终点
                 * @param y3终点坐标的三次曲线
                 *
                 *//*
                mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            } else {
                *//**连接到终点x,底部y*//*
                mPath.lineTo(startp.x, getNeedDrawHeight() + getBrokenLineTop());
                *//**连接到起点x,底部y*//*
                mPath.lineTo(mPoints[0].x, getNeedDrawHeight() + getBrokenLineTop());
                *//**连接到起点x,起点y*//*
                mPath.lineTo(mPoints[0].x, mPoints[0].y);
            }
        }
        Paint paint = getBrokenLinePaint();
        *//**修改画笔属性*//*
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, getBrokenLinePaint());
    }*/
}