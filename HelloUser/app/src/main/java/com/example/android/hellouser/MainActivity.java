package com.example.android.hellouser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btnSubmit);
        final EditText editText = (EditText) findViewById(R.id.txtInput);
        final TextView textView = (TextView)findViewById(R.id.txtResult);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = editText.getText().toString();
                String existText = (String)textView.getText();
                textView.setText(existText+ " " +result);

                Toast toast = Toast.makeText(getApplicationContext(),"Append text to the label", Toast.LENGTH_LONG);
                toast.show();

            }
        });

    }


}
