package jollywing.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewTask extends Activity
{
    private EditText taskNameEdit;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task);
        taskNameEdit = (EditText)findViewById(R.id.new_task_edit);        
    }

    public void onNewTaskCancel(View v)
    {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onNewTaskOK(View v)
    {
        String taskName = taskNameEdit.getText().toString();
        if ( taskName != ""){
            Bundle data = new Bundle();
            data.putString("new_task_name", taskName);
            setResult(RESULT_OK, getIntent().putExtras(data));
            finish();
        }
    }
}
