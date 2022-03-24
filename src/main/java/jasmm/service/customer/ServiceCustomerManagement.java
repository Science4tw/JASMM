package jasmm.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jasmm.persistence.Customer;
import jasmm.persistence.CustomerRepository;

@Controller
public class ServiceCustomerManagement {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@PutMapping(path = "/api/customer/createCustomer", produces = "application/json")
	public Customer createCustomer(@RequestBody Customer customer) {
		
		Customer c = new Customer();
		
		// Zuweisen der Attribute
		c.setEmail(customer.getEmail());
		c.setName(customer.getName());
		
		// Speichern in Repo
		customerRepository.save(c);
		System.out.println("Konsolen Test Create Customer");
		return c;
		
	}

	
	
}
