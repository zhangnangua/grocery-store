package com.pumpkin.automatic_execution.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.automatic_execution.View.SelfView.SurfaceViewHandWriting

/**
 * pumpkin
 * SurfaceView测试Activity，绘制小Demo
 */
class PaintActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SurfaceViewHandWriting(this))
    }
}