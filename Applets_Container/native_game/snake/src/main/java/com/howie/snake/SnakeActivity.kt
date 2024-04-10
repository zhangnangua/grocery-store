package com.howie.snake

import android.os.Bundle
import com.howie.snake.databinding.SnakeLayoutBinding
import com.pumpkin.mvvm.view.BaseActivity

class SnakeActivity : BaseActivity() {

    lateinit var binding: SnakeLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SnakeLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
//        binding.game.attachMoveHandlerView()
//        binding.game.attachAttackHandlerView()
    }
}