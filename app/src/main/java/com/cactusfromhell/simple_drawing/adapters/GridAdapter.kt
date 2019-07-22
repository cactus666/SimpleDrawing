package com.cactusfromhell.simple_drawing.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import java.io.File
import com.cactusfromhell.simple_drawing.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class GridAdapter(context: Context, private val mData: Array<File>): RecyclerView.Adapter<GridAdapter.ViewHolder>() {


    private val mInflater: LayoutInflater
    private val context: Context
    private val format = SimpleDateFormat("dd.MM.yy, HH:mm")
    init {
        this.mInflater = LayoutInflater.from(context)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_for_list, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = mData[position].name
        holder.data.text = format.format(mData[position].lastModified())
        holder.file = mData[position]
        Picasso.with(context)
            .load(mData[position])
            .into(holder.image)

    }


    override fun getItemCount(): Int {
        return mData.size
    }



    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var name: TextView
        var data: TextView
//        var setting: ImageView
        var image: ImageView
        lateinit var file: File

        init {
            name = itemView.findViewById(R.id.name_image)
            data = itemView.findViewById(R.id.date_image)
//            setting = itemView.findViewById(R.id.setting)
            image = itemView.findViewById(R.id.src_image)
            itemView.setOnClickListener(this)
//            setting.setOnClickListener(this)
        }


        override fun onClick(view: View) {
            when (view.id) {
//                R.id.setting -> {

//                }
                else -> {
                    val cancelButtonListener = object : DialogInterface.OnClickListener {
                        override fun onClick(arg0: DialogInterface, arg1: Int) {}
                    }

                    val factory = LayoutInflater.from(context)
                    val view: View = factory.inflate(R.layout.alert_dialog_with_image, null)

                    Picasso.with(context)
                        .load(file)
                        .into(view.findViewById<ImageView>(R.id.dialog_imageview))

                    AlertDialog.Builder(context)
                        .setView(view)
                        .setNegativeButton("Свернуть", cancelButtonListener)
                        .show()
                }

            }
        }
    }


}
