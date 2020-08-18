package com.naltynbekkz.core

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationController(
    private val activity: Activity,
    @IdRes private val containerId: Int,
    @IdRes private val appStartDestinationId: Int,
    private val navGraphProvider: NavGraphProvider,
    private val bottomNavigationView: BottomNavigationView,
    savedInstanceState: Bundle?,
    private val customAnimations: CustomAnimations? = null
) {

    private var navigationBackStack: BackStack =
        if (savedInstanceState == null || !savedInstanceState.containsKey(BOTTOM_NAV_BACKSTACK_KEY)) {
            BackStack.of(
                appStartDestinationId
            )
        } else {
            BackStack()
                .apply { addAll((savedInstanceState[BOTTOM_NAV_BACKSTACK_KEY] as IntArray).toTypedArray()) }
        }

    var fragmentManager: FragmentManager = (activity as FragmentActivity).supportFragmentManager

    init {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            onNavigationItemSelected(it.itemId)
        }

        bottomNavigationView.setOnNavigationItemReselectedListener {

            val navController = activity.findNavController(containerId)

            if (navController.previousBackStackEntry != null) {
                navController.navigate(
                    navController.graph.id,
                    null,
                    NavOptions.Builder().setPopUpTo(navController.graph.id, true).build()
                )
            } else {
                val currentFragment = fragmentManager
                    .findFragmentById(containerId)!!
                    .childFragmentManager
                    .fragments
                    .last()
                if (currentFragment is LandingFragment) {
                    currentFragment.navigationItemReselected()
                }
            }
        }

        if (savedInstanceState == null) {
            onNavigationItemSelected()
        }
    }

    private fun onNavigationItemSelected(itemId: Int = navigationBackStack.last()): Boolean {

        // Replace fragment representing a navigation item
        val fragment = fragmentManager.findFragmentByTag(itemId.toString())
            ?: NavHostFragment.create(navGraphProvider.getNavGraphId(itemId))

        fragmentManager.beginTransaction()
            .apply {
                if (customAnimations != null) {
                    setCustomAnimations(
                        customAnimations.enter,
                        customAnimations.exit,
                        customAnimations.popEnter,
                        customAnimations.popExit
                    )
                }
            }
            .replace(containerId, fragment, itemId.toString())
            .addToBackStack(null)
            .commit()

        // Add to back stack
        navigationBackStack.moveLast(itemId)

        // Update checked icon
        bottomNavigationView.menu.findItem(itemId).isChecked = true

        return true
    }

    @SuppressLint("RestrictedApi")
    fun onBackPressed() {
        val navController = fragmentManager.findFragmentById(containerId)!!
            .findNavController()
        when {
            navController.backStack.size > 2 -> {
                navController.popBackStack()
            }
            navigationBackStack.size > 1 -> {
                navigationBackStack.removeLast()
                onNavigationItemSelected()
            }
            else -> {
                activity.finish()
            }
        }
    }

    class BackStack : ArrayList<Int>() {
        companion object {
            fun of(vararg elements: Int): BackStack {
                val b =
                    BackStack()
                b.addAll(elements.toTypedArray())
                return b
            }
        }

        fun removeLast() = removeAt(size - 1)

        fun moveLast(item: Int) {
            remove(item) // if present, remove
            add(item) // add to end of list
        }
    }

    interface NavGraphProvider {
        @NavigationRes
        fun getNavGraphId(itemId: Int): Int
    }

    companion object {
        const val BOTTOM_NAV_BACKSTACK_KEY =
            "com.naltynbekkz.core.BottomNavigationController.bottom_nav_backstack"
    }

    interface LandingFragment {
        fun navigationItemReselected()
    }

    fun getNavigationBackStackIntArray() = navigationBackStack.toIntArray()

    data class CustomAnimations(
        @AnimatorRes @AnimRes val enter: Int,
        @AnimatorRes @AnimRes val exit: Int,
        @AnimatorRes @AnimRes val popEnter: Int,
        @AnimatorRes @AnimRes val popExit: Int
    )

    interface HidingBottomNav {
        fun hideBottomNav()
        fun showBottomNav()
    }

    interface NavigatesToAuthActivity {
        fun navigateToAuthActivity()
    }

}


