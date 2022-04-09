package jasmm.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(path = "/api")
public class ServiceCustomerManagement {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@PostMapping(path = "/demo/createCustomer", produces = "application/json")
	public int createCustomer(@RequestBody MessageNewCustomer m) {
		System.out.println("Bis hierhin geschafft...!");
		Customer c = new Customer();
		c.setUsername(m.getUsername());
		c.setPassword(m.getPassword());
		c = customerRepository.save(c);
		System.out.println("Username: " + c.getUsername());
		return c.getId();
	}
	
	
	/*@PutMapping(path = "/api/customer/createCustomer", produces = "application/json")
	public Customer createCustomer(@RequestBody Customer customer) {
		
		Customer c = new Customer();
		
		// Zuweisen der Attribute
		c.setEmail(customer.getEmail());
		c.setName(customer.getName());
		
		// Speichern in Repo
		customerRepository.save(c);
		System.out.println("Konsolen Test Create Customer");
		return c;
		
	}*/

	
	
}
