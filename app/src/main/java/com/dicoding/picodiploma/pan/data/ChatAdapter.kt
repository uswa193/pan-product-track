package com.dicoding.picodiploma.pan.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.pan.R


class ChatAdapter(private val messages: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_SYSTEM = 2
    }

    override fun getItemViewType(position: Int): Int {
//        return when (messages[position].messageType) {
//            MessageType.USER -> VIEW_TYPE_USER
//            MessageType.SYSTEM -> VIEW_TYPE_SYSTEM
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_user, parent, false)
            UserMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_pan, parent, false)
            SystemMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
//        if (holder is UserMessageViewHolder) {
//            holder.bind(message)
//        } else if (holder is SystemMessageViewHolder) {
//            holder.bind(message)
//        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)

//        fun bind(message: Message) {
//            tvMessage.text = message.text
//            tvTimestamp.text = message.timestamp
//        }
    }

    class SystemMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp)

//        fun bind(message: Message) {
//            tvMessage.text = message.text
//            tvTimestamp.text = message.timestamp
//        }
//    }
    }
}

