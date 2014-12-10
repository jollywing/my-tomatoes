package jollywing.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.util.HashMap;
import java.util.ArrayList;
import android.util.Log;
import android.content.Intent;

public class TaskList extends Activity
{
    private ListView taskList;
    private ArrayList<HashMap<String, Object>> taskListData =
            new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter taskAdapter;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        taskList = (ListView)findViewById(R.id.task_list);

        loadTaskData();
        taskAdapter =new SimpleAdapter(this, taskListData, R.layout.task_item,
                new String[] {"taskName", "taskTomatoNum"},
                new int[]{R.id.task_name, R.id.task_tomato_num});
        
        taskList.setAdapter(taskAdapter);
        taskList.setOnItemClickListener(new OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent,
                        View view, int position, long id){
                    Intent intent = new Intent(TaskList.this, TaskClock.class);
                    startActivityForResult(intent, 0);
                }
            });
    }

    private void loadTaskData()
    {
        for(int i = 0; i < 5; i ++){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("taskName", "Task " + i);
            map.put("taskTomatoNum", i);
            taskListData.add(map);
        }
    }

    public void onNewTaskClick(View view){
        Intent intent = new Intent(this, NewTask.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (resultCode == RESULT_OK){
            Bundle data = intent.getExtras();
            String taskName = data.getString("new_task_name");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("taskName", taskName);
            map.put("taskTomatoNum", 0);
            taskListData.add(map);
            taskAdapter.notifyDataSetChanged();
        }
    }
}
