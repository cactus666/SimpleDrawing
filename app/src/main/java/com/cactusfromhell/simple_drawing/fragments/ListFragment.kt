package com.cactusfromhell.simple_drawing.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusfromhell.simple_drawing.R
import com.cactusfromhell.simple_drawing.adapters.GridAdapter
import java.io.File
import com.google.android.material.floatingactionbutton.FloatingActionButton as FloatingActionButton


class ListFragment: Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var list: RecyclerView
    private val REQUEST_READ_STORAGE: Int = 10

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
        settingPermission()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> navController.navigate(R.id.action_listFragment_to_drawFragment)
        }
    }



    private fun settingPermission() {
        if (android.os.Build.VERSION.SDK_INT <= 22) {
            val file_dir = File("/storage/emulated/0/Signature")

            if (!file_dir.exists() ||  !file_dir.isDirectory()) {
                file_dir.mkdir()
            }

            list.adapter = GridAdapter(activity!!, File("/storage/emulated/0/Signature").listFiles().toCollection(ArrayList()))
        } else {
            if (ContextCompat.checkSelfPermission(
                    activity!!.applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_STORAGE)
            } else {
                val file_dir = File("/storage/emulated/0/Signature")

                if (!file_dir.exists() ||  !file_dir.isDirectory()) {
                    file_dir.mkdir()
                }

                list.adapter = GridAdapter(activity!!, File("/storage/emulated/0/Signature").listFiles().toCollection(ArrayList()))
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_READ_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val file_dir = File("/storage/emulated/0/Signature")

                    if (!file_dir.exists() ||  !file_dir.isDirectory()) {
                        file_dir.mkdir()
                    }

                    list.adapter = GridAdapter(activity!!, File("/storage/emulated/0/Signature").listFiles().toCollection(ArrayList()))
                } else {
                    activity!!.finish()
                }
            }
        }
    }
}