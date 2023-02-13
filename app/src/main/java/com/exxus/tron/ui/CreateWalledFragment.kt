package com.exxus.tron.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.centerprime.tronsdk.sdk.TronWalletManager
import com.centerprime.tronsdk.sdk.Wallet
import com.exxus.tron.databinding.CreateWalletBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreateWalledFragment : Fragment() {
    var progressDialog: ProgressDialog? = null
    private var _binding: CreateWalletBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CreateWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tronWalletManager: TronWalletManager = TronWalletManager.getInstance()
        tronWalletManager.init(requireContext())

        binding.createWallet.setOnClickListener {
            if ((!TextUtils.isEmpty(
                    binding.password.text.toString()
                ) && !TextUtils.isEmpty(binding.confirmPassword.text.toString())
                        && binding.password.text.toString() == binding.confirmPassword.text.toString()) && binding.password.text
                    .toString().trim()
                    .length >= 6 && binding.confirmPassword.text.toString().trim()
                    .length >= 6
            ) {
                progressDialog = ProgressDialog.show(
                    requireContext(), "",
                    "Creating TRX wallet address...", true
                )
                tronWalletManager.createWallet(binding.password.text.toString(), requireContext())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ walletAddress ->
                        progressDialog?.dismiss()
                        binding.address.text = walletAddress.address
                        binding.copy.visibility = View.VISIBLE
                    },
                        { error: Throwable ->
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT)
                                .show()
                        })
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter the correct password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.copy.setOnClickListener { v ->
            val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("label", binding.address.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard！", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}