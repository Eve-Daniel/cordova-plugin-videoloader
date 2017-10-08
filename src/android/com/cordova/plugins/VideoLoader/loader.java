package com.cordova.plugins.VideoLoader;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.cordova.CallbackContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by eve on 10/7/17.
 */

public class loader implements Runnable {

    private String url;
    private CallbackContext context;
    private String $token;
    private Context appcontext;
    private long lap;
    private long contextTimestamp; //отметка - когда произошло изменение видеохоста
    // как удалять ненужные, смаестить в сторону кеша?

    private loader(String aurl, String token,long lap, CallbackContext acontext) {
        this.url = aurl;
        this.context = acontext;
        this.$token = token;
        this.lap = lap;
    }


    public static loader F(String url, String token, long lap,CallbackContext context) {
        return new loader(url, token, lap, context);
    }

    private String MD5(String $in) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update($in.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString() + ".webm";
        } catch (Exception $e) {
            return $in.replace('/', '_').replace('\\', '_').replace('.', '_').replace(':', '_') + ".webm";
        }
    }

    private String getFileName() {
        return this.MD5(this.url);
    }

    private void onSuccess(String $localFile) {
        this.context.success($localFile);
    }

    private void onFail(Exception $e) {
        this.onFail($e.getMessage());
    }

    private void onFail(String $msg) {
        this.context.error($msg);
    }

    public loader setContext(Context context){
        this.appcontext = context;

        return this;
    }

    @Override
    public void run() {

        File $dir = this.appcontext.getCacheDir(); //new File($DIR);
       // $dir= new File($dir,$DIR);
        if (!$dir.exists()) {
            $dir.mkdirs();
        }
        File $file = new File($dir, this.getFileName());
        if($file.exists() && $file.isFile() && $file.lastModified()<this.lap*1000){
            $file.delete(); // удалить файл если он устарел
        }
        if ($file.exists() && $file.isFile() && $file.canWrite()){
            this.onSuccess("file://"+$file.getAbsolutePath());
            return;
        }
        //<editor-fold defaultstate="collapsed" desc="connection">
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

// Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        //</editor-fold>
        BufferedInputStream in = null;
        FileOutputStream fout = null;

        try {
            // And as before now you can use URL and URLConnection
            URL url = new URL(this.url + "?uiToken=" + this.$token);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            try {
                in = new BufferedInputStream(url.openStream());
                fout = new FileOutputStream($file);
                byte data[] = new byte[8192];
                int count;
                while ((count = in.read(data, 0, 8192)) != -1) {
                    fout.write(data, 0, count);
                }
            } finally {
                if (in != null)
                    in.close();
                if (fout != null)
                    fout.close();
            }
        } catch (Exception $e) {
            this.context.error("inCatch:"+$e.getMessage()+":hprev="+$e.toString());
            this.onFail($e);
            return;
        }
        this.onSuccess("file://"+$file.getAbsolutePath());


    }
}
