package jasmm.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderItem {
	
	@Id
	@GeneratedValue
	private Integer oderitemid;
	
	private Integer articleid;
	private Integer amount;
	
	private Integer oderid;
	
	public Integer getOderitemid() {
		return oderitemid;
	}
	public void setOderitemid(Integer oderitemid) {
		this.oderitemid = oderitemid;
	}
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
	public Integer getOderid() {
		return oderid;
	}
	public void setOderid(Integer oderid) {
		this.oderid = oderid;
	}
	
	
	

}
