package com.exxus.tron.ui


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.centerprime.tronsdk.sdk.TronWalletManager
import com.centerprime.tronsdk.sdk.Wallet
import com.exxus.tron.databinding.ImportPrivateKeyBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImportWalletFromPrivateKeyFragment : Fragment() {

    private var _binding: ImportPrivateKeyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = ImportPrivateKeyBinding.inflate(inflater, container, false)
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

        binding.checkBtn.setOnClickListener { v ->
            val password: String = binding.password2.getText().toString()
            val privateKey = binding.privateKey.text.toString()
            tronWalletManager.importByPrivateKey(privateKey, password, requireContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ walletAddress: Wallet ->
                    /**
                     * if function successfully completes result can be caught in this block
                     */
                    binding.address.setText(walletAddress.address)
                    binding.copyBtn.setVisibility(View.VISIBLE)
                }) { error: Throwable ->
                    /**
                     * if function fails error can be caught in this block
                     */
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    println("** ** ** ** " + error.message)
                }
        }
        binding.copyBtn.setOnClickListener { v ->
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("label", binding.privateKey.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}