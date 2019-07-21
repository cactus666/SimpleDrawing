package com.cactusfromhell.simple_drawing.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment
import com.cactusfromhell.simple_drawing.R
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import kotlin.math.max
import kotlin.math.min



class CanvasFragment: Activity()/*, View.OnClickListener*/{

    private lateinit var btnClear: Button
    private lateinit var btnSave: Button
    private lateinit var file: File
    private lateinit var canvasLL: LinearLayout
    private lateinit var view: View
    private lateinit var mSignature: Signature
    private lateinit var bitmap: Bitmap
/*
//     Creating Separate Directory for saving Generated Images
    private val DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/"
    private val pic_name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    private val StoredPath = DIRECTORY + pic_name + ".png"
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_canvas)
        canvasLL = findViewById(R.id.canvasLL)
        mSignature = Signature(getApplicationContext(), null)
        mSignature.setBackgroundColor(Color.WHITE)
        // Dynamically generating Layout through java code
        canvasLL.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        /*
        btnClear = findViewById(R.id.btnclear)
        btnSave = findViewById(R.id.btnsave)

        view = canvasLL

        btnClear.setOnClickListener(this)
        btnSave.setOnClickListener(this)

        // Method to create Directory, if the Directory doesn't exists
        file = File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        */
    }

/*
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnclear -> {
                mSignature.clear()
            }
            R.id.btnsave -> {
                view.setDrawingCacheEnabled(true)
                mSignature.save(view,StoredPath)
                Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }
*/


    inner class Signature: View {

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            paint.isAntiAlias = true
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeWidth = STROKE_WIDTH
        }

        private val STROKE_WIDTH = 5f;
        private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private val paint = Paint()
        private val path = Path()
        private var lastTouchX: Float = 0.0f
        private var lastTouchY: Float = 0.0f
        private val RectF = RectF()
        private val dirtyRect = RectF()

/*
        @SuppressLint("WrongThread")
        fun save(v: View, StoredPath: String) {
            Log.v("log_tag", "Width: " + v.getWidth())
            Log.v("log_tag", "Height: " + v.getHeight())
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(canvasLL.getWidth(), canvasLL.getHeight(), Bitmap.Config.RGB_565)
            }
            val canvas = Canvas(bitmap)
            try {
                // Output the file
                val mFileOutStream = FileOutputStream(StoredPath)
                v.draw(canvas)

                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream)
                mFileOutStream.flush()
                mFileOutStream.close()

            } catch (e: Exception) {
                Log.v("log_tag", e.toString())
            }
        }

        fun clear() {
            path.reset();
            invalidate();
        }
*/

        override fun onDraw(canvas: Canvas) {
            canvas.drawPath(path, paint);
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
}


