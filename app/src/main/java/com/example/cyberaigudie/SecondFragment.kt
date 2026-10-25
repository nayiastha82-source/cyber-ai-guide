package com.example.cyberaigudie

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cyberaigudie.databinding.FragmentSecondBinding
import com.example.cyberaigudie.databinding.ItemRiskReasonBinding

/**
 * The Risk Result screen, shared by the Message Checker and the URL Checker.
 *
 * The checker screens send the result here using navigation arguments
 * (a simple Bundle — see the keys in the companion object below).
 *
 * IMPORTANT: this screen shows an awareness result, never a guaranteed verdict.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Read the result values sent by the checker screen.
        val args = arguments ?: Bundle()
        val type = args.getString(ARG_TYPE, TYPE_MESSAGE)
        val level = try {
            RiskLevel.valueOf(args.getString(ARG_LEVEL, RiskLevel.LOW.name))
        } catch (e: IllegalArgumentException) {
            RiskLevel.LOW
        }
        val score = args.getInt(ARG_SCORE, 0)
        val reasons = args.getStringArrayList(ARG_REASONS) ?: arrayListOf()
        val recommendation = args.getString(ARG_RECOMMENDATION, RiskLevel.RECOMMENDATION_LOW)
        val summary = args.getString(ARG_SUMMARY, "")

        // 2. Colors and texts depend on the risk level.
        //    Triple holds: header color, level text, small label text.
        val (colorRes, levelText, labelText) = when (level) {
            RiskLevel.HIGH -> Triple(
                R.color.danger,
                getString(R.string.risk_high),
                getString(R.string.risk_label_detected)
            )
            RiskLevel.MEDIUM -> Triple(
                R.color.warning,
                getString(R.string.risk_medium),
                getString(R.string.risk_label_detected)
            )
            RiskLevel.LOW -> Triple(
                R.color.success,
                getString(R.string.risk_low),
                getString(R.string.risk_label_complete)
            )
        }

        // 3. Fill the big colored header.
        val headerColor = ContextCompat.getColor(requireContext(), colorRes)
        binding.layoutRiskHeader.background?.mutate()?.setTint(headerColor)
        binding.textRiskLevelLabel.text = labelText
        binding.textRiskLevelLabel.setTextColor(Color.WHITE)
        binding.textRiskLevel.text = levelText
        binding.textRiskLevel.setTextColor(Color.WHITE)
        binding.textRiskScore.text = getString(R.string.risk_score_format, score)
        binding.textRiskScore.setTextColor(Color.WHITE)

        // 4. Show the detected warning signs, or "no signs" when the list is empty.
        if (reasons.isEmpty()) {
            binding.textNoReasons.visibility = View.VISIBLE
        } else {
            binding.textNoReasons.visibility = View.GONE
            for (reason in reasons) {
                val row = ItemRiskReasonBinding.inflate(layoutInflater, binding.containerReasons, false)
                row.textReason.text = reason
                binding.containerReasons.addView(row.root)
            }
        }

        // 5. Fill the recommended action and the summary of what was checked.
        binding.textRecommendation.text = recommendation
        binding.textCheckedSummary.text = summary

        // 6. The button text depends on which checker sent us here.
        binding.buttonCheckAnother.text = if (type == TYPE_URL) {
            getString(R.string.button_check_another_url)
        } else {
            getString(R.string.button_check_another_message)
        }

        // 7. Go back to the checker screen to check something else.
        binding.buttonCheckAnother.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Keys for the Bundle that carries the result between screens.
        const val ARG_TYPE = "type"
        const val ARG_LEVEL = "level"
        const val ARG_SCORE = "score"
        const val ARG_REASONS = "reasons"
        const val ARG_RECOMMENDATION = "recommendation"
        const val ARG_SUMMARY = "summary"

        // Which checker sent the result.
        const val TYPE_MESSAGE = "message"
        const val TYPE_URL = "url"
    }
}
