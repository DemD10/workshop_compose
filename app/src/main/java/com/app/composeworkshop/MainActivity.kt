package com.app.composeworkshop

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.setBackgroundColor(Color.BLACK)
        super.onCreate(savedInstanceState)
    }
}