package com.exxus.tron.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.centerprime.tronsdk.sdk.TronWalletManager
import com.exxus.tron.databinding.CheckBalanceBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.RoundingMode

class CheckBalanceFragment : Fragment() {

    private var _binding: CheckBalanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = CheckBalanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tronWalletManager = TronWalletManager.getInstance()
        tronWalletManager.init(requireContext())
        binding.checkBtn.setOnClickListener { v ->
            val address: String = binding.address.getText().toString()
            tronWalletManager.getBalanceTrx(address, requireContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ balance: BigDecimal ->
                    /**
                     * if function successfully completes result can be caught in this block
                     */
                    val balanceRound = balance.setScale(2, RoundingMode.HALF_UP)
                    binding.balanceTxt.text = "TRX balance is: $balanceRound"
                    binding.balanceTxt.visibility = View.VISIBLE
                }) { error: Throwable ->
                    /**
                     * if function fails error can be caught in this block
                     */
                    binding.balanceTxt.visibility = View.INVISIBLE
                    println(error.message)
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}