package com.cactusfromhell.simple_drawing.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cactusfromhell.simple_drawing.R
import com.cactusfromhell.simple_drawing.Signature
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.widget.PopupWindowCompat.showAsDropDown
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.core.view.marginLeft
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import android.content.DialogInterface
import android.media.AudioManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.cactusfromhell.simple_drawing.Callback
import com.squareup.picasso.Picasso
import com.yandex.metrica.impl.ob.di
import kotlinx.android.synthetic.main.fragment_action_bar.*
import kotlinx.android.synthetic.main.popup_tools.*


class ActionBarFragment: Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private val DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/"
//    private val DIRECTORY = "/data/data/Signature/com.cactusfromhell.simple_drawing/cache/"
    private var pic_name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    private lateinit var file: File
    private lateinit var palette: ImageView
    private lateinit var brush: ImageView
    private lateinit var layout_palette: View
    private lateinit var layout_brush: View
    private lateinit var popup: PopupWindow
    private lateinit var view_for_alert: View
    private lateinit var tool_brush: ImageView
    private lateinit var tool_pencil: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var percent: TextView
    private lateinit var tools_img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layout_palette = layoutInflater.inflate(R.layout.popup_palette, null)
        this.layout_brush = layoutInflater.inflate(R.layout.popup_tools, null)
        layout_palette.findViewById<ImageView>(R.id.color_palette_1).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_2).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_3).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_4).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_5).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_6).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_7).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_8).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_9).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_10).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_11).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_12).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_13).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_14).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_15).setOnClickListener(this)
        layout_palette.findViewById<ImageView>(R.id.color_palette_16).setOnClickListener(this)

        tool_brush = layout_brush.findViewById(R.id.tools_brush)
        tool_pencil = layout_brush.findViewById(R.id.tools_pencil)
        tools_img = layout_brush.findViewById(R.id.tools_img)
        ImageViewCompat.setImageTintList(this.tool_pencil,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_main)))
        tool_pencil.setOnClickListener(this)
        tool_brush.setOnClickListener(this)
        seekBar = layout_brush.findViewById(R.id.tools_seekBar)
        percent = layout_brush.findViewById(R.id.tools_percent)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                percent.text = "$progress%"
                Signature.getInstance(activity!!.applicationContext, null).changeSizeStroke(progress.toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_action_bar, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ImageView>(R.id.arrow_back).setOnClickListener(this)
        palette = view.findViewById(R.id.palette)
        palette.setOnClickListener(this)
        brush = view.findViewById(R.id.brush)
        brush.setOnClickListener(this)

        view.findViewById<ImageView>(R.id.brush).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.save).setOnClickListener(this)

        this.popup = PopupWindow(activity)
        popup.setOnDismissListener(PopupWindow.OnDismissListener() {
            ImageViewCompat.setImageTintList(this.palette,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_white)))
            ImageViewCompat.setImageTintList(this.brush,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_white)))
        }
        )

        val factory = LayoutInflater.from(context)
        view_for_alert= factory.inflate(R.layout.alert_dialog_with_save, null)
    }

    override fun onStart() {
        super.onStart()
        navController =  Navigation.findNavController(this.parentFragment!!.view!!)
    }

    @SuppressLint("ResourceAsColor")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.arrow_back -> navController.popBackStack()
            R.id.palette ->{
                ImageViewCompat.setImageTintList(this.palette,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_main)))
//                this.popup = PopupWindow(activity)
                popup.contentView = layout_palette
                popup.height = WindowManager.LayoutParams.WRAP_CONTENT
                popup.width = WindowManager.LayoutParams.WRAP_CONTENT
                popup.isOutsideTouchable = true
                popup.isFocusable = true
                popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                popup.showAsDropDown(palette, -65, 20)
            }
            R.id.brush ->{
                ImageViewCompat.setImageTintList(this.brush,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_main)))
//                this.popup = PopupWindow(activity)
                popup.contentView = layout_brush
                popup.height = WindowManager.LayoutParams.WRAP_CONTENT
                popup.width = WindowManager.LayoutParams.WRAP_CONTENT
                popup.isOutsideTouchable = true
                popup.isFocusable = true
                popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                popup.showAsDropDown(palette, -65, 20)
            }
            R.id.save -> {
                val cancelButtonListener = DialogInterface.OnClickListener { arg0, arg1 ->
                    navController.popBackStack()
                }

                val saveButtonListener = DialogInterface.OnClickListener { arg0, arg1 ->
                    val name = view_for_alert.findViewById<EditText>(R.id.name_save).text.toString()
                    if (name.trim().length > 0) {
                        pic_name = name
                        Log.d("debug", "name = ${name}")
                    }else
                        pic_name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

                    Signature.getInstance(activity!!.applicationContext, null).save(view, DIRECTORY + pic_name + ".png")
                    navController.popBackStack()
                }


                var alert_dialog = AlertDialog.Builder(context, R.style.MyDialogTheme)
                    .setView(view_for_alert)
                    .setNegativeButton(R.string.cancel, cancelButtonListener)
                    .setPositiveButton(R.string.save, saveButtonListener)
                    .show()
                alert_dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity!!.applicationContext, R.color.color_white))
                alert_dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity!!.applicationContext, R.color.color_white))
            }
            R.id.color_palette_1, R.id.color_palette_2, R.id.color_palette_3, R.id.color_palette_4,
            R.id.color_palette_5, R.id.color_palette_6, R.id.color_palette_7, R.id.color_palette_8,
            R.id.color_palette_9, R.id.color_palette_10, R.id.color_palette_11, R.id.color_palette_12,
            R.id.color_palette_13, R.id.color_palette_14, R.id.color_palette_15, R.id.color_palette_16 -> {
                Signature.getInstance(activity!!.applicationContext, null).changeColor(ImageViewCompat.getImageTintList(view as ImageView)!!.getDefaultColor())
                popup.dismiss()
            }
            R.id.tools_pencil -> {
                ImageViewCompat.setImageTintList(this.tool_pencil,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_main)))
                ImageViewCompat.setImageTintList(this.tool_brush,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_white)))
                Picasso.with(activity)
                    .load(R.drawable.tool_1)
                    .into(tools_img)
            }
            R.id.tools_brush -> {
                ImageViewCompat.setImageTintList(this.tool_brush,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_main)))
                ImageViewCompat.setImageTintList(this.tool_pencil,  ColorStateList.valueOf(ContextCompat.getColor(activity!!.applicationContext, R.color.color_white)))
                Picasso.with(activity)
                    .load(R.drawable.tool_2)
                    .into(tools_img)
            }

        }
    }

    inner class CallBackChnagePercent : Callback {
        override fun callingBack() {
            activity!!.runOnUiThread {
                percent.text = "2%"
            }
        }
    }

}


