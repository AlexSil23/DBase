<!DOCTYPE html>
<html lang="es">
<head>
</head>
<body>
    <main>
        <header>
            <h1>JDBC (Java Database Connectivity)</h1>
        </header>
        <section>
            <h2>Descripción</h2>
            <p>JDBC es una API de Java que permite la conexión y manipulación de bases de datos relacionales desde aplicaciones Java.</p>
        </section>
        <section>
            <h2>Funcionalidades</h2>
            <ul>
                <li><strong>Conexión a la base de datos:</strong> Cómo establecer una conexión.</li>
                <li><strong>Consulta de datos:</strong> Cómo recuperar datos con SQL.</li>
                <li><strong>Actualización y modificación:</strong> Cómo insertar, actualizar y eliminar datos.</li>
                <li><strong>Manejo de excepciones:</strong> Cómo gestionar errores y excepciones en JDBC.</li>
            </ul>
        </section>
        <section>
            <h2>Ejemplos</h2>
            <article>
                <h3>Conexión a la base de datos</h3>
                <pre><code>
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
                    
public class ConexionDB {

    public static void main(String[] args) {
    
        String url = "jdbc:mysql://localhost:3306/mi_base";
        String usuario = "root";
        String contrasena = "password"; 
        
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {
                System.out.println("Conexión exitosa");
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
                </code></pre>
            </article>
            <article>
            
                <h3>Consulta de datos</h3>
                <pre><code>
import java.sql.*;

public class ConsultaDatos {

    public static void main(String[] args) {
    
        String url = "jdbc:mysql://localhost:3306/mi_base";
        String usuario = "root";
        String contrasena = "password";
        
            try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena);
                 Statement stmt = conexion.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")) {            
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", Nombre: " + rs.getString("nombre"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
                </code></pre>
            </article>
        </section>
        <section>
            <h2>Requisitos</h2>
            <ul>
                <li>Java JDK instalado (recomendado: Java 8+).</li>
                <li>Conector JDBC para la base de datos correspondiente.</li>
                <li>Dependencias en <code>pom.xml</code> (para proyectos Maven).</li>
            </ul>
        </section>
    </main>
</body>
</html>
