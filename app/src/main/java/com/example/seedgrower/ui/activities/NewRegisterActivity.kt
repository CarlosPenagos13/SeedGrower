package com.example.seedgrower.ui.activities

import android.graphics.Color
import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher

import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import com.example.seedgrower.R
import com.example.seedgrower.ui.util.Util

import com.example.seedgrower.databinding.ActivityNewRegisterBinding
import com.example.seedgrower.handler.SocketHandler
import com.example.seedgrower.models.User


class NewRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewRegisterBinding
    private lateinit var socketHandler: SocketHandler
    private var isInitiateSessions : Boolean = false

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        socketHandler = SocketHandler()

        iniViews()
    }

    private fun iniViews(){
        setToolBarInView(binding.toolBar)
        reviewCurrentData()

        binding.btnRegisterUser.setOnClickListener{
            if(!Util().isDebugModeOn() && isValidData){
                doRegister()
            }else {
                showAlertDialog()
            }
        }
    }

    //ToolBar View
    private fun setToolBarInView(toolbar:Toolbar){
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    //Valid Data Form
    private val isValidData : Boolean
        get() {
            if (binding.textInputUser.text!!.length < 5 && binding.textInputUser.text.toString().isBlank()){
                binding.textInputUser.error = "Ingrese un usuario válido"
                return false
            }

            if (!Util().isValidEmail((binding.textInputEmail.text.toString()))){
                binding.textInputEmail.error = "Ingrese un correo Valido"
                return false
            }

            if (binding.textInputPassword.text!!.length < 5 && binding.textInputPassword.text.toString().isBlank()){
                return false
            }

            return true
        }

    //do Register
    private lateinit var alertDialog : AlertDialog

    private fun doRegister(){
        val user = User(
            binding.textInputUser.text.toString(),
            binding.textInputEmail.text.toString(),
            binding.textInputPassword.text.toString()
        )
        socketHandler.emitRegisterUser(user)
        isInitiateSessions = socketHandler.onRegisterUser()

        if(isInitiateSessions){
            showAlertDialog()
        }else{
            Toast.makeText(this, "Tuvimos un problema con el registro intenta mas tarde", Toast.LENGTH_LONG).show()
        }
    }

    private fun showAlertDialog(){
        val builderDialog = AlertDialog.Builder(this)
        val layoutInflater = layoutInflater.inflate(R.layout.dialog_fragment_success, null)

        val dialogButton : AppCompatButton = layoutInflater.findViewById(R.id.buttonSuccess)
        val dialogText : TextView = layoutInflater.findViewById(R.id.dialogTextSuccess)
        dialogText.text = resources.getText(R.string.register_sucessful_dialog)

        builderDialog.setView(layoutInflater)
        alertDialog = builderDialog.create()
        alertDialog.show()

        dialogButton.setOnClickListener {
            alertDialog.dismiss()
            onBackPressed()
        }
    }

    //Change Appearance of Button
    // Watchers of text listener
    private fun reviewCurrentData(){
        binding.textInputEmail.addTextChangedListener(listenerOfChangeText)
        binding.textInputPassword.addTextChangedListener(listenerOfChangeText)
        binding.btnRegisterUser.isClickable = false
    }

    private var listenerOfChangeText : TextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val mailWatcher = binding.textInputEmail.getText().toString()
            val passWord = binding.textInputPassword.getText().toString()

            if (mailWatcher.length > 5 && passWord.length > 5){
                binding.btnRegisterUser.isClickable = true
                binding.btnRegisterUser.setBackgroundColor(Color.BLUE)
            }else{
                binding.btnRegisterUser.isClickable = false
                binding.btnRegisterUser.setBackgroundColor(Color.LTGRAY)
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    }
}