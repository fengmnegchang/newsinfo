/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2017-1-4上午10:18:49
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
package com.example.newsinfo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.example.newsinfo.R;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :fengguangjing
 * @createTime:2017-1-4上午10:18:49
 * @version:4.2.4
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class DownLoadAsyncTask extends AsyncTask<Void, Void, String> {  
    Context mContext;  
    String srcUrl;
    
    public DownLoadAsyncTask( Context mContext, String srcUrl) {  
        this.mContext = mContext;  
        this.srcUrl = srcUrl; 
    }  
  
    @Override  
    protected String doInBackground(Void... params) {  
        String result = mContext.getResources().getString(R.string.save_picture_failed);  
        try {  
            String sdcard = Environment.getExternalStorageDirectory().toString();  
            File file = new File(sdcard + "/"+mContext.getPackageName()+"/");  
            if (!file.exists()) {  
                file.mkdirs();  
            }  
            //http://i1.umei.cc/uploads/tu/201609/33/mimn5ha5dfo.jpg
            File imageFile = new File(file.getAbsolutePath(),  URLEncoder.encode(srcUrl.replace(".jpg", ""),"UTF-8")+".jpg");  
            imageFile.deleteOnExit();
            imageFile.createNewFile();
            FileOutputStream outStream = null;  
            outStream = new FileOutputStream(imageFile);
            
            URL picUrl = new URL(srcUrl);  
            //通过图片的链接打开输入流  
            InputStream  is = picUrl.openStream();  
            if(is==null){  
                return null;  
            }  
            byte[] b=new byte[1024];  
            int end ;  
            while ((end=is.read(b))!=-1){  
            	outStream.write(b,0,end);  
            }  
            outStream.flush();  
            outStream.close();  
            is.close();
            result = mContext.getResources().getString(R.string.save_picture_success, file.getAbsolutePath()); 
            scanFile(mContext,file.getAbsolutePath());
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
    
    /**
	* 通知媒体库更新文件
	* @param context
	* @param filePath 文件全路径
	* 
	* */
	public void scanFile(Context context, String filePath) {
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		scanIntent.setData(Uri.fromFile(new File(filePath)));
		context.sendBroadcast(scanIntent);
	}

    @Override  
    protected void onPostExecute(String result) {  
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();  
    }  
} 
