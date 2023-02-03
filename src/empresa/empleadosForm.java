package empresa;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class empleadosForm {
  private JTextField txtNombre;
  private JSpinner spinnerEdad;
  private JComboBox comboNacionalidad;
  private JSlider sliderHsTrabajadas;
  private JRadioButton btnMasculino;
  private JRadioButton btnFemenino;
  private JTextArea areaInfo;
  private JButton btnEnviar;
  private JButton btnConsultar;
  private JButton btnLimpiar;
  private JPanel panel1;
  private JButton btnEliminar;
  private JButton btnModificar;
  private JTextField txtBuscar;
  private JButton btnBuscar;

  //SE CREA LA INSTANCIA DE LA CLASS CONEXION QUE SE LLAMA CONECTAR
  Conexion conectar = Conexion.getInstance();

  //CREO LA FUNCION SELECCIONARSERXO PARA DETERMINAR EL SEXO SELECCIONADO
  // Y PREGUNTO SI EL BOTONMASCULINO ESTA SELECCIONADO DEVOLVEMOS EL VALOR DEL MISMO
  // SINO DEVOLVEMOS EL VALOR DEL OTRO BOTON. (MASCULINO O FEMENINO).
  public String seleccionSexo(){
    if (btnMasculino.isSelected()){
      return "Masculino";
    }
    return "Femenino";
  }
  public empleadosForm() {

    sliderHsTrabajadas.setMinimum(0);
    sliderHsTrabajadas.setMaximum(100);
    sliderHsTrabajadas.setValue(0);
    sliderHsTrabajadas.setPaintTicks(true);
    sliderHsTrabajadas.setPaintLabels(true);
    sliderHsTrabajadas.setMajorTickSpacing(25);
    sliderHsTrabajadas.setMinorTickSpacing(5);

    btnEnviar.addActionListener((e)->{
      try {

        //CREAMOS LA CONEXION A LA BASE DE DATOS LA VARIABLE DE TIPO CONNECTION SE LLAMA CONEXION
        //Y LE PASAMOS EL METODO CONECTAR A LA VARIABLE CONECTAR DE LA GETINSTANCIA.
        Connection conexion = conectar.conectar();

        //CREAMOS UNA VARIABLE QUE LA LLAMAMOS INSERTAR DE TIPO PREPAREDSTATEMENT Y LA IGUALAMOS AL NOMBRE DE LA VARIABLE DE ARRIBA
        //EN ESTE CASO SE LLAMA CONEXION Y LE PASAMOS EL PETODO PREPARESTATEMENT Y ENTRE PARENTECIS LE PASAMOS LA SENTENCIA SQL
        // EN SETE CASO ESTAMOS INSERTANDO EN EMPLEADOS LOS VALORES.
        PreparedStatement insertar = conexion.prepareStatement("insert into empleados values(?,?,?,?,?,?)");


        //A LA VARIABLE DE TIPO PREPAREDSTATEMENT LLAMADA INGRESAR LE PASAMOS LOS DATOS QUE VAMOS A INGRESAR EN LA BD
        insertar.setString(1,"0");
        insertar.setString(2,txtNombre.getText().trim());
        insertar.setString(3, spinnerEdad.getValue().toString());
        insertar.setString(4, comboNacionalidad.getSelectedItem().toString());
        insertar.setInt(5, sliderHsTrabajadas.getValue());
        insertar.setString(6,seleccionSexo());

        //CUANTO TERMINAMOS DE PASAR LOS DATOS ACTUALIZAMOS LA LISTA LLAMANDO A LA MISMA VARIABLE
        //Y PASANDO EL METODO EXECUTEUPDATE
        insertar.executeUpdate();

        JOptionPane.showMessageDialog(null,"datos registrados");
        conectar.cerrarConexion();
        //ACA PASAMOS EL MENSAJE DE DATOS REGISTRADOS Y CERRAMOS LA CONEXION.

      }catch (Exception ex){
        JOptionPane.showMessageDialog(null,"error: " + ex);
      }
    });

    //BTNlIMPIAR ES PARA LIMPIAR LOS CAMPOS DEL FORM
    btnLimpiar.addActionListener(e->{
      txtNombre.setText("");
      spinnerEdad.setValue(0);
      comboNacionalidad.setSelectedIndex(0);
      sliderHsTrabajadas.setValue(0);
      btnMasculino.setSelected(false);
      btnFemenino.setSelected(false);

      JOptionPane.showMessageDialog(null,"Se limpiaron los campos");
    });


    btnConsultar.addActionListener((e)->{
      areaInfo.setText(" ");

      try{
        //PARA CONSULTAR LOS DATOS ES MASOMENOS LO MISMO QUE PARA INSERTAR TRAEMOS
        //LA CONEXION Y CON PREPAREDSTATEMENT LE PASAMOS A LA CONEXION LA SENTENCIA SQL QUE
        //SELECCIONA A TODOS DE EMPLELADOS

        Connection conexion = conectar.conectar();
        PreparedStatement seleccionar = conexion.prepareStatement("SELECT * FROM empleados");

        //CON RESULTSET SE TRAE LOS DATOS DE LA BD Y ESTOS SE PUEDEN MANIPULAR
        ResultSet consulta = seleccionar.executeQuery();

        //CON EL WHILE ITERAMOS LA VARIABLE QUE TIENE RESULTSET Y CON EL .NEXT MOVEMOS EL CURSOR A LA SIGUIENTE FILA
        while (consulta.next()){
          areaInfo.append(consulta.getString(1));
          areaInfo.append("      ");
          areaInfo.append(consulta.getString(2));
          areaInfo.append("      ");
          areaInfo.append(consulta.getString(3));
          areaInfo.append("      ");
          areaInfo.append(consulta.getString(4));
          areaInfo.append("      ");
          areaInfo.append(consulta.getString(5));
          areaInfo.append("      ");
          areaInfo.append(consulta.getString(6));
          areaInfo.append("\n");
        }


          //CERRAMOS LA CONEXION COMO SIEMPRE Y PASAMOS LOS MENSAJES Y LOS ERRORES
          conectar.cerrarConexion();
        JOptionPane.showMessageDialog(null,"estos son los datos");
      }catch (Exception ex){
        JOptionPane.showMessageDialog(null,"error: " + ex);
      }
    });

    //BTN ELIMINAR TAMBIEN ES PARECIDO A LOS DE ARRIBA
    btnEliminar.addActionListener((e)->{
      try {
        //CONECTAMOS A BASE DE DATOS
        Connection conexion = conectar.conectar();

        //PASAMOS SENTENCIA SQL BORRAR DE EMPLEADOS POR ID EN ESTE CASO
        PreparedStatement eliminar = conexion.prepareStatement("delete from empleados WHERE id = ?");

        // A ELIMINAR LE PASAMOS LOS DATOS QUE ESTA EN EL CAMPO BUSCAR
        eliminar.setString(1,txtBuscar.getText().trim());

        JOptionPane.showMessageDialog(null,"Registro eliminado!");

        //ACTUALIZAMOS LA LISTA
        eliminar.executeUpdate();

        //CERRAMOS CONEXION
        conectar.cerrarConexion();
      }catch (Exception ex){
        JOptionPane.showMessageDialog(null,"error: " + ex);
      }

    });

    //BTNMODIFICAR
    btnModificar.addActionListener((e)->{
      try {

        //ESTABLECEMOS CONEXION
        Connection conexion = conectar.conectar();

        //CREAMOS UNA VARIABLE DE TIPOS STRING LLAMADA ID PARA DESPUES PASARLA A LA SENTENCIA SQL
        String ID = txtBuscar.getText().trim();

        //CREAMOS LA VARIABLE PARA PASAR LA SENTENCIA SQL Y LE DECIMOS QUE TRAIGA EMPLEADOS Y CON SET ESTABLECEMOS LOS VALORES A TRAER
        PreparedStatement modificar = conexion.prepareStatement("UPDATE empleados SET nombre = ?, edad = ?, pais = ?, horas_trabajadas = ?, sexo = ? WHERE id = " + ID);

        modificar.setString(1,txtNombre.getText().trim());
        modificar.setString(2, spinnerEdad.getValue().toString());
        modificar.setString(3, comboNacionalidad.getSelectedItem().toString());
        modificar.setInt(4, sliderHsTrabajadas.getValue());
        modificar.setString(5,seleccionSexo());
        modificar.executeUpdate();

        JOptionPane.showMessageDialog(null,"Datos modificados.");
        
        //CERRAMOS CONEXION
        conectar.cerrarConexion();
      }catch (Exception ex){
        JOptionPane.showMessageDialog(null,"Error: " + ex);
      }
    });
    btnBuscar.addActionListener((e)->{
      try {
        Connection conexion = conectar.conectar();

        PreparedStatement buscar = conexion.prepareStatement("SELECT * FROM empleados WHERE id = ?");
        buscar.setString(1, txtBuscar.getText().trim());

        ResultSet consulta = buscar.executeQuery();

        while (consulta.next() ){
          txtNombre.setText(consulta.getString("nombre"));
          spinnerEdad.setValue(consulta.getInt("edad"));
          comboNacionalidad.setSelectedItem(consulta.getString("pais"));
        }

        JOptionPane.showMessageDialog(null,"Se encontraron datos");

        conectar.cerrarConexion();
      }catch (Exception ex){
        JOptionPane.showMessageDialog(null,"error: " + ex);
      }
    });
  }


  public static void main(String[] args) {
    JFrame frame = new JFrame("empleadosForm");
    frame.setContentPane(new empleadosForm().panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }


}

