package com.example.project2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(MapsFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentFrame, fragment)
        fragmentTransaction.commit()
    }


    //    Menu options
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_map -> {
                loadFragment(MapsFragment());
                true
            }

            R.id.option_place -> {
                loadFragment(PlaceFragment())
                return true
            }

            R.id.option_email -> {
                loadFragment(EmailFragment())
                return true
            }

            R.id.option_about -> {
                loadFragment(AboutFragment())
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}