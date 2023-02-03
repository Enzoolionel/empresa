package empresa;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion{

  private static Connection conexion;
  private static Conexion instancia;

  private static final String url = "jdbc:mysql://localhost/db_trabajo";
  private static final String userName = "root";
  private static final String password = "";

  public Connection conectar(){
    try{
      Class.forName("com.mysql.cj.jdbc.Driver");
      conexion = DriverManager.getConnection(url,userName,password);
      return conexion;
    }catch(Exception e){
      JOptionPane.showMessageDialog(null,"error: " + e);
    }
    return conexion;
  }
  public void cerrarConexion() throws SQLException {
    try {
      conexion.close();
    }catch (Exception e){
      JOptionPane.showMessageDialog(null,"error: " + e);
      conexion.close();
    }finally {
      conexion.close();
    }
  }

  private Conexion(){}

  public static Conexion getInstance(){
    if (instancia == null){
      instancia = new Conexion();
    }
    return instancia;
  }

}