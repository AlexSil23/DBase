package learning.tokioschool.parking.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.h2.tools.RunScript;
import learning.tokioschool.parking.Coche;

// TODO: Auto-generated Javadoc
/**
 * The Class ParkingDb_MySQL.
 * @author Alexander Silvera
 */
public class ParkingDb_MySQL extends ManagerDbAbstract {

	/** The connection. */
	private Connection connection;

	/**
	 * Constructor de la clase ParkingDb_MySQL.
	 * Este constructor se encarga de inicializar la conexión a la base de datos 
	 * y de crear la tabla en la base de datos, asegurando que el entorno esté 
	 * listo para realizar operaciones con los datos del estacionamiento.
	 * Si ocurre un error al establecer la conexión o crear la tabla, se 
	 * captura la excepción correspondiente y se imprime un mensaje de error.
	 */
	public ParkingDb_MySQL() {
		try {
			this.connection = iniConexion();
			crearTabla();
//			 try (FileReader fileReader = new FileReader("./src/main/resources/coches.sql")) {
//		            RunScript.execute(connection, fileReader);
//		        } catch (FileNotFoundException  e) {
//		            e.printStackTrace();
//		        } catch (IOException e) {
//					e.printStackTrace();
//				}		
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Error en conexión a Base de Datos.");
			e.printStackTrace();
		}
	}

	/**
	 * Actualiza la hora de salida de un vehículo en la base de datos.
	 * 
	 * Este método toma la matrícula de un vehículo y la hora de salida proporcionada,
	 * y actualiza la información del vehículo en la base de datos con la hora de salida 
	 * correspondiente. Si ocurre un error durante la ejecución de la consulta, se captura
	 * la excepción y se imprime un mensaje de error.
	 * 
	 * @param matricula La matrícula del vehículo cuyo registro se desea actualizar.
	 * @param horaSalida La hora de salida del vehículo a registrar.
	 * @return Un entero que indica el número de filas afectadas por la actualización. 
	 *         Si la actualización es exitosa, será mayor que 0.
	 */
	@Override
	public int update(String matricula, LocalDateTime horaSalida) {
		int aux = 0;
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setTimestamp(1, java.sql.Timestamp.valueOf(horaSalida));
			ps.setString(2, matricula);
			aux = ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println(
					"Erros al insertar hora de salida : " + horaSalida + " de vehículo con matrícula " + matricula);
			// e.printStackTrace();
		}
		return aux;
	}

	/**
	 * Inserta un nuevo vehículo en la base de datos.
	 * 
	 * Este método recibe la matrícula de un vehículo y un objeto de tipo `Coche` que contiene
	 * los detalles del vehículo (como marca, modelo, hora de entrada y hora de salida). 
	 * Luego, inserta esta información en la base de datos a través de una consulta SQL `INSERT`. 
	 * Si ocurre un error durante la ejecución de la consulta, se captura la excepción y se imprime
	 * un mensaje de error detallado.
	 * 
	 * @param matricula La matrícula del vehículo que se desea insertar en la base de datos.
	 * @param coche El objeto `Coche` que contiene la información del vehículo a insertar.
	 * @return Un entero que indica el número de filas afectadas por la inserción. 
	 *         Si la inserción es exitosa, será mayor que 0.
	 */
	@Override
	public int insert(String matricula, Coche coche) {
		int aux = 0;
		try (PreparedStatement ps = connection.prepareStatement(INSERT)) {
			ps.setString(1, matricula);
			ps.setString(2, coche.getMarca());
			ps.setString(3, coche.getModelo());
			ps.setTimestamp(4, java.sql.Timestamp.valueOf(coche.getHoraEntrada()));
			if (coche.getHoraSalida() != null) {
				ps.setTimestamp(5, java.sql.Timestamp.valueOf(coche.getHoraSalida()));
			} else {
				ps.setNull(5, java.sql.Types.TIMESTAMP);
			}
			aux = ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error al insertar el vehículo " + coche.toString() + " con matrícula " + matricula);
			// e.printStackTrace();
		}
		return aux;
	}

	/**
	 * Busca un vehículo en la base de datos mediante su matrícula.
	 * 
	 * Este método realiza una consulta SQL utilizando la matrícula proporcionada como parámetro
	 * para buscar un vehículo en la base de datos. Si se encuentra el vehículo, se crea un objeto
	 * `Coche` con los datos obtenidos de la base de datos (marca, modelo, hora de entrada y hora de salida).
	 * Si no se encuentra el vehículo o ocurre un error durante la consulta, el método retorna `null`.
	 * 
	 * @param matricula La matrícula del vehículo a buscar.
	 * @return Un objeto de tipo `Coche` con los datos del vehículo si se encuentra en la base de datos,
	 *         o `null` si no se encuentra o si ocurre un error.
	 */
	@Override
	public Coche search(String matricula) {
		Coche coche = null;
		try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_MATRICULA)) {
			ps.setString(1, matricula);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					coche = new Coche(rs.getString("marca"),
							rs.getString("modelo"),
							rs.getTimestamp("HoraEntrada").toLocalDateTime(),
							rs.getTimestamp("HoraSalida") != null ? rs.getTimestamp("HoraSalida").toLocalDateTime()
									: null);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error en la busqueda de vehículo con matrícula  " + matricula);
			// e.printStackTrace();
		}
		return coche;
	}

	/**
	 * Busca todos los vehículos en el sistema y los devuelve como un mapa.
	 * 
	 * Este método ejecuta una consulta SQL para obtener todos los vehículos almacenados en la base de datos. 
	 * Cada vehículo se almacena en un `Map`, donde la clave es la matrícula del vehículo y el valor es el objeto `Coche`
	 * con los datos correspondientes (marca, modelo, hora de entrada, hora de salida). Si ocurre un error en la consulta,
	 * se captura la excepción y se imprime un mensaje de error.
	 * 
	 * @return Un `Map` con las matrículas de los vehículos como clave y los objetos `Coche` como valor.
	 *         Si no se encuentra ningún vehículo o hay un error, el mapa estará vacío.
	 */
	@Override
	public Map<String, Coche> searchAll() {
		Map<String, Coche> listCoche = new HashMap<>();
		try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					listCoche.put(rs.getString("matricula"),
							new Coche(rs.getString("marca"), rs.getString("modelo"),
									rs.getTimestamp("HoraEntrada").toLocalDateTime(),
									rs.getTimestamp("HoraSalida") != null
											? rs.getTimestamp("HoraSalida").toLocalDateTime()
											: null));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error en la busqueda de vehículo en el sistema");
			// e.printStackTrace();
		}

		return listCoche;
	}

	/**
	 * Busca todos los vehículos que no tienen hora de salida registrada y los devuelve como un mapa.
	 * 
	 * Este método ejecuta una consulta SQL para obtener los vehículos almacenados en la base de datos que no tengan una
	 * hora de salida registrada. Cada vehículo se almacena en un `Map`, donde la clave es la matrícula del vehículo 
	 * y el valor es el objeto `Coche` con los datos correspondientes (marca, modelo, hora de entrada, hora de salida).
	 * Si ocurre un error en la consulta, se captura la excepción y se imprime un mensaje de error.
	 * 
	 * @return Un `Map` con las matrículas de los vehículos como clave y los objetos `Coche` como valor.
	 *         Si no se encuentra ningún vehículo o hay un error, el mapa estará vacío.
	 */
	@Override
	public Map<String, Coche> searchAllFilterHoraSalida() {
		Map<String, Coche> listCoche = new HashMap<>();
		try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_WITHOUT_HORA_SALIDA)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					listCoche.put(rs.getString("matricula"),
							new Coche(rs.getString("marca"), rs.getString("modelo"),
									rs.getTimestamp("HoraEntrada").toLocalDateTime(),
									rs.getTimestamp("HoraSalida") != null
											? rs.getTimestamp("HoraSalida").toLocalDateTime()
											: null));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error en filtrado de vehículo dentro del Parking");
			// e.printStackTrace();
		}
		return listCoche;
	}

	/**
	 * Cierra la conexión a la base de datos si está abierta.
	 * 
	 * Este método verifica si la conexión a la base de datos es diferente de null y si no está cerrada. Si ambas condiciones
	 * se cumplen, se procede a cerrar la conexión. En caso de que se produzca un error al intentar cerrar la conexión, 
	 * se captura la excepción `SQLException` y se imprime la traza de la excepción.
	 */
	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
