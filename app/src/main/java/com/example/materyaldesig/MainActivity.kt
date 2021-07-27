package com.example.materyaldesig

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.materyaldesig.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*      метод newInstance() высвечивает красным.
        Поэтому даный код пока закомментирую
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commit()
        }
 */
    }
}