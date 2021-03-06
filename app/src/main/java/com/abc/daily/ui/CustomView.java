package com.abc.daily.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomView extends FrameLayout {

    private Paint paint;
    private Path path;

    private static final int shadowRadius = 24;
    private static final int padding = shadowRadius;

    public CustomView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(padding , 0, 0, Color.parseColor("#0d000000"));

        path = new Path();

        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.addRoundRect(new RectF(padding, padding, getWidth() - padding, getHeight() - padding), 50, 50 ,Path.Direction.CCW);
        canvas.drawPath(path, paint);
    }
}
