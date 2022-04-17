package jasmm.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/*
 * Repräsentiert die Items einer Bestellungen, falls wir uns noch zeit nehmen für mehr Artikel
 * @author Matthias
 */
@Entity
public class OrderItem {

	@Id
	@GeneratedValue
	private int orderItemId;

	private int articleid;
	private int amount;

	public int getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(int orderItemId) {
		this.orderItemId = orderItemId;
	}

	public int getArticleid() {
		return articleid;
	}

	public void setArticleid(int articleid) {
		this.articleid = articleid;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
