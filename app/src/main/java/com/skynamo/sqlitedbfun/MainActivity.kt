package com.skynamo.sqlitedbfun

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.skynamo.sqlitedbfun.data.dao.DatabaseHelper
import com.skynamo.sqlitedbfun.databinding.ActivityMainBinding
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Copy shipped db if needed
        val dbFile = applicationContext.getDatabasePath(DatabaseHelper.DATABASE_NAME)
        if (!dbFile.exists()) {
            val inputStream = applicationContext.assets.open(DatabaseHelper.DATABASE_NAME)
            val outputStream = FileOutputStream(dbFile)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        }

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


}