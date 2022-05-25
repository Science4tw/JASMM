package jasmm.application.service;

/*
 * Repräsentiert die Message der berechneten Transportkosten (nicht mehr benötigt)
 * @author Matthias
 */
public class MessageCalculatedOrder {
	
	private int orderid;
	private int customerid;
	
	private int anzahlPaletten;
	private Float transportKosten;

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public int getCustomerid() {
		return customerid;
	}

	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}

	public int getAnzahlPaletten() {
		return anzahlPaletten;
	}

	public void setAnzahlPaletten(int anzahlPaletten) {
		this.anzahlPaletten = anzahlPaletten;
	}

	public Float getTransportKosten() {
		return transportKosten;
	}

	public void setTransportKosten(Float transportKosten) {
		this.transportKosten = transportKosten;
	}

	
	

}
