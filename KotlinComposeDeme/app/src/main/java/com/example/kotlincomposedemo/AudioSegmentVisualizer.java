package com.example.kotlincomposedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

public class AudioSegmentVisualizer extends View {
    private static final String TAG = "AudioSegmentVisualizer";
    private List<AudioSegmentProcessor.Segment> segments;
    private float totalDuration = 0f;
    private final Paint paint;
    private final Paint textPaint;
    private final int[] segmentColors = {
        Color.rgb(255, 99, 71),   // 红色
        Color.rgb(255, 165, 0),   // 橙色
        Color.rgb(255, 215, 0),   // 黄色
        Color.rgb(50, 205, 50),   // 绿色
        Color.rgb(30, 144, 255),  // 蓝色
        Color.rgb(147, 112, 219), // 紫色
        Color.rgb(255, 182, 193), // 粉色
        Color.rgb(160, 82, 45)    // 棕色
    };

    public AudioSegmentVisualizer(Context context) {
        this(context, null);
    }

    public AudioSegmentVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setSegments(List<AudioSegmentProcessor.Segment> segments) {
        this.segments = segments;
        if (segments != null && !segments.isEmpty()) {
            totalDuration = segments.get(segments.size() - 1).endTime;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (segments == null || segments.isEmpty()) return;

        float width = getWidth();
        float height = getHeight();
        float segmentHeight = height * 0.8f;  // 留出空间显示文本
        float yOffset = (height - segmentHeight) / 2;

        // 绘制时间轴
        paint.setColor(Color.LTGRAY);
        canvas.drawLine(0, height - 20, width, height - 20, paint);

        // 绘制每个分段
        for (AudioSegmentProcessor.Segment segment : segments) {
            float startX = (segment.startTime / totalDuration) * width;
            float endX = (segment.endTime / totalDuration) * width;
            
            // 绘制分段矩形
            paint.setColor(segmentColors[segment.predictedClass % segmentColors.length]);
            RectF rect = new RectF(startX, yOffset, endX, yOffset + segmentHeight);
            canvas.drawRect(rect, paint);

            // 绘制类别标签
            String label = String.valueOf(segment.predictedClass);
            float textX = (startX + endX) / 2;
            float textY = yOffset + segmentHeight / 2;
            canvas.drawText(label, textX, textY, textPaint);

            // 绘制时间标记
            String timeLabel = String.format("%.1fs", segment.startTime);
            canvas.drawText(timeLabel, startX, height - 5, textPaint);
        }

        // 绘制最后一个时间标记
        String endTimeLabel = String.format("%.1fs", totalDuration);
        canvas.drawText(endTimeLabel, width, height - 5, textPaint);
    }
} 