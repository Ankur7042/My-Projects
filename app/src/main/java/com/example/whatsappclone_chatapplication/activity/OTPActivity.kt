package com.example.whatsappclone_chatapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.whatsappclone_chatapplication.Constants
import com.example.whatsappclone_chatapplication.FirebaseRepository
import com.example.whatsappclone_chatapplication.R
import com.example.whatsappclone_chatapplication.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOtpactivityBinding
    private lateinit var verificationId : String
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val builder = AlertDialog.Builder(this)
        builder.setMessage(Constants.DIALOG_BOX_MESSAGE)
        builder.setTitle(Constants.DIALOG_BOX_TITLE)
        builder.setCancelable(false)

        dialog =  builder.create()
        dialog.show()


        val phoneNumber = "+91" + intent.getStringExtra(Constants.PHONE_NUMBER)

        val options = PhoneAuthOptions.newBuilder(FirebaseRepository.getInstance().auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    dialog.dismiss()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@OTPActivity,"${p0.suppressedExceptions}",Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                override fun onCodeSent(VerificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(VerificationId, p1)
                    dialog.dismiss()
                    verificationId = VerificationId
                }

            }).build()

        // sending the otp to the user
        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.button.setOnClickListener{
            if(binding.otp.text!!.isEmpty()){
                Toast.makeText(this,Constants.ENTER_OTP_TOAST,Toast.LENGTH_SHORT).show()
            }
            else
            {
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(verificationId,binding.otp.text.toString())

                FirebaseRepository.getInstance().auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            dialog.dismiss()
                            startActivity(Intent(this,ProfileActivity::class.java))
                            finish()
                        }
                        else{
                            dialog.dismiss()
                            Toast.makeText(this,"Error ${it.exception}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }
}