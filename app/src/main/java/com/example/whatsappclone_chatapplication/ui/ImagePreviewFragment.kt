package com.example.whatsappclone_chatapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.whatsappclone_chatapplication.utils.Constants
import com.example.whatsappclone_chatapplication.repository.FirebaseRepository
import com.example.whatsappclone_chatapplication.activity.ChatActivity
import com.example.whatsappclone_chatapplication.activity.NumberActivity
import com.example.whatsappclone_chatapplication.databinding.FragmentImagePreviewBinding


class ImagePreviewFragment : Fragment() {

    private lateinit var binding : FragmentImagePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding = FragmentImagePreviewBinding.inflate(inflater, container, false)

        // Load the image
        val imageUrl = arguments?.getString(Constants.URL_PHOTO)
        Glide.with(this).load(imageUrl).centerCrop().into(binding.imagePreview)

        // Handle back button press
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as ChatActivity).showChatActivityLayout()
        }

        // Sign Out logic
        binding.signOut.setOnClickListener {
            signOutTheUser()
        }


        // Handling the back button provided by the android
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as ChatActivity).showChatActivityLayout()
        }

        return binding.root
    }


    private fun signOutTheUser() {
        // Clear the back stack of the fragment manager
        requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Sign out the user from Firebase Authentication
        FirebaseRepository.getInstance().auth.signOut()

        // Start the NumberActivity to handle the login process
        val intent = Intent(requireContext(), NumberActivity::class.java)
        startActivity(intent)

        // Finish all other activities to ensure only NumberActivity remains open
        requireActivity().finishAffinity()
    }


}


