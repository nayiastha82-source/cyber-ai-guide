package com.example.cyberaigudie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cyberaigudie.databinding.FragmentGuideBinding
import com.example.cyberaigudie.databinding.ItemGuideSectionBinding

/**
 * The Cyber Safety Guide screen.
 * It shows the ten short lessons from [CyberSafetyData] as cards.
 */
class GuideFragment : Fragment() {

    private var _binding: FragmentGuideBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Build one card per guide section and add it to the container.
        for (section in CyberSafetyData.sections) {
            val card = ItemGuideSectionBinding.inflate(layoutInflater, binding.guideContainer, false)
            card.textSectionTitle.text = section.title
            card.textSectionExplanation.text = section.explanation
            card.textSectionTip.text = getString(R.string.guide_tip_label, section.tip)
            binding.guideContainer.addView(card.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
