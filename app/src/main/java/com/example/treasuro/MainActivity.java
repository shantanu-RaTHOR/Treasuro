package com.example.treasuro;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity
{
    EditText nme,strng;
    FirebaseAuth mAuth;
    ProgressBar p;
    String name,string;
    TextToSpeech toSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nme=(EditText) findViewById(R.id.name);
        strng=(EditText) findViewById(R.id.str);
        p=(ProgressBar) findViewById(R.id.pb);
        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        toSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                if(status!=TextToSpeech.ERROR)
                    toSpeech.setLanguage(Locale.UK);
            }
        });
        toSpeech.setSpeechRate(0.97f);

    }
    public void signup(View v)
    {
        Intent i=new Intent(this,register.class);
        startActivity(i);
    }
    public void login(View v)
    {
     name=(nme.getText().toString());
     string=strng.getText().toString();
     p.setVisibility(ProgressBar.VISIBLE);
        if (name.isEmpty())
        {
            p.setVisibility(ProgressBar.INVISIBLE);
            nme.setError("mail is empty");
            nme.requestFocus();
            return;
        }
        if (string.isEmpty())
        {
            strng.setVisibility(ProgressBar.INVISIBLE);
            strng.setError("Password is empty");
            strng.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(string+"@gmail.com",name).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {

                    p.setVisibility(ProgressBar.INVISIBLE);
                    Intent i=new Intent(getApplicationContext(),hme.class);
                    i.putExtra("NAME",name);
                    i.putExtra("USTRING",string);
                    toSpeech.speak("Welcome"+name+", Let's Hunt treasure ", TextToSpeech.QUEUE_FLUSH,null);
                    startActivity(i);

                }
                else
                {
                    p.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), LENGTH_LONG).show();
                }
            }
        });


    }


}
