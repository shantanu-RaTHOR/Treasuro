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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Queue;

import static android.widget.Toast.LENGTH_LONG;

public class register extends AppCompatActivity
{
    EditText Name,rans;
    private FirebaseAuth mAuth;
    ProgressBar p;
    DatabaseReference dbr,dbdash,dbcounterf,dbcounteru;
    String ranstring,nme,no_registeration;
    TextToSpeech toSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        p=(ProgressBar) findViewById(R.id.pbLoading);
        Name=(EditText) findViewById(R.id.name);
        rans=(EditText) findViewById(R.id.stringl);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        dbr= FirebaseDatabase.getInstance().getReference("PARTICIPANTS");
        dbdash=FirebaseDatabase.getInstance().getReference("DASHBOARD");
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
        p.setVisibility(ProgressBar.VISIBLE);
      nme=(Name.getText().toString());
      ranstring=rans.getText().toString();
        if (nme.isEmpty())
        {
            p.setVisibility(ProgressBar.INVISIBLE);
            Name.setError("NAME IS EMPTY");
            Name.requestFocus();
            return;
        }
        if (ranstring.isEmpty())
        {
            p.setVisibility(ProgressBar.INVISIBLE);
            rans.setError("STRING IS EMPTY");
            rans.requestFocus();
            return;
        }
        if (nme.length() < 6)
        {
            p.setVisibility(ProgressBar.INVISIBLE);
            rans.setError("Name should be 6 chars");
            rans.requestFocus();
            return;
        }
        else
        {
            int check=0;
            String s[]={"zxcvbn","asdfgh","qwerty"};
            for(int i=0;i<3;i++)
            {
                if((s[i].equals(ranstring)))
                {
                  check=1;
                }

            }
            if(check==0)
            {
                p.setVisibility(ProgressBar.INVISIBLE);
                rans.setError("STRING IS INVALID");
                rans.requestFocus();
                return;
            }
        }
        mAuth.createUserWithEmailAndPassword(ranstring+"@gmail.com",nme ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {

                    partiinfo obj=new partiinfo(nme,ranstring,0,1,5);
                    dbr.child(ranstring).setValue(obj);


                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    p.setVisibility(ProgressBar.INVISIBLE);
                    startActivity(i);
                    toSpeech.speak("REGistered successfully", TextToSpeech.QUEUE_FLUSH,null);
                   Toast.makeText(getApplicationContext(),"user registered succesfully",Toast.LENGTH_LONG).show();


                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        p.setVisibility(ProgressBar.INVISIBLE);
                        toSpeech.speak("User already registered", TextToSpeech.QUEUE_FLUSH,null);
                        Toast.makeText(getApplicationContext(),"User already registered,",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), LENGTH_LONG).show();
                }
            }


        });
    }
    public void login(View v)
    {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);

    }




}
