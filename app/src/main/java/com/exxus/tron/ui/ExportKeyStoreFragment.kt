package com.exxus.tron.ui


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.centerprime.tronsdk.sdk.TronWalletManager
import com.exxus.tron.databinding.ExportKeystoreBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ExportKeyStoreFragment : Fragment() {

    private var _binding: ExportKeystoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = ExportKeystoreBinding.inflate(inflater, container, false)
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
                Toast.makeText(requireContext(), "输入钱包地址", Toast.LENGTH_SHORT).show()
            }
            tronWalletManager.exportKeyStore(walletAddress, requireContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ keystore: String? ->
                    /**
                     * if function successfully completes result can be caught in this block
                     */
                    binding.keystoreT.visibility = View.VISIBLE
                    binding.copy.visibility = View.VISIBLE
                    binding.keystoreT.text = keystore
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
                ClipData.newPlainText("label", binding.keystoreT.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}