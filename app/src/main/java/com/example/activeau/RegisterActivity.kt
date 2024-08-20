package com.example.activeau

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.activeau.Connection.ConnectionBD
import java.sql.Connection
import java.sql.PreparedStatement

class RegisterActivity : AppCompatActivity() {

    // Declaración de variables
    private lateinit var nombre: EditText
    private lateinit var correo: EditText
    private lateinit var usuario: EditText
    private lateinit var contraseña: EditText
    private lateinit var btnregistrar: Button
    private lateinit var lblLogin: TextView
    private var con: Connection? = null

    init {
        // Inicialización de la conexión en el constructor
        val instanceConnection = ConnectionBD()
        con = instanceConnection.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicialización de vistas
        nombre = findViewById(R.id.txtnombre)
        correo = findViewById(R.id.txtcorreo)
        usuario = findViewById(R.id.txtusuario)
        contraseña = findViewById(R.id.txtcontrasena)
        btnregistrar = findViewById(R.id.btnregistrar)
        lblLogin = findViewById(R.id.txtlogin)

        btnregistrar.setOnClickListener {
            RegisterUser().execute()
        }

        lblLogin.setOnClickListener {
            val login = Intent(applicationContext, LoginActivity::class.java)
            startActivity(login)
        }
    }

    inner class RegisterUser : AsyncTask<String, String, String>() {
        private var z: String? = null
        private var isSuccess = false

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String? {
            if (con == null) {
                runOnUiThread {
                    Toast.makeText(this@RegisterActivity, "Verifique su conexión", Toast.LENGTH_SHORT).show()
                }
                z = "Sin conexión"
            } else {
                try {
                    val sql = "INSERT INTO USUARIO (usuario, clave, nombre, email) VALUES (?, ?, ?, ?)"
                    val preparedStatement: PreparedStatement = con!!.prepareStatement(sql)
                    preparedStatement.setString(1, usuario.text.toString())
                    preparedStatement.setString(2, contraseña.text.toString())
                    preparedStatement.setString(3, nombre.text.toString())
                    preparedStatement.setString(4, correo.text.toString())

                    val rowsInserted = preparedStatement.executeUpdate()
                    if (rowsInserted > 0) {
                        isSuccess = true
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            val mainActivity = Intent(applicationContext, MainActivity::class.java)
                            startActivity(mainActivity)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@RegisterActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    isSuccess = false
                    Log.e("Error de conexión: ", e.message ?: "Sin mensaje de error")
                }
            }
            return z
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
        }
    }
}
