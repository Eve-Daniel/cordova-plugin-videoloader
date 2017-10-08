package com.cordova.plugins.VideoLoader;

import android.content.Context;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by eve on 10/8/17.
 * синглтон. удалит все файлы, еоторые созданы более 4 дней назад
 * при повторном запуске не сделает ничего.
 * (во всяком случае пока не завершит работу)
 * для кеша используем правило - не старше 3 дней и дата создания больше таймштампа респонса
 */

public class GlobalCleaner {

    private static GlobalCleaner instance;
    private boolean runned = false;
    private long lastRun=0;

    private GlobalCleaner(){
        GlobalCleaner.instance = this;
    }
    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }



    private synchronized GlobalCleaner run(Context context){
        if(!runned){
            if(lastRun+(60*60*3*1000)<System.currentTimeMillis()){
                runned=true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File cacheDir = context.getCacheDir();
                        File[] list = cacheDir.listFiles(new FileFilter() {
                            @Override
                            public boolean accept(File pathname) {
                                if(pathname.isFile()){
                                    if(pathname.lastModified()+1000*4*24*60*60<System.currentTimeMillis()){
                                        if("webm".equals(getFileExtension(pathname))){
                                            return true;
                                        }
                                    }
                                }
                                return false;
                            }
                        });
                        for(File a:list){
                            if(a.canWrite()){
                                a.delete();
                            }
                        }
                        lastRun=System.currentTimeMillis();
                        runned=false;
                    }
                }).start();
            }
        }
        return this;
    }



    public static GlobalCleaner F(Context context){
        return (GlobalCleaner.instance==null?new GlobalCleaner():GlobalCleaner.instance).run(context);
    }

}
