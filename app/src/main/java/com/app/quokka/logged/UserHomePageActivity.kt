package com.app.quokka.logged

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.app.quokka.databinding.ActivityUserHomePageBinding
import android.widget.ImageView
import com.app.quokka.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO overwrite back button to go to LogIn activity

class UserHomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_home_page)

        Log.i("Opened", "UserHomePageActivity")
        // back_arrow is in fact a part of the toolbar that I modified
        val userProfile: ImageView = findViewById(R.id.back_arrow)

        val homeFragment = AvailableTasksBrowser()
        val inProgress = InProgressFragment()
        val myTasks = UserTasksFragment()

        makeCurrentFragment(inProgress)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_in_progress -> makeCurrentFragment(inProgress)
                R.id.ic_my_tasks -> makeCurrentFragment(myTasks)
                R.id.ic_browse -> makeCurrentFragment(homeFragment)
            }
            true
        }

        userProfile.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        val fab: View = findViewById(R.id.fAddTaskButton)
        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        binding.notifyButton.setOnClickListener {
            val intent = Intent(this, FinishedTasksActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}