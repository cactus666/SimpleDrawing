package com.cactusfromhell.simple_drawing.fragments

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cactusfromhell.simple_drawing.R
import com.cactusfromhell.simple_drawing.Signature
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ActionBarFragment: Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private val DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/"
//    private val DIRECTORY = "/data/data/Signature/com.cactusfromhell.simple_drawing/cache/"
    private val pic_name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    private val StoredPath = DIRECTORY + pic_name + ".png"
    private lateinit var file: File

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_action_bar, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.arrow_back).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.palette).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.brush).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.save).setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        navController =  Navigation.findNavController(this.parentFragment!!.view!!)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.arrow_back -> navController.popBackStack()
            R.id.palette ->{}
            R.id.brush ->{}
            R.id.save ->{
                // Method to create Directory, if the Directory doesn't exists
                file = File(DIRECTORY);
                if (!file.exists()) {
                    Log.d("debug", "create dir $StoredPath")
                    file.mkdir()
//                    file.createNewFile()
                }
//                file.getParentFile().mkdirs()
//                file.createNewFile()
                Log.d("debug", "path = $StoredPath")
                view.setDrawingCacheEnabled(true)
                Signature.getInstance(activity!!.applicationContext, null).save(view,StoredPath)
//                Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

}