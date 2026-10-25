package com.example.cyberaigudie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cyberaigudie.databinding.ItemChatMessageBotBinding
import com.example.cyberaigudie.databinding.ItemChatMessageUserBinding

/**
 * Adapter for the chat message list.
 * It shows two kinds of rows: user messages (right side) and AI messages (left side).
 *
 * A short note on notifyDataSetChanged() vs notifyItemInserted():
 * the second one tells the list exactly which row is new, so the
 * animation looks smooth.
 */
class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    override fun getItemViewType(position: Int): Int =
        if (messages[position].isUser) TYPE_USER else TYPE_BOT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            UserViewHolder(ItemChatMessageUserBinding.inflate(inflater, parent, false))
        } else {
            BotViewHolder(ItemChatMessageBotBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is UserViewHolder -> holder.binding.textMessage.text = message.text
            is BotViewHolder -> holder.binding.textMessage.text = message.text
        }
    }

    override fun getItemCount(): Int = messages.size

    /** Adds one message to the end of the chat and shows it. */
    fun add(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    private class UserViewHolder(val binding: ItemChatMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    private class BotViewHolder(val binding: ItemChatMessageBotBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val TYPE_USER = 0
        private const val TYPE_BOT = 1
    }
}
