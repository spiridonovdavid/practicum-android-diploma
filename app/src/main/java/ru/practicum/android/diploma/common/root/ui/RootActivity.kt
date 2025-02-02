package ru.practicum.android.diploma.common.root.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.filter.presentation.viewmodel.FilterScreenViewModel
import ru.practicum.android.diploma.filter.presentation.viewmodel.RegionFilterViewModel

class RootActivity : AppCompatActivity() {
    val viewModel by viewModel<RegionFilterViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRootBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.isVisible =
                destination.id == R.id.searchFragment
                || destination.id == R.id.favoritesFragment
                || destination.id == R.id.teamFragment
        }

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)


        viewModel.loadRegions()
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

}
