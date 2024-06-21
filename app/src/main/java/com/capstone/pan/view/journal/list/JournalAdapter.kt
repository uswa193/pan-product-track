package com.capstone.pan.view.journal.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.pan.data.retrofit.response.JournalsItem
import com.dicoding.picodiploma.pan.databinding.ItemJournalBinding

class JournalAdapter(private val itemClickListener: (JournalsItem) -> Unit) : ListAdapter<JournalsItem, JournalAdapter.JournalViewHolder>(
    JournalDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JournalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = getItem(position)
        holder.bind(journal)
        holder.itemView.setOnClickListener { itemClickListener.invoke(journal) }
    }

    inner class JournalViewHolder(private val binding: ItemJournalBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(journal: JournalsItem) {
            binding.textViewNote.text = journal.note ?: ""
            binding.textViewDate.text = journal.date?.toString() ?: ""
            binding.textViewEmotion.text = journal.emotion ?: ""

            // Handling text truncation for textViewNoteBottom
            if (!journal.note.isNullOrEmpty() && journal.note.length > 20) {
                binding.textViewNote.text = journal.note.substring(0, 20) + "...."
            }

            // You can bind other views here if needed
        }
    }

    class JournalDiffCallback : DiffUtil.ItemCallback<JournalsItem>() {
        override fun areItemsTheSame(oldItem: JournalsItem, newItem: JournalsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JournalsItem, newItem: JournalsItem): Boolean {
            return oldItem == newItem
        }
    }
}
