package jasmm.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceCustomerManagement {

	@Autowired
	private CustomerRepository customerRepository;

	// Michèle
	@PostMapping(path = "/demo/createCustomer", produces = "application/json")
	public int createCustomer(@RequestBody MessageNewCustomer m) {
		boolean userNameCheck = customerRepository.existsByUsername(m.getUsername());

		if (userNameCheck == false) {
			Customer c = new Customer();
			c.setUsername(m.getUsername());
			c.setPassword(m.getPassword());
			c.setFirstName(m.getFirstName());
			c.setLastName(m.getLastName());
			c.setStreet(m.getStreet());
			c.setStreetNr(m.getStreetNr());
			c.setZipCode(m.getZipCode());
			c.setCity(m.getCity());
			c = customerRepository.save(c);

			return c.getCustomerid();
		} else {
			System.out.println("Username " + m.getUsername() + " existiert schon in der DB.");
			return 0;
		}

	}
	
	//Michèle
	@PostMapping(path = "/demo/login", produces = "application/json")
	public int validateLogin(@RequestBody MessageLogin m) {
		boolean userNameCheck = customerRepository.existsByUsername(m.getUsername());
				
		if (userNameCheck == true) {
			Customer c = customerRepository.findByUsername(m.getUsername());
			String passwordFromDB = c.getPassword();
			if (m.getPassword().equals(passwordFromDB)) {
				System.out.println("Korrektes Passwort");
				return c.getCustomerid();
			} else {
				System.out.println("Falsches Passwort");
				return 0;
			}			
		} else {
			System.out.println("Username " + m.getUsername() + " in DB NICHT gefunden");
			return 0;
		}
		
	}

}
