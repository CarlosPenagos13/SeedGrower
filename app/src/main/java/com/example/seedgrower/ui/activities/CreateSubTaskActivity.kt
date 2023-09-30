package com.example.seedgrower.ui.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar

import com.example.seedgrower.R
import com.example.seedgrower.databinding.ActivityCreateSubTaskBinding
import com.example.seedgrower.handler.SocketHandler
import com.example.seedgrower.models.CreateSubTask
import com.example.seedgrower.models.SubTaskModel
import com.example.seedgrower.models.TaskModel
import com.example.seedgrower.models.UpdateTaskList

import com.pixplicity.easyprefs.library.Prefs


@Suppress("DEPRECATION")
class CreateSubTaskActivity() : AppCompatActivity() {

    private lateinit var binding : ActivityCreateSubTaskBinding
    private lateinit var onSubTaskUser: CreateSubTask
    private lateinit var alertDialog : AlertDialog
    private lateinit var taskSelected : TaskModel

    private var mLocation: Location? = null
    private var socketHandler: SocketHandler = SocketHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSubTaskBinding.inflate(LayoutInflater.from(this))

        if(intent.extras != null){
            mLocation = intent.extras!!.getParcelable("KEY_GPS")
        }

        if(intent.extras != null){
            taskSelected = intent.extras!!.getParcelable("KEY_TASK_DETAIL")!!
        }

        socketHandler.initGetSocket()

        initViews()
    }

    private fun initViews(){
        setContentView(binding.root)
        setToolBarInView(binding.toolBarSubTask.toolBar)

        binding.btnCreateSubTask.setOnClickListener{
            if(isValidData){
                val createSubTask = CreateSubTask(
                    token = Prefs.getString("JWT"),
                    data = UpdateTaskList(
                        uuid = taskSelected.uuid,
                        subTasks = SubTaskModel(
                            title = binding.textInputSubTask.text.toString(),
                            description = binding.textInputDescriptionSubTask.text.toString()
                        )

                    )
                )

                socketHandler.emitRegisterSubTaskUser(createSubTask)
                onSubTaskUser = socketHandler.onRegisterSubTaskUser()

                showAlertDialog()
                startActivity(Intent(this, TaskManagerActivity::class.java))
            }
        }
    }

    private fun setToolBarInView(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolBarSubTask.toolBarText.text = "Create SubTask"
    }

    private val isValidData : Boolean
        get() {
            if (binding.textInputSubTask.text.toString().isBlank()){
                binding.textInputSubTask.error = "Ingresa un titulo valido"
                return false
            }

            if (binding.textInputDescriptionSubTask .text.toString().isBlank()){
                binding.textInputDescriptionSubTask.error = "Ingrese una descripcion valida"
                return false
            }

            return true
        }


    private fun showAlertDialog(){
        val builderDialog = AlertDialog.Builder(this)
        val layoutInflater = layoutInflater.inflate(R.layout.dialog_fragment_success, null)

        val dialogButton : AppCompatButton = layoutInflater.findViewById(R.id.buttonSuccess)
        val dialogText :TextView = layoutInflater.findViewById(R.id.dialogTextSuccess)
        dialogText.text = resources.getText(R.string.create_task_sucessful_dialog)

        builderDialog.setView(layoutInflater)
        alertDialog = builderDialog.create()

        alertDialog.show()

        dialogButton.setOnClickListener {
            alertDialog.dismiss()
            startActivity(Intent(this, TaskManagerActivity::class.java))
        }
    }
}