package learning.tokioschool.parking;

import java.time.LocalDateTime;


import learning.tokioschool.parking.db.ParkingDb_MySQL;

public class Parking {

	final ParkingDb_MySQL parkingDB;
//	final Map<String, Coche> parking;

	public Parking() {
		parkingDB = new ParkingDb_MySQL();
//		parking = new HashMap<>();
	}

	/**
	 * Metodo que comprueba si existe un coche en el sistema
	 * 
	 * @param matricula
	 * @return
	 */
	public boolean existeCoche(final String matricula) {
		 return getCoche(matricula)!=null ? true : false;
//		return parking.containsKey(matricula);
	}

	/**
	 * Metodo que obtiene un coche del sistema
	 * 
	 * @param matricula
	 * @return
	 */
	public Coche getCoche(final String matricula) {
		return parkingDB.search(matricula);
//		return parking.get(matricula);
	}

	/**
	 * Metodo que añade un coche al sistema
	 * 
	 * @param matricula
	 * @param coche
	 */
	public void putCoche(final String matricula, final Coche coche) {
		parkingDB.insert(matricula, coche);
//		parking.put(matricula, coche);
	}
	
	public int update(final String matricula, final LocalDateTime data) {
		return parkingDB.update(matricula, data);
//		parking.put(matricula, coche);
	}

	/**
	 * Metodo que imprime todos los coches del sistema, tanto los que estan dentro del parking como los que no.
	 */
	public void imprimirCochesSistema() {
		try {
			parkingDB.searchAll().forEach((k, v) -> {System.out.println("Matricula: " + k + " Datos del " + v);
			});
//			parking.forEach((k, v) -> {
//				System.out.println("Matricula: " + k + " Datos del " + v);
//			});
		} catch (Exception ex) {
			System.out.println("Error al imprimir coches en el sistema");
		}
	}

	/**
	 * Metodo que imprime los coches que estan dentro del parking (horaSalida = null)
	 */
	public void imprimirCochesParking() {
		
		try {
			parkingDB.searchAllFilterHoraSalida().forEach((k, v) -> {System.out.println("Matricula: " + k + " Datos del " + v);
			});
//			parkingDB.searchAllFilterHoraSalida().forEach((k, v) -> {
//				if (v.getHoraSalida() == null) {
//					System.out.println("Matricula: " + k + " Datos del " + v);
//				}
//			});
		} catch (Exception ex) {
			System.out.println("Error al imprimir coches en el parking");
		}
	}

	/**
	 * Método que calcula la cantidad a pagar por un coche según el tiempo que ha estado dentro del parking
	 * @param matricula
	 */
	public void cantidadAPagar(final String matricula) {
		if (matricula != null) {
			System.out.println("Cantidad a pagar " + getCoche(matricula).cantidadAPagar());
//			System.out.println("Cantidad a pagar " + parking.get(matricula).cantidadAPagar());
		}
	}
	
	public void close() {
		parkingDB.close();
	}
}
