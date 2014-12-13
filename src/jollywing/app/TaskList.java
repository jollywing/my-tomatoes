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
    private final int NEW_TASK_REQUEST = 1;
    private final int RANDOM_TOMATO_REQUEST = 2;
    private final int TASK_TOMATO_REQUEST = 3;

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
                    Bundle data = new Bundle();
                    data.putInt("task_id", position);
                    HashMap<String, Object> map = taskListData.get(position);
                    data.putString("task_name", (String)map.get("taskName"));
                    intent.putExtras(data);
                    startActivityForResult(intent, TASK_TOMATO_REQUEST);
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
        startActivityForResult(intent, NEW_TASK_REQUEST);
    }

    public void onRandomTomatoClick(View view)
    {
        Intent intent = new Intent(TaskList.this, TaskClock.class);
        Bundle data = new Bundle();
        data.putString("task_name", getResources().getString(R.string.random_tomato_text));
        intent.putExtras(data);
        startActivityForResult(intent, RANDOM_TOMATO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (resultCode == RESULT_OK){
            if(requestCode == NEW_TASK_REQUEST){
                Bundle data = intent.getExtras();
                String taskName = data.getString("new_task_name");
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("taskName", taskName);
                map.put("taskTomatoNum", 0);
                taskListData.add(map);
                taskAdapter.notifyDataSetChanged();
            }
            else if(requestCode == TASK_TOMATO_REQUEST){
                Bundle data = intent.getExtras();
                int taskId = data.getInt("task_id");
                Log.i("MyTomatoes", "" + taskId);
                HashMap<String, Object> map = taskListData.get(taskId);
                map.put("taskTomatoNum", ((Integer)map.get("taskTomatoNum")).intValue() + 1);
                taskAdapter.notifyDataSetChanged();
            }
        }
    }
}
