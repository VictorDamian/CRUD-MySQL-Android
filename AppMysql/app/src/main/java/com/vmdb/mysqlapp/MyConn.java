package com.vmdb.mysqlapp;
import android.os.StrictMode;
import android.util.Log;
import java.sql.*;

public class MyConn {
    private Statement st;
    private String url="jdbc:mysql://yourIP:3306/yourDB";
    private String root ="yourUser";
    private String pass ="";

    public Connection Conectar()
    {
        final String drive = "com.mysql.jdbc.Driver";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        try {
            Class.forName(drive);
            conn = DriverManager.getConnection(url, root, pass);
        } catch (SQLException se) {
            Log.e("ERROR1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR2", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR3", e.getMessage());
        }
        return conn;
    }

    public ResultSet consultarDatos(String cadena){
        ResultSet rs = null;
        try {
            Connection ConnexionMSQ = Conectar();
            st = ConnexionMSQ.createStatement();
            rs = st.executeQuery(cadena);
            return rs;
        }catch (SQLException e){
            return null;
        }
    }
    public String ejecuarSQL(String cadena){
        Conectar();
        try{
            int r=st.executeUpdate(cadena);
            return "La operacion se realizo, se afectaron " + r + " registros.";
        }catch(SQLException e){
            return null;
        }
    }
}
