package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.amhamogus.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Click handler that makes a call to backend.
     */
    public void tellJoke(View view) {

        new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));

    }

    /**
     * AsycnTask that makes a call to the end point and retrieves a joke.
     */
    class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        //was private static
        MyApi myApiService = null;
        private Context context;

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
                Intent intent = new Intent(getApplicationContext(),
                        amogus.com.androidjokelib.JokeMainActivity.class);
                intent.putExtra("JOKE", result);
                startActivity(intent);
            } else {
                {
                    // Error State
                    Toast.makeText(context, "Empty result from the cloud.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
