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
import android.util.Log;
import android.content.Intent;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;  
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;  
import org.w3c.dom.Node;  
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;  
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;

public class TaskList extends Activity
{
    private final int NEW_TASK_REQUEST = 1;
    private final int RANDOM_TOMATO_REQUEST = 2;
    private final int TASK_TOMATO_REQUEST = 3;
    private final String DATA_FILE_PATH = "/data/data/jollywing.app.MyTomatoes/tasks.xml";
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
        try {
            File dataXml = new File(DATA_FILE_PATH);
            if(!dataXml.exists())
                return;
            FileInputStream fis = new FileInputStream(dataXml);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(fis);
            Element rootElement = doc.getDocumentElement();
            NodeList items = rootElement.getElementsByTagName("task");
            for (int i = 0; i < items.getLength(); i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                Node item = items.item(i);  
                NamedNodeMap attrs = item.getAttributes();
                Node attr = attrs.getNamedItem("TaskName");
                map.put("taskName", attr.getNodeValue());
                attr = attrs.getNamedItem("TomatoCount");
                map.put("taskTomatoNum",
                        Integer.parseInt(attr.getNodeValue()));
                taskListData.add(map);
            }
        }
        catch(FileNotFoundException e){
            return;
        }
        catch(ParserConfigurationException e){
            Log.e("MyTomatoes", e.toString());
        }
        catch(SAXException e){
            Log.e("MyTomatoes", e.toString());
        }
        catch(IOException e){
            Log.e("MyTomatoes", e.toString());
        }
        

        // HashMap<String, Object> map = new HashMap<String, Object>();
        // map.put("taskName", "MyTask");
        // map.put("taskTomatoNum", 0);
        // taskListData.add(map);
    }

    private void saveTaskData()
    {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
            DocumentBuilder builder = factory.newDocumentBuilder();  
            Document doc = builder.newDocument();   //由builder创建新文档  
          
            Element rootElement = doc.createElement("tasks");  
  
            for (HashMap<String, Object> taskMap : taskListData) {  
                Element taskElement = doc.createElement("task");  
                taskElement.setAttribute("TaskName", (String)taskMap.get("taskName"));
                taskElement.setAttribute("TomatoCount", ((Integer)taskMap.get("taskTomatoNum")).toString());
                rootElement.appendChild(taskElement);  
            }  
          
            doc.appendChild(rootElement);  
          
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
          
            StringWriter writer = new StringWriter();  
          
            Source source = new DOMSource(doc);
            Result result = new StreamResult(writer);
            transformer.transform(source, result);

            FileOutputStream fos = new FileOutputStream(DATA_FILE_PATH);
            fos.write(writer.toString().getBytes());
            fos.close();
        }
        catch(ParserConfigurationException e){
            Log.e("MyTomatoes", e.toString());
        }
        catch(TransformerException e){
        }
        catch(IOException e){
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
                saveTaskData();
            }
            else if(requestCode == TASK_TOMATO_REQUEST){
                Bundle data = intent.getExtras();
                int taskId = data.getInt("task_id");
                Log.i("MyTomatoes", "" + taskId);
                HashMap<String, Object> map = taskListData.get(taskId);
                map.put("taskTomatoNum", ((Integer)map.get("taskTomatoNum")).intValue() + 1);
                taskAdapter.notifyDataSetChanged();
                saveTaskData();
            }
        }
    }
}
