package jasmm.application.service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jasmm.application.ServiceLocator;
import jasmm.application.persistence.City;
import jasmm.application.persistence.CityRepository;
import jasmm.application.persistence.Customer;
import jasmm.application.persistence.CustomerRepository;

/* Service für das Customer Handling
 * Author: Michèle
 */

@RestController
public class ServiceCustomerManagement {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CityRepository cityRepository;

	// Logger
	Logger logger = ServiceLocator.getServiceLocator().getLogger();

	// Für Passwortverschlüsselung (Hash)
	private static final int iterations = 127;
	private final byte[] salt = new byte[64];

	
	// Registrierung: neuen Kunden erstellen & in DB speichern
	@PostMapping(path = "/createCustomer", produces = "application/json")
	public int createCustomer(@RequestBody MessageNewCustomer m) {
		boolean userNameCheck = customerRepository.existsByUsername(m.getUsername());
		boolean plzCheck = cityRepository.existsByZipcode(m.getZipCode());

		if (userNameCheck == false) { // Username existiert noch nicht in DB
			if (plzCheck == true) { // PLZ in DB vorhanden
				City cityOfCustomer = cityRepository.findByZipcode(m.getZipCode());
				Float distanceOfCustomer = cityOfCustomer.getDistance();
				Customer c = new Customer();
				c.setUsername(m.getUsername());
				String hashedPassword = hash(m.getPassword()); // Passwortverschlüsselung
				c.setPassword(hashedPassword);
				c.setFirstName(m.getFirstName());
				c.setLastName(m.getLastName());
				c.setStreet(m.getStreet());
				c.setStreetNr(m.getStreetNr());
				c.setZipCode(m.getZipCode());
				c.setCity(m.getCity());
				c.setDistance(distanceOfCustomer);
				c = customerRepository.save(c);
				logger.info("Registrierung erfolgreich. Neuer Kunde: " + c.getFirstName() + " " + c.getLastName()
						+ " (Kunden-ID " + c.getCustomerid() + "); Lieferdistanz: " + c.getDistance() + "km");
				return c.getCustomerid();
			} else { // PLZ in DB nicht vorhanden
				logger.info("Registrierung fehlgeschlagen. Ungültige Postleitzahl " + m.getZipCode());
				return -2;
			}
		} else { // Username existiert bereits in der DB
			logger.info("Registrierung fehlgeschlagen. Benutzername " + m.getUsername() + " bereits vergeben");
			return 0;
		}
	}

	// Passwortverschlüsselung (Hash)
	// Quelle: https://nullbeans.com/hashing-passwords-in-spring-applications/
	private String hash(String password) {
		try {
			char[] chars = password.toCharArray(); // Die einzelnen Zeichen des Passworts wird in ein Zeichen-Array
													// gespeichert
			// Hashing des Passworts mit 127 Iterationen des Hash-Algorithmus und Key-Grösse
			// von 512 Bytes
			PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 512);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512"); // Hash-Länge von 512 Bits;
																							// PBKDF2WithHmacSHA1 wäre
																							// eine Hash-Länge von 160
																							// Bits
			byte[] hash = skf.generateSecret(spec).getEncoded();
			// Das Hash-Byte-Array in einen String konvertieren mittels Base64, damit es in
			// der DB gespeichert werden kann
			String hashAsString = Base64.getMimeEncoder().encodeToString(hash);
			return hashAsString;
		} catch (Exception e) {
			logger.warning("Hashing des Passworts nicht möglich");
			return null;
		}
	}

	
	// Login: Validierung Benutzername & Passwort
	@PostMapping(path = "/login", produces = "application/json")
	public int validateLogin(@RequestBody MessageLogin m) {
		boolean userNameCheck = customerRepository.existsByUsername(m.getUsername());

		if (userNameCheck == true) { //Benutzername existiert in der DB
			Customer c = customerRepository.findByUsername(m.getUsername());
			
			String hashedPasswordFromDB = c.getPassword(); //das verschlüsselte Passwort von der DB holen
			String hashedPasswordFromLogin = hash(m.getPassword()); //das vom User eingegebene Passwort verschlüsseln

			if (hashedPasswordFromDB.equals(hashedPasswordFromLogin)) {
				logger.info("Login erfolgreich. Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID "
						+ c.getCustomerid() + ")");
				return c.getCustomerid();
			} else {
				logger.info("Login fehlgeschlagen. Falsches Passwort. Kunde: " + c.getFirstName() + " "
						+ c.getLastName() + " (Kunden-ID " + c.getCustomerid() + ")");
				return 0;
			}
		} else {
			logger.info("Login fehlgeschlagen. Unbekannter Benutzername " + m.getUsername());
			return 0;
		}
	}

	
	// Kundenkonto: Daten eines Kunden abfragen
	@GetMapping(path = "/getCustomer/{customerid}", produces = "application/json")
	public Customer getCustomer(@PathVariable int customerid) {
		return customerRepository.findById(customerid).get(); //komplettes Customer-Objekt wird zurückgegeben

	}

	
	// Kundenkonto: Kundendaten ändern bzw. überschreiben
	@PutMapping(path = "/updateCustomer/{customerid}", produces = "application/json")
	public boolean updateCustomer(@PathVariable int customerid, @RequestBody MessageUpdateCustomer m) {
		Customer c = customerRepository.getById(customerid);
		if (c == null)
			return false;
		boolean plzCheck = cityRepository.existsByZipcode(m.getZipCode());
		if (plzCheck == true) { //Eingabe einer gültigen PLZ
			City cityOfCustomer = cityRepository.findByZipcode(m.getZipCode());
			Float distanceOfCustomer = cityOfCustomer.getDistance();
			c.setFirstName(m.getFirstName());
			c.setLastName(m.getLastName());
			c.setStreet(m.getStreet());
			c.setStreetNr(m.getStreetNr());
			c.setZipCode(m.getZipCode());
			c.setCity(m.getCity());
			c.setDistance(distanceOfCustomer);
			c = customerRepository.save(c);
			logger.info("Kundendaten erfolgreich geändert. Kunde: " + c.getFirstName() + " " + c.getLastName()
					+ " (Kunden-ID " + c.getCustomerid() + "). Lieferdistanz: " + c.getDistance() + "km");
			return true;
		} else { //bei Eingabe einer ungültigen PLZ
			logger.info("Kundendatenänderung fehlgeschlagen. Ungültige Postleitzahl " + m.getZipCode() + ". Kunde: "
					+ c.getFirstName() + " " + c.getLastName() + " (Kunden-ID " + c.getCustomerid() + ")");
			return false;
		}
	}


	// Kundenkonto: Passwort ändern
	@PutMapping(path = "/changePassword/{customerid}", produces = "application/json")
	public boolean changePassword(@PathVariable int customerid, @RequestBody MessageChangePassword m) {
		System.out.println("Kunden-ID bei PW-Änderung: " + customerid);
		Customer c = customerRepository.getById(customerid);
		if (c == null)
			return false;
		String hashedOldPasswordFromDB = c.getPassword(); //bestehendes verschlüsseltes PW aus DB holen
		String hashedOldPasswordFromInput = hash(m.getPassword()); //bestehendes PW aus der Benutzereingabe verschlüsseln
		if (hashedOldPasswordFromDB.equals(hashedOldPasswordFromInput)) { //Eingabe bestehendes Passwort = korrekt
			String hashedNewPassword = hash(m.getPasswordToUpdate()); //das neue PW aus der Benutzereingabe verschlüsseln
			c.setPassword(hashedNewPassword);
			c = customerRepository.save(c);
			logger.info("Passwortänderung erfolgreich. Kunde: " + c.getFirstName() + " " + c.getLastName()
					+ " (Kunden-ID: " + c.getCustomerid() + ")");
			return true;
		} else { //Eingabe bestehendes PW = falsch
			logger.info("Passwortänderung fehlgeschlagen. Falsche Eingabe beim bestehenden Passwort. Kunde: "
					+ c.getFirstName() + " " + c.getLastName() + " (Kunden-ID: " + c.getCustomerid() + ")");
			return false;
		}

	}

	
	// Logout
	@PostMapping(path = "/logout", produces = "application/json")
	public boolean logoutCustomer(@RequestBody MessageLogout m) {
		Customer c = customerRepository.getById(m.getCustomerid());
		if (c == null) {
			logger.warning("Logout. Kunde mit der ID " + m.getCustomerid() + "  in der Datenbank nicht gefunden.");
			return false;
		} else {
			logger.info("Logout erfolgreich. Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID "
					+ c.getCustomerid() + ")");
			return true;
		}
	}

}
