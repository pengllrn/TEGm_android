package com.pengllrn.tegm.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BrokenLineChartView extends View {
    private Paint paint;

    //像素点
    // X轴原点坐标
    private int XPoint = 50;
    // Y轴原点坐标
    private int YPonit = 50;
    // Y轴的长度
    private int YScale = 800;
    // X轴的长度
    private int XScale = 1000;
    // Y轴的单位长度
    private int YSPace = 100;
    // X轴的单位长度
    private int XSPace = 100;


    private int leftrightlines;

    private boolean isOne;

    private ArrayList<Float> listX = new ArrayList<>();
    private ArrayList<Float> listY = new ArrayList<>();
    private int count = 1;
    private int number = 1; // 最大10

    private boolean isFinish;
    private List<String> date;
    private List<Double> score;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (number < 50) {
                number++;
                invalidate();
                handler.sendEmptyMessageDelayed(1, 80);
            } else if (count < leftrightlines - 1) {
                number = 1;
                count++;
                invalidate();
                handler.sendEmptyMessageDelayed(1, 80);
            } else {
                isFinish = true;
                invalidate();
            }
        }
    };

    public BrokenLineChartView(Context context) {
        super(context);
    }

    public BrokenLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BrokenLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initXY(canvas);
        getPaint().reset();
        /**
         * 外框线
         */
        // 设置颜色
        getPaint().setColor(Color.parseColor("#A5A5A5"));
        // 设置宽度
        getPaint().setStrokeWidth(2);
        // 线的坐标点 （四个为一条线）,画两条连续的线，分别便是Y轴和X轴
        float[] pts = {XPoint, YPonit - 20, XPoint, YScale, XPoint, YScale, XScale + 20, YScale};
        // 画线
        canvas.drawLines(pts, getPaint());

        /**
         * 箭头
         */
        // 通过路径画三角形
        Path path = new Path();
        getPaint().setStyle(Paint.Style.FILL);// 设置为空心
        path.moveTo(XPoint - 5, YPonit - 20);// 此点为多边形的起点
        path.lineTo(XPoint + 5, YPonit - 20);
        path.lineTo(XPoint, YPonit - 35);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, getPaint());
        // 第二个箭头
        path.moveTo(XScale + 20, YScale - 5);// 此点为多边形的起点
        path.lineTo(XScale + 20, YScale + 5);
        path.lineTo(XScale + 35, YScale);
        canvas.drawPath(path, getPaint());

        /**
         *  中间虚线
         */
        int updownlines = (YScale - YPonit) / YSPace;
        float[] pts2 = new float[(5 + leftrightlines) * 4];
        // 计算位置
        getPaint().setColor(Color.BLACK);
        getPaint().setTextSize(25);
        canvas.drawText("0", XPoint - 25, YScale + 10, getPaint());
        for (int i = 0; i < 5; i++) {
            float x1 = XPoint;
            float y1 = YScale - (i + 1) * YSPace;
            float x2 = XScale;
            float y2 = YScale - (i + 1) * YSPace;
            pts2[i * 4] = x1;
            pts2[i * 4 + 1] = y1;
            pts2[i * 4 + 2] = x2;
            pts2[i * 4 + 3] = y2;
            if (i < 4) {
                canvas.drawText(String.valueOf(i * 20 + 20), x1 - 35, y1 + 10, getPaint());
            } else {
                canvas.drawText(String.valueOf(i * 20 + 20), x1 - 45, y1 + 10, getPaint());
            }
        }
        // 计算位置
        for (int i = 0; i < leftrightlines; i++) {
            float x1 = XPoint + (i + 1) * XSPace;
            float y1 = YPonit;
            float x2 = XPoint + (i + 1) * XSPace;
            float y2 = YScale;
            pts2[(i + updownlines) * 4] = x1;
            pts2[(i + updownlines) * 4 + 1] = y1;
            pts2[(i + updownlines) * 4 + 2] = x2;
            pts2[(i + updownlines) * 4 + 3] = y2;
            canvas.drawText(date.get(i), x2 - 25, y2 + 30, getPaint());
        }
        getPaint().setColor(Color.parseColor("#E0E0E0"));
        getPaint().setStrokeWidth(1);
        canvas.drawLines(pts2, getPaint());

        if (isOne) {
            // 线的路径
            Path path2 = new Path();
            // 共几个转折点
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    path2.moveTo(listX.get(i), listY.get(i));
                } else {
                    path2.lineTo(listX.get(i), listY.get(i));
                }
            }
            // 上一个点  减去 下一个点的位置 计算中间点位置
            path2.lineTo(listX.get(count - 1) + (listX.get(count) - listX.get(count - 1)) / 50 * number,
                    listY.get(count - 1) + (listY.get(count) - listY.get(count - 1)) / 50 * number);
            getPaint().setColor(Color.parseColor("#06c2f3"));
            getPaint().setStrokeWidth(3);
            getPaint().setStyle(Paint.Style.STROKE);// 设置为空心
            canvas.drawPath(path2, getPaint());

            path2.lineTo(listX.get(count - 1) + (listX.get(count) - listX.get(count - 1)) / 50 * number, YScale);
            path2.lineTo(listX.get(0), YScale);
            path2.lineTo(listX.get(0), listY.get(0));
            getPaint().setStyle(Paint.Style.FILL);// 设置为空心
            canvas.drawPath(path2, getShadeColorPaint());
            getPaint().reset();
            // 画出转折点圆圈
            for (int i = 0; i < count; i++) {
                // 画外圆
                getPaint().setColor(Color.parseColor("#06c2f3"));
                getPaint().setStyle(Paint.Style.FILL);// 设置为空心
                canvas.drawCircle(listX.get(i), listY.get(i), 7, getPaint());
                // 画中心点为白色
                getPaint().setColor(Color.WHITE);
                getPaint().setStyle(Paint.Style.FILL);
                canvas.drawCircle(listX.get(i), listY.get(i), 4, getPaint());
            }
            if (isFinish) {
                getPaint().setColor(Color.parseColor("#06c2f3"));
                getPaint().setStyle(Paint.Style.FILL);// 设置为空心
                canvas.drawCircle(listX.get(count), listY.get(count), 7, getPaint());
                getPaint().setColor(Color.WHITE);
                getPaint().setStyle(Paint.Style.FILL);
                canvas.drawCircle(listX.get(count), listY.get(count), 4, getPaint());
            }
            handler.sendEmptyMessage(1);
        }
    }

    private void initXY(Canvas canvas) {
        YScale = canvas.getHeight() - YPonit * 2;
        YSPace = YScale / 6;
        XScale = canvas.getWidth() - XPoint * 2;
        XSPace = XScale / (leftrightlines + 1);
        //leftrightlines=10;
        for (int i = 0; i < leftrightlines; i++) {

            float x1 = XPoint + (i + 1) * XSPace;
            listX.add(x1);
        }
        for (int i = 0; i < leftrightlines; i++) {
            double t = score.get(i) * (YScale - YSPace);
            float y1 = YScale - (float) t;
            listY.add(y1);
        }
    }

    public void drawBrokenLine(List<String> date, List<Double> score) {

        isOne = true;
        number = 1;
        count = 1;
        isFinish = false;

        this.date = date;
        this.score = score;
        leftrightlines = date.size();
        invalidate();
    }

    // 获取笔
    private Paint getPaint() {
        if (paint == null)
            paint = new Paint();
        return paint;
    }

    // 修改笔的颜色
    private Paint getShadeColorPaint() {
        Shader mShader = new LinearGradient(XScale / 2, YPonit, XScale / 2, YScale,
                new int[]{Color.parseColor("#06c2f3"), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        getPaint().setShader(mShader);
        return getPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub 点击效果没有写了
        System.out.println("==========" + event.getX() + "===" + event.getY());
        return super.onTouchEvent(event);
    }
}