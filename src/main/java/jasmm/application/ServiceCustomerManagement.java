package jasmm.application;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceCustomerManagement {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CityRepository cityRepository;

	// Logger
	Logger logger = ServiceLocator.getServiceLocator().getLogger();

	// Michèle
	// Registrierung: neuen Kunden erstellen & in DB speichern
	@PostMapping(path = "/demo/createCustomer", produces = "application/json")
	public int createCustomer(@RequestBody MessageNewCustomer m) {
		boolean userNameCheck = customerRepository.existsByUsername(m.getUsername());
		boolean plzCheck = cityRepository.existsByZipcode(m.getZipCode());
		
		if (userNameCheck == false) { // Username existiert noch nicht in DB
			if (plzCheck == true) { // PLZ in DB vorhanden
				City cityOfCustomer = cityRepository.findByZipcode(m.getZipCode());
				Float distanceOfCustomer = cityOfCustomer.getDistance();
				Customer c = new Customer();
				c.setUsername(m.getUsername());
				c.setPassword(m.getPassword());
				c.setFirstName(m.getFirstName());
				c.setLastName(m.getLastName());
				c.setStreet(m.getStreet());
				c.setStreetNr(m.getStreetNr());
				c.setZipCode(m.getZipCode());
				c.setCity(m.getCity());
				c.setDistance(distanceOfCustomer);
				c = customerRepository.save(c);
				logger.info("Registrierung erfolgreich. Neuer Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID " + c.getCustomerid() + "); Lieferdistanz: " + c.getDistance() + "km");
				return c.getCustomerid();
			} else { // PLZ in DB nicht vorhanden
				logger.info("Registrierung fehlgeschlagen. Ungültige Postleitzahl " + m.getZipCode());
				return -2;
			}
		} else {
			logger.info("Registrierung fehlgeschlagen. Benutzername " + m.getUsername() + " bereits vergeben");
			return 0;
		}
	}

	// Michèle
	// Login: Validierung Benutzername & Passwort
	@PostMapping(path = "/demo/login", produces = "application/json")
	public int validateLogin(@RequestBody MessageLogin m) {
		boolean userNameCheck = customerRepository.existsByUsername(m.getUsername());

		if (userNameCheck == true) {
			Customer c = customerRepository.findByUsername(m.getUsername());
			String passwordFromDB = c.getPassword();
			if (m.getPassword().equals(passwordFromDB)) {
				logger.info("Login erfolgreich. Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID " + c.getCustomerid() +")");
				return c.getCustomerid();
			} else {
				logger.info("Login fehlgeschlagen. Falsches Passwort. Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID " + c.getCustomerid() + ")");
				return 0;
			}
		} else {
			logger.info("Login fehlgeschlagen. Unbekannter Benutzername " + m.getUsername());
			return 0;
		}

	}

	// Michèle
	// Kundenkonto: Daten eines Kunden abfragen
	@GetMapping(path = "/demo/getCustomer/{customerid}", produces = "application/json")
	public Customer getCustomer(@PathVariable int customerid) {
		return customerRepository.findById(customerid).get();

	}

	// Michèle
	// Kundenkonto: Kundendaten ändern bzw. überschreiben
	@PutMapping(path = "/demo/updateCustomer/{customerid}", produces = "application/json")
	public boolean updateCustomer(@PathVariable int customerid, @RequestBody MessageUpdateCustomer m) {
		Customer c = customerRepository.getById(customerid);
		if (c == null)
			return false;
		boolean plzCheck = cityRepository.existsByZipcode(m.getZipCode());
		if (plzCheck == true) {
			City cityOfCustomer = cityRepository.findByZipcode(m.getZipCode());
			Float distanceOfCustomer = cityOfCustomer.getDistance();
			c.setPassword(m.getPassword());
			c.setFirstName(m.getFirstName());
			c.setLastName(m.getLastName());
			c.setStreet(m.getStreet());
			c.setStreetNr(m.getStreetNr());
			c.setZipCode(m.getZipCode());
			c.setCity(m.getCity());
			c.setDistance(distanceOfCustomer);
			c = customerRepository.save(c);
			logger.info("Kundendaten erfolgreich geändert. Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID " + c.getCustomerid() + "). Lieferdistanz: " + c.getDistance() +"km");
			return true;
		} else {
			logger.info("Kundendatenänderung fehlgeschlagen. Ungültige Postleitzahl " + m.getZipCode() + ". Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID " + c.getCustomerid() + ")");
			return false;
		}
	}

	// Michèle
	// Logout
	@PostMapping(path = "/demo/logout", produces = "application/json")
	public boolean logoutCustomer(@RequestBody MessageLogout m) {
		Customer c = customerRepository.getById(m.getCustomerid());
		if (c == null) {
			logger.warning("Logout. Kunde mit der ID " + m.getCustomerid() + "  in der Datenbank nicht gefunden.");
			return false;
		} else {
			logger.info("Logout erfolgreich. Kunde: " + c.getFirstName() + " " + c.getLastName() + " (Kunden-ID " + c.getCustomerid() + ")" );
			return true;
		}
	}
}
