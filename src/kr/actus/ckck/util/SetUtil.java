package kr.actus.ckck.util;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

public class SetUtil {

	
	
		
		
	//�ε��� ǥ�� ���α׷�����
	public static Dialog setProgress(Context context){
		Dialog dg = new Dialog(context, android.R.style.Theme_Dialog);
		dg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dg.setContentView(new ProgressBar(context,null,android.R.attr.progressBarStyle));
		dg.setCanceledOnTouchOutside(false);
		dg.setCancelable(false);
		dg.show();
		
		return dg;
		
	}
	
	public static String filePath(String temp) {

		String dir = "";
//		String [] saveItem = new String[3];
		String fullPath = null;
		String fileName = null;
		
		int n = temp.lastIndexOf("/");

		if (n >= 0) {

			dir = temp.substring(0, n);
			fullPath = dir;
			
			File file = new File(fullPath);
			if (!file.exists())
				file.mkdirs();
			fileName = temp.substring(n + 1);
			
			if (n == 0)
				dir = "/";
		}
		fullPath = fullPath+"/"+fileName; 
		// SDī�� �����ι� ���ϸ� ����.
		return fullPath;

	}
}
