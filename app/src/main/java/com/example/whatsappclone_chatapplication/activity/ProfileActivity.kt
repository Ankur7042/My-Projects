package com.example.whatsappclone_chatapplication.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone_chatapplication.Constants
import com.example.whatsappclone_chatapplication.FirebaseRepository
import com.example.whatsappclone_chatapplication.databinding.ActivityProfileBinding
import com.example.whatsappclone_chatapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private lateinit var storage : FirebaseStorage
    private lateinit var selectedImage : Uri
    private lateinit var dialog : AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()

        binding.userImage.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        binding.continueBtn.setOnClickListener{
            if(binding.userName.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your name",Toast.LENGTH_SHORT).show()
            }
            else{
                uploadData()
                binding.progressBar.visibility = View.VISIBLE
            }
        }

    }

    private fun uploadData() {
        val reference = storage.reference.child(Constants.PROFILE_NODE).child(Date().time.toString())
        reference.putFile(selectedImage).addOnCompleteListener {
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }

        }
    }

    // saving the data of the user in the realtime database

    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(FirebaseRepository.getInstance().auth.uid.toString(),binding.userName.text.toString(),FirebaseRepository.getInstance().auth.currentUser!!.phoneNumber.toString(),imgUrl)
        FirebaseRepository.getInstance().database.reference.child(Constants.USERS_NODE)
            .child(FirebaseRepository.getInstance().auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Data inserted",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                binding.progressBar.visibility = View.GONE
                finish()
            }
    }

    // When we entered into the library then we come in this function . Now we will check whether user have selcted something or not , if yes then we will check
    // whether the imageUrl that is selected is null or not

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null){

            // checking whether image url is null or not
            if(data.data != null){
                selectedImage = data.data!!
                binding.userImage.setImageURI(selectedImage)
            }
        }
    }
}