package com.cactusfromhell.simple_drawing.fragments


import android.os.Bundle
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.cactusfromhell.simple_drawing.R
import com.cactusfromhell.simple_drawing.Signature



class CanvasFragment: Fragment() {

    private lateinit var canvasLL: LinearLayout
    private lateinit var mSignature: Signature

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_canvas, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        canvasLL = view.findViewById(R.id.canvasLL)
        mSignature = Signature.getInstance(activity!!.applicationContext, canvasLL)
        mSignature.setBackgroundColor(Color.WHITE)
        canvasLL.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }


}


