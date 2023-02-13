package com.example.centerprimesampletronsdk;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.tronsdk.sdk.TronWalletManager;
import com.example.centerprimesampletronsdk.databinding.ActivityBoardcastTrx20TransformBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BoardcastTransformActivity extends AppCompatActivity {
    ActivityBoardcastTrx20TransformBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_boardcast_trx20_transform);

        TronWalletManager tronWalletManager = TronWalletManager.getInstance();
        tronWalletManager.init(this);
        binding.boardcastBtn.setOnClickListener(v -> {
            binding.boardcastBtn.setEnabled(false);

            if(!TextUtils.isEmpty(binding.keystoreT.getText().toString().trim())) {

                String txData = binding.keystoreT.getText().toString();

                tronWalletManager.broadcastTRC20Transform(txData)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(r -> {
                            if(r) {
                                binding.boardcastResult.setText("Announcement completed");
                                binding.boardcastBtn.setEnabled(true);
                            } else {
                                binding.boardcastResult.setText("Announcement failed with exception");
                                Toast.makeText(this, "Announcement failed with exception", Toast.LENGTH_SHORT).show();
                                binding.boardcastBtn.setEnabled(true);
                            }
                        }, error -> {
                            binding.boardcastResult.setText(error.getMessage() + ". Please check if the entry is correct!");
                            error.printStackTrace();
                            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.boardcastBtn.setEnabled(true);
                        });
            } else {
                Toast.makeText(this, "Please check if the entry is correct!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
