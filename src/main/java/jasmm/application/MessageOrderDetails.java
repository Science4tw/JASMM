package jasmm.application;

/*
 * Repräsentiert die Message mit den Order Details
 * @author Matthias
 */
public class MessageOrderDetails {

	private int orderid;
	private int customerid;

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

}
