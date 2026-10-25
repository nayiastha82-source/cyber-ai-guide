package com.example.cyberaigudie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cyberaigudie.databinding.FragmentChatBinding

/**
 * The AI Cyber Chat screen.
 *
 * This Fragment only handles the user interface. The AI logic itself lives
 * in CyberAiRepository, which uses MockCyberAi (demo mode) or
 * OpenAiCompatibleApi (when an API key is configured).
 */
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val chatAdapter = ChatAdapter()

    // True while we are waiting for the AI answer (prevents double messages).
    private var waitingForReply = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the message list.
        binding.chatMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.chatMessages.adapter = chatAdapter

        // Start the conversation with a greeting.
        chatAdapter.add(ChatMessage(getString(R.string.chat_greeting), isUser = false))

        // In demo mode, tell the user how the chat is working.
        if (CyberAiRepository.isDemoMode) {
            chatAdapter.add(ChatMessage(getString(R.string.chat_demo_notice), isUser = false))
        }
        scrollToBottom()

        binding.buttonSend.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val question = binding.editChatMessage.text?.toString()?.trim().orEmpty()
        if (question.isEmpty() || waitingForReply) return

        // 1. Show the user's message in the list.
        chatAdapter.add(ChatMessage(question, isUser = true))
        binding.editChatMessage.text?.clear()
        scrollToBottom()

        // 2. Ask the AI and show its answer when it arrives.
        waitingForReply = true
        CyberAiRepository.ask(question) { answer ->
            // The answer always arrives on the main thread.
            // The screen may already be closed by then, so use the safe call.
            chatAdapter.add(ChatMessage(answer, isUser = false))
            waitingForReply = false
            _binding?.chatMessages?.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    private fun scrollToBottom() {
        binding.chatMessages.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
