package jasmm.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>{
	
	public boolean existsByZipcode(Integer zipcode);
	
	public City findByZipcode(Integer zipcode);

}
