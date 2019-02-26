package org.acecambodia.aceapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.idp.camtesol.R;
import  org.acecambodia.aceapp.R;
public class NoInternet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        TextView txt =(TextView)findViewById(R.id.txt_no_internet);
        txt.setText("No internet connection.");
        Button btnok = (Button) findViewById(R.id.btnOk);
        btnok.setText("Refresh");
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

    }
}
