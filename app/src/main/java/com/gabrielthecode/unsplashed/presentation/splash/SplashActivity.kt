package com.gabrielthecode.unsplashed.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.gabrielthecode.unsplashed.R
import com.gabrielthecode.unsplashed.databinding.ActivitySplashBinding
import com.gabrielthecode.unsplashed.presentation.search.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    private lateinit var binding: ActivitySplashBinding
    private lateinit var upToDown: Animation
    private lateinit var downToUp: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        downToUp = AnimationUtils.loadAnimation(this, R.anim.layout_animation_fall_down)
        upToDown = AnimationUtils.loadAnimation(this, R.anim.layout_animation_fall_down)

        binding.apply {
            imageLogo.animation = upToDown
            textTitle.animation = downToUp

            activityScope.launch {
                delay(2000)
                finish()
                startActivity(Intent(applicationContext, SearchActivity::class.java))
            }
        }
    }
}