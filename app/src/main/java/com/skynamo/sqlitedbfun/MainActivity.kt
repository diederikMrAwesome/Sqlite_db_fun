package com.skynamo.sqlitedbfun

import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.skynamo.sqlitedbfun.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navItemsArray: Array<Int> = arrayOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)

    private val _handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        _handler.postDelayed(task, TimeUnit.SECONDS.toMillis(10))
    }

    override fun onPause() {
        super.onPause()
        _handler.removeCallbacksAndMessages(null)
    }

    private val task = object : Runnable {
        override fun run() {
            // Do something here
            goToNextNavItem()
            // Reschedule the task to run again in 10 seconds
            _handler.postDelayed(this, TimeUnit.SECONDS.toMillis(10))
        }
    }

    private fun goToNextNavItem() {
        var selectedId = binding.navView.selectedItemId
        var selectedIndex = navItemsArray.indexOf(selectedId)
        var newIndex = (selectedIndex+1) % navItemsArray.size
        var newId = navItemsArray[newIndex]
        binding.navView.selectedItemId = newId
    }

}