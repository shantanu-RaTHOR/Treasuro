package com.example.treasuro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;


public class dashboard extends Activity
{
    String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};
    DatabaseReference database;
    String username;
    TextView mrank;
    TextToSpeech toSpeech,toSpeech2;
    int c=0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        database= FirebaseDatabase.getInstance().getReference("PARTICIPANTS");
        mrank=(TextView)findViewById(R.id.rank);
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
        toSpeech2=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int i)
            {
                if(i!=TextToSpeech.ERROR)
                    toSpeech2.setLanguage(Locale.UK);

            }
        });
        toSpeech2.setSpeechRate(0.97f);
        fetch();

    }
    public void fetch()
    {

     database.addListenerForSingleValueEvent(new ValueEventListener()
    {
        DataSnapshot dataSnapshot;
    @Override
    public void onDataChange(@NonNull DataSnapshot d)
    {
        dataSnapshot=d;
        for (DataSnapshot snp:dataSnapshot.getChildren())
        {
            c = c + 1;
        }

        int score[]=new int[c];
        String name[]=new String[c];
        Intent intent=getIntent();
        username=intent.getStringExtra("name");
        String myrank ="";
        c=0;
        for (DataSnapshot snp:dataSnapshot.getChildren())
        {
            partiinfo info=snp.getValue(partiinfo.class);
           score[c]=(info.score);
           name[c]=info.name;
            c=c+1;
        }
        String topper="";
        for (int i = 0; i < c; i++)
        {

            int max_idx = i;
            for (int j = i; j < c; j++)
                if (score[j] > score[max_idx])
                    max_idx = j;
            String ts=name[max_idx];
            name[max_idx]=name[i];
            name[i]=ts;
            int temp=score[max_idx];
            score[max_idx]=score[i];
            score[i]=temp;
            if(username.equals(name[i]))
                myrank=""+(i+1);
            int l=10-ts.length();
            if(i==0)
                topper=name[i];
            for(int p=1;p<=l;p++)
                name[i]=name[i]+" ";
            name[i]=(i+1)+")-"+name[i]+"            "+score[i];
        }




        toSpeech2.speak("Your current rank is,"+myrank, TextToSpeech.QUEUE_FLUSH,null);

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.activity_listview, name);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        if(topper.equals(username))
        {
            toSpeech.speak( "WEll done,You are leading the game",TextToSpeech.QUEUE_FLUSH,null);
        }
        toSpeech.speak( topper+",is leading the game",TextToSpeech.QUEUE_FLUSH,null);
        mrank.setText(myrank);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError)
    {

    }

}
        );
}
}