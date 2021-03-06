package com.united_states_of_aizu.kingbeko;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by sansuke05 on 2016/08/20.
 */
public class HttpAsyncLoader extends AsyncTaskLoader<JSONObject>{
    //音声認識,AIサーバーへデータ送受信,音声合成

    private URL url = null;
    String inputPhrase = "";

    public HttpAsyncLoader(Context context,String urltext,String inputPhrase){
        super(context);
        this.inputPhrase = inputPhrase;
        try {
            this.url = new URL(urltext);
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject loadInBackground() {

        HttpURLConnection con = null;
        DataOutputStream os = null;

        try {
            con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);

            con.setUseCaches(false);

            con.setChunkedStreamingMode(0);

            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");

            String postData = "message=" + inputPhrase;
            os = new DataOutputStream(con.getOutputStream());
            os.writeBytes(postData);

            int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK){
                //通信に成功

                //JSONを取得
                BufferedInputStream in = new BufferedInputStream(con.getInputStream());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) != -1){
                    if (length > 0){
                        outputStream.write(buffer, 0, length);
                    }
                }

                JSONObject json = new JSONObject(new String(outputStream.toByteArray()));

                in.close();
                return json;
            } else {
                throw new ConnectException(String.valueOf(status));
            }

            //Thread.sleep(1000);
        } catch (ConnectException e1){
            e1.printStackTrace();
        } catch (JSONException e1){
            e1.printStackTrace();
        } catch (MalformedURLException e1){
            e1.printStackTrace();
        } catch (IOException e1){
            e1.printStackTrace();
        } //catch (InterruptedException e){}
        finally {
            if (con != null){
                con.disconnect();
            }
        }

        return null;
    }
}

class ConnectException extends Exception {
    public ConnectException(String msg) {
        super(msg);
    }
}
