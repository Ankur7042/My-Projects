package com.example.whatsappclone_chatapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.whatsappclone_chatapplication.Constants
import com.example.whatsappclone_chatapplication.R
import com.example.whatsappclone_chatapplication.activity.ChatActivity
import com.example.whatsappclone_chatapplication.databinding.ChatUserItemLayoutBinding
import com.example.whatsappclone_chatapplication.model.UserModel

class ChatAdapter(var context : Context, private var list: ArrayList<UserModel>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(view : View) :RecyclerView.ViewHolder(view){
        var binding :ChatUserItemLayoutBinding = ChatUserItemLayoutBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
       return ChatViewHolder(LayoutInflater.from(parent.context)
           .inflate(R.layout.chat_user_item_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val user = list[position]
        Glide.with(context).load(user.imageUrl).into(holder.binding.userImage)
        holder.binding.userName.text = user.name

        // i am using the whatsapp and i clicked on a user so ,now i will the id of the user to whom i am sending the messager
        holder.itemView.setOnClickListener {
            val intent = Intent(context ,ChatActivity::class.java)
            intent.putExtra(Constants.INTENT_EXTRA_UID,user.uid)
            intent.putExtra(Constants.INTENT_EXTRA_NAME,user.name)
            intent.putExtra(Constants.INTENT_EXTRA_IMAGE_URL,user.imageUrl)
            context.startActivity(intent)
        }
    }
}