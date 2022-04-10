package jasmm.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageOrderDetails {
	
	private int orderid;
	private int customerid;

	private List<MessageOrderItem> items = new ArrayList<>();

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

	public List<MessageOrderItem> getItems() {
		return items;
	}

	public void setItems(List<MessageOrderItem> items) {
		this.items = items;
	}

	

}
