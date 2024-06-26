package com.team.testeffective.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.team.testeffective.R
import com.team.testeffective.adapter_delegation.RecommendedOfferDiffCallback
import com.team.testeffective.adapter_delegation.recommendedOfferDelegate
import com.team.testeffective.databinding.FragmentSearchPlaceSelectedBinding
import com.team.testeffective.navigate.NavigateHelper
import com.team.testeffective.view_models.SearchPlaceSelectedViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchPlaceSelectedFragment : Fragment() {

    private var _binding: FragmentSearchPlaceSelectedBinding? = null
    private val binding: FragmentSearchPlaceSelectedBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_search_place_selected_place_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private val searchPlaceSelectedViewModel by viewModel<SearchPlaceSelectedViewModel>()

    private val glide by lazy { Glide.with(this) }

    private val adapter by lazy {
        AsyncListDifferDelegationAdapter(
            RecommendedOfferDiffCallback(),
            recommendedOfferDelegate(glide)
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
        _binding = FragmentSearchPlaceSelectedBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fromPlace.setText(getValueFromCache())
        binding.toPlace.setText(getValueToCache())
        searchPlaceSelectedViewModel.getRecommendedOfferList()
        binding.listRecommendedOffer.adapter = adapter
        observeViewModel()
        clearEditTextToPlace()
        changeValue()
        listeningOnBackPressed()
        onClickBackArrowListener()
        val calendar = Calendar.getInstance(
            TimeZone.getTimeZone(getString(R.string.text_time_zone_msk))
        )
        getCurrentDay(calendar, binding.dayDepartures)
        getCurrentMonth(calendar, binding.monthDepartures)
        getCurrentDayNumber()
        onClickListenerDatePlace(
            binding.blockBackPlace,
        ) { calendar ->
            updateDateBackPlace(calendar)
        }

        onClickListenerDatePlace(
            binding.blockBackPlaceDate,
        ) { calendar ->
            updateBlockDateBackPlace(calendar)
        }

        onClickListenerDatePlace(
            binding.blockDateDepartures,
        ) { calendar ->
            updateBlockDateDepartures(calendar)
        }

        onClickBtnShowAllTickets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // ****** lifecycle *****

    private fun observeViewModel() {
        lifecycleScope.launch {
            searchPlaceSelectedViewModel.recommendedOfferList.flowWithLifecycle(lifecycle)
                .collect { listRecommendedOffer ->
                    adapter.items = listRecommendedOffer.take(3)
                }
        }

        lifecycleScope.launch {
            searchPlaceSelectedViewModel.isOnline.flowWithLifecycle(lifecycle)
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

    private fun onClickBtnShowAllTickets() {
        binding.btnShowAllTickets.setOnClickListener {
            navigateHelper.navigateTo(
                ShowAllTicketsFragment.newInstance(
                    binding.numberDayDepartures.text.toString(),
                    binding.monthDepartures.text.toString()
                )
            )
        }
    }

    private fun onClickListenerDatePlace(
        onCLickBlock: LinearLayout,
        updateDataBlockFun: (Calendar) -> Unit
    ) {
        val calendar = Calendar.getInstance(
            TimeZone.getTimeZone(getString(R.string.text_time_zone_msk))
        )

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDataBlockFun(calendar)
        }

        onCLickBlock.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePicker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateBlockDateDepartures(calendar: Calendar) {
        with(binding) {
            numberDayDepartures.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            getCurrentMonth(calendar, monthDepartures)
            getCurrentDay(calendar, dayDepartures)
        }
    }

    private fun updateDateBackPlace(calendar: Calendar) {
        with(binding) {
            blockBackPlace.visibility = View.GONE
            blockBackPlaceDate.visibility = View.VISIBLE
            numberDayBackPlace.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            getCurrentMonth(calendar, monthBackPlace)
            getCurrentDay(calendar, dayBackPlace)
        }
    }

    private fun updateBlockDateBackPlace(calendar: Calendar) {
        with(binding) {
            numberDayBackPlace.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            getCurrentMonth(calendar, monthBackPlace)
            getCurrentDay(calendar, dayBackPlace)
        }
    }

    private fun getCurrentDayNumber() {
        val calendar = Calendar.getInstance(
            TimeZone.getTimeZone(getString(R.string.text_time_zone_msk))
        )
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        binding.numberDayDepartures.text = dayOfMonth.toString()
    }

    private fun getCurrentMonth(calendar: Calendar, blockMonth: TextView) {
        when (calendar.get(Calendar.MONTH) + 1) {
            1 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_january
                )
            }

            2 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_february
                )
            }

            3 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_march
                )
            }

            4 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_april
                )
            }

            5 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_may
                )
            }

            6 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_june
                )
            }

            7 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_july
                )
            }

            8 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_august
                )
            }

            9 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_september
                )
            }

            10 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_october
                )
            }

            11 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_november
                )
            }

            12 -> {
                blockMonth.text = getString(
                    R.string.abbreviated_text_december
                )
            }
        }
    }

    private fun getCurrentDay(calendar: Calendar, blockDay: TextView) {
        when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> {
                blockDay.text = getString(
                    R.string.abbreviated_text_sunday
                )
            }

            Calendar.MONDAY -> {
                blockDay.text = getString(
                    R.string.abbreviated_text_monday
                )
            }

            Calendar.TUESDAY -> {
                blockDay.text = getString(
                    R.string.abbreviated_text_tuesday
                )
            }

            Calendar.WEDNESDAY -> {
                blockDay.text = getString(
                    R.string.abbreviated_text_wednesday
                )
            }

            Calendar.THURSDAY -> {
                blockDay.text = getString(
                    R.string.abbreviated_text_thursday
                )
            }

            Calendar.FRIDAY -> {
                blockDay.text = getString(
                    R.string.abbreviated_text_friday
                )
            }

            Calendar.SATURDAY -> {
                blockDay.text = getString(
                    R.string.abbreviated_text_saturday
                )
            }
        }
    }

    private fun onClickBackArrowListener() {
        with(binding) {
            arrowBack.setOnClickListener {
                saveValueFromPlace(fromPlace)
                saveValueToPlace(toPlace)
                navigateHelper.navigateTo(MainFragment.newInstance())
            }
        }
    }

    private fun changeValue() {
        with(binding) {
            changeIcon.setOnClickListener {
                val valueFrom = fromPlace.text
                fromPlace.text = toPlace.text
                toPlace.text = valueFrom
            }
        }
    }

    private fun clearEditTextToPlace() {
        with(binding) {
            clearIcon.setOnClickListener {
                if (toPlace.text?.isNotEmpty() == true) {
                    toPlace.text?.clear()
                }
            }
        }
    }

    private fun listeningOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback {
            saveValueFromPlace(binding.fromPlace)
            saveValueToPlace(binding.toPlace)
            navigateHelper.navigateTo(MainFragment.newInstance())
        }
    }

    private fun getValueFromCache(): String {
        val sharedPref = requireContext().getSharedPreferences(
            MainFragment.KEY_CACHE_VALUE_FROM, Context.MODE_PRIVATE
        )
        return sharedPref.getString(MainFragment.KEY_CACHE_VALUE_FROM, "") ?: ""
    }

    private fun saveValueFromPlace(etFromPlace: AppCompatEditText) {
        val sharedPref = requireContext().getSharedPreferences(
            MainFragment.KEY_CACHE_VALUE_FROM, Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(MainFragment.KEY_CACHE_VALUE_FROM, etFromPlace.text.toString())
            apply()
        }
    }

    private fun getValueToCache(): String {
        val sharedPref = requireContext().getSharedPreferences(
            MainFragment.KEY_CACHE_VALUE_TO, Context.MODE_PRIVATE
        )
        return sharedPref.getString(MainFragment.KEY_CACHE_VALUE_TO, "") ?: ""
    }

    private fun saveValueToPlace(etToPlace: AppCompatEditText) {
        val sharedPref = requireContext().getSharedPreferences(
            MainFragment.KEY_CACHE_VALUE_TO, Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(MainFragment.KEY_CACHE_VALUE_TO, etToPlace.text.toString())
            apply()
        }
    }

    companion object {

        fun newInstance(): SearchPlaceSelectedFragment {
            return SearchPlaceSelectedFragment()
        }
    }
}