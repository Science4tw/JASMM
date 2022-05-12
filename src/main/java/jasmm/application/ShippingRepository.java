package jasmm.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/*
 * Repräsentiert die Artikel Repo
 * @author Matthias & Julia
 */
@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Integer> {
	
	
//	public Shipping findByDistance(Float distance);
	
//	@Query("SELECT pa FROM PromoAmtRange pa WHERE :intValue BETWEEN pa.low AND pa.high")
//	List<PromoAmtRange> findByThreshold(@Param("intValue") Integer intValue,Pageable pageable);
	
	

}
