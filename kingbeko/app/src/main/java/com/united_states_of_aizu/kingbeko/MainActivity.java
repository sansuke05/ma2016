package com.united_states_of_aizu.kingbeko;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<JSONObject> {

    //------------------------------
    //クラス、変数
    //------------------------------
    //音声認識後のテキスト
    String input_Phrases_ = "";

    //URL
    private URL url = null;

    //進捗ダイアログ
    private ProgressDialog progressDialog_ = null;

    @Override
    public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
        String urltext = "https://king-beco.herokuapp.com/talk";

        HttpAsyncLoader httpAsyncLoader = new HttpAsyncLoader(this,urltext);
        httpAsyncLoader.forceLoad();
        return httpAsyncLoader;
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> looader, JSONObject data){
        if (data != null){
            try {
                Toast.makeText(this,data.getString("response"), Toast.LENGTH_LONG).show();
            } catch (JSONException e){
                Log.e("onLoadFinished","JSONのパースに失敗しました。　JSONException=" + e);
            }
        } else {
            Log.e("onLoadFinished","onLoadFinished error!");
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.d("kingbeko","button is clicked!");
                getLoaderManager().restartLoader(1, null, MainActivity.this);
            }
        });
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
}
