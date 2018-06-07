package ml.shahidkamal.popularmovies.API;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by shaah on 07-06-2018.
 */

public class VolleySingleton {
    private static VolleySingleton volleySingleton;
    private static RequestQueue requestQueue;
    private static Context ctx;

    private VolleySingleton(Context context){
        ctx=context;
        requestQueue = getRequestQueue();
    }

    public static VolleySingleton getInstance(Context context){
        if (volleySingleton == null) {
            volleySingleton = new VolleySingleton(context);
        }
        return  volleySingleton;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        return requestQueue;
    }

    public void addToRequestQueue(Request request){
        getRequestQueue().add(request);
    }

}
