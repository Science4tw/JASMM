package jasmm.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JasmmApplication {
	
	@Autowired
	private CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(JasmmApplication.class, args);
		
		System.out.println("Konsolen Test");
	}
	
	@PostConstruct
	public void createTestData() {
		
		Customer c = new Customer();
		c.setUsername("test.client@mail.com");
		c.setPassword("TestPwd");
		c.setName("Test Client");		
		customerRepository.save(c);
	}

}
