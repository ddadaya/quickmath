package com.example.quickmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class math_test extends AppCompatActivity{

    //объявляем переменные под объекты с активити
    private TextView exm, score, health;
    private Button fb, sb, tb;

    //игровые переменные
    private int cnt,answer, lifes = 3;

    //подключение к firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference bd_con = database.getReference().child("Players_data").child(uid);

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_test);

        //определяем наши объекты с активити
        exm = findViewById(R.id.task);
        score = findViewById(R.id.urscore);
        health = findViewById(R.id.health);
        fb = findViewById(R.id.first);
        sb = findViewById(R.id.second);
        tb = findViewById(R.id.third);

        score.setText("Текущий счет: " + cnt);
        health.setText("Жизни: " + lifes);

        playertask();

        //3 обработчика кликов под разные кнопки
        fb.setOnClickListener(v->{
            CheckAns(fb.getText().toString());
            playertask();
        });

        sb.setOnClickListener(v->{
            CheckAns(sb.getText().toString());
            playertask();
        });

        tb.setOnClickListener(v->{
            CheckAns(tb.getText().toString());
            playertask();
        });
    }

    //генерация задания
    public void playertask(){
        Random rd = new Random();
        int a = rd.nextInt(30) + 1;
        int b = rd.nextInt(30) + 1;
        answer = 0;
        char sign = '0';
        switch (rd.nextInt(3)) {
            case 0:
                sign = '+';
                answer = a + b;
                break;
            case 1:
                sign = '*';
                answer = a * b;
                break;
            case 2:
                sign = '-';
                answer = a - b;
                break;
        }
        exm.setText(a + " " + sign + " " + b);
        puttask();
    }

    //проверка ответа
    private void CheckAns(String userAns){
        if(userAns.equals(String.valueOf(answer))){
            cnt++;
            score.setText("Ваш счет: " + cnt);
        } else {
            if(lifes > 0){
                lifes--;
                health.setText("Жизни: " + lifes);
            } else{

                //обновляем поле total в бд
                bd_con.child("total").setValue(cnt);

                //переход в некст окно
                Intent intent = new Intent(math_test.this, result.class);

                //передаем uid в некст окно
                intent.putExtra("uid",  uid);

                startActivity(intent);
            }


        }
    }

    //заполнение задания в кнопки
    private void puttask(){
        Random rd = new Random();

        //создаем массив ответов
        List<Integer> list = Arrays.asList( answer + (rd.nextInt(20) + 1), answer + (rd.nextInt(20) + 1), answer);

        Collections.shuffle(list); //перемешиваем массив

        //заполняем кнопки
        fb.setText(String.valueOf(list.get(0)));
        sb.setText(String.valueOf(list.get(1)));
        tb.setText(String.valueOf(list.get(2)));
    }
}