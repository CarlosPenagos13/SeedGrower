package com.example.seedgrower.ui.activities

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.seedgrower.ui.util.Util
import com.example.seedgrower.databinding.ActivityTaskManagerBinding
import com.example.seedgrower.handler.SocketHandler
import com.example.seedgrower.models.TaskListModel
import com.example.seedgrower.models.TaskModel
import com.example.seedgrower.models.Token
import com.example.seedgrower.ui.adapters.TaskAdapter
import com.pixplicity.easyprefs.library.Prefs

@Suppress("DEPRECATION")
class TaskManagerActivity : AppCompatActivity(), TaskAdapter.OnTaskListener {

    private lateinit var binding: ActivityTaskManagerBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var socketHandler: SocketHandler
    private lateinit var isHaveTaskUser: TaskListModel
    private lateinit var taskCreate : TaskModel
    private lateinit var isDeleteTask: String

    private var taskList : ArrayList<TaskModel>? = arrayListOf()
    private var tokenJWT : String? = ""
    private var mLocation : Location? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        tokenJWT = Util().loadStringPref("JWT")
        socketHandler = SocketHandler()

        if(intent.extras != null){
            mLocation = intent.extras!!.getParcelable("KEY_GPS")
        }

        initViews()

        if(!Util().isDebugModeOn()){
            loadTaskUser()
        }else{
            if(intent != null && intent.extras != null && intent.extras!!.containsKey("KEY_TASK_CREATE")){
                taskCreate = intent.extras!!.getParcelable("KEY_TASK_CREATE")!!
                taskList!!.add(taskCreate)
                taskList!!.add(taskCreate)
            }else{
                taskList = arrayListOf()
            }
            viewListSelected(taskList)
        }
    }

    private fun initViews(){
        binding = ActivityTaskManagerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.fab.setOnClickListener{
            val intent = Intent(this, CreateTaskActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("KEY_GPS", mLocation)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        binding.dismissActivityClose.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            taskList = arrayListOf()
        }
    }

    private fun processTaskAdapter(){
        taskAdapter = TaskAdapter(this, taskList!!)

        val lManager = LinearLayoutManager(this)

        binding.recyclerTask.layoutManager = lManager
        binding.recyclerTask.itemAnimator = DefaultItemAnimator()
        binding.recyclerTask.adapter = taskAdapter
    }

    private fun loadTaskUser(){
        val token = Token(tokenJWT)

        socketHandler.emitListTaskUser(token)
        isHaveTaskUser = socketHandler.onListTask()

        if(isHaveTaskUser.ok.toString().toBoolean()){
            viewListSelected(isHaveTaskUser.taskObtainUser)
        }else{
            viewListSelected(arrayListOf())
            Toast.makeText(this, "Tuvimos un problema al conseguir las tareas creadas", Toast.LENGTH_LONG).show()
        }
    }


    private fun viewListSelected(taskListModel: ArrayList<TaskModel>?){
        taskList = taskListModel
        if( taskList != null && taskList!!.isNotEmpty()) {
            binding.recyclerTask.visibility = View.VISIBLE
            binding.emptyTaskList.visibility = View.GONE
        }else{
            binding.recyclerTask.visibility = View.GONE
            binding.emptyTaskList.visibility = View.VISIBLE
        }
        processTaskAdapter()
    }


    //Listener Buttons Task
    override fun onTaskChangedClose(task: TaskModel) {
        if (!Util().isDebugModeOn()){
            for (taskM: TaskModel in taskList!!) {
                if (taskM.uuid == task.uuid){
                    socketHandler.emitDeleteTaskUser(Util().loadStringPref("JWT"), task.uuid)
                    isDeleteTask = socketHandler.onDeleteTask()
                    if (isDeleteTask == "success"){
                        taskList!!.remove(task)
                    }
                    break
                }
            }
            viewListSelected(taskList)

        }else{
            for (taskM: TaskModel in taskList!!) {
                if (taskM.uuid == task.uuid){
                    taskList!!.remove(task)
                    break
                }
            }
            viewListSelected(taskList)
        }
    }

    override fun onTaskChangedSuccess(taskModl: TaskModel){
        if(!Util().isDebugModeOn()){
            socketHandler.emitDetailTaskUser(Prefs.getString("JWT"), taskModl)
            val taskSelected = socketHandler.onDetailTaskUser()
            val intent = Intent(this, DetailTaskActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("KEY_TASK_DETAIL", taskSelected)
            intent.putExtras(bundle)
            startActivity(intent)
        }else{
            val intent = Intent(this, DetailTaskActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("KEY_TASK_DETAIL", taskModl)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

}