package com.exxus.tron.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.centerprime.tronsdk.sdk.TronWalletManager
import com.exxus.tron.databinding.SendTrxBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class SendTrxFragment : Fragment() {

    private var _binding: SendTrxBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = SendTrxBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Using this sendTrx function you can send TRX from walletAddress to another walletAddress.
         * @params Context, senderWalletAddress, password, receiverWalletAddress, trxAmount
         * @return transactionHash
         */
        val tronWalletManager = TronWalletManager.getInstance()

        tronWalletManager.init(requireContext())

        binding.sendTrx.setOnClickListener { v ->
            if (!TextUtils.isEmpty(
                    binding.address.text.toString().trim()
                ) && !TextUtils.isEmpty(binding.trxAmount.text.toString().trim())
                && !TextUtils.isEmpty(binding.receiverAddress.text.toString().trim())
                && !TextUtils.isEmpty(binding.password.text.toString().trim())
            ) {
                val walletAddress = binding.address.text.toString()
                val password = binding.password.text.toString()
                val trxAmount: BigDecimal =
                    BigDecimal(binding.trxAmount.text.toString().trim())
                val receiverAddress: String =
                    binding.receiverAddress.text.toString().trim()
                tronWalletManager.sendTRX(
                    requireContext(),
                    walletAddress,
                    password,
                    receiverAddress,
                    trxAmount
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { tx: String ->
                            binding.result.text = "Transaction Hash: $tx"
                            Toast.makeText(
                                requireContext(),
                                "Sent TRX : $trxAmount",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) { error: Throwable ->
                        binding.result.text = error.message + ". Please check if the entry is correct!"
                        error.printStackTrace()
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}