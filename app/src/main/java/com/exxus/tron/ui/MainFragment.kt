package com.exxus.tron.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.exxus.tron.R
import com.exxus.tron.databinding.MainBinding
import org.tron.trident.core.key.KeyPair

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: MainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.to_CreateWalledFragment)
        }
        binding.checkBalance.setOnClickListener {
            findNavController().navigate(R.id.to_CheckBalanceFragment)
        }
        binding.exportKeystore.setOnClickListener {
            findNavController().navigate(R.id.to_ExportKeyStoreFragment)
        }
        binding.exportPrivateKeyBtn.setOnClickListener {
            findNavController().navigate(R.id.to_ExportPrivateKeyFragment)
        }
        binding.importBtn.setOnClickListener {
            findNavController().navigate(R.id.to_ImportWalletFromKeyStoreFragment)
        }
        binding.importPrivateKeyBtn.setOnClickListener {
            findNavController().navigate(R.id.to_ImportWalletFromPrivateKeyFragment)
        }
        binding.checkERCTokenkBalance.setOnClickListener {
            findNavController().navigate(R.id.to_CheckTrx20TokenBalanceFragment)
        }
        binding.sendTrx.setOnClickListener {
            findNavController().navigate(R.id.to_SendTrxFragment)
        }
        binding.sendUSDT.setOnClickListener {
            findNavController().navigate(R.id.to_SendUSDTFragment)
        }

        binding.createTrans.setOnClickListener {
            findNavController().navigate(R.id.to_CreateTransformFragment)
        }
        binding.sigMutilsigTrans.setOnClickListener {
            findNavController().navigate(R.id.to_SignTransformFragment)
        }
        binding.boardcastTrans.setOnClickListener {
            findNavController().navigate(R.id.to_BoardcastTransformFragment)
        }
        trident()
    }

    private fun trident() {
        //val keyPair = KeyPair("f09c3fd11ec35f168eac641e5583c955e0e8d78d3e426bf120cb6ec9f3722c42")
        val keyPair = KeyPair.generate()
        Log.d("Trident", keyPair.toBase58CheckAddress())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}