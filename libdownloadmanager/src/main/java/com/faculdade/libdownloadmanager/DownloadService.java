package com.faculdade.libdownloadmanager;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;

public class DownloadService extends Service {

    private final int REQUEST_CODE = 1;

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private void download(String url) {
        // TODO download
        Uri uriURL = Uri.parse(url);

        // Apenas para ter certeza de permissão da internet para uso em testes
        if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "INTERNET PERMISSION", "permission granted");
        } else {
            Log.println(Log.INFO, "INTERNET PERMISSION", "permission refused");
        }

        // Apenas para ter certeza de permissão da escrita para uso em testes
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.println(Log.INFO, "WRITE_EXTERNAL_STORAGE", "permission granted");
        } else {
            Log.println(Log.INFO, "WRITE_EXTERNAL_STORAGE", "permission refused");
        }

//        File file = new File(getFilesDir(), "downloads");
//        if (!file.exists()) {
//            file.mkdir();
//        }
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "pa_ap2");

        DownloadManager.Request request = new DownloadManager.Request(uriURL)
                .setDestinationUri(Uri.fromFile(file));

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

        Log.println(Log.INFO, "DOWNLOADING URL", url);
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            String url = msg.getData().getString("url");
            Log.println(Log.INFO, "DOWNLOAD THREAD", "arg1: " + msg.arg1 + "; url: " + url);
            download(url);

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Toast.makeText(this, "This message will only appear once", Toast.LENGTH_LONG).show();

        // TODO pedir aurotização para usar internet e o armazenamento interno
        // Log.println(Log.INFO, "REQUESTS", "REQUESTING INTERNET ACCESS AND INTERNAL STORAGE");

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Aqui é feito o pedido do download

        String url = intent.getExtras().getString("url");
        // Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        Log.println(Log.INFO, "STARTING DOWNLOAD", url);

        // Construindo mensagem
        Message message = serviceHandler.obtainMessage();
        message.arg1 = startId;
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        message.setData(bundle);
//        Log.println(Log.INFO, "MESSAGE CONTENT", message.arg1 + "; " + message.getData().getString("url"));

        // Enviando mensagem
        serviceHandler.sendMessage(message);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.println(Log.INFO, "FINISHING DOWNLOAD", "download finished");
    }
}
