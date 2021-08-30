package com.vmdb.mysqlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText txtNombre;
    EditText txtTelefono;
    GridView tablaDatos;
    MyConn objBD;
    Button btnUpdate;
    boolean actualiza;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objBD=new MyConn();
        btnUpdate = findViewById(R.id.button1);
        txtNombre = findViewById(R.id.editText1);
        txtTelefono = findViewById(R.id.editText2);

        tablaDatos = findViewById(R.id.gridView1);
        tablaDatos.setOnItemClickListener(this);

        try {
            llenaDatos();
        }catch (SQLException e) {

        }
    }

    public void llenaDatos() throws SQLException{
        tablaDatos.setAdapter(null);
        ResultSet c=objBD.consultarDatos("SELECT * FROM Agenda ORDER BY Nombre");

        int cont;
        c.last();
        cont=c.getRow();

        if (c.first()) {
            String datos[]=new String[cont*2];
            for(int i=0;i<cont;i++){
                datos[i*2]=c.getString("nombre");
                System.out.println(datos[i*2]);
                datos[i*2+1]=c.getString("telefon");
                System.out.println(datos[i*2+1]);
                c.next();
            }
            ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datos);
            tablaDatos.setAdapter(adaptador);
        }
    }

    public void proceso(View v) throws SQLException{
        if (v.getId() == R.id.button1) {
            if (!actualiza){
                ResultSet rs=objBD.consultarDatos("SELECT * FROM Agenda WHERE Nombre='"
                        + txtNombre.getText().toString() + "'");
                if(!rs.first()){
                    String cadena = "INSERT INTO Agenda (Nombre,Telefon) "
                            + "VALUES ('" + txtNombre.getText().toString() + "', '"
                            + txtTelefono.getText().toString() + "')";
                    objBD.ejecuarSQL(cadena);
                }
            }else{
                String cadena = "UPDATE Agenda SET Telefon='" + txtTelefono.getText().toString()
                        + "' WHERE Nombre='" + txtNombre.getText().toString() + "'";
                objBD.ejecuarSQL(cadena);
            }
        }
        if (v.getId() == R.id.button2) {
            objBD.ejecuarSQL("DELETE FROM Agenda WHERE Nombre='" + txtNombre.getText().toString() + "'");
        }

        llenaDatos();
    }

    private void LimpiarCam(){
        txtNombre.setText("");
        txtTelefono.setText("");
        txtNombre.setEnabled(true);
        btnUpdate.setText("Agregar");
        txtNombre.requestFocus();
        actualiza=false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView,
                            View view, int i, long l) {

        int renglon=i/2;

        String dato1=adapterView.getItemAtPosition(renglon*2).toString();
        String dato2=adapterView.getItemAtPosition(renglon*2+1).toString();
        txtNombre.setText(dato1);
        txtTelefono.setText(dato2);
        actualiza=true;
        txtNombre.setEnabled(false);

        btnUpdate.setText("ACTUALIZAR");
    }

    public void Reset(View view) {
        LimpiarCam();
    }
}