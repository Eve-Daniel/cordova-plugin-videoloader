package com.cordova.plugins.VideoLoader;

import org.apache.cordova.CallbackContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by eve on 10/7/17.
 */

public class cleaner implements Runnable {
    public static String $DIR = "mordoboy_videos";

    private CallbackContext $context;

    protected cleaner(CallbackContext context) {
        this.$context = context;
    }


    public static cleaner F(CallbackContext context) {
        return new cleaner(context);
    }


    @Override
    public void run() {
        File $dir = new File($DIR);
        if ($dir.exists()) {
            String deleteCmd = "rm -r " + $DIR; //Создаем текстовую командную строку
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd); //Выполняем системные команды
            } catch (IOException e) {
                this.$context.error(e.getMessage());
                return;
            }
        }
        this.$context.success("ready");
    }
}
