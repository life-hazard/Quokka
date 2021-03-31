package com.app.quokka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
//import androidx.navigation.findNavController
//import androidx.navigation.ui.NavigationUI
import com.app.quokka.databinding.ActivityMainBinding
//import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }

}