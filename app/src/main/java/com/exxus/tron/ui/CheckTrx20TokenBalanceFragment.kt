package com.exxus.tron.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.centerprime.tronsdk.sdk.TronWalletManager
import com.exxus.tron.databinding.Trx20TokenBalanceBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.RoundingMode

class CheckTrx20TokenBalanceFragment : Fragment() {

    private var _binding: Trx20TokenBalanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = Trx20TokenBalanceBinding.inflate(inflater, container, false)
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
            /**
             * Using this getTokenBalance function you can check balance of provided walletAddress with smart contract.
             *
             * @param walletAddress - which user want to check it's balance
             * @param contractAddress - contract address of token
             *
             * @return balance
             */
            val walletAddress = binding.address.text.toString().trim()
            val trx20TokenContractAddress: String =
                binding.contractAddress.getText().toString().trim()
            //  String trx20TokenContractAddress = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
            tronWalletManager.getTokenTRX20Balance(walletAddress, trx20TokenContractAddress, requireContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ balance: BigDecimal ->
                    /**
                     * if function successfully completes result can be caught in this block
                     */
                    val balance = balance.divide(
                        BigDecimal(1000000),
                        2,
                        RoundingMode.HALF_UP
                    )
                    binding.balanceTxt.text = "USDT balance is:$balance"
                    Toast.makeText(requireContext(), "Account Balance: $balance", Toast.LENGTH_SHORT).show()
                }) { error: Throwable ->
                    /**
                     * if function fails error can be catched in this block
                     */
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}