package ml.shahidkamal.popularmovies.API;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by shaah on 07-06-2018.
 */

public interface ResponseHandler {
    void onSuccess(JSONObject response);

    void onError(VolleyError error);
}
