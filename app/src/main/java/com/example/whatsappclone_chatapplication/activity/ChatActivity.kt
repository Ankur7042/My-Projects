package com.example.whatsappclone_chatapplication.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.whatsappclone_chatapplication.utils.Constants
import com.example.whatsappclone_chatapplication.repository.FirebaseRepository
import com.example.whatsappclone_chatapplication.R
import com.example.whatsappclone_chatapplication.adapter.MessageAdapter
import com.example.whatsappclone_chatapplication.databinding.ActivityChatBinding
import com.example.whatsappclone_chatapplication.model.MessageModel
import com.example.whatsappclone_chatapplication.ui.ImagePreviewFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private lateinit var senderUid: String
    private lateinit var receiverUid: String

    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String

    private lateinit var list: ArrayList<MessageModel>

    private var isSendingMessage = false // Flag to track if a message is currently being sent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        senderUid = FirebaseRepository.getInstance().auth.uid.toString()
        // Code Review: Don't use force null wrap and put these strings in constant
        receiverUid = intent.getStringExtra(Constants.INTENT_EXTRA_UID).toString()


        binding.name.text = intent.getStringExtra(Constants.INTENT_EXTRA_NAME)!!

        val imageUrlReceived = intent.getStringExtra(Constants.INTENT_EXTRA_IMAGE_URL)

        // Now if i will click on the image photo then a preview should be opened.so i will pass the imageUrlReceived to the
        // new fragment named as image preview fragment
        binding.profileImage.setOnClickListener {
            loadFragment(imageUrlReceived!!)
        }

        Glide.with(this).load(imageUrlReceived).into(binding.profileImage)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this@ChatActivity, MainActivity::class.java))
            finish()
        }


        list = ArrayList()

        // Creating a sender room and a receiver room . Whenever a user sends a message to anyone then that message stored in the
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid


        binding.imageView.setOnClickListener {
            if (!isSendingMessage && binding.messageBox.text.isNotEmpty()) {
                isSendingMessage = true
                sendMessage(binding.messageBox.text.toString())
            } else {
                Toast.makeText(this, Constants.ENTER_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }


//        //Code Review: Use ViewModel


        //Getting the data from the database i.e chats -> senderRoom -> messages
        FirebaseRepository.getInstance().database.reference.child(Constants.CHAT_NODE)
            .child(senderRoom)
            .child(Constants.MESSAGES_NODE)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.messageBox.text.clear()
                    list.clear()

                    // traversing on all nodes of the messages and retrieving the MessageModel and then adding in the list and then passing it to the
                    // Recycler view
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }

                    binding.recyclerView.adapter = MessageAdapter(this@ChatActivity, list)
                    binding.recyclerView.scrollToPosition(list.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@ChatActivity,
                        Constants.DATABASE_ERROR + error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }


    private fun sendMessage(messageText: String) {
        val message = MessageModel(messageText, senderUid, Date().time)
        val randomKey = FirebaseRepository.getInstance().database.reference.push().key

        FirebaseRepository.getInstance().database.reference.child(Constants.CHAT_NODE)
            .child(senderRoom)
            .child(Constants.MESSAGES_NODE).child(randomKey!!)
            .setValue(message)
            .addOnSuccessListener {
                FirebaseRepository.getInstance().database.reference.child(Constants.CHAT_NODE)
                    .child(receiverRoom)
                    .child(Constants.MESSAGES_NODE).child(randomKey)
                    .setValue(message)
                    .addOnSuccessListener {
                        Toast.makeText(this, Constants.MESSAGE_SENT_SUCCESS, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, Constants.MESSAGE_SEND_FAILED, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnCompleteListener {
                        isSendingMessage = false
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, Constants.MESSAGE_SEND_FAILED, Toast.LENGTH_SHORT).show()
                isSendingMessage = false
            }
    }


    private fun loadFragment(imageUrlReceived: String) {
        val fragment = ImagePreviewFragment()

        val bundle = Bundle()
        bundle.putString(Constants.URL_PHOTO, imageUrlReceived)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()

        // Make the container visible
        binding.container.visibility = View.VISIBLE

        // Hide other UI elements
        hideChatActivityLayout()
    }

    private fun hideChatActivityLayout() {
        binding.userInfo.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.cardView.visibility = View.GONE
    }

    fun showChatActivityLayout() {
        binding.userInfo.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.imageView.visibility = View.VISIBLE
        binding.cardView.visibility = View.VISIBLE
    }
}