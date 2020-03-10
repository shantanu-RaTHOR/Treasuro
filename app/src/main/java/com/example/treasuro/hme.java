package com.example.treasuro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class hme extends AppCompatActivity
{
    DatabaseReference dbr, dbrq;
    TextView tv,tvname,tvscore,tvlevel,tvattmpt;
    Button b;
    String naam,string,qno,score,qq,aa,attmpt;
    TextToSpeech toSpeech;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hme);
        Intent i=getIntent();
         naam=i.getStringExtra("NAME");
         string=i.getStringExtra("USTRING");
        progressDialog = new ProgressDialog(hme.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Wait for a while");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
         toSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
         {
             @Override
             public void onInit(int i)
             {
                 if(i!=TextToSpeech.ERROR)
                     toSpeech.setLanguage(Locale.UK);

             }
         });
        toSpeech.setSpeechRate(0.97f);
        fetchqno();

    }
    public void fetchqno()
    {

        dbr = FirebaseDatabase.getInstance().getReference("PARTICIPANTS").child(string);

        dbr.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                qno = dataSnapshot.child("qno").getValue().toString();
                score=dataSnapshot.child("score").getValue().toString();
                attmpt=dataSnapshot.child("attmpt").getValue().toString();
                tv = (TextView) findViewById(R.id.textv_q);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        dbrq = FirebaseDatabase.getInstance().getReference("QUES");
        dbrq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                qq=dataSnapshot.child(qno).child("q").getValue().toString();
                aa=dataSnapshot.child(qno).child("a").getValue().toString();
                tv=(TextView) findViewById(R.id.textv_q);
                tvname=(TextView) findViewById(R.id.name);
                tvlevel=(TextView) findViewById(R.id.level);
                tvscore=(TextView) findViewById(R.id.score);
                tvattmpt=(TextView) findViewById(R.id.att);
                tvname.setText(naam);
                tvlevel.setText("Level-"+qno);
                tvscore.setText(""+score);
                tv.setText(qq);
                tvattmpt.setText("Remaining attempts-"+attmpt);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
    public void Home(View v)
    {
        Toast.makeText(getApplicationContext(),"Already on home",Toast.LENGTH_LONG).show();
    }
    public void dashboard(View v)
    {
        Intent i=new Intent(getApplicationContext(),dashboard.class);
        i.putExtra("name",naam);
        startActivity(i);
    }
    public void info(View v)
    {
        Intent i=new Intent(getApplicationContext(),info.class);
        startActivity(i);
    }
    public void rules(View v)
    {
        Intent i=new Intent(getApplicationContext(),rules.class);
        startActivity(i);
    }




    public void scanner(View v)
    {
        Intent i=new Intent(getApplicationContext(),scanner.class);
        i.putExtra("ans",aa);
        i.putExtra("name",naam);
        i.putExtra("ustring",string);
        i.putExtra("score",score);
        i.putExtra("qno",qno);
        i.putExtra("at",attmpt);
        startActivity(i);
    }



}
