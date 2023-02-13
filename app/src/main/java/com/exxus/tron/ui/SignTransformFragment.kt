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
import com.exxus.tron.databinding.SignTrx20TransformBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SignTransformFragment : Fragment() {

    private var _binding: SignTrx20TransformBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = SignTrx20TransformBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tronWalletManager = TronWalletManager.getInstance()
        tronWalletManager.init(requireContext())
        binding.signBtn.setOnClickListener { v ->
            binding.signBtn.isEnabled = false
            if (!TextUtils.isEmpty(binding.unsignedData.text.toString().trim())
                && !TextUtils.isEmpty(binding.address2.text.toString().trim())
                && !TextUtils.isEmpty(binding.password.text.toString().trim())
            ) {
                val unsignedData: String = binding.unsignedData.text.toString()
                val address: String = binding.address2.text.toString()
                val password = binding.password.text.toString()
                tronWalletManager.signTRX20Transform(requireContext(), unsignedData, address, password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { signedData: String? ->
                            binding.signedData.text = signedData
                            binding.copy.visibility = View.VISIBLE
                            binding.signBtn.isEnabled = true
                        }
                    ) { error: Throwable ->
                        binding.signedData.text = error.message + ". Please check if the entry is correct!"
                        error.printStackTrace()
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        binding.signBtn.isEnabled = true
                    }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please check if the entry is correct!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        binding.copy.setOnClickListener { v ->
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText("label", binding.signedData.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}