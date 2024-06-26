package com.team.testeffective.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.team.testeffective.R
import com.team.testeffective.adapter_delegation.OfferDiffCallback
import com.team.testeffective.adapter_delegation.offerAdapterDelegate
import com.team.testeffective.databinding.FragmentMainBinding
import com.team.testeffective.navigate.NavigateHelper
import com.team.testeffective.view_models.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_main_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private val mainViewModel by viewModel<MainViewModel>()

    private val glide by lazy { Glide.with(this) }

    private val adapter by lazy {
        AsyncListDifferDelegationAdapter(
            OfferDiffCallback(),
            offerAdapterDelegate(glide)
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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeningOnBackPressed()
        setValueFrom()
        setValueTo()
        binding.offerList.adapter = adapter
        mainViewModel.getOfferList()
        observeViewModel()
        checkFocusEditTextToPlace()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveValueFromPlace(binding.fromPlace)
        saveValueToPlace(binding.toPlace)
        _binding = null
    }
    // ****** lifecycle *****

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.offerList.flowWithLifecycle(lifecycle)
                .collect { offerList ->
                    adapter.items = offerList
                }
        }

        lifecycleScope.launch {
            mainViewModel.isOnline.flowWithLifecycle(lifecycle)
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

    private fun checkFocusEditTextToPlace() {
        binding.toPlace.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                createDialogWindowSearch(R.layout.search_dialog)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createDialogWindowSearch(dialogLayout: Int) {
        val searchDialog = layoutInflater.inflate(
            dialogLayout, null
        )
        val dialog = BottomSheetDialog(requireContext())
        dialog.window?.apply {
            setContentView(searchDialog)
        }
        dialog.setCancelable(true)
        dialog.show()

        val etToPlace = searchDialog.findViewById<AppCompatEditText>(
            R.id.to_place
        )
        val etFromPlace = searchDialog.findViewById<AppCompatEditText>(
            R.id.from_place
        )
        val hardWay = searchDialog.findViewById<LinearLayout>(
            R.id.block_hard_way
        )
        val blockWeekend = searchDialog.findViewById<LinearLayout>(
            R.id.block_weekend
        )
        val blockHotTicket = searchDialog.findViewById<LinearLayout>(
            R.id.block_hot_ticket
        )
        val blockAnywhere = searchDialog.findViewById<LinearLayout>(
            R.id.block_anywhere
        )
        val blockIstanbul = searchDialog.findViewById<LinearLayout>(
            R.id.block_istanbul
        )
        val blockSochi = searchDialog.findViewById<LinearLayout>(
            R.id.block_sochi
        )
        val blockPhuket = searchDialog.findViewById<LinearLayout>(
            R.id.block_phuket
        )
        val clearIcon = searchDialog.findViewById<AppCompatImageView>(
            R.id.clear_icon
        )

        val lineCloseDialog = dialog.findViewById<View>(R.id.line_close_dialog)
        var startY = 0f
        val touchSlop = ViewConfiguration.get(requireContext()).scaledTouchSlop

        lineCloseDialog?.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = motionEvent.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val endY = motionEvent.rawY
                    val distance = endY - startY
                    if (distance > touchSlop * 2) {
                        sendingTextFomToFromMain(etFromPlace)
                        sendingTextToPlaceToPlaceToMain(etToPlace)
                        dialog.dismiss()
                        true
                    } else {
                        false
                    }
                }

                else -> false
            }
        }

        setTextFromEditTextFrom(etFromPlace)
        setTextToEditTextFrom(etToPlace)
        clearEditTextToPlace(clearIcon, etToPlace)
        listeningOnClickBlockToOpenPlug(
            hardWay,
            dialog,
            etFromPlace,
            etToPlace
        )
        listeningOnClickBlockToOpenPlug(
            blockWeekend,
            dialog,
            etFromPlace,
            etToPlace
        )
        listeningOnClickBlockToOpenPlug(
            blockHotTicket,
            dialog,
            etFromPlace,
            etToPlace
        )
        onCLickListenerBlockAnywhere(blockAnywhere, etToPlace)
        onClickListenerIstanbul(etToPlace, blockIstanbul)
        onClickListenerSochi(etToPlace, blockSochi)
        onClickListenerPhuket(etToPlace, blockPhuket)
        dialog.onBackPressedDispatcher.addCallback {
            sendingTextFomToFromMain(etFromPlace)
            sendingTextToPlaceToPlaceToMain(etToPlace)
            dialog.dismiss()
        }

        etToPlace.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (etToPlace.text?.isNotEmpty() == true) {
                    saveValueFromPlace(etFromPlace)
                    saveValueToPlace(etToPlace)
                    sendingTextFomToFromMain(etFromPlace)
                    sendingTextToPlaceToPlaceToMain(etToPlace)
                    dialog.dismiss()
                    navigateHelper.navigateTo(SearchPlaceSelectedFragment.newInstance())
                }
            }
        }
    }

    private fun onClickListenerIstanbul(
        etToPlace: AppCompatEditText,
        blockIstanbul: LinearLayout
    ) {
        blockIstanbul.setOnClickListener {
            etToPlace.setText(TEXT_ISTANBUL)
        }
    }

    private fun onClickListenerPhuket(
        etToPlace: AppCompatEditText,
        blockPhuket: LinearLayout
    ) {
        blockPhuket.setOnClickListener {
            etToPlace.setText(TEXT_PHUKET)
        }
    }

    private fun onClickListenerSochi(
        etToPlace: AppCompatEditText,
        blockSochi: LinearLayout
    ) {
        blockSochi.setOnClickListener {
            etToPlace.setText(TEXT_SOCHI)
        }
    }

    private fun onCLickListenerBlockAnywhere(
        blockAnywhere: LinearLayout,
        etToPlace: AppCompatEditText
    ) {
        blockAnywhere.setOnClickListener {
            etToPlace.setText(TEXT_ANYWHERE)
        }
    }

    private fun listeningOnClickBlockToOpenPlug(
        block: LinearLayout,
        dialog: BottomSheetDialog,
        etFromPlace: AppCompatEditText,
        etToPlace: AppCompatEditText
    ) {
        block.setOnClickListener {
            sendingTextFomToFromMain(etFromPlace)
            sendingTextToPlaceToPlaceToMain(etToPlace)
            dialog.dismiss()
            navigateHelper.navigateTo(PlugFragment.newInstance())
        }
    }

    private fun sendingTextFomToFromMain(etFromPlace: AppCompatEditText) {
        if (etFromPlace.text?.isNotEmpty() == true) {
            binding.fromPlace.text = etFromPlace.text
        }
    }

    private fun sendingTextToPlaceToPlaceToMain(etToPlace: AppCompatEditText) {
        if (etToPlace.text?.isNotEmpty() == true) {
            binding.toPlace.text = etToPlace.text
        }
    }

    private fun setTextFromEditTextFrom(etFromPlace: AppCompatEditText) {
        with(binding) {
            val text = fromPlace.text
            if (text?.isNotEmpty() == true) {
                etFromPlace.text = text
            }
        }
    }

    private fun setTextToEditTextFrom(etToPlace: AppCompatEditText) {
        with(binding) {
            val text = toPlace.text
            if (text?.isNotEmpty() == true) {
                etToPlace.text = text
            }
        }
    }

    private fun clearEditTextToPlace(
        clearIcon: AppCompatImageView,
        etToPlace: AppCompatEditText
    ) {
        clearIcon.setOnClickListener {
            if (etToPlace.text?.isNotEmpty() == true) {
                etToPlace.text?.clear()
            }
        }
    }

    private fun listeningOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun saveValueFromPlace(etFromPlace: AppCompatEditText) {
        val sharedPref = requireContext().getSharedPreferences(
            KEY_CACHE_VALUE_FROM, Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(KEY_CACHE_VALUE_FROM, etFromPlace.text.toString())
            apply()
        }
    }

    private fun getValueFromCache(): String {
        val sharedPref = requireContext().getSharedPreferences(
            KEY_CACHE_VALUE_FROM, Context.MODE_PRIVATE
        )
        return sharedPref.getString(KEY_CACHE_VALUE_FROM, "") ?: ""
    }

    private fun setValueFrom() {
        val value: String = getValueFromCache()
        if (value.isNotEmpty()) {
            binding.fromPlace.setText(value)
        }
    }

    private fun saveValueToPlace(etToPlace: AppCompatEditText) {
        val sharedPref = requireContext().getSharedPreferences(
            KEY_CACHE_VALUE_TO, Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(KEY_CACHE_VALUE_TO, etToPlace.text.toString())
            apply()
        }
    }

    private fun getValueToCache(): String {
        val sharedPref = requireContext().getSharedPreferences(
            KEY_CACHE_VALUE_TO, Context.MODE_PRIVATE
        )
        return sharedPref.getString(KEY_CACHE_VALUE_TO, "") ?: ""
    }

    private fun setValueTo() {
        val value: String = getValueToCache()
        if (value.isNotEmpty()) {
            binding.toPlace.setText(value)
        }
    }

    companion object {

        const val KEY_CACHE_VALUE_FROM = "value_from"
        const val KEY_CACHE_VALUE_TO = "value_to"

        private const val TEXT_ANYWHERE = "Куда угодно"
        private const val TEXT_ISTANBUL = "Стамбул"
        private const val TEXT_SOCHI = "Сочи"
        private const val TEXT_PHUKET = "Пхукет"

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}