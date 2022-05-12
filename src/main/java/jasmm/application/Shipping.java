package jasmm.application;

import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * Repräsentiert die Artikel
 * @author Matthias & Julia
 */
@Entity(name = "shipping")
public class Shipping {
	
	@Id
	private int shippingid;
	
	private Float shippingCost;

	public int getShippingid() {
		return shippingid;
	}

	public void setShippingid(int shippingid) {
		this.shippingid = shippingid;
	}

	public Float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(Float shippingCost) {
		this.shippingCost = shippingCost;
	}
	
	
	

}
