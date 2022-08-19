package com.griffindfung.ballerstatsnow.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.griffindfung.ballerstatsnow.R
import com.griffindfung.ballerstatsnow.databinding.ActivityMainBinding
import com.griffindfung.ballerstatsnow.ui.gamesearch.GameSearchFragment
import com.griffindfung.ballerstatsnow.ui.playersearch.PlayerSearchFragment
import com.griffindfung.ballerstatsnow.ui.today.TodayFragment

/*
Initial opening of app starts on this MainActivity. This activity hosts multiple fragments for game and
player search, thus also handles exchanging of visible fragments. Users navigate across fragments
with the bottom nav bar.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val todayFragment = TodayFragment.newInstance()
        val playerSearchFragment = PlayerSearchFragment()
        val gameSearchFragment = GameSearchFragment()

        // Set current and starting fragment
        var currentFragment: Fragment = todayFragment
        supportFragmentManager.beginTransaction().add(R.id.fgMain, todayFragment).commit()

        // Handles changing of fragment shown in activity. Does nothing if curr and new frag are the same.
        // If they're different, hides current and shows new frag. Function expects currentFragment set previously.
        fun updateCurrentFragment(newFragment: Fragment, title: String) {
            if (currentFragment == newFragment) return
            supportFragmentManager.beginTransaction().apply {
                // Hide current fragment in background
                hide(currentFragment)

                // Show new current fragment selected
                if (!newFragment.isAdded) add(R.id.fgMain, newFragment)
                show(newFragment)
                commit()
            }
            currentFragment = newFragment
            supportActionBar?.title = title
        }

        // Changes fragment depending on what bottom nav item is selected
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.today -> {
                    updateCurrentFragment(todayFragment, getString(R.string.title_today))
                }
                R.id.playerSearch -> {
                    updateCurrentFragment(playerSearchFragment, getString(R.string.title_player_search))
                }
                R.id.gameSearch -> {
                    updateCurrentFragment(gameSearchFragment, getString(R.string.title_game_search))
                }
            }
            true
        }
    }
}