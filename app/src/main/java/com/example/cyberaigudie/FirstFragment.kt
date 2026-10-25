package com.example.cyberaigudie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cyberaigudie.databinding.FragmentFirstBinding

/**
 * The Home / Dashboard screen (start destination of the app).
 * Each feature card opens one part of the app using Navigation actions.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Open each feature when its card is tapped.
        binding.cardMessageChecker.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_messageCheckerFragment)
        }

        binding.cardUrlChecker.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_urlCheckerFragment)
        }

        binding.cardChat.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_chatFragment)
        }

        binding.cardGuide.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_guideFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
