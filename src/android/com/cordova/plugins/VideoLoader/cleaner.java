package com.cordova.plugins.VideoLoader;

import android.content.Context;

import org.apache.cordova.CallbackContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by eve on 10/7/17.
 */

public class cleaner implements Runnable {

    private CallbackContext $context;
    private Context appcontext;

    protected cleaner(CallbackContext context) {
        this.$context = context;
    }


    public static cleaner F(CallbackContext context) {
        return new cleaner(context);
    }

    public cleaner setContext(Context co){
        this.appcontext =co;
        return this;
    }


    @Override
    public void run() {
        GlobalCleaner.F(this.appcontext);
        this.$context.success("ready");
    }
}
