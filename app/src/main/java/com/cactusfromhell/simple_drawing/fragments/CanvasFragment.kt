package com.cactusfromhell.simple_drawing.fragments


import android.content.res.ColorStateList
import android.os.Bundle
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.cactusfromhell.simple_drawing.R
import com.cactusfromhell.simple_drawing.Signature
import org.w3c.dom.Text
import androidx.core.graphics.drawable.DrawableCompat
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import android.graphics.PorterDuff
import android.R.color
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat.getColor


class CanvasFragment: Fragment(), View.OnClickListener {

    private lateinit var canvasLL: LinearLayout
    private lateinit var mSignature: Signature
    private lateinit var navigate_before: ImageView
//    private lateinit var eraser: TextView
//    private var old_color: Int = 0
//    private var isEraser = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_canvas, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        canvasLL = view.findViewById(R.id.canvasLL)
        mSignature = Signature.getInstance(activity!!, canvasLL)
        mSignature.setBackgroundColor(Color.WHITE)
        canvasLL.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        navigate_before = view.findViewById(R.id.navigate_before)
        navigate_before.setOnClickListener(this)
        view.findViewById<ImageView>(R.id.navigate_next).setOnClickListener(this)
        view.findViewById<TextView>(R.id.eraser).setOnClickListener(this)
//        eraser = view.findViewById(R.id.eraser)
//        eraser.setOnClickListener(this)
    }



    override fun onClick(view: View) {
        when (view.id) {
            R.id.eraser -> {
//                print("....qwer....")
//                if (isEraser) {
//                    isEraser = false
                    //this.old_color =
                        Signature.getInstance(activity!!.applicationContext, null).changeColor(ContextCompat.getColor(activity!!.applicationContext, R.color.color_white))
//                   print("white")
//                }
//                else {
//                    isEraser = true
//                    Signature.getInstance(activity!!.applicationContext, null).changeColor(old_color)
//                    print("no white")
//                }

            }
            R.id.navigate_before -> {
//                navigate_before.isClickable = Signature.getInstance(activity!!.applicationContext, null).getSceepCount() != 0
//                if (Signature.getInstance(activity!!.applicationContext, null).getSceepCount() != 1)
//                navigate_before.isClickable =
                    Signature.getInstance(activity!!.applicationContext, null).sceep() != 0
            }
            R.id.navigate_next -> {
                Signature.getInstance(activity!!.applicationContext, null).sceepBack()
            }
        }
    }
}


