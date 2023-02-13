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
import com.exxus.tron.databinding.BoardcastTrx20TransformBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BoardcastTransformFragment : Fragment() {
    private var _binding: BoardcastTrx20TransformBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = BoardcastTrx20TransformBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tronWalletManager = TronWalletManager.getInstance()
        tronWalletManager.init(requireContext())
        binding.boardcastBtn.setOnClickListener { v ->
            binding.boardcastBtn.isEnabled = false
            if (!TextUtils.isEmpty(binding.keystoreT.text.toString().trim())) {
                val txData: String = binding.keystoreT.text.toString()
                tronWalletManager.broadcastTRC20Transform(txData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { r: Boolean ->
                            if (r) {
                                binding.boardcastResult.text = "Announcement completed"
                                binding.boardcastBtn.isEnabled = true
                            } else {
                                binding.boardcastResult.text = "Announcement failed with exception"
                                Toast.makeText(
                                    requireContext(),
                                    "Announcement failed with exception",
                                    Toast.LENGTH_SHORT
                                ).show()
                                binding.boardcastBtn.isEnabled = true
                            }
                        }
                    ) { error: Throwable ->
                        binding.boardcastResult.text = error.message + ". Please check if the entry is correct!"
                        error.printStackTrace()
                        Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                        binding.boardcastBtn.isEnabled = true
                    }
            } else {
                Toast.makeText(requireContext(), "Please check if the entry is correct!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}