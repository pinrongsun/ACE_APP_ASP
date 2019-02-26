package org.acecambodia.aceapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

        import java.io.DataOutputStream;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import android.util.Log;
import android.widget.Toast;


import javax.net.ssl.HttpsURLConnection;

public class HttpFileUpload implements Runnable{
    URL connectURL;
    String responseString;
    String Title;
    String FileName;
    String Description;
    Bitmap bitmap;
    byte[ ] dataToServer;
    InputStream fileInputStream = null;

    HttpFileUpload(String urlString, String vFileName, String vTitle, String vDescription){
        try{
            connectURL = new URL(urlString);
            FileName = vFileName;
            Title = vTitle;
            Description = vDescription;
            Log.d("FileName", "test");
        }catch(Exception ex){
            Log.i("HttpFileUpload","URL Malformatted");
        }
    }

    void Send_Now(Bitmap mybitmap){
        //fileInputStream = fStream;
        bitmap = mybitmap;
        Sending();
    }

    void Sending(){
        String attachmentName = "ace";
        String attachmentFileName = "ace.jpg";
        String iFileName = FileName;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="fSnd";
        String crlf = "\r\n";

        try
        {
            Log.d(Tag,"Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            HttpsURLConnection conn = (HttpsURLConnection)connectURL.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");

            conn.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);


            byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
            for (int i = 0; i < bitmap.getWidth(); ++i) {
                for (int j = 0; j < bitmap.getHeight(); ++j) {
                    //we're interested only in the MSB of the first byte,
                    //since the other 3 bytes are identical for B&W images
                    pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
                }
            }

            Log.d(Tag,pixels.toString());
            Log.d(Tag,connectURL.toString());

            DataOutputStream dos;

            try{
                Log.d(Tag,"Headers are written");
                 dos = new DataOutputStream(
                        conn.getOutputStream());
            }catch (Exception e){
                Log.d(Tag,"Error doc:" + e.getMessage());
                dos = null;

            }




            dos.writeBytes(twoHyphens + boundary + crlf);
            dos.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            dos.writeBytes(crlf);

            Log.d(Tag,"Headers are written");

            //I want to send only 8 bit black & white bitmaps


            dos.write(pixels);

            dos.writeBytes(crlf);
            dos.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);

            dos.flush();
            dos.close();

            InputStream responseStream = new
                    BufferedInputStream(conn.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();

            responseStream.close();
            conn.disconnect();


        }
        catch (MalformedURLException ex)
        {
            Log.d(Tag, "URL error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe)
        {
            Log.d(Tag, "IO error: " + ioe.getMessage(), ioe);
        }

        class uploadtoserver extends AsyncTask<String,String,String>{
            public uploadtoserver() {
                super();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                return null;
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }



}