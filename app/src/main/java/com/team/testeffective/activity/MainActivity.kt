package com.team.testeffective.activity

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.team.testeffective.R
import com.team.testeffective.databinding.ActivityMainBinding
import com.team.testeffective.fragments.MainFragment
import com.team.testeffective.fragments.PlugFragment
import com.team.testeffective.navigate.NavigateHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : AppCompatActivity(), NavigateHelper, KoinComponent {

    private lateinit var binding: ActivityMainBinding

    private val navigator: Navigator by lazy {
        AppNavigator(
            this,
            R.id.main_fragment_container,
            supportFragmentManager,
        )
    }

    private val navigatorHolder: NavigatorHolder by inject<NavigatorHolder>()
    private val router: Router by inject<Router>()

    // ****** lifecycle *****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navigateTo(MainFragment.newInstance())
        onClickListenerItemInMenu()
        listeningOnBackPressed()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
    // ****** lifecycle *****

    override fun navigateTo(fragment: Fragment) {
        router.navigateTo(FragmentScreen { fragment })
    }

    override fun replaceScreen(fragment: Fragment) {
        router.replaceScreen(FragmentScreen { fragment })
    }

    private fun listeningOnBackPressed() {
        this.onBackPressedDispatcher.addCallback {
            this@MainActivity.finish()
        }
    }

    private fun onClickListenerItemInMenu() {
        with(binding) {
            btnNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.plane -> {
                        replaceScreen(MainFragment.newInstance())
                    }

                    R.id.hotel -> {
                        replaceScreen(PlugFragment.newInstance())
                    }

                    R.id.place -> {
                        replaceScreen(PlugFragment.newInstance())
                    }

                    R.id.profile -> {
                        replaceScreen(PlugFragment.newInstance())
                    }

                    R.id.subscription -> {
                        replaceScreen(PlugFragment.newInstance())
                    }

                    else -> {

                    }
                }
                true
            }
        }
    }
}