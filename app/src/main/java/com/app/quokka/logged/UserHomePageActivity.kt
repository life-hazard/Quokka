package com.app.quokka.logged

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.app.quokka.databinding.ActivityUserHomePageBinding
import android.widget.ImageView
import android.widget.Toast
import com.app.quokka.AddTaskScreen
import com.app.quokka.R
import com.app.quokka.unlogged.LogInActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

// TODO overwrite back button to go to LogIn activity

class UserHomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_home_page)

        // back_arrow is in fact a part of the toolbar that I modified
        val userProfile: ImageView = findViewById(R.id.back_arrow)

        val homeFragment = AvailableTasksBrowser()
        val addTask = AddTaskScreen()
        val inProgress = InProgressFragment()
        val myTasks = UserTasksFragment()
        val user = UserProfile()

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
            //view: View ->
            //view.findNavController().navigate(R.id.action_userMainScreenFragment_to_userProfile)
            Toast.makeText(applicationContext, "User", Toast.LENGTH_SHORT).show()
            //makeCurrentFragment(user)
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

            val fab: View = findViewById(R.id.fAddTaskButton)
            fab.setOnClickListener { view ->
                //view.findNavController().navigate(R.id.action_userMainScreenFragment_to_addTaskScreen)
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .show()
                val intent = Intent(this, AddTaskActivity::class.java)
                startActivity(intent)
            }
    }

    private fun makeCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.fl_wrapper, fragment)
        commit()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}