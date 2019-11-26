package exa3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Alba
 */
public class Exa3 {

    public static Connection conexion = null;

    public static Connection getConexion() throws SQLException {
        String usuario = "hr";
        String password = "hr";
        String host = "localhost";
        String puerto = "1521";
        String sid = "orcl";
        String ulrjdbc = "jdbc:oracle:thin:" + usuario + "/" + password + "@" + host + ":" + puerto + ":" + sid;

        conexion = DriverManager.getConnection(ulrjdbc);
        return conexion;
    }

    public static void closeConexion() throws SQLException {
        conexion.close();
    }

    public static void main(String[] args) throws SQLException, FileNotFoundException, IOException {
        Exa3.getConexion();
        BufferedReader br = new BufferedReader(new FileReader("analisis.txt"));

        String[] leido;
        String linea, codigoa, tipoU, nomeU, dnia, trataAcidez;
        int acidez, acidezMin, acidezMax, cantidade;

        while ((linea = br.readLine()) != null) {
            leido = linea.split(",");
            codigoa = leido[0];
            tipoU = leido[4];
            acidez = Integer.parseInt(leido[1]);
            cantidade = Integer.parseInt(leido[5]);
            dnia = leido[6];
            //System.out.println(codigoa);
            //System.out.println(tipoU);
            //System.out.println(acidez);
            //System.out.println(cantidade);

            String sql2 = "select * from uvas where tipo='" + tipoU + "'";
            Statement stmt2 = conexion.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql2);
            rs2.next();
            nomeU = rs2.getString("nomeu");
            //System.out.println(nomeU);
            acidezMin = rs2.getInt("acidezmin");
            acidezMax = rs2.getInt("acidezmax");

            if (acidez < acidezMin) {
                trataAcidez = "subir acidez";
                //System.out.println("subir acidez");
            } else if (acidez > acidezMax) {
                trataAcidez = "baixar acidez";
                //System.out.println("baixar acidez");
            } else {
                trataAcidez = "equilibrada";
                //System.out.println("equilibrada");
            }

            cantidade = cantidade * 15;
            //System.out.println(cantidade);

            //solo se puede ejecutar una vez, si se quiere volver a ejecutar hay q lanzar el script de nuevo
            String sql = "insert into xerado values('" + codigoa + "','" + nomeU + "','" + trataAcidez + "'," + cantidade + ")";
            Statement stmt = conexion.createStatement();
            stmt.executeUpdate(sql);
            String sql4 = "update clientes set numerodeanalisis= numerodeanalisis+1 where dni='" + dnia + "'";
            Statement stmt4 = conexion.createStatement();
            stmt4.executeUpdate(sql4);
        }
        br.close();
        Exa3.closeConexion();
    }

}
