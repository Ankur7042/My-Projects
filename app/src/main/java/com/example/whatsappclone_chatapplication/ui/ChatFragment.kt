package com.example.whatsappclone_chatapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.whatsappclone_chatapplication.Constants
import com.example.whatsappclone_chatapplication.FirebaseRepository
import com.example.whatsappclone_chatapplication.adapter.ChatAdapter
import com.example.whatsappclone_chatapplication.databinding.FragmentChatBinding
import com.example.whatsappclone_chatapplication.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    lateinit var userList: ArrayList<UserModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)

        userList = ArrayList()

        FirebaseRepository.getInstance().database.reference.child(Constants.USERS_NODE)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    // Clearing the old data so that there will be no Duplicate. see i am fetching the data everytime from the database.
                    //so if in previous list there is one Ankur and now i added the himanshu and again fetched the data without clearing the
                    // list , then there will be 2 Ankur in the list
                    userList.clear()

                    //iterating through the children of the snapshot (which represents the data under the users node).
                    for (snapshot1 in snapshot.children) {
                        val user = snapshot1.getValue(UserModel::class.java)

                        // For each child, you’re extracting a UserModel object and checking if its uid is different from
                        // the current user’s uid. If so, you add it to the userList.
                        if (user!!.uid != FirebaseRepository.getInstance().auth.uid) {
                            userList.add(user)
                        }
                    }

                    binding.userListRecyclerView.adapter = ChatAdapter(requireContext() , userList)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        return binding.root
    }


}