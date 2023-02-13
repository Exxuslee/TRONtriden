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
import com.exxus.tron.databinding.CreateTrx20TransformBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class CreateTransformFragment : Fragment() {

    private var _binding: CreateTrx20TransformBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = CreateTrx20TransformBinding.inflate(inflater, container, false)
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
        binding.createTransform.setOnClickListener { v ->
            binding.createTransform.isEnabled = false
            if (!TextUtils.isEmpty(binding.address.text.toString().trim()) && !TextUtils.isEmpty(
                    binding.gasLimit.text.toString().trim()
                ) && !TextUtils.isEmpty(
                    binding.ethAmount.text.toString().trim()
                ) && !TextUtils.isEmpty(
                    binding.receiverAddress.text.toString().trim()
                ) && !TextUtils.isEmpty(binding.password3.text.toString().trim())
            ) {
                val walletAddress = binding.address.text.toString()
                val password: String = binding.password3.text.toString()
                val usdtAmount: BigDecimal = BigDecimal(binding.ethAmount.text.toString().trim())
                val gasLimit: BigDecimal = BigDecimal(binding.gasLimit.text.toString().trim())
                val receiverAddress: String = binding.receiverAddress.text.toString().trim()
                val usdtContractAddress = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t"
                tronWalletManager.createTRX20Transform(
                    requireContext(),
                    walletAddress,
                    password,
                    receiverAddress,
                    usdtContractAddress,
                    usdtAmount
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ transform: String? ->
                        binding.result.text = transform
                        binding.copy2.visibility = View.VISIBLE
                        binding.createTransform.isEnabled = true
                    }) { error: Throwable ->
                        binding.result.text =
                            error.message + ". Please check if the entry is correct!"
                        error.printStackTrace()
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        binding.createTransform.isEnabled = true
                    }
            } else {
                Toast.makeText(
                    requireContext(), "Please check if the entry is correct!", Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.copy2.setOnClickListener { v ->
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", binding.result.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}