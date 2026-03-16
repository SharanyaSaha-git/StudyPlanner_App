package com.example.studyplanner;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyplanner.Adapter.ToDoAdapter;
import com.example.studyplanner.Model.ToDoModel;
import com.example.studyplanner.Utils.DataBaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{
    private RecyclerView tasksRecyclerview;
    private ToDoAdapter taskadapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;
    private DataBaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),(v,insets)->
        {
            Insets systemBars=insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left,systemBars.top,systemBars.right,systemBars.bottom);
            return insets;
        });
        getSupportActionBar().hide();
        db=new DataBaseHandler(this);
        db.opendatabase();
        taskList=new ArrayList<>();
        tasksRecyclerview=findViewById(R.id.taskrecyclerview);
        tasksRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        taskList=db.getAllTasks();
        taskadapter=new ToDoAdapter(db,this);
        taskadapter.setTasks(taskList);
        tasksRecyclerview.setAdapter(taskadapter);
        fab=findViewById(R.id.fab);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(taskadapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerview);
        Collections.reverse(taskList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }
    @Override
    public void handleDialogClose(DialogInterface dialog)
    {
        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        taskadapter.setTasks(taskList);
        taskadapter.notifyDataSetChanged();
    }
}