package com.pratiksha.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.pratiksha.bookhub.*
import com.pratiksha.bookhub.databinding.ActivityMainBinding
import com.pratiksha.bookhub.fragment.AboutFragment
import com.pratiksha.bookhub.fragment.DashboardFragment
import com.pratiksha.bookhub.fragment.FavouritesFragment
import com.pratiksha.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var previousMenuItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater, null, false)

        setContentView(binding.root)

        setUpToolbar()

        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    binding.drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouritesFragment()).commit()
                    supportActionBar?.title = "Favourites"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment()).commit()
                    supportActionBar?.title = "Profile"
                    binding.drawerLayout.closeDrawers()
                }
                R.id.aboutApp -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment()).commit()
                    supportActionBar?.title = "About App"
                    binding.drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun openDashboard (){
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        binding.navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when (frag){
            !is DashboardFragment -> openDashboard()
            else  -> super.onBackPressed()
        }
    }

}