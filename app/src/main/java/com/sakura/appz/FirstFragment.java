package com.sakura.appz;

import static android.app.Activity.RESULT_OK;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sakura.Intercept.LoggingInterceptor;
import com.sakura.Intercept.MyVpnService;
import com.sakura.appz.databinding.FragmentFirstBinding;

import okhttp3.OkHttpClient;
import com.sakura.Intercept.MyVpnService;

import java.io.Serializable;

public class FirstFragment extends Fragment {

    public static int nums = 0;
    private FragmentFirstBinding binding;
    private MainActivity mainActivity;

    public static FragmentFirstBinding BIND = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity)(getActivity());
        BIND = binding;
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();


        // 初始化信息框
        AlertDialog dialog = new AlertDialog.Builder(view.getContext()).setTitle("消息框哦").setMessage(String.valueOf(nums++)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(view.getContext(), "点击了确定框", Toast.LENGTH_SHORT).show();
            }
        }).create();

        binding.buttonFirst.setOnClickListener(v -> {
            binding.textviewFirst.setText(String.valueOf(nums++));
            dialog.setMessage(String.valueOf(nums));

            Intent intent = VpnService.prepare(view.getContext());
            if (intent != null) {
                startActivityForResult(intent, 0);
                // startActivityIfNeeded()
            } else {
                this.onActivityResult(0, RESULT_OK, null);
            }

//            Runtime.getRuntime().exec("/system/bin/su")
//            dialog.show();

//        跳转
//            NavHostFragment.findNavController(FirstFragment.this)
//                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
        });
    }

    //    启动VPN服务
    public void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        if (result == RESULT_OK) {
            Intent intent = new Intent(this.getContext(), MyVpnService.class);
            intent.putExtra("mainActivity", (Parcelable) mainActivity);
            mainActivity.startService(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}