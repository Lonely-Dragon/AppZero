package com.sakura.Intercept;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.sakura.appz.FirstFragment;
import com.sakura.appz.MainActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import kotlin.jvm.internal.Intrinsics;

public class MyVpnService extends VpnService {
    static String SERVER_ADDR = "192.168.1.169";
    static int SERVER_PORT = 7194;
    static int MAX_BKG_LEN = 65535;

    private MainActivity mainActivity;
    static ParcelFileDescriptor itface;
    FileInputStream in;
    FileOutputStream out;
    DatagramSocket sock;
    InetAddress serverAddr;

    Thread writeThread;
    Thread readThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mainActivity = intent.getParcelableExtra("mainActivity", MainActivity.class);
        }

        Builder builder = new Builder();
        builder.setSession("MyVPNService");
        builder.addAddress("10.0.0.2", 32);
        builder.addDnsServer("119.29.29.29");
        builder.addRoute("0.0.0.0", 0);
        builder.setSession("VPN-xbx");
        builder.setBlocking(true);
        // builder.setConfigureIntent(getMConfigureIntent());
        for (PackageInfo packageInfo : getPackageManager().getInstalledPackages(0)) {
            String str = packageInfo.packageName;
            Intrinsics.checkNotNullExpressionValue(str, "pack.packageName");
            if (str.contains("xbxxz")) {
                try {
                    builder.addAllowedApplication(packageInfo.packageName);
                } catch (PackageManager.NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        itface = builder.establish();
        in = new FileInputStream(itface.getFileDescriptor());
        out = new FileOutputStream(itface.getFileDescriptor());

        try {
            serverAddr = InetAddress.getByName(SERVER_ADDR);
            sock = new DatagramSocket();
            sock.setSoTimeout(0); // 0代表无穷大
            protect(sock);
        } catch (Exception e) {
            e.printStackTrace();
            return START_NOT_STICKY;
        }

        writeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int length;
                    byte[] ip_pkg = new byte[MAX_BKG_LEN];
                    while ((length = in.read(ip_pkg)) >= 0) {
                        if (length == 0) {
                            continue;
                        }
                        DatagramPacket msg = new DatagramPacket(ip_pkg, length, serverAddr, SERVER_PORT);
                        sock.send(msg);
                    }
                    in.close();
                } catch (Exception e) {
                    Log.e(null, e.getMessage());
                }
            }
        }, "sender");

        readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] ip_buf = new byte[MAX_BKG_LEN];
                    while (true) {
                        DatagramPacket msg_r = new DatagramPacket(ip_buf, MAX_BKG_LEN, serverAddr, SERVER_PORT);
                        sock.receive(msg_r);
                        int pkg_len = msg_r.getLength();
                        if (pkg_len > 0) {
                            FirstFragment.BIND.textviewFirst.append(new String(msg_r.getData()));
                            out.write(ip_buf, 0, pkg_len);

                        } else if (pkg_len < 0) {
                            break;
                        }
                    }

                    out.close();
                } catch (Exception e) {
                    Log.e(null, e.getMessage());
                }
            }
        }, "receiver");

        writeThread.start();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        readThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (writeThread != null) {
            writeThread.interrupt();
        }
        super.onDestroy();
    }

    public void StopMyVPN() {
        try {
            if (itface != null) {
                itface.close();
                itface = null;
            }
            // isRunning = false;
        } catch (Exception e) {

        }
        stopSelf();
    }
}
