package com.naltynbekkz.nulife.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.naltynbekkz.nulife.BaseApplication
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.ActivityMainBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.di.main.MainComponent
import com.naltynbekkz.nulife.di.main.clubs.ClubsComponent
import com.naltynbekkz.nulife.di.main.courses.CoursesComponent
import com.naltynbekkz.nulife.di.main.food.FoodComponent
import com.naltynbekkz.nulife.di.main.market.MarketComponent
import com.naltynbekkz.nulife.di.main.profile.ProfileComponent
import com.naltynbekkz.nulife.di.main.timetable.TimetableComponent
import com.naltynbekkz.nulife.ui.auth.AuthActivity
import com.naltynbekkz.nulife.util.BottomNavController
import com.naltynbekkz.nulife.util.setUpNavigation
import javax.inject.Inject

class MainActivity : AppCompatActivity(), BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener {

    private lateinit var mainComponent: MainComponent

    val coursesComponent: CoursesComponent by lazy {
        mainComponent.coursesComponent().create()
    }
    val timetableComponent: TimetableComponent by lazy {
        mainComponent.timetableComponent().create()
    }
    val foodComponent: FoodComponent by lazy {
        mainComponent.foodComponent().create()
    }
    val clubsComponent: ClubsComponent by lazy {
        mainComponent.clubsComponent().create()
    }
    val profileComponent: ProfileComponent by lazy {
        mainComponent.profileComponent().create()
    }
    val marketComponent: MarketComponent by lazy {
        mainComponent.marketComponent().create()
    }

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: MainViewModel by viewModels { viewModelProvider.create(this) }

    private lateinit var bottomNavController: BottomNavController
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        mainComponent = (applicationContext as BaseApplication).appComponent.mainComponent().create()
        mainComponent.inject(this)

        super.onCreate(savedInstanceState)

        if (viewModel.auth.currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        bottomNavController = BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.courses,
            this,
            this
        )

        mainBinding.bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

    }

    fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun getNavGraphId(itemId: Int) = when (itemId) {
        R.id.courses -> {
            R.navigation.courses_navigation
        }
        R.id.timetable -> {
            R.navigation.timetable_navigation
        }
        R.id.food -> {
            R.navigation.food_navigation
        }
        R.id.clubs -> {
            R.navigation.clubs_navigation
        }
        else -> {
            R.navigation.profile_navigation
        }
    }

    override fun onGraphChange() {
//        TODO("Not yet implemented")
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) {
//        TODO("Not yet implemented")
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    fun hideBottomNavigation() {
        //TODO: Animate
        mainBinding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigation() {
        mainBinding.bottomNavigationView.visibility = View.VISIBLE
    }

}
