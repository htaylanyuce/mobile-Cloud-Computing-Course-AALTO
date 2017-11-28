package management;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.firebaseauthentication.R;

/**
 * Created by juho on 13.11.2017.
 */

public class CreateGroup extends AppCompatActivity {

    Button buttonCreate;
    TextView textViewGroupName;
    TextView textViewGoupDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        textViewGroupName = (TextView)findViewById(R.id.editTextGroupName);
        textViewGoupDuration = (TextView)findViewById(R.id.editTextDuration);

        buttonCreate = (Button)findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });
    }

    private void createGroup(){
        String groupName = textViewGroupName.getText().toString();
        String minutes = textViewGoupDuration.getText().toString();

        //implement backend calls here
    }
}
