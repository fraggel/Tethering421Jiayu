package es.jiayu.tethering421jiayu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.File;

public class MainActivity extends Activity {
    File root = null;
    Resources res = null;
    Button enable = null;
    Button check = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = getResources();
        root = Environment.getRootDirectory();
        firstConfig();
        if (controlRoot()) {
            if (!controlBusybox()) {
                instalarBusyBox();
            }
        }
    }

    private boolean controlRoot() {
        boolean rootB = false;
        File f = new File(root.getPath() + "/bin/su");
        if (!f.exists()) {
            f = new File(root.getPath() + "/xbin/su");
            if (!f.exists()) {
                f = new File(root.getPath() + "/system/bin/su");
                if (!f.exists()) {
                    f = new File(root.getPath() + "/system/xbin/su");
                    if (f.exists()) {
                        rootB = true;
                    }
                } else {
                    rootB = true;
                }
            } else {
                rootB = true;
            }
        } else {
            rootB = true;
        }
        if (rootB) {
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("su");
            } catch (Exception e) {

            }
        }
        if (!rootB) {
            AlertDialog diag = new AlertDialog.Builder(this).create();
            diag.setMessage(res.getString(R.string.msgNoRoot));
            diag.show();

        }

        return rootB;
    }

    private void instalarBusyBox() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(res.getString(R.string.msgNoBusybox));
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                res.getString(R.string.cancelar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int witch) {
                        finish();
                    }
                });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE,
                res.getString(R.string.aceptar),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int witch) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri
                                    .parse("market://details?id=com.jrummy.busybox.installer"));
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {


                        }
                    }
                });
        dialog.show();

    }

    private boolean controlBusybox() {
        boolean busybox = true;
        File f = new File(root.getPath() + "/bin/busybox");
        if (!f.exists()) {
            f = new File(root.getPath() + "/xbin/busybox");
            if (!f.exists()) {
                busybox = false;
            } else {
                busybox = true;
            }
        } else {
            busybox = true;
        }
        return busybox;
    }

    public void firstConfig() {
        try {
                Runtime rt = Runtime.getRuntime();
                Process p = rt.exec("su");
                BufferedOutputStream bos = new BufferedOutputStream(p.getOutputStream());
                bos.write(("iptables -A POSTROUTING -s 192.168.45.1/24 -j MASQUERADE -t nat\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ap0 -o ccmni1\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ccmni1 -o ap0\n").getBytes());
                bos.write(("iptables -A POSTROUTING -s 192.168.44.1/24 -j MASQUERADE -t nat\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ap0 -o ccmni2\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ccmni2 -o ap0\n").getBytes());
                bos.write(("iptables -A POSTROUTING -s 192.168.43.1/24 -j MASQUERADE -t nat\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ap0 -o ccmni0\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ccmni0 -o ap0\n").getBytes());
                bos.write(("iptables -A POSTROUTING -s 192.168.42.1/24 -j MASQUERADE -t nat\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i rndis0 -o ccmni0\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ccmni0 -o rndis0\n").getBytes());
                bos.write(("iptables -A POSTROUTING -s 192.168.41.1/24 -j MASQUERADE -t nat\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i rndis0 -o wlan0\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i wlan0 -o rndis0\n").getBytes());
                bos.write(("iptables -A POSTROUTING -s 192.168.40.1/24 -j MASQUERADE -t nat\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i rndis0 -o ccmni1\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ccmni1 -o rndis0\n").getBytes());
                bos.write(("iptables -A POSTROUTING -s 192.168.39.1/24 -j MASQUERADE -t nat\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i rndis0 -o ccmni2\n").getBytes());
                bos.write(("iptables -A FORWARD -j ACCEPT -i ccmni2 -o rndis0\n").getBytes());
                bos.write(("busybox sysctl -w net.ipv4.ip_forward=1\n").getBytes());
                bos.flush();
                bos.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
