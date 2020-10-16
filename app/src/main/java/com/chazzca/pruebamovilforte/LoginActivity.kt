package com.chazzca.pruebamovilforte

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chazzca.pruebamovilforte.utils.Utils
import com.chazzca.pruebamovilforte.utils.snackAlerta
import com.chazzca.pruebamovilforte.utils.snackError
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        /*val myPref = prefs?.getString(userKey, "0")
        if(!myPref.equals("0")){
            val intent = Intent(this, ListadoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }*/
        setContentView(R.layout.activity_main)

        btnInicio.setOnClickListener {
            val usuario = edtUsuario.text.toString()
            val pass = edtPass.text.toString()

            if (Utils.isConnected(this)){
                if(usuario.isEmpty() or pass.isEmpty())
                    btnInicio.snackAlerta("No deje campos vacios")
                else{
                    postLogin(usuario, pass)
                }
             }else{
                 btnInicio.snackAlerta("No cuentas con conexión a  internet")
             }
        }

    }

    fun postLogin(user: String, pass: String){

        val queue = Volley.newRequestQueue(applicationContext)
        val url: String = Utils.urlBase+"/api/auth/login"

        val parameters = JSONObject()
        try {
            parameters.put("Usuario", user)
            parameters.put("Password", pass)
        } catch (e: Exception) {
        }

        val sr = object :
            JsonObjectRequest(Method.POST, url, parameters,
                Response.Listener { response ->
                    var strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    Log.e("bienAPI",jsonObj.getInt("code").toString())
                    if(jsonObj.getInt("code") == 200){
                        val data: JSONObject = jsonObj.getJSONObject("data")
                        val token = data.getString("token")
                        val editor = prefs?.edit()
                        editor?.putString(Utils.TOKEN_KEY,token)
                        editor?.putString(Utils.USER_KEY,user)
                        editor?.apply()


                        val intent = Intent(this, ListadoActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    }else {
                        btnInicio?.snackAlerta("Usuario o contraseña no válido")
                    }

                },
                Response.ErrorListener { error ->
                    btnInicio?.snackError("error: ${error.message}")
                    Log.e("errorAPI", "error: $error")
                }) {

        }
        queue.add(sr)
    }

}