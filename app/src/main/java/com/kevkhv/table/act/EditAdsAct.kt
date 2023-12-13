package com.kevkhv.table.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kevkhv.table.R
import com.kevkhv.table.databinding.ActivityEditAdsBinding


class EditAdsAct : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}