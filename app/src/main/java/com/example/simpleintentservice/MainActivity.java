package com.example.simpleintentservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button Start,Stop;
    private TextView percent;
    private ProgressBar progress;
    private Intent serviceIntent;

    private ResponceReciver reciver=new ResponceReciver();
    public class ResponceReciver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyIntentService.ACTION_1))
            {
                int value=intent.getIntExtra(MyIntentService.PARAM_PERCENT,0);
                new ShowProgressTaskBar().execute(value);
            }
        }
    }

    class ShowProgressTaskBar extends AsyncTask<Integer,Integer,Integer>
    {
        @Override
        protected Integer doInBackground(Integer... integers) {
            return integers[0];
        }
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progress.setProgress(integer);
            percent.setText(integer+" %Loaded");
            if(integer==100){
                percent.setText("Completed");
                Start.setEnabled(true);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Start=findViewById(R.id.start);
        Stop=findViewById(R.id.stop);
        percent=findViewById(R.id.percent);
        progress=findViewById(R.id.progress);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Start.setEnabled(false);
                serviceIntent=new Intent(MainActivity.this,MyIntentService.class);
                startService(serviceIntent);
            }
        });
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serviceIntent!=null)
                    MyIntentService.shouldStop=true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(reciver,new IntentFilter(MyIntentService.ACTION_1));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(reciver);
    }
}