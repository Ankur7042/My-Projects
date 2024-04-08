package com.example.whatsappclone_chatapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsappclone_chatapplication.Constants
import com.example.whatsappclone_chatapplication.FirebaseRepository
import com.example.whatsappclone_chatapplication.R
import com.example.whatsappclone_chatapplication.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class NumberActivity : AppCompatActivity() {

    private lateinit var binding : ActivityNumberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNumberBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // User already  login so we will go to mainActivity directly
        // on back press user will not be able to get back to this activity again so we will finish it
        if(FirebaseRepository.getInstance().auth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Now user is not registered already
        binding.button.setOnClickListener{
            if(binding.phoneNumber.text!!.isEmpty()){
                Toast.makeText(this,Constants.ENTER_NUMBER_TOAST,Toast.LENGTH_SHORT).show()
            }
            else if(binding.phoneNumber.text!!.length < 10){
                Toast.makeText(this,Constants.ENTER_VALID_NUMBER_TOAST,Toast.LENGTH_SHORT).show()
            }
            // Take the user to the OTP activity
            else{
                val intent = Intent(this,OTPActivity::class.java)
                intent.putExtra(Constants.PHONE_NUMBER,binding.phoneNumber.text!!.toString())
                startActivity(intent)
            }
        }
    }
}