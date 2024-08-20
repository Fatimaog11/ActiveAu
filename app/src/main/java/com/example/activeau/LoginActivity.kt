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
import java.sql.ResultSet
import java.sql.Statement

class LoginActivity : AppCompatActivity() {

    // Declaración de variables
    private lateinit var usuario: EditText
    private lateinit var contraseña: EditText
    private lateinit var btningresar: Button
    private lateinit var lblRegistrar: TextView
    private var con: Connection? = null

    init {
        // Inicialización de la conexión en el constructor
        val instanceConnection = ConnectionBD()
        con = instanceConnection.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        // Inicialización de vistas
        usuario = findViewById(R.id.txtusuario)
        contraseña = findViewById(R.id.txtcontrasena)
        btningresar = findViewById(R.id.btningresar)
        lblRegistrar = findViewById(R.id.txtclave)

        btningresar.setOnClickListener {
            Login().execute()
        }

        lblRegistrar.setOnClickListener {
            val reg = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(reg)
        }
    }

    inner class Login : AsyncTask<String, String, String>() {
        private var z: String? = null
        private var exito = false

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): String? {
            if (con == null) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Verifique su conexión", Toast.LENGTH_SHORT).show()
                }
                z = "Sin conexión"
            } else {
                try {
                    val sql = "SELECT * FROM USUARIO WHERE usuario = '${usuario.text}' AND clave ='${contraseña.text}'"
                    val stm: Statement = con!!.createStatement()
                    val rs: ResultSet = stm.executeQuery(sql)

                    if (rs.next()) {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Acceso Exitoso", Toast.LENGTH_SHORT).show()
                            val menu = Intent(applicationContext, MainActivity::class.java)
                            startActivity(menu)
                        }

                        usuario.setText("")
                        contraseña.setText("")
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Error en el usuario o contraseña", Toast.LENGTH_SHORT).show()
                        }
                        usuario.setText("")
                        contraseña.setText("")
                    }

                } catch (e: Exception) {
                    exito = false
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
