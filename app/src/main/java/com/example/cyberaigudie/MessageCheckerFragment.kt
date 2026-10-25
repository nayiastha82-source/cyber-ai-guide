package com.example.cyberaigudie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cyberaigudie.databinding.FragmentMessageCheckerBinding

/**
 * The Message Checker screen.
 * The user pastes a suspicious message, [MessageAnalyzer] checks it on the
 * phone, and the result is shown on the Risk Result screen (SecondFragment).
 */
class MessageCheckerFragment : Fragment() {

    private var _binding: FragmentMessageCheckerBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val analyzer = MessageAnalyzer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageCheckerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCheckMessage.setOnClickListener {
            val message = binding.editMessage.text?.toString()?.trim().orEmpty()

            // Simple validation: the message must not be empty.
            if (message.isEmpty()) {
                binding.inputLayoutMessage.error = getString(R.string.error_empty_message)
                return@setOnClickListener
            }
            binding.inputLayoutMessage.error = null

            // Analyze the message with the rule-based analyzer.
            val result = analyzer.analyze(message)

            // Send the result to the Risk Result screen.
            val resultBundle = Bundle().apply {
                putString(SecondFragment.ARG_TYPE, SecondFragment.TYPE_MESSAGE)
                putString(SecondFragment.ARG_LEVEL, result.level.name)
                putInt(SecondFragment.ARG_SCORE, result.score)
                putStringArrayList(SecondFragment.ARG_REASONS, ArrayList(result.reasons))
                putString(SecondFragment.ARG_RECOMMENDATION, result.recommendation)
                putString(SecondFragment.ARG_SUMMARY, result.checkedText)
            }
            findNavController().navigate(
                R.id.action_messageCheckerFragment_to_SecondFragment,
                resultBundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
