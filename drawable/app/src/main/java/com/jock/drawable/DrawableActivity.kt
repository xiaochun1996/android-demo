package com.jock.drawable

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.jock.drawable.databinding.ActivityDrawableBinding

/**
 * Description:
 * Author: lxc
 * Date: 2022/3/7 10:11
 */
class DrawableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityDrawableBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        val bitmap = ContextCompat.getDrawable(this, R.drawable.drawable_bitmap)
        viewBinding.tvBitmapDrawable.background = bitmap
    }
}