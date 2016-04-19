package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.amhamogus.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by amhamogus on 4/14/16.
 */
public class EndpointsTask extends AsyncTask<Pair<Context, String>, Void, String> {

    MyApi myApiService = null;
    private Context context;
    private EndpointTaskListener listener = null;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://amogus-udacity-project4.appspot.com/_ah/api/");
            // end options for devappserver

            // call the backend
            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        try {
            return myApiService.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    /**
     * Called when the AsyncTask has attempted to contact the backend.
     *
     * @param result A string that represents a joke.
     */
    @Override
    protected void onPostExecute(String result) {
        // check if we have a joke or not
        if (result != null) {
            // Success State
            if (this.listener != null) {
                // added callback to for testing purposes
                this.listener.onEndpointReturned(result);
            }
            // Pass the returned joke to the next screen
            Intent intent = new Intent(context,
                    amogus.com.androidjokelib.JokeMainActivity.class);
            intent.putExtra("JOKE", result);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } else {
            {
                // Error State
                Toast.makeText(context, "Empty result from the cloud.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public EndpointsTask setListener(EndpointTaskListener listener) {
        this.listener = listener;
        return this;
    }

    public interface EndpointTaskListener {
        void onEndpointReturned(String joke);
    }

}
