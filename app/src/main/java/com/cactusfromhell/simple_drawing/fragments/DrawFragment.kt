package com.cactusfromhell.simple_drawing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cactusfromhell.simple_drawing.R
import com.cactusfromhell.simple_drawing.Signature

class DrawFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_draw, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        Signature.clearInstance()
    }
}