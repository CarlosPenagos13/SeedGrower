package com.example.seedgrower.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity

import android.content.Context
import android.content.Intent

import android.content.pm.PackageManager

import android.location.Location
import android.location.LocationManager

import android.os.Bundle

import android.view.LayoutInflater

import android.widget.Toast

import androidx.core.app.ActivityCompat

import com.example.seedgrower.ui.util.Util
import com.example.seedgrower.databinding.ActivityLoginBinding
import com.example.seedgrower.handler.SocketHandler
import com.example.seedgrower.models.LoginResponse
import com.example.seedgrower.models.User

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var socketHandler: SocketHandler
    private lateinit var isLoginSuccess: LoginResponse

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        socketHandler = SocketHandler()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        initView()
    }

   private fun initView(){
       val context : Context = this

       binding.btnRegister.setOnClickListener{
           clearForm()
           startActivity(Intent(context, NewRegisterActivity::class.java))
       }
       binding.btnLogin.setOnClickListener{
           if(!Util().isDebugModeOn() && isValidData){
               val user = User(
                   binding.passwordLogin.text.toString(),
                   binding.emailLogin.text.toString()
               )
               socketHandler.emitLoginUser(user)
               isLoginSuccess = socketHandler.onLoginUser()

               if(isLoginSuccess.ok.toString().toBoolean()){
                   Util().saveStringPref("JWT", isLoginSuccess.token.toString())
                   clearForm()
                   val intent = Intent(this, TaskManagerActivity::class.java)
                   val bundle = Bundle()
                   bundle.putParcelable("KEY_GPS", mLocation)
                   intent.putExtras(bundle)
                   startActivity(intent)
               }else{
                   Toast.makeText(this, "Tuvimos un problema al iniciar sesion intentalo de nuevo", Toast.LENGTH_LONG).show()
               }
           }else{
               Util().saveStringPref("JWT", "jsonToken")

               val intent = Intent(this, TaskManagerActivity::class.java)
               val bundle = Bundle()
               bundle.putParcelable("KEY_GPS", mLocation)
               intent.putExtras(bundle)
               startActivity(intent)
           }
       }
    }

    private val isValidData : Boolean
        get() {
            if (!Util().isValidEmail(binding.emailLogin.text.toString())){
                binding.emailLogin.error = "Ingrese un correo Valido"
                return false
            }

            if (binding.passwordLogin.text!!.length < 5 && binding.passwordLogin.text.toString().isBlank()){
                binding.passwordLogin.error = "Ingrese una contraseña válida"
                return false
            }

            return true
        }

    private fun clearForm(){
        binding.passwordLogin.text?.clear()
        binding.emailLogin.text?.clear()
    }


    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private var mLocation : Location? = null

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener (this){ task->
                   mLocation = task.result
                }
            } else {
                mLocation = null
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

}