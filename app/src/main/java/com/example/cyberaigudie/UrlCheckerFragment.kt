package com.example.cyberaigudie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cyberaigudie.databinding.FragmentUrlCheckerBinding

/**
 * The URL Checker screen.
 * The user types a website link, [UrlAnalyzer] checks the link text on the
 * phone (the link is NEVER opened), and the result is shown on the
 * Risk Result screen (SecondFragment).
 */
class UrlCheckerFragment : Fragment() {

    private var _binding: FragmentUrlCheckerBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val analyzer = UrlAnalyzer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUrlCheckerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCheckUrl.setOnClickListener {
            val url = binding.editUrl.text?.toString()?.trim().orEmpty()

            // Simple validation: not empty and looks like an address.
            if (url.isEmpty()) {
                binding.inputLayoutUrl.error = getString(R.string.error_empty_url)
                return@setOnClickListener
            }
            if (!url.contains(".")) {
                binding.inputLayoutUrl.error = getString(R.string.error_invalid_url)
                return@setOnClickListener
            }
            binding.inputLayoutUrl.error = null

            // Analyze the URL text with the rule-based analyzer.
            val result = analyzer.analyze(url)

            // Send the result to the Risk Result screen.
            val resultBundle = Bundle().apply {
                putString(SecondFragment.ARG_TYPE, SecondFragment.TYPE_URL)
                putString(SecondFragment.ARG_LEVEL, result.level.name)
                putInt(SecondFragment.ARG_SCORE, result.score)
                putStringArrayList(SecondFragment.ARG_REASONS, ArrayList(result.reasons))
                putString(SecondFragment.ARG_RECOMMENDATION, result.recommendation)
                putString(SecondFragment.ARG_SUMMARY, result.checkedText)
            }
            findNavController().navigate(
                R.id.action_urlCheckerFragment_to_SecondFragment,
                resultBundle
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
