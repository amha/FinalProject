package amogus.com.androidjokelib;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class JokeMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_main);

        TextView textView = (TextView) findViewById(R.id.joke_display);
        if(getIntent() != null){
            textView.setText(getIntent().getStringExtra("JOKE"));
        }
    }

}
