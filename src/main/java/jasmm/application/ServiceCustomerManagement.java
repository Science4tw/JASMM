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
		logger.info("PLZ-Check in DB = " + plzCheck);

		if (userNameCheck == false) { // Username existiert noch nicht in DB
			if (plzCheck == true) { // PLZ in DB vorhanden
				City cityOfCustomer = cityRepository.findByZipcode(m.getZipCode());
				Float distanceOfCustomer = cityOfCustomer.getDistance();
				logger.info("Lieferdistanz zum Kunden: " + distanceOfCustomer);
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
				return c.getCustomerid();
			} else { // PLZ in DB nicht vorhanden
				logger.info("PLZ " + m.getZipCode() + " in DB nicht vorhanden");
				return -2;
			}
		} else {
			logger.info("Username " + m.getUsername() + " existiert schon in der DB");
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
				logger.info("Korrektes Passwort. Customerid: " + c.getCustomerid());
				return c.getCustomerid();
			} else {
				logger.info("Falsches Passwort");
				return 0;
			}
		} else {
			logger.info("Username " + m.getUsername() + " in DB NICHT gefunden");
			return 0;
		}

	}

	// Michèle
	// Kundenkonto: Daten eines Kunden abfragen
	@GetMapping(path = "/demo/getCustomer/{customerid}", produces = "application/json")
	public Customer getCustomer(@PathVariable int customerid) {
		logger.info("Customer mit der ID: " + customerid + " abgefragt.");
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
		logger.info("PLZ-Check in DB = " + plzCheck);
		if (plzCheck == true) {
			City cityOfCustomer = cityRepository.findByZipcode(m.getZipCode());
			Float distanceOfCustomer = cityOfCustomer.getDistance();
			logger.info("Lieferdistanz zum Kunden: " + distanceOfCustomer);
			c.setPassword(m.getPassword());
			c.setFirstName(m.getFirstName());
			c.setLastName(m.getLastName());
			c.setStreet(m.getStreet());
			c.setStreetNr(m.getStreetNr());
			c.setZipCode(m.getZipCode());
			c.setCity(m.getCity());
			c.setDistance(distanceOfCustomer);
			c = customerRepository.save(c);
			return true;
		} else {
			logger.info("PLZ " + m.getZipCode() + " in DB nicht vorhanden");
			return false;
		}
	}
}
