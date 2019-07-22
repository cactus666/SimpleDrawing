package com.cactusfromhell.simple_drawing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusfromhell.simple_drawing.R
import com.cactusfromhell.simple_drawing.adapters.GridAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class ListFragment: Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var list: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener(this)
        list = view.findViewById(R.id.list)
        list.layoutManager = GridLayoutManager(activity!!, 2)
        val file_dir = File("/storage/emulated/0/Signature")
        if (!file_dir.exists()) {
            file_dir.mkdir()
        }
        list.adapter = GridAdapter(activity!!, File("/storage/emulated/0/Signature").listFiles())
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> navController.navigate(R.id.action_listFragment_to_drawFragment)
        }
    }
}