package com.example.seedgrower.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.seedgrower.R
import com.example.seedgrower.databinding.ActivityDetailTaskBinding
import com.example.seedgrower.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        binding.btnGoHome.setOnClickListener{
            startActivity(Intent(this, TaskManagerActivity::class.java))
        }
    }
}