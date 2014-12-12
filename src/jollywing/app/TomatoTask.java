package jollywing.app;

import java.util.TimerTask;
import android.os.Handler;
import android.os.Message;

public class TomatoTask extends TimerTask
{
    private int remainSeconds;
    private Handler handler;

    public TomatoTask(int interval, Handler h){
        super();
        remainSeconds = interval;
        handler = h;
    }

    @Override
    public void run(){
        remainSeconds--;
        Message message = new Message();
        message.what = remainSeconds;
        handler.sendMessage(message);
    }

}
