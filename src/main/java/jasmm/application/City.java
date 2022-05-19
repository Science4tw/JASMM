package jasmm.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class City {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer cityid;
	
	private Integer zipcode;
	
	private String cityname;
	
	private float distance;
	
	private Integer zone;

	public Integer getZipcode() {
		return zipcode;
	}

	public void setZipCode(Integer zipcode) {
		this.zipcode = zipcode;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityName(String cityname) {
		this.cityname = cityname;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Integer getZone() {
		return zone;
	}

	public void setZone(Integer zone) {
		this.zone = zone;
	}
	
		
	

}
