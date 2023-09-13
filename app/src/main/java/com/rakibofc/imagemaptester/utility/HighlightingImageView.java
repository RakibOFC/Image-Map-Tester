package com.rakibofc.imagemaptester.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.rakibofc.imagemaptester.R;
import com.rakibofc.imagemaptester.model.GlyphInfo;

import java.util.List;

public class HighlightingImageView extends AppCompatImageView {

    private Paint highlightPaint;
    private List<GlyphInfo> glyphInfoList;
    private float lastTouchX, lastTouchY;
    private OnRectClickListener clickListener;
    private boolean isRectFScaled = false;
    private int ayahNo = 0;

    public HighlightingImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HighlightingImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HighlightingImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        highlightPaint = new Paint();
        // highlightPaint.setColor(R.color.color_grey);
        highlightPaint.setColor(ContextCompat.getColor(context, R.color.black));
        highlightPaint.setAlpha(0);

        setClickable(true);
        setOnClickListener(this::handleClickEvent);
    }

    public void setHighlight(List<GlyphInfo> glyphInfoList) {
        this.glyphInfoList = glyphInfoList;
    }

    public void setTargetAyahNo(int targetAyahNo) {
        ayahNo = targetAyahNo;
        highlightPaint.setAlpha(60);
        invalidate();
    }

    public void setOnRectClickListener(OnRectClickListener listener) {
        clickListener = listener;
    }

    private void handleClickEvent(View v) {

        if (glyphInfoList != null && clickListener != null) {
            float x = lastTouchX;
            float y = lastTouchY;

            for (int i = 0; i < glyphInfoList.size(); i++) {

                GlyphInfo glyphInfo = glyphInfoList.get(i);
                if (getRectF(glyphInfo.minX, glyphInfo.maxX, glyphInfo.minY, glyphInfo.maxY).contains(x, y)) {

                    ayahNo = glyphInfo.ayahNumber;
                    clickListener.onRectClick(v, glyphInfo.suraNumber, ayahNo);
                }
            }
        }
    }

    private void scalingRectF() {

        if (!isRectFScaled) {

            Bitmap bitmap = drawableToBitmap(getDrawable());
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            int viewWidth = getWidth();
            int viewHeight = getHeight();

            float scaleX = (float) viewWidth / bitmapWidth;
            float scaleY = (float) viewHeight / bitmapHeight;

            for (GlyphInfo glyphInfo : glyphInfoList) {

                glyphInfo.minX *= scaleX;
                glyphInfo.minY *= scaleY;
                glyphInfo.maxX *= scaleX;
                glyphInfo.maxY *= scaleY;
            }
            isRectFScaled = true;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private RectF getRectF(float minX, float maxX, float minY, float maxY) {
        return new RectF(minX, minY, maxX, maxY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getDrawable() != null && glyphInfoList != null) {
            scalingRectF();

            for (int i = 0; i < glyphInfoList.size(); i++) {

                GlyphInfo glyphInfo = glyphInfoList.get(i);

                if (ayahNo == glyphInfo.ayahNumber) {
                    canvas.drawRect(getRectF(glyphInfo.minX, glyphInfo.maxX, glyphInfo.minY, glyphInfo.maxY), highlightPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        lastTouchX = event.getX();
        lastTouchY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            highlightPaint.setAlpha(60);
            invalidate();
            performClick();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            highlightPaint.setAlpha(0);
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public interface OnRectClickListener {
        void onRectClick(View v, int suraNo, int ayahNo);
    }
}