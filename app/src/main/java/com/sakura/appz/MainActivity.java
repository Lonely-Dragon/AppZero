package com.sakura.appz;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sakura.appz.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sakura.Intercept.MyVpnService;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //        try {
        //            Process p  = Runtime.getRuntime().exec("su");
        //            DataInputStream is = new DataInputStream(p.getInputStream());
        //            DataOutputStream os = new DataOutputStream(p.getOutputStream());
        //        } catch (IOException e) {
        //            throw new RuntimeException(e);
        //        }

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 隐藏
                // binding.fab.setVisibility(View.INVISIBLE);
                // 获取权限
                // Intent intent = VpnService.prepare(view.getContext());
                // if (intent != null) {
                //     startActivityForResult(intent, 0);
                //     // startActivityIfNeeded()
                // } else {
                //     onActivityResult(0, RESULT_OK, null);
                // }

            }
        });
    }

    // public void onActivityResult(int request, int result, Intent data) {
    //     super.onActivityResult(request, result, data);
    //     if (result == RESULT_OK) {
    //         Intent intent = new Intent(this.getContext(), MyVpnService.class);
    //         startService(intent);
    //     }
    // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}