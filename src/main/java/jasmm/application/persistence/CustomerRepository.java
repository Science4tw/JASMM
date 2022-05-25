package jasmm.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	public boolean existsByUsername(String username);
	
	public Customer findByUsername(String username);


}
