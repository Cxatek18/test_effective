package com.team.testeffective.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.testeffective.R
import com.team.testeffective.databinding.FragmentPlugBinding

class PlugFragment : Fragment() {

    private var _binding: FragmentPlugBinding? = null
    private val binding: FragmentPlugBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_plug_fragment)
        )

    // ****** lifecycle *****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlugBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // ****** lifecycle *****

    companion object {
        fun newInstance(): PlugFragment {
            return PlugFragment()
        }
    }
}