package com.example.mvisamplecoroutines.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mvisamplecoroutines.databinding.ActivityNavigatorBinding
import com.example.mvisamplecoroutines.ui.activity.main.TeamBuildingActivity
import com.example.mvisamplecoroutines.ui.activity.wallpaper.WallPaperActivity
import com.example.mvisamplecoroutines.utils.clicks
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class NavigatorActivity : AppCompatActivity() {
    private val binding by lazy { ActivityNavigatorBinding.inflate(layoutInflater) }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        merge(
            binding.teamBuildingApp.clicks()
                .map { TeamBuildingActivity.getInstance(this) },
            binding.wallPaperApp.clicks()
                .map { WallPaperActivity.getInstance(this) }
        )
            .onEach { startActivity(it) }
            .launchIn(lifecycleScope)
    }
}