package com.fabmenusample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import com.cheekiat.fabmenu.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {

                var setDuration = 150+(progress*5).toLong()
                duration.text = "Duration : " + setDuration
                fabMenu.setDuration(setDuration)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        seekBar.progress = 20

        fabMenu.addItem(R.drawable.ic_add_a_photo_black_24dp, android.R.color.holo_orange_light)
        fabMenu.addItem(R.drawable.ic_call_black_24dp, android.R.color.holo_orange_light)
        fabMenu.addItem(R.drawable.ic_content_copy_black_24dp, android.R.color.holo_orange_light)

        fabMenu.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {

                Toast.makeText(this@MainActivity, "position " + position, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
