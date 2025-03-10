package learning.tokioschool.parking.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

// TODO: Auto-generated Javadoc
/**
 * The Class DataSPoolDB.
 * @author Alexander Silvera
 */
public class DataSPoolDB {
	
	/** The config. */
	private static HikariConfig config = new HikariConfig();
	
	/** The ds. */
	private static HikariDataSource ds;

	/**
	 * Bloque estático que configura y inicializa la fuente de datos HikariCP para la conexión a la base de datos.
	 * 
	 * Este bloque se ejecuta cuando la clase es cargada en memoria y realiza los siguientes pasos:
	 * 1. Establece la URL de conexión, el nombre de usuario y la contraseña para acceder a la base de datos.
	 * 2. Configura varias propiedades de HikariCP para mejorar el rendimiento de las consultas SQL.
	 * 3. Crea una nueva instancia de HikariDataSource utilizando la configuración proporcionada.
	 * 4. Añade un "Shutdown Hook" para cerrar correctamente el pool de conexiones cuando la aplicación se apague.
	 */
	static {
		config.setJdbcUrl(ManagerDbAbstract.DB_URL);
		config.setUsername(ManagerDbAbstract.USER);
		config.setPassword(ManagerDbAbstract.PASS);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("maximumPoolSize", "10");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (ds != null) {
				ds.close();
			}
		}));
	}

	/**
	 * Obtiene una conexión a la base de datos desde el pool de conexiones administrado por HikariCP.
	 * 
	 * Este método devuelve una conexión disponible del pool de conexiones de HikariCP. Si no hay conexiones disponibles,
	 * el método esperará hasta que una se libere. Si ocurre un error al obtener la conexión, se lanza una excepción 
	 * de tipo `SQLException`.
	 *
	 * @return Una conexión a la base de datos.
	 * @throws SQLException Si ocurre un error al intentar obtener una conexión desde el pool.
	 */ 
	static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
