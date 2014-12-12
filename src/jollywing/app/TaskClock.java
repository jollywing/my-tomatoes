package jollywing.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import java.util.Timer;

public class TaskClock extends Activity
{
    private final int TOMATO_INTERVAL = 25 * 60; // seconds
    private final Timer timer = new Timer();
    private TextView clockView;
    private Button startBtn, cancelBtn;
    private Handler handler;
    private TomatoTask task = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tomato_clock);

        clockView = (TextView)findViewById(R.id.clock_time);
        clockView.setText(formatTimeString(TOMATO_INTERVAL));
        startBtn = (Button)findViewById(R.id.clock_start_btn);
        cancelBtn = (Button)findViewById(R.id.clock_abort_btn);

        handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    super.handleMessage(msg);
                    String timeStr = formatTimeString(msg.what);
                    clockView.setText(timeStr);
                    if(msg.what == 0){
                    }
                }
            };

    }

    public void onCancelClock(View view)
    {
        if(task != null){
            task.cancel();
            task = null;
        }
        clockView.setText(formatTimeString(TOMATO_INTERVAL));
        cancelBtn.setEnabled(false);
        startBtn.setEnabled(true);
    }

    public void onStartClock(View view)
    {
        // new Thread
        task = new TomatoTask(TOMATO_INTERVAL, handler);
        timer.schedule(task, 1000, 1000);
        // disable button
        startBtn.setEnabled(false);
        cancelBtn.setEnabled(true);
    }


    public void onBackToList(View view)
    {
        if(task != null){
            task.cancel();
            task = null;
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    public String formatTimeString(int seconds)
    {
        String minStr, secStr;
        int sec = seconds % 60;
        int min = seconds / 60;
        if (sec < 10)
            secStr = "0" + sec;
        else
            secStr = "" + sec;
        if (min < 10)
            minStr = "0" + min;
        else
            minStr = "" + min;
        return minStr + ":" + secStr;
    }

}
