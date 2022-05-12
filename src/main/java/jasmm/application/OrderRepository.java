package jasmm.application;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/*
 * Repräsentiert die Order Repo
 * @author Matthias
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	/** Damit wird von Spring automatisch die Methode implentiert,
	 * da "customerid" ein Attribut von Order ist und davor "findBy" ist */
	public List<Order> findByOrderid(int customerid);
	
	/** Damit wird von Spring automatisch die Methode implentiert,
	 * da "customerid" ein Attribut von Order ist und davor "findBy" ist */
	public List<Order> findByCustomerid(int customerid);

}
