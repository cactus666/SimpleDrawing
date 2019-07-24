package com.cactusfromhell.simple_drawing

import android.annotation.SuppressLint
import android.content.Context
import android.gesture.GestureStroke
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
import android.widget.TextView
import com.cactusfromhell.simple_drawing.fragments.ActionBarFragment
import com.cactusfromhell.simple_drawing.fragments.ActionBarFragment.CallBackChnagePercent

class Draw(val path: Path, val paint: Paint)

class Signature: View {

    private var canvasSave: Canvas? = null
    private var canvasLL: LinearLayout
    private var paint = Paint()
    private var bitmap: Bitmap? = null
    private var STROKE_WIDTH = 2f;
    private val HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
    private var path = Path()
    private var lastTouchX: Float = 0.0f
    private var lastTouchY: Float = 0.0f
    private val RectF = android.graphics.RectF()
    private val dirtyRect = android.graphics.RectF()
    private val now_list_path_paint: ArrayList<Draw> = ArrayList()
    private var old_list_path_paint: ArrayList<Draw> = ArrayList()
    private var old_paint = Color.BLACK
    private var sceep_count = 1
    private var callback: Callback? = null

    private var can: Canvas? = null

    companion object {
        private var instance : Signature? = null

        fun getInstance(context: Context, canvasLL: LinearLayout?): Signature = synchronized(this) {
            if (instance == null && canvasLL != null)
                instance = Signature(context, null, canvasLL)
//            instance!!.callback = callback
            return instance!!
        }

        fun clearInstance() {
            instance = null
        }
    }


    private constructor(context: Context, attrs: AttributeSet?, canvasLL: LinearLayout) : super(context, attrs) {
        this.canvasLL = canvasLL
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = STROKE_WIDTH
        now_list_path_paint.add(Draw(path, paint))
        old_list_path_paint.add(Draw(path, paint))
        println("count size in constr = ${now_list_path_paint.size}")
    }


    fun changeSizeStroke(new_stroke_width: Float = 1.0f) {
        //STROKE_WIDTH = new_stroke_width
        old_paint = paint.color
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = new_stroke_width
        path = Path()
        now_list_path_paint.add(Draw(path, paint))
        old_list_path_paint.clear()
        old_list_path_paint.addAll(now_list_path_paint)
        sceep_count++
    }

    fun sceep(): Int{
//        callback!!.callingBack()
//        (context as MainActivity).findViewById<TextView>(R.id.tools_percent).text = "2%"

        println("sceep = ${now_list_path_paint.size}, ${sceep_count}")
        try {
//            now_list_path_paint.removeAt(now_list_path_paint.size - 1)
            now_list_path_paint.removeAt(sceep_count-1)
            //paint = now_list_path_paint[now_list_path_paint.size - 1].paint
            sceep_count--
            path = old_list_path_paint[now_list_path_paint.size - 1].path
        }catch(e: java.lang.Exception){
            println("error")
            paint = Paint()
            paint.isAntiAlias = true
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeWidth = STROKE_WIDTH
           // println("error")
            path = Path()
            now_list_path_paint.add(Draw(path, paint))
            sceep_count=1
//            path = old_list_path_paint[0].path
        }
//        println("sceep = ${now_list_path_paint.size}, ${sceep_count}")
//        now_list_path_paint.removeAt(now_list_path_paint.size - 1)
//////        path = Path()
//////        now_list_path_paint.add(Draw(path, paint))
//
//        println("sceep = ${now_list_path_paint.size}, ${sceep_count}")
//        if (sceep_count != 0)
//            sceep_count++
//        if (now_list_path_paint.size < 1) {
//            paint.isAntiAlias = true
//            paint.color = Color.BLACK
//            paint.style = Paint.Style.STROKE
//            paint.strokeJoin = Paint.Join.ROUND
//            paint.strokeWidth = STROKE_WIDTH
//            path = Path()
//            now_list_path_paint.add(Draw(path, paint))
//            sceep_count = 0
//        }
//
//        path = now_list_path_paint.get(now_list_path_paint.size - 1).path
//
//        println("sceep = ${now_list_path_paint.size}, ${sceep_count}")
        invalidate()
        return sceep_count
    }

    fun getSceepCount() = sceep_count
/*
    fun sceep(): Int{
        println("sceep = ${now_list_path_paint.size}")
        sceep_count++
        if (now_list_path_paint.size >= 1) {
            now_list_path_paint.removeAt(now_list_path_paint.size - 1)
            if(now_list_path_paint.size == 0){
                paint.isAntiAlias = true
                paint.color = Color.BLACK
                paint.style = Paint.Style.STROKE
                paint.strokeJoin = Paint.Join.ROUND
                paint.strokeWidth = STROKE_WIDTH
                path = Path()
                now_list_path_paint.add(Draw(path, paint))
            }
        } else {
            paint.isAntiAlias = true
            paint.color = Color.BLACK
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeWidth = STROKE_WIDTH
            path = Path()
            now_list_path_paint.add(Draw(path, paint))
        }

        invalidate()
        return sceep_count

//        sceep_count++
//        now_list_path_paint.removeAt(now_list_path_paint.size - 1)
//        if (now_list_path_paint.size == 0) {
//            path = Path()
//            now_list_path_paint.add(Draw(path, paint))
//        }
//        invalidate()
//        return sceep_count
    }

* */


    fun sceepBack(): Int {
//        callback!!.callingBack()
        println("sceep_back = ${now_list_path_paint.size}, ${old_list_path_paint.size}, ${sceep_count}")

        try{
//            if(now_list_path_paint.size == 1)
//                now_list_path_paint[0] = (old_list_path_paint[0])
//            else


            now_list_path_paint.add(old_list_path_paint.get(sceep_count -1))
            //paint = old_list_path_paint.get(sceep_count -1).paint
//            now_list_path_paint.add(old_list_path_paint.get(now_list_path_paint.size - 1))
            sceep_count++
        }catch (e: java.lang.Exception){
            println("error in sceepBack")
        }
//        if (now_list_path_paint.size == 1) {
//            if (sceep_count > 0 ) {
//                now_list_path_paint.add(old_list_path_paint.get(now_list_path_paint.size - 1))
//                sceep_count--
//            }
//            if (sceep_count == -1 ) {
//                now_list_path_paint[0] = (old_list_path_paint[0])
//                sceep_count = 0
//                sceep_count--
//            }
//        }
//        else {
//            if (now_list_path_paint.size < old_list_path_paint.size) {
//                now_list_path_paint.add(old_list_path_paint.get(now_list_path_paint.size))
//                sceep_count--
//            }
//        }

        invalidate()
        return sceep_count
    }


/*
 fun sceepBack(): Int {
        println("sceep_back = ${now_list_path_paint.size}, ${old_list_path_paint.size}")
        sceep_count--


        if(now_list_path_paint.size <= old_list_path_paint.size)
            now_list_path_paint.add(old_list_path_paint.get(now_list_path_paint.size))




        invalidate()
        return sceep_count
//        sceep_count--
//        if(now_list_path_paint.size <= old_list_path_paint.size)
//            now_list_path_paint.add(old_list_path_paint.get(now_list_path_paint.size - 1))
//        invalidate()
//        return sceep_count
    }
*/



    @SuppressLint("ResourceAsColor")
    fun changeColor(new_color: Int = R.color.color_black): Int {
        old_paint = paint.color
        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = STROKE_WIDTH
        paint.color = new_color
        path = Path()
        now_list_path_paint.add(Draw(path, paint))
//        old_list_path_paint.add(Draw(path, paint))
        old_list_path_paint.clear()
        old_list_path_paint.addAll(now_list_path_paint)
        sceep_count++
        println("count size = ${now_list_path_paint.size}")
        return old_paint
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

    override fun onDraw(canvas: Canvas) {

        if (canvasSave == null) {
            bitmap = Bitmap.createBitmap(canvasLL.width, canvasLL.height, Bitmap.Config.RGB_565)
            canvasSave = Canvas(bitmap!!)
            canvasSave!!.drawColor(Color.WHITE)
        }

        canvas.save()
        canvasSave!!.save()
        for (draw in now_list_path_paint) {
            canvas.drawPath(draw.path, draw.paint)
            canvasSave!!.drawPath(draw.path, draw.paint)
        }
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