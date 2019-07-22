package com.cactusfromhell.simple_drawing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min
import android.view.View
import android.widget.LinearLayout
import java.io.File
import android.graphics.Bitmap


class Signature: View {

    private var canvasSave: Canvas? = null
    private var canvasLL: LinearLayout
    private val paint = Paint()
    private var bitmap: Bitmap? = null
    private val STROKE_WIDTH = 5f;
    private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private val path = Path()
    private var lastTouchX: Float = 0.0f
    private var lastTouchY: Float = 0.0f
    private val RectF = android.graphics.RectF()
    private val dirtyRect = android.graphics.RectF()


    companion object {
        private var instance : Signature? = null

        fun getInstance(context: Context, canvasLL: LinearLayout?): Signature = synchronized(this) {
            if (instance == null && canvasLL != null)
                instance = Signature(context, null, canvasLL!!)
            return instance!!
        }

        fun clearInstance() {
            instance = null
        }
    }


    private constructor(context: Context, attrs: AttributeSet?, canvasLL: LinearLayout) : super(context, attrs) {
        this.canvasLL = canvasLL
        paint.isAntiAlias = true
        paint.color = Color.YELLOW
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = STROKE_WIDTH
    }



    @SuppressLint("WrongThread")
    fun save(v: View, StoredPath: String) {
        try {
            val gpxfile = File(StoredPath)
            val mFileOutStream = FileOutputStream(gpxfile)
            v.draw(canvasSave)

            bitmap!!.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream)
            mFileOutStream.flush()
            mFileOutStream.close()

        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
    }
/*
        fun clear() {
            path.reset();
            invalidate();
        }
*/
    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.drawPath(path, paint)

    if (canvasSave == null) {
        bitmap = Bitmap.createBitmap(canvasLL.width, canvasLL.height, Bitmap.Config.RGB_565)
        canvasSave = Canvas(bitmap!!)
        canvasSave!!.drawColor(Color.WHITE)
    }
        canvasSave!!.save()
        canvasSave!!.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var eventX = event.getX();
        var eventY = event.getY();

        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(eventX, eventY)
                lastTouchX = eventX
                lastTouchY = eventY
                return true
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                resetDirtyRect(eventX, eventY)
                var historySize = event.getHistorySize()
                for (i in 0..(historySize-1)) {
                    var historicalX = event.getHistoricalX(i)
                    var historicalY = event.getHistoricalY(i)
                    expandDirtyRect(historicalX, historicalY)
                    path.lineTo(historicalX, historicalY)
                }
                path.lineTo(eventX, eventY)
            }
            else -> {
                debug("Ignored touch event: " + event.toString())
                return false
            }
        }

        invalidate((dirtyRect.left - HALF_STROKE_WIDTH).toInt(),
            (dirtyRect.top - HALF_STROKE_WIDTH).toInt(),
            (dirtyRect.right + HALF_STROKE_WIDTH).toInt(),
            (dirtyRect.bottom + HALF_STROKE_WIDTH).toInt());

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true
    }

    fun debug(string: String) {
        Log.v("log_tag", string);
    }

    fun expandDirtyRect(historicalX: Float, historicalY: Float) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }

        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    fun resetDirtyRect(eventX: Float, eventY: Float) {
        dirtyRect.left = min(lastTouchX, eventX);
        dirtyRect.right = max(lastTouchX, eventX);
        dirtyRect.top = min(lastTouchY, eventY);
        dirtyRect.bottom = max(lastTouchY, eventY);
    }

}