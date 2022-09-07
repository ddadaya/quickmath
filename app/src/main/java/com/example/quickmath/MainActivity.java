package com.example.quickmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity{

    //объявление переменных с activity
    private EditText email;
    private EditText pass;
    private Button reg;
    private Button log;

    //подключение к firebase
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Players_data");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //определение объектов с аctivity
        reg=findViewById(R.id.registration);
        log=findViewById(R.id.login);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);

        //обработчики кликов по кнопкам регистрации и входа
        reg.setOnClickListener(view -> {
            auth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnSuccessListener(authResult->{

                //стартовое заполнение бд
                ref.child(auth.getCurrentUser().getUid()).child("mail").setValue(email.getText().toString());
                ref.child(auth.getCurrentUser().getUid()).child("password").setValue(pass.getText().toString());
                ref.child(auth.getCurrentUser().getUid()).child("total").setValue("0");

            });
        });

        log.setOnClickListener(view -> {
            auth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                    .addOnCompleteListener(this,task->{
                        if(task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "успешно", Toast.LENGTH_SHORT).show();

                              //переход на новое окно
                              Intent intent = new Intent(MainActivity.this, math_test.class);
                              startActivity(intent);

                        }else {
                            Toast.makeText(MainActivity.this, "безуспешно", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}