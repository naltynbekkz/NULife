package com.naltynbekkz.nulife.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.naltynbekkz.core.BottomNavigationController
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationController.NavGraphProvider,
    BottomNavigationController.HidingBottomNav, BottomNavigationController.NavigatesToAuthActivity {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var bottomNavController: BottomNavigationController
    private lateinit var binding: ActivityMainBinding

    private val translationSpring by lazy {
        SpringAnimation(
            binding.bottomNavigationView,
            DynamicAnimation.TRANSLATION_Y
        ).apply {
            spring = SpringForce().apply {
                dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                stiffness = SpringForce.STIFFNESS_LOW
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (viewModel.isSignedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        bottomNavController = BottomNavigationController(
            this,
            R.id.main_nav_host_fragment,
            R.id.courses,
            this,
            binding.bottomNavigationView,
            savedInstanceState,
            customAnimations = BottomNavigationController.CustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
        )

    }

    override fun getNavGraphId(itemId: Int) = when (itemId) {
        R.id.courses -> {
            com.naltynbekkz.courses.R.navigation.courses_navigation
        }
        R.id.timetable -> {
            com.naltynbekkz.timetable.R.navigation.timetable_navigation
        }
        R.id.food -> {
            com.naltynbekkz.food.R.navigation.food_navigation
        }
        R.id.clubs -> {
            com.naltynbekkz.clubs.R.navigation.clubs_navigation
        }
        else -> {
            com.naltynbekkz.profile.R.navigation.profile
        }
    }

    override fun showBottomNav() {
        translationSpring.animateToFinalPosition(0f)
    }

    override fun hideBottomNav() {
        translationSpring.animateToFinalPosition(binding.bottomNavigationView.height.toFloat())
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun navigateToAuthActivity() {
        viewModel.signOut()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

}