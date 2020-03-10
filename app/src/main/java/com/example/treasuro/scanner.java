package com.example.treasuro;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

//implementing onclicklistener
public class scanner extends AppCompatActivity
{

    //View Objects
    private Button buttonScan;
    private TextView textViewName, textViewAddress;
    String ans,name,ustring,score,qno,attmpt;
    String scan_result;
    DatabaseReference dbr;
    int s,q;
    //qr code scanner object
    private IntentIntegrator qrScan;
    TextToSpeech toSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        Intent i=getIntent();
        ans=i.getStringExtra("ans");
        name=i.getStringExtra("name");
        ustring=i.getStringExtra("ustring");
        score=i.getStringExtra("score");
        qno=i.getStringExtra("qno");
        attmpt=i.getStringExtra("at");
        //intializing scan object
        qrScan = new IntentIntegrator(this);
        dbr = FirebaseDatabase.getInstance().getReference("PARTICIPANTS");
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
        qrScan.initiateScan();

        //attaching onclick listener
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),hme.class);
                i.putExtra("NAME",name);
                i.putExtra("USTRING",ustring);
                startActivity(i);

            }
            else
            {
                scan_result=result.getContents();
                s=Integer.parseInt(score);
                q=Integer.parseInt(qno);
                int attpt=Integer.parseInt(attmpt);
                if(scan_result.equals(ans))
                {
                    Toast.makeText(getApplicationContext(),"Well Done",Toast.LENGTH_LONG).show();
                    toSpeech.speak("Well done,Let's move to next question", TextToSpeech.QUEUE_FLUSH,null);
                    partiinfo obj=new partiinfo(name,ustring,(s+10-2*(5-attpt)),(q+1),5);
                    dbr.child(ustring).setValue(obj);
                    Intent i=new Intent(getApplicationContext(),hme.class);
                    i.putExtra("NAME",name);
                    i.putExtra("USTRING",ustring);
                    startActivity(i);

                }
                else
                {

                    partiinfo obj=new partiinfo(name,ustring,s,q,(attpt-1));
                    dbr.child(ustring).setValue(obj);
                    Toast.makeText(getApplicationContext(),"WRONG ANSWER",Toast.LENGTH_LONG).show();
                    toSpeech.speak("Wrong answer,Keep trying!", TextToSpeech.QUEUE_FLUSH,null);
                    Intent i=new Intent(getApplicationContext(),hme.class);
                    i.putExtra("NAME",name);
                    i.putExtra("USTRING",ustring);
                    startActivity(i);


                }

            }
        }
        else
            {
            super.onActivityResult(requestCode, resultCode, data);
            }
    }

    public void sc(View view)
    {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}
