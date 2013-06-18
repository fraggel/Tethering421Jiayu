package com.example.tethering421jiayu;

import java.io.BufferedOutputStream;
import java.io.File;

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
import android.widget.Toast;

public class MainActivity extends Activity {
	File root=null;
	Resources res=null;
	Button enable=null;
	Button check=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		res=getResources();
		root=Environment.getRootDirectory();
		addListenerOnButton();
		if (controlRoot()) {
			if (!controlBusybox()) {
				instalarBusyBox();
			}else{
				
			}
		}
		
	}
	private boolean controlRoot() {
		boolean rootB = false;
		File f = new File(root.getPath()+"/bin/su");
		if (!f.exists()) {
			f = new File(root.getPath()+"/xbin/su");
			if (!f.exists()) {
				f = new File(root.getPath()+"/system/bin/su");
				if (!f.exists()) {
					f = new File(root.getPath()+"/system/xbin/su");
					if (f.exists()) {
						rootB=true;
					}
				}else{
					rootB=true;
				}
			}else{
				rootB=true;
			}
		}else{
			rootB=true;
		}
		if (rootB) {
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec("su");
			} catch (Exception e) {
				
			}
		}
		if(!rootB){
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
		File f = new File(root.getPath()+"/bin/busybox");
		if (!f.exists()) {
			f = new File(root.getPath()+"/xbin/busybox");
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
	public void addListenerOnButton() {
    	try {
			enable = (Button) findViewById(R.id.button1);
			enable.setOnClickListener(new View.OnClickListener() {	 
				public void onClick(View arg0) {
					try {
						Runtime rt = Runtime.getRuntime();
						Process p = rt.exec("su");
						BufferedOutputStream bos = new BufferedOutputStream(p.getOutputStream());
						bos.write(("iptables -A POSTROUTING -s 192.168.43.1/24 -j MASQUERADE -t nat\n").getBytes());
						bos.write(("iptables -A FORWARD -j ACCEPT -i ap0 -o ccmni0\n").getBytes());
						bos.write(("iptables -A FORWARD -j ACCEPT -i ccmni0 -o ap0\n").getBytes());
						bos.write(("busybox sysctl -w net.ipv4.ip_forward=1\n").getBytes());
						bos.flush();
						bos.close();
						/*AlertDialog dialog = new AlertDialog.Builder(getApplicationContext()).create();
						dialog.setMessage("Proceso terminado correctamente\nAhora se abrir� la ventana de Wifi Theathering\nDebes en ese momento habilitar el twthering");
						dialog.show();
						*/
						startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
						finish();
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
	 
			});
			check = (Button) findViewById(R.id.button2);
			check.setOnClickListener(new View.OnClickListener() {	 
				public void onClick(View arg0) {
					try {
						startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
	 
			});
			
    	} catch (Exception e) {
    		Toast.makeText(getBaseContext(), getResources().getString(R.string.errorGenerico), Toast.LENGTH_SHORT).show();
		} 
 
	}
}
