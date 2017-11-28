package management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.firebaseauthentication.R;
 
/**
 * Created by taylan on 16.11.2017.
 */

public class GroupManagement extends AppCompatActivity {
    Button buttonJoinToGroup;
    Button buttonCreateGroup;
    Button buttonGroupSettings;
    Button buttonDeleteGroup;
    Button buttonShowQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_management);

        buttonCreateGroup = (Button)findViewById(R.id.buttonCreate);
        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
        });

        buttonJoinToGroup = (Button)findViewById(R.id.buttonJoin);
        buttonJoinToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinToGroup();
            }
        });

        buttonShowQR = (Button)findViewById(R.id.buttonShowQR);
        buttonShowQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQr();
            }
        });
    }
    private void createGroup(){
        Intent intent = new Intent(this,CreateGroup.class);
        startActivity(intent);
    }
    private void joinToGroup(){
        Intent intent = new Intent(this,QrReaderActivity.class);
        startActivity(intent);
    }
    private void showQr(){
        Intent intent = new Intent(this,QrActivity.class);
        startActivity(intent);
    }
}
