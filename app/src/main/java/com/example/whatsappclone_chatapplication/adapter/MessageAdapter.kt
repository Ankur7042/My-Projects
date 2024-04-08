package com.example.whatsappclone_chatapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone_chatapplication.FirebaseRepository
import com.example.whatsappclone_chatapplication.R
import com.example.whatsappclone_chatapplication.databinding.ReceiverLayoutBinding
import com.example.whatsappclone_chatapplication.databinding.SentItemLayoutBinding
import com.example.whatsappclone_chatapplication.model.MessageModel
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(var context: Context, private var list: ArrayList<MessageModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemSent = 1
    private var itemReceive = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == itemSent)
            SentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.sent_item_layout, parent, false)
            )
        else ReceiverViewHolder(
            LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (FirebaseRepository.getInstance().auth.uid == list[position].senderId) itemSent else itemReceive
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageSavedToDatabase = list[position]
        if (holder.itemViewType == itemSent) {
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.userMessage.text = messageSavedToDatabase.message
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.userMessage.text = messageSavedToDatabase.message
        }
    }

    inner class SentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = SentItemLayoutBinding.bind(view)
    }

    inner class ReceiverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = ReceiverLayoutBinding.bind(view)
    }
}