package com.exxus.tron.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.centerprime.tronsdk.sdk.TronWalletManager
import com.exxus.tron.databinding.ExportPrivateKeyBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExportPrivateKeyFragment : Fragment() {

    private var _binding: ExportPrivateKeyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = ExportPrivateKeyBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tronWalletManager = TronWalletManager.getInstance()
        /**
         * @param context - Initialize tronWalletManager
         */
        /**
         * @param context - Initialize tronWalletManager
         */
        tronWalletManager.init(requireContext())

        binding.button.setOnClickListener { v ->
            /**
             * Using this getKeyStore function user can get keyStore of provided walletAddress.
             *
             * @param WalletAddress - wallet address which user want to get key store
             * @param Context - activity context
             *
             * @return if the function is completed successfully returns keyStore JSON file or error name
             */
            var walletAddress = ""
            if (!TextUtils.isEmpty(binding.address.text.toString())) {
                walletAddress = binding.address.text.toString()
            } else {
                Toast.makeText(requireContext(), "Enter wallet address", Toast.LENGTH_SHORT).show()
            }
            var password = ""
            if (!TextUtils.isEmpty(binding.password.text.toString())
                && binding.password.text.toString().trim().length >= 6
            ) {
                password = binding.password.text.toString()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Password does not comply with the rules",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            tronWalletManager.exportPrivateKey(walletAddress, password, requireContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ privateKey: String? ->
                    /**
                     * if function successfully completes result can be caught in this block
                     */
                    binding.privateKey.visibility = View.VISIBLE
                    binding.copy.visibility = View.VISIBLE
                    binding.privateKey.text = privateKey
                    hideKeyboard(requireActivity())
                }) { error: Throwable? ->
                    /**
                     * if function fails error can be catched in this block
                     */
                    Toast.makeText(
                        requireContext(),
                        "Please enter a valid wallet address",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
        }

        binding.copy.setOnClickListener { v ->
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("label", binding.privateKey.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}