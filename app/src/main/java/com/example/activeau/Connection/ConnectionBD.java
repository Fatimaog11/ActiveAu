package com.example.activeau.Connection;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBD {
    private final String ip = "10.0.31.27";
    private final String usuario = "sa";
    private final String password = "1234";
    private final String basedatos = "ACTIVEAURA";

    @SuppressLint("NewApi")
    public Connection connect() {
        Connection connection = null;
        String connectionURL;

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");  // Corregido el nombre del paquete
            connectionURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + basedatos + ";user=" + usuario + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionURL);

        } catch (ClassNotFoundException e) {
            Log.e("Error de conexión SQL: ", "Driver no encontrado", e);
        } catch (SQLException e) {
            Log.e("Error de conexión SQL: ", "Error de SQL", e);
        } catch (Exception e) {
            Log.e("Error de conexión SQL: ", "Error inesperado", e);
        }

        return connection;
    }
}
