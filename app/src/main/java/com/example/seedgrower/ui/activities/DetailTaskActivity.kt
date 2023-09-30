package com.example.seedgrower.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.seedgrower.R
import com.example.seedgrower.databinding.ActivityDetailTaskBinding
import com.example.seedgrower.handler.SocketHandler
import com.example.seedgrower.models.CheckSubTask
import com.example.seedgrower.models.SubTaskModel
import com.example.seedgrower.models.TaskModel
import com.example.seedgrower.models.UpdateTaskList
import com.example.seedgrower.ui.adapters.SubTaskAdapter
import com.pixplicity.easyprefs.library.Prefs

@Suppress("DEPRECATION")
class DetailTaskActivity : AppCompatActivity() , SubTaskAdapter.OnTaskListener {
    private lateinit var binding : ActivityDetailTaskBinding
    private lateinit var subTaskAdapter: SubTaskAdapter
    private lateinit var taskSelected : TaskModel
    private lateinit var subTaskSelected : ArrayList<SubTaskModel>
    private lateinit var socketHandler: SocketHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTaskBinding.inflate(LayoutInflater.from(this))
        socketHandler = SocketHandler()

        if(intent.extras != null){
            taskSelected = intent.extras!!.getParcelable("KEY_TASK_DETAIL")!!
        }

        subTaskSelected = arrayListOf(SubTaskModel("dsad", "sadaasd"))

        initViews()
    }

    private fun initViews(){
        setContentView(binding.root)
        setToolBarInView(binding.toolBarDetailTask.toolBar)
        setButtonInitiate()
        binding.toolBarDetailTask.toolBarText.text = taskSelected.title
        binding.toolBarDetailTask.toolBarText.setTextColor(getColor(R.color.white))
        viewOfTaskDetail()
        viewListSelected()
    }

    override fun onSupportNavigateUp(): Boolean {
        val taskUpdater = UpdateTaskList(
            token = Prefs.getString("JWT"),
            taskSelected.uuid,
            subTaskSelected
        )

        socketHandler.emitUpdateTaskUser(taskUpdater)
        socketHandler.onUpdateTask()
        onBackPressed()
        return true
    }

    private fun setToolBarInView(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setBackgroundColor((Math.random() * 16777215).toInt() or (0xFF shl 24))
    }

    private fun viewOfTaskDetail(){
        binding.detailTaskItem.checkTask.visibility = View.GONE
        binding.detailTaskItem.closeTask.visibility = View.GONE

        binding.detailTaskItem.nameOfTask.text = taskSelected.title
        binding.detailTaskItem.descriptionTask.text = taskSelected.description

    }

    private fun processSubTaskAdapter(){
        subTaskAdapter = SubTaskAdapter(this, subTaskSelected)

        val lManager = LinearLayoutManager(this)

        binding.recyclerSubTask.layoutManager = lManager
        binding.recyclerSubTask.itemAnimator = DefaultItemAnimator()
        binding.recyclerSubTask.adapter = subTaskAdapter
    }

    private fun viewListSelected(){
        processSubTaskAdapter()
    }

    private fun setButtonInitiate(){
        binding.newObject.setOnClickListener{
            val taskSelected = taskSelected
            val intent = Intent(this, DetailTaskActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("KEY_TASK_DETAIL", taskSelected)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        binding.btnEndTask.setOnClickListener{
            val finishTask = CheckSubTask(Prefs.getString("JWT"), taskSelected.uuid)
            socketHandler.emitFinishedTaskUser(finishTask)
            socketHandler.onFinishTask()
            startActivity(Intent(this, FinishActivity::class.java))
        }
    }

    override fun onSubCheck(subTask: SubTaskModel?) {
        val taskCreated = CheckSubTask(
            Prefs.getString("JWT"),
            taskSelected.uuid,
            subTask!!.uuidSubtask
        )
        socketHandler.emitCheckSubTaskUser(taskCreated)
        socketHandler.onCheckTask()
    }
}