package com.example.materyaldesig.apibottom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.materyaldesig.R
import com.example.materyaldesig.api.EarthFragment
import com.example.materyaldesig.api.MarsFragment
import com.example.materyaldesig.api.WeatherFragment
import com.example.materyaldesig.databinding.ActivityApiBottomBinding

class ApiBottomActivity : AppCompatActivity() {

    private var _binding: ActivityApiBottomBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_bottom)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_api_bottom_container, EarthFragment())
                        .commit()
                    true
                }
                R.id.bottom_view_mars -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_api_bottom_container, MarsFragment())
                        .commit()
                    true
                }
                R.id.bottom_view_weather -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.activity_api_bottom_container, WeatherFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigationView.selectedItemId = R.id.bottom_view_earth

        binding.bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    //Item tapped
                }
                R.id.bottom_view_mars -> {
                    //Item tapped
                }
                R.id.bottom_view_weather -> {
                    //Item tapped
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}