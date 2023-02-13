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
import com.exxus.tron.databinding.ImportWalletKeystoreBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImportWalletFromKeyStoreFragment : Fragment() {

    private var _binding: ImportWalletKeystoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = ImportWalletKeystoreBinding.inflate(inflater, container, false)
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


        binding.importBtn.setOnClickListener { v ->
            /**
             * Using this importFromKeyStore function user can import his wallet from keystore.
             *
             * @param keystore - keystore JSON file
             * @param password - password of provided keystore
             * @param Context - activity context
             *
             * @return walletAddress
             */
            val password = binding.password.text.toString()
            val keystore: String = binding.keystoreT.text.toString()
            tronWalletManager.importByKeystore(password, keystore, requireContext())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ walletAddress: Wallet ->
                    /**
                     * if function successfully completes result can be caught in this block
                     */
                    binding.walletAddress.text = walletAddress.address
                    binding.copy.visibility = View.VISIBLE
                }) { error: Throwable ->
                    /**
                     * if function fails error can be caught in this block
                     */
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    println("** ** ** ** " + error.message)
                }
        }

        binding.copy.setOnClickListener { v ->
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", binding.walletAddress.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}