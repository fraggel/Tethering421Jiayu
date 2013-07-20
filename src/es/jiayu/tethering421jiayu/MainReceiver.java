package es.jiayu.tethering421jiayu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MainReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
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
        }catch(Exception e){
            Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
        }

    }

}
