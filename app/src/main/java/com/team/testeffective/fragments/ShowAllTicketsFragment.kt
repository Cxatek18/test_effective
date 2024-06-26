package com.team.testeffective.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.team.testeffective.R
import com.team.testeffective.adapter_delegation.TicketDiffCallback
import com.team.testeffective.adapter_delegation.ticketDelegate
import com.team.testeffective.databinding.FragmentShowAllTicketsBinding
import com.team.testeffective.navigate.NavigateHelper
import com.team.testeffective.view_models.ShowAllTicketsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@RequiresApi(Build.VERSION_CODES.O)
class ShowAllTicketsFragment : Fragment() {

    private var _binding: FragmentShowAllTicketsBinding? = null
    private val binding: FragmentShowAllTicketsBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_show_all_tickets_fragment)
        )


    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private lateinit var numberDay: String
    private lateinit var month: String

    private val showAllTicketsViewModel by viewModel<ShowAllTicketsViewModel>()

    private val adapter by lazy {
        AsyncListDifferDelegationAdapter(
            TicketDiffCallback(),
            ticketDelegate()
        )
    }


    // ****** lifecycle *****
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowAllTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArgs()
        binding.ticketList.adapter = adapter
        setPassengerWay()
        onClickListenerBtnBack()
        showAllTicketsViewModel.getTicketList()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // ****** lifecycle *****

    private fun observeViewModel() {
        lifecycleScope.launch {
            showAllTicketsViewModel.ticketList.flowWithLifecycle(lifecycle)
                .collect { ticketList ->
                    adapter.items = ticketList
                }
        }

        lifecycleScope.launch {
            showAllTicketsViewModel.isOnline.flowWithLifecycle(lifecycle)
                .collect { isOnline ->
                    if (!isOnline) {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.text_error),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun onClickListenerBtnBack() {
        binding.btnBack.setOnClickListener {
            navigateHelper.navigateTo(SearchPlaceSelectedFragment.newInstance())
        }
    }

    private fun setPassengerWay() {
        binding.passengerWay.text = "${getValueFromCache()}-${getValueToCache()}"
        formattingCorrectMonth()
        binding.passengerInfo.text = "$numberDay $month, 1 пассажир"
    }

    private fun formattingCorrectMonth() {
        when (month) {
            getString(R.string.abbreviated_text_january) -> {
                month = getString(R.string.full_text_january)
            }

            getString(R.string.abbreviated_text_february) -> {
                month = getString(R.string.full_text_february)
            }

            getString(R.string.abbreviated_text_march) -> {
                month = getString(R.string.full_text_march)
            }

            getString(R.string.abbreviated_text_april) -> {
                month = getString(R.string.full_text_april)
            }

            getString(R.string.abbreviated_text_may) -> {
                month = getString(R.string.full_text_may)
            }

            getString(R.string.abbreviated_text_june) -> {
                month = getString(R.string.full_text_june)
            }

            getString(R.string.abbreviated_text_july) -> {
                month = getString(R.string.full_text_july)
            }

            getString(R.string.abbreviated_text_august) -> {
                month = getString(R.string.full_text_august)
            }

            getString(R.string.abbreviated_text_september) -> {
                month = getString(R.string.full_text_september)
            }

            getString(R.string.abbreviated_text_october) -> {
                month = getString(R.string.full_text_october)
            }

            getString(R.string.abbreviated_text_november) -> {
                month = getString(R.string.full_text_november)
            }

            getString(R.string.abbreviated_text_december) -> {
                month = getString(R.string.full_text_december)
            }
        }
    }

    private fun getValueFromCache(): String {
        val sharedPref = requireContext().getSharedPreferences(
            MainFragment.KEY_CACHE_VALUE_FROM, Context.MODE_PRIVATE
        )
        return sharedPref.getString(MainFragment.KEY_CACHE_VALUE_FROM, "") ?: ""
    }

    private fun getValueToCache(): String {
        val sharedPref = requireContext().getSharedPreferences(
            MainFragment.KEY_CACHE_VALUE_TO, Context.MODE_PRIVATE
        )
        return sharedPref.getString(MainFragment.KEY_CACHE_VALUE_TO, "") ?: ""
    }

    private fun parseArgs() {
        val args = requireArguments()
        numberDay = args.getString(EXTRA_STRING_NUMBER_DAY).toString()
        month = args.getString(EXTRA_STRING_MONTH).toString()
    }

    companion object {

        private const val EXTRA_STRING_NUMBER_DAY = "number_day"
        private const val EXTRA_STRING_MONTH = "month"

        fun newInstance(
            numberDay: String,
            month: String,
        ): ShowAllTicketsFragment {
            return ShowAllTicketsFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_STRING_NUMBER_DAY, numberDay)
                    putString(EXTRA_STRING_MONTH, month)
                }
            }
        }
    }
}