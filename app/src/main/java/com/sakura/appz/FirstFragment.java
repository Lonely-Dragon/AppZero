package com.sakura.appz;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sakura.Intercept.LoggingInterceptor;
import com.sakura.appz.databinding.FragmentFirstBinding;

import okhttp3.OkHttpClient;

public class FirstFragment extends Fragment {

    public static int nums = 0;
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
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
//            Runtime.getRuntime().exec("/system/bin/su")
//            dialog.show();

//        跳转
//            NavHostFragment.findNavController(FirstFragment.this)
//                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}