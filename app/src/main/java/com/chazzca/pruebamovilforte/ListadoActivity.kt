package com.chazzca.pruebamovilforte

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chazzca.pruebamovilforte.adapters.RecyclerViewAdapter
import com.chazzca.pruebamovilforte.modelos.ModeloCliente
import com.chazzca.pruebamovilforte.modelos.ModeloListadoUsuarios
import com.chazzca.pruebamovilforte.utils.Utils
import com.chazzca.pruebamovilforte.utils.snackAlerta
import com.chazzca.pruebamovilforte.utils.snackError
import com.chazzca.pruebamovilforte.utils.snackExito
import kotlinx.android.synthetic.main.activity_listado.*
import kotlinx.android.synthetic.main.activity_listado.btnAgregar
import kotlinx.android.synthetic.main.agregar_editar_modal.*
import kotlinx.android.synthetic.main.agregar_editar_modal.view.*
import kotlinx.android.synthetic.main.agregar_editar_modal.view.btnBorrar
import kotlinx.android.synthetic.main.agregar_editar_modal.view.btnEditar
import kotlinx.android.synthetic.main.listado_row.view.*
import org.json.JSONArray
import org.json.JSONObject

class ListadoActivity : AppCompatActivity() {

    var recyclerListado : RecyclerView? = null
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapter: RecyclerViewAdapter
    var usuarios:MutableList<ModeloListadoUsuarios> = ArrayList()
    var cliente:MutableList<ModeloCliente> = ArrayList()

    private var prefs: SharedPreferences? = null
    private var tokenKey : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val userStr = prefs!!.getString(Utils.USER_KEY, "")
        tokenKey = prefs!!.getString(Utils.TOKEN_KEY, "")

        setTitle("Bienvenido ${userStr}")

        recyclerListado = recyclerListadoUsuarios

        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerListado?.setHasFixedSize(true)
        recyclerListado?.layoutManager = gridLayoutManager

        adapter = RecyclerViewAdapter(
            getDatosListDumb(),
            object : RecyclerViewAdapter.OnItemClickListener {

                override fun onItemClickEditar(posicion: Int) {
                    getCliente(usuarios[posicion].clienteId, true)
                }

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onItemClickBorrar(posicion: Int) {
                    this?.let { mostrarDialogoEliminar(posicion) }
                }
            })
        recyclerListado?.adapter = adapter

        btnAgregar.setOnClickListener{
            mostrarModalCompleto(cliente, false)
        }

        swipeR.setOnRefreshListener {
            getListadoUsuarios(tokenKey!!)
        }

       getListadoUsuarios(tokenKey!!)
    }

    //Modales
    private fun mostrarModalCompleto(cliente: MutableList<ModeloCliente>, edicion: Boolean){
        val dialogBuilder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        ).create()
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.agregar_editar_modal, null)
        val btnBorrar = dialogView.findViewById<ImageView>(R.id.btnBorrar)

        dialogView.btnBorrar.setOnClickListener{
            dialogBuilder.dismiss()
        }

        dialogView.btnEditar.setOnClickListener {


                val clienteId = cliente[0].clienteId
                val nombreCompleto = dialogView.edtNombre.text.toString()
                val rfc = dialogView.edtRfc.text.toString()
                val fechaNacimiento = "1992-10-16T07:00:48.114Z"
                val correoElectronico = dialogView.edtCorreo.text.toString()
                val telefonoMovil = dialogView.edtTelefono.text.toString()
                val domicilio = dialogView.edtDomicilio.text.toString()
                val limiteCredito = dialogView.edtLimite.text.toString()
                val estatusCliente = 1

                if (nombreCompleto == "" || limiteCredito == "" || correoElectronico == "") {
                    dialogView.btnBorrar.snackError("Tienes que llenar todos los campos")
                } else {
                    cliente.add(
                        ModeloCliente(
                            clienteId,
                            nombreCompleto,
                            rfc,
                            fechaNacimiento,
                            correoElectronico,
                            telefonoMovil,
                            domicilio,
                            limiteCredito.toDouble(),
                            estatusCliente
                        )
                    )

                    editCliente(cliente, dialogBuilder,dialogView)


            }
        }

        dialogView.btnAgregar.setOnClickListener {
            val correoCliente = dialogView.edtCorreo.text.toString()
            val valor:ModeloListadoUsuarios? = usuarios.find { it.correoElectronico == correoCliente }

            if(valor == null){
                val clienteId = 0
                val nombreCompleto = dialogView.edtNombre.text.toString()
                val rfc = dialogView.edtRfc.text.toString()
                val fechaNacimiento = "1992-10-16T07:00:48.114Z"
                val correoElectronico = dialogView.edtCorreo.text.toString()
                val telefonoMovil =  dialogView.edtTelefono.text.toString()
                val domicilio = dialogView.edtDomicilio.text.toString()
                val limiteCredito = dialogView.edtLimite.text.toString()
                val estatusCliente = 1

                if(nombreCompleto == "" || limiteCredito== "" ||  correoElectronico== "" ){
                    dialogView.btnBorrar.snackError("Tienes que llenar todos los campos")
                }else{
                    cliente.add(
                        ModeloCliente(
                            clienteId,
                            nombreCompleto,
                            rfc,
                            fechaNacimiento,
                            correoElectronico,
                            telefonoMovil,
                            domicilio,
                            limiteCredito.toDouble(),
                            estatusCliente
                        )
                    )
                }



                setClient(cliente, dialogBuilder,dialogView)
            }else{
                btnBorrar.snackAlerta("No puedes agregar un cliente nuevo con el mismo correo")
            }
        }

        if(edicion){
            dialogView.findViewById<Button>(R.id.btnEditar).visibility = View.VISIBLE
            dialogView.edtNombre.setText(cliente[0].nombreCompleto)
            dialogView.edtCorreo.setText(cliente[0].correoElectronico)
            dialogView.edtRfc.setText(cliente[0].rfc)
            dialogView.edtFechaNac.setText(cliente[0].fechaNacimiento)
            dialogView.edtTelefono.setText(cliente[0].telefonoMovil)
            dialogView.edtDomicilio.setText(cliente[0].domicilio)
            dialogView.edtLimite.setText(cliente[0].limiteCredito.toString())
        }else{
            dialogView.findViewById<Button>(R.id.btnAgregar).visibility = View.VISIBLE
        }

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    private fun mostrarDialogoEliminar(posicion: Int){

        val dialogBuilder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialogo_pregunta, null)
        val txtAlerta = dialogView.findViewById<TextView>(R.id.txtAlerta)
        val button1 = dialogView.findViewById<Button>(R.id.buttonSubmit)
        val button2 = dialogView.findViewById<Button>(R.id.buttonCancel)


        button1.setOnClickListener {
            deleteCliente(usuarios[posicion].clienteId)
            dialogBuilder.dismiss()
        }
        button2.setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.setView(dialogView)
        dialogBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBuilder.show()
    }

    //////////Peticiones API
    fun setClient(cliente: MutableList<ModeloCliente>, dialogBuilder: AlertDialog,view: View) {
        val queue = Volley.newRequestQueue(applicationContext)
        val url: String = Utils.urlBase+"/api/cliente"
        val parameters = JSONObject()
        try {
            parameters.put("clienteId", cliente[0].clienteId)
            parameters.put("nombreCompleto", cliente[0].nombreCompleto)
            parameters.put("rfc", cliente[0].rfc)
            parameters.put("fechaNacimiento", cliente[0].fechaNacimiento)
            parameters.put("correoElectronico", cliente[0].correoElectronico)
            parameters.put("telefonoMovil", cliente[0].telefonoMovil)
            parameters.put("domicilio", cliente[0].domicilio)
            parameters.put("limiteCredito", cliente[0].limiteCredito)
            parameters.put("estatusClienteId", cliente[0].estatusClienteId)
        } catch (e: Exception) {
        }
        val sr = object :
            JsonObjectRequest(
                Method.POST, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    Log.e("bienAPI", jsonObj.getInt("code").toString())
                    if (jsonObj.getInt("code") == 200) {
                        view.btnAgregar.snackExito("Cliente insertado con éxito")
                        adapter.notifyDataSetChanged()
                        dialogBuilder.dismiss()
                    } else if (jsonObj.getInt("code") == 500) {
                        view.btnAgregar?.snackAlerta("Error interno del servidor (500)")
                    }else {
                        view.btnAgregar?.snackAlerta("Ups algo ocurrió")
                    }
                },
                Response.ErrorListener { error ->
                    view.btnAgregar?.snackError("error: ${error.message}")
                    Log.e("errorAPI", "error: $error")
                }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $tokenKey"
                return headers
            }
        }
        queue.add(sr)
    }

    fun editCliente(cliente: MutableList<ModeloCliente>, dialogBuilder: AlertDialog,view: View) {
        val queue = Volley.newRequestQueue(applicationContext)
        val url: String = Utils.urlBase+"/api/cliente/"+cliente[0].clienteId
        val parameters = JSONObject()
        try {
            parameters.put("clienteId", cliente[0].clienteId)
            parameters.put("nombreCompleto", cliente[0].nombreCompleto)
            parameters.put("rfc", cliente[0].rfc)
            parameters.put("fechaNacimiento", cliente[0].fechaNacimiento)
            parameters.put("correoElectronico", cliente[0].correoElectronico)
            parameters.put("telefonoMovil", cliente[0].telefonoMovil)
            parameters.put("domicilio", cliente[0].domicilio)
            parameters.put("limiteCredito", cliente[0].limiteCredito)
            parameters.put("estatusClienteId", cliente[0].estatusClienteId)
        } catch (e: Exception) {
        }
        val sr = object :
            JsonObjectRequest(
                Method.PUT, url, parameters,
                Response.Listener { response ->
                    val strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    Log.e("bienAPI", jsonObj.getInt("code").toString())
                    if (jsonObj.getInt("code") == 200) {
                        view.btnAgregar.snackExito("Cliente editado con éxito")
                        adapter.notifyDataSetChanged()
                        dialogBuilder.dismiss()
                    } else if (jsonObj.getInt("code") == 500) {
                        view.btnAgregar?.snackAlerta("Error interno del servidor (500)")
                    }else {
                        view.btnAgregar?.snackAlerta("Ups algo ocurrió")
                    }

                },
                Response.ErrorListener { error ->
                    view.btnAgregar?.snackError("error: ${error.message}")
                    Log.e("errorAPI", "error: $error")
                }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $tokenKey"
                return headers
            }
        }
        queue.add(sr)
    }

    fun getCliente(idCliente: Int, editar: Boolean) {
        val queue = Volley.newRequestQueue(applicationContext)
        val url: String = Utils.urlBase+"/api/cliente/"+idCliente

        val sr = object :
            StringRequest(
                Method.GET, url,
                Response.Listener { response ->
                    cliente.clear()
                    val strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    Log.e("bienAPI", jsonObj.getInt("code").toString())
                    if (jsonObj.getInt("code") == 200) {
                        val data: JSONObject = jsonObj.getJSONObject("data")
                        val clienteId = data.getInt("clienteId")
                        val nombreCompleto = data.getString("nombreCompleto")
                        val rfc = data.getString("rfc")
                        val fechaNacimiento = data.getString("fechaNacimiento")
                        val correoElectronico = data.getString("correoElectronico")
                        val telefonoMovil = data.getString("telefonoMovil")
                        val domicilio = data.getString("domicilio")
                        val limiteCredito = data.getDouble("limiteCredito")
                        val estatusCliente = data.getInt("estatusClienteId")

                        cliente.add(
                            ModeloCliente(
                                clienteId,
                                nombreCompleto,
                                rfc,
                                fechaNacimiento,
                                correoElectronico,
                                telefonoMovil,
                                domicilio,
                                limiteCredito,
                                estatusCliente
                            )
                        )

                        mostrarModalCompleto(cliente, editar)

                    } else {
                        btnAgregar?.snackAlerta("U")
                    }
                },
                Response.ErrorListener { error ->
                    btnAgregar?.snackError("error: ${error.message}")
                    Log.e("errorAPI", "error: $error")
                    swipeR.isRefreshing = false
                }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $tokenKey"
                return headers
            }
        }
        queue.add(sr)
    }

    fun deleteCliente(idCliente: Int) {
        val queue = Volley.newRequestQueue(applicationContext)
        val url: String = Utils.urlBase+"/api/cliente/"+idCliente

        val sr = object :
            StringRequest(
                Method.DELETE, url,
                Response.Listener { response ->
                    usuarios.clear()
                    val strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    Log.e("bienAPI", jsonObj.getInt("code").toString())
                    if (jsonObj.getInt("code") == 200) {
                       btnBorrar.snackExito("Cliente eliminado con éxito")
                    } else {
                        btnBorrar?.snackAlerta("Ups")
                    }
                    swipeR.isRefreshing = false
                    adapter.notifyDataSetChanged()
                },
                Response.ErrorListener { error ->
                    btnAgregar?.snackError("error: ${error.message}")
                    Log.e("errorAPI", "error: $error")
                    swipeR.isRefreshing = false
                }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $tokenKey"
                return headers
            }
        }
        queue.add(sr)
    }
    fun getListadoUsuarios(token: String){
        //Log.e("tokenAPI",token)
        val queue = Volley.newRequestQueue(applicationContext)
        val url: String = Utils.urlBase+"/api/clientes"

        val sr = object :
            StringRequest(
                Method.GET, url,
                Response.Listener { response ->
                    usuarios.clear()
                    var strResp = response.toString()
                    val jsonObj: JSONObject = JSONObject(strResp)
                    Log.e("bienAPI", jsonObj.getInt("code").toString())
                    if (jsonObj.getInt("code") == 200) {
                        val data: JSONArray = jsonObj.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val jsonInner: JSONObject = data.getJSONObject(i)
                            val clienteId = jsonInner.getInt("clienteId")
                            val nombreCompleto = jsonInner.getString("nombreCompleto")
                            val correoElectronico = jsonInner.getString("correoElectronico")
                            val edad = jsonInner.getInt("edad")
                            val limiteCredito = jsonInner.getDouble("limiteCredito")
                            val estatusClienteId = jsonInner.getInt("estatusClienteId")
                            val estatusCliente = jsonInner.getString("estatusCliente")
                            usuarios.add(
                                ModeloListadoUsuarios(
                                    clienteId,
                                    nombreCompleto,
                                    correoElectronico,
                                    edad,
                                    limiteCredito,
                                    estatusClienteId,
                                    estatusCliente
                                )
                            )
                        }

                    } else {
                        btnAgregar?.snackAlerta("U")
                    }
                    swipeR.isRefreshing = false
                    adapter.notifyDataSetChanged()

                },
                Response.ErrorListener { error ->
                    btnAgregar?.snackError("error: ${error.message}")
                    Log.e("errorAPI", "error: $error")
                    swipeR.isRefreshing = false
                }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }

        }
        queue.add(sr)
    }


    fun getDatosListDumb(): MutableList<ModeloListadoUsuarios>{
       /*
        usuarios.add(ModeloListadoUsuarios(8, "Carlodds", "chazzam@gmaila.com",28,23000,1,"ACTIVO"))*/
        return usuarios
    }
}