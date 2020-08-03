package com.example.mvisamplecoroutines.ui.activity.wallpaper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mvisamplecoroutines.R
import com.example.mvisamplecoroutines.data.source.locale.preferences.SharePreferences
import com.example.mvisamplecoroutines.databinding.ActivityMainWallpaperBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class WallPaperActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences: SharePreferences
    private val binding by lazy { ActivityMainWallpaperBinding.inflate(layoutInflater) }
    private val navController by lazy(NONE) { initNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentFragment = navController.currentDestination
        when (currentFragment?.id) {
            R.id.listWallpaperFragment -> finish()
            else -> navController.popBackStack()
        }
    }

    private fun initNavController() =
        supportFragmentManager.findFragmentById(binding.navHostContainerWallpaper.id)
            ?.findNavController() ?: findNavController(binding.navHostContainerWallpaper.id)

    companion object {
        private val TAG = this::class.java.simpleName
        fun getInstance(context: Context): Intent {
            return Intent(context, WallPaperActivity::class.java)
        }
    }
}