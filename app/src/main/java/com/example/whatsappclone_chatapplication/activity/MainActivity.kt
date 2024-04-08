package com.example.whatsappclone_chatapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.whatsappclone_chatapplication.FirebaseRepository
import com.example.whatsappclone_chatapplication.R
import com.example.whatsappclone_chatapplication.adapter.ViewPagerAdapter
import com.example.whatsappclone_chatapplication.databinding.ActivityMainBinding
import com.example.whatsappclone_chatapplication.ui.CallFragment
import com.example.whatsappclone_chatapplication.ui.ChatFragment
import com.example.whatsappclone_chatapplication.ui.StatusFragment
import com.example.whatsappclone_chatapplication.viewmodels.MainViewModel

//class MainActivity : AppCompatActivity() {
//    private lateinit var binding : ActivityMainBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding=ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        //Code Review: use Nav Graph
//        val fragmentArrayList =ArrayList<Fragment>()
//
//        fragmentArrayList.add(ChatFragment())
//        fragmentArrayList.add(StatusFragment())
//        fragmentArrayList.add(CallFragment())
//
//        //Code Review: make Singleton
//
//        // User is not login we will go to Number directly
//        // on back press user will not be able to get back to this activity again so we will finish it
//        if(FirebaseRepository.getInstance().auth.currentUser == null){
//            startActivity(Intent(this,NumberActivity::class.java))
//            finish()
//        }
//
//        // Code Review: Make separate functions
//        val adapter = ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
//        binding.viewPager.adapter=adapter
//        binding.tabs.setupWithViewPager(binding.viewPager)
//
//    }
//
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        finishAffinity()
//    }
//}


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Observe the login status
        viewModel.isLoggedIn.observe(this) { isLoggedIn ->
            if (!isLoggedIn) {
                // User is not logged in, navigate to NumberActivity
                startActivity(Intent(this, NumberActivity::class.java))
                finish()
            }
        }

        // Setup ViewPager and Tabs
        setupViewPager()
    }


    private fun setupViewPager() {
        val fragmentArrayList = arrayListOf<Fragment>(
            ChatFragment(),
            StatusFragment(),
            CallFragment()
        )

        val adapter = ViewPagerAdapter(this, supportFragmentManager, fragmentArrayList)
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
