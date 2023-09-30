package com.example.seedgrower.ui.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar

import com.example.seedgrower.R
import com.example.seedgrower.ui.util.Util
import com.example.seedgrower.databinding.FragmentCreateTaskBinding
import com.example.seedgrower.handler.SocketHandler
import com.example.seedgrower.models.CreateTaskModel
import com.example.seedgrower.models.SubTaskModel
import com.example.seedgrower.models.TaskModel


@Suppress("DEPRECATION")
class CreateTaskActivity() : AppCompatActivity() {

    private lateinit var binding : FragmentCreateTaskBinding
    private lateinit var onCreateTaskSuccess: CreateTaskModel
    private lateinit var alertDialog : AlertDialog


    private var mLocation: Location? = null
    private var socketHandler: SocketHandler = SocketHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCreateTaskBinding.inflate(LayoutInflater.from(this))

        if(intent.extras != null){
            mLocation = intent.extras!!.getParcelable("KEY_GPS")
        }

        socketHandler.initGetSocket()

        initViews()
    }

    private fun initViews(){
        setContentView(binding.root)
        setToolBarInView(binding.toolBarTextActivity.toolBar)

        binding.btnRegisterUser.setOnClickListener{
            if(!Util().isDebugModeOn() && isValidData){
                 val createTaskModel = CreateTaskModel(null,
                     Util().loadStringPref("JWT"),
                     TaskModel(
                         mLocation,
                         binding.textInputNameGrow.text?.toString(),
                         binding.textInputDescriptionGrow.text.toString(),
                        null
                     )
                 )

                socketHandler.emitCreateTaskUser(createTaskModel)
                onCreateTaskSuccess = socketHandler.onCreateTaskUser()

                if(onCreateTaskSuccess.ok.toString().toBoolean()){
                    showAlertDialog()
                    startActivity(Intent(this, TaskManagerActivity::class.java))
                }else{
                    Toast.makeText(this, "Tuvimos un problema al iniciar sesion intentalo de nuevo", Toast.LENGTH_LONG).show()
                }
            }else{
                showAlertDialog()
            }
        }
    }

    private fun setToolBarInView(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolBarTextActivity.toolBarText.text = resources.getText(R.string.crear_grow)
    }

    private val isValidData : Boolean
        get() {
            if (binding.textInputNameGrow.text.toString().isBlank()){
                binding.textInputNameGrow.error = "Ingresa un titulo valido"
                return false
            }

            if (binding.textInputDescriptionGrow.text.toString().isBlank()){
                binding.textInputDescriptionGrow.error = "Ingrese una descripcion valida"
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
            if(!Util().isDebugModeOn()){
                startActivity(Intent(this, TaskManagerActivity::class.java))
            }else{
                val intent = Intent(this, TaskManagerActivity::class.java)
                val bundle = Bundle()
                val task = TaskModel(
                    coordinates =  mLocation,
                    title = binding.textInputNameGrow.text?.toString(),
                    description =  binding.textInputDescriptionGrow.text.toString(),
                    uuid = "asdasdasd-asdasdasd",
                )
                bundle.putParcelable("KEY_TASK_CREATE", task)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }
}