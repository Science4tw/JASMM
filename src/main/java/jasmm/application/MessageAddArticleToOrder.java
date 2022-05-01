package jasmm.application;

/*
 * Repräsentiert die Message zum Artikel der Bestellung hinzuzufügen
 * @author Matthias
 */
public class MessageAddArticleToOrder {
		
	private Integer articleid;
	private Integer amount;
	private Integer orderid;
	private Integer customerid;
	
	public Integer getArticleid() {
		return articleid;
	}
	public void setArticleid(Integer articleid) {
		this.articleid = articleid;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getOrderid() {
		return orderid;
	}
	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
	public Integer getCustomerid() {
		return customerid;
	}
	public void setCustomerid(Integer customerid) {
		this.customerid = customerid;
	}
	
	
	
	

}
