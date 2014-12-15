package jollywing.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class NewTask extends Activity
{
    private EditText taskNameEdit;
    private TextView newTaskHint;
    private final String LOG_TAG = "MyTomatoes.NewTask";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        taskNameEdit = (EditText)findViewById(R.id.new_task_edit);
        newTaskHint = (TextView)findViewById(R.id.new_task_hint);
        Log.i(LOG_TAG, taskNameEdit.getText().toString());
    }

    public void onNewTaskCancel(View v)
    {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onNewTaskOK(View v)
    {
        String taskName = taskNameEdit.getText().toString();
        // Toast.makeText(this, taskNameEdit.getText().toString(), Toast.LENGTH_LONG);
        if ( taskName.length() > 0){
            Bundle data = new Bundle();
            data.putString("new_task_name", taskName);
            setResult(RESULT_OK, getIntent().putExtras(data));
            finish();
        }
        else {
            String newTaskWarning = this.getResources().getString(R.string.new_task_warning);
            newTaskHint.setText(newTaskWarning);
        }
    }
}
