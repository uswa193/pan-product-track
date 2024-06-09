package com.dicoding.picodiploma.pan.ui





import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.pan.R
import com.dicoding.picodiploma.pan.data.ChatAdapter
import com.dicoding.picodiploma.pan.data.Message
import com.dicoding.picodiploma.pan.data.MessageType

class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Example data
        val messages = listOf(
            Message(1, MessageType.SYSTEM, "Welcome to the chat!", "Just now"),
            Message(2, MessageType.USER, "Hello, how are you?", "Just now")
        )
        adapter = ChatAdapter(messages)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        messageRecyclerView.layoutManager = LinearLayoutManager(context)
        messageRecyclerView.adapter = adapter

        return view
    }
}
