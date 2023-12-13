package com.kevkhv.table.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import com.kevkhv.table.R
import com.kevkhv.table.databinding.ActivityEditAdsBinding
import com.kevkhv.table.utils.CityHelper


class EditAdsAct : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, CityHelper.getAllCountries(this)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCountry.adapter = adapter
    }
}