package org.apache.cordova.VideoLoader;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * Created by eve on 10/7/17.
 */

public class VideoLoader extends CordovaPlugin {

    public static String ACTION_GET = "loadVideo";
    public static String ACTION_CLEAR = "clearStorage";


    private Context xgetContext(){
        return this.cordova.getActivity().getApplicationContext();
    }
    
    

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if (ACTION_GET.equals(action)) {
            String url = args.getString(0);
            String token = args.getString(1);
            loader r = loader.F(url, token, callbackContext).setContext(this.xgetContext());
            new Thread(r).start();
            return true;
        }

        if(ACTION_CLEAR.equals(action)){
            cleaner c = cleaner.F(callbackContext).setContext(this.xgetContext());
            new Thread(c).start();
            return true;
        }
        callbackContext.error("Invalid action");
        return false;
    }
}
