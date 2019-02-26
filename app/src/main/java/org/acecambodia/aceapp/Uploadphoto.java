package org.acecambodia.aceapp;

/**
 * Created by Chanpheakdey.Vong on 20/07/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
//import com.idp.camtesol.R;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.acecambodia.aceapp.R;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Uploadphoto extends  AppCompatActivity {
    Button GetImageFromGalleryButton, UploadImageOnServerButton, CameraButton;

    ImageView ShowSelectedImage;

    EditText GetImageName;

    Bitmap FixBitmap;

    String ImageTag = "image_tag" ;

    String ImageName = "image_data" ;

    String ServerUploadPath ="https://app.acecambodia.org/UploadHander2.ashx" ;

    ProgressDialog progressDialog ;

    ByteArrayOutputStream byteArrayOutputStream ;

    byte[] byteArray ;

    String ConvertImage ;

    String GetImageNameFromEditText;

    HttpsURLConnection httpURLConnection ;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter ;

    int RC ;

    BufferedReader bufferedReader ;

    StringBuilder stringBuilder;

    String upload_url;
    String ContactID;
    boolean check = true;
    private static final int CAMERA_REQUEST = 1888;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        Log.d("ACECambodia","2");

        Intent myIntent = getIntent(); // gets the previously created intent
        upload_url = myIntent.getStringExtra("upload_url");
        Log.d("ACECambodia","3");
        String[] separated = upload_url.split("PhotoID=");
        ContactID = separated[1];
        Log.d("ACECambodia","4");
        GetImageFromGalleryButton = (Button)findViewById(R.id.btnUpload);

        //UploadImageOnServerButton = (Button)findViewById(R.id.button2);

        ShowSelectedImage = (ImageView)findViewById(R.id.imageView);

        byteArrayOutputStream = new ByteArrayOutputStream();

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);

            }
        });

        CameraButton = (Button) findViewById(R.id.btnCamera);
        CameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });




    }
    @Override
    protected void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == CAMERA_REQUEST && RQC == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) I.getExtras().get("data");

            try {
                FixBitmap = photo;
                ShowSelectedImage.setImageBitmap(FixBitmap);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String formattedDate = df.format(c.getTime());

                GetImageNameFromEditText = ContactID + formattedDate + ".jpg";
                UploadImageToServer();

                //Intent intent = new Intent(Uploadphoto.this,MainActivity.class);
                //intent.putExtra("redirect_url",upload_url.replace("upload.aspx?","crop.aspx?photourl=" + GetImageNameFromEditText + "&"));
               // startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {
           // Bitmap photo = (Bitmap) I.getExtras().get("data");

            Uri uri = I.getData();

            try {

                FixBitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ShowSelectedImage.setImageBitmap(FixBitmap);
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String formattedDate = df.format(c.getTime());

                GetImageNameFromEditText = ContactID + formattedDate + ".jpg";
                UploadImageToServer();


            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    public void UploadImageFile(){
        try {
            // Set your file path here
            //FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/DCIM/file.mp4");

            Toast.makeText(Uploadphoto.this,"Start Upload",Toast.LENGTH_LONG).show();

           // FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            //byteArray = byteArrayOutputStream.toByteArray();

            //ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            //ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
           // byte[] bitmapdata = bos.toByteArray();
           // InputStream bs = new ByteArrayInputStream(bitmapdata);
            //FileInputStream is = new FileInputStream(bos.toString());
            Log.d("myfileupload","upload");
            //FileInputStream fstream = new FileInputStream(bs.toString());
            Log.d("myfileupload","uploaded");
            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload("https://app.acecambodia.org/UploadHander2.ashx", "ttt.jpg","upload image","my file description");

            hfu.Send_Now(FixBitmap);

        } catch (Exception e) {
            // Error: File not found
            Log.d("myerror",e.getMessage());
        }
    }

    public void Upload2(){
        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("image",  ConvertImage);
        postData.put("Content-Disposition","form-data");
        postData.put("Content-Type","image/jpg");

        HttpClient client1 = new DefaultHttpClient();


        PostResponseAsyncTask task  = new PostResponseAsyncTask(Uploadphoto.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
        });
        task.execute("https://app.acecambodia.org/UploadHander2.ashx");


    }
    public void UploadImageToServer(){

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(Uploadphoto.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();
                Intent intent = new Intent(Uploadphoto.this,MainActivity.class);
                intent.putExtra("redirect_url",upload_url.replace("upload.aspx?","crop.aspx?photourl=" + GetImageNameFromEditText + "&"));
                startActivity(intent);

                //Toast.makeText(Uploadphoto.this,string1, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                Log.d("ACECambodia","5");
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                //HashMap<String,String> HashMapParams = new HashMap<String,String>();
                //HashMapParams.put(ImageTag, GetImageNameFromEditText);
                //HashMapParams.put(ImageName, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, FixBitmap, GetImageNameFromEditText);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }



    public class ImageProcessClass{

    public String ImageHttpRequest(String requestURL,Bitmap bitmap,String filename) {
        String attachmentName = "ace000";
        String attachmentFileName = filename;
        //String iFileName = FileName;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="fSnd";
        String crlf = "\r\n";
        StringBuilder stringBuilder = new StringBuilder();

        try {

            url = new URL(requestURL);

            httpURLConnection = (HttpsURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(20000);

            httpURLConnection.setConnectTimeout(20000);

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            httpURLConnection.setDoInput(true);

            httpURLConnection.setDoOutput(true);
            Log.d("ACECambodia","6");
            //outputStream = httpURLConnection.getOutputStream();
            Log.d("ACECambodia","7");

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + filename +"\"" + lineEnd);

            dos.writeBytes("Content-Type: image/jpeg" + lineEnd);
            dos.writeBytes(lineEnd);
            FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            dos.write(byteArray);//your image array here buddy
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"your parameter name\"" + crlf);
            dos.writeBytes(lineEnd);
            //dos.writeBytes(testName);//your parameter value
            dos.writeBytes(lineEnd); //to add multiple parameters write Content-Disposition: form-data; name=\"your parameter name\"" + crlf again and keep repeating till here :)
            dos.writeBytes(twoHyphens + boundary + twoHyphens);
            dos.flush();
            dos.close();



            Log.d("ACECambodia","88");


            RC = httpURLConnection.getResponseCode();

            if (RC == HttpsURLConnection.HTTP_OK) {
                Log.d("ACECambodia","9:");
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                stringBuilder = new StringBuilder();

                String RC2;

                while ((RC2 = bufferedReader.readLine()) != null){

                    stringBuilder.append(RC2);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

        public String ImageHttpsRequest(String requestURL,Bitmap bitmap)
        {

           return Sending(requestURL, bitmap);
        }

    private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

        stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
            if (check)
                check = false;
            else
                stringBuilder.append("&");

            stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

            stringBuilder.append("=");

            stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
        }

        return stringBuilder.toString();
    }

        private String Sending(String requestUrl, Bitmap bitmap){
            String attachmentName = "ace";
            String attachmentFileName = "ace.jpg";
            //String iFileName = FileName;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String Tag="fSnd";
            String crlf = "\r\n";

            try
            {
                Log.d(Tag,"Starting Http File Sending to URL");
                url = new URL(requestUrl);
                // Open a HTTP connection to the URL
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

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
                Log.d(Tag,url.toString());

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
                Log.d("ACECambodia","8");
                InputStream responseStream = new
                        BufferedInputStream(conn.getInputStream());
                Log.d("ACECambodia","9");
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
            return stringBuilder.toString();

        }

}


}
