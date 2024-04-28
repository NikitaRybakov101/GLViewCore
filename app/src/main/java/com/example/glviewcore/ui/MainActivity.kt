package com.example.glviewcore.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.TextureView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.glviewcore.R

class MainActivity : AppCompatActivity() , View.OnTouchListener {

    private var touchMap: TouchMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRender()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initRender() {
        val textureView = findViewById<TextureView>(R.id.textureView)
        val mainRender = MainRender(this, textureView)
        textureView.surfaceTextureListener = mainRender.renderGLThread

        touchMap = TouchMap(mainRender)
        textureView.setOnTouchListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        return touchMap?.processingTouch(motionEvent) ?: false
    }
}