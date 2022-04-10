package jasmm.application;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "tblorder")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int orderid;

	private Integer customerid;
	private Date orderdate;

	@OneToOne
	private Article article1;
	private Integer amountarticle1;

	@OneToOne
	private Article article2;
	private Integer amountarticle2;

	@OneToOne
	private Article article3;
	private Integer amountarticle3;

	@OneToOne
	private Article article4;
	private Integer amountarticle4;

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public Integer getCustomerid() {
		return customerid;
	}

	public void setCustomerid(Integer customerid) {
		this.customerid = customerid;
	}

	public Date getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}

	public Article getArticle1() {
		return article1;
	}

	public void setArticle1(Article article1) {
		this.article1 = article1;
	}

	public Integer getAmountarticle1() {
		return amountarticle1;
	}

	public void setAmountarticle1(Integer amountarticle1) {
		this.amountarticle1 = amountarticle1;
	}

	public Article getArticle2() {
		return article2;
	}

	public void setArticle2(Article article2) {
		this.article2 = article2;
	}

	public Integer getAmountarticle2() {
		return amountarticle2;
	}

	public void setAmountarticle2(Integer amountarticle2) {
		this.amountarticle2 = amountarticle2;
	}

	public Article getArticle3() {
		return article3;
	}

	public void setArticle3(Article article3) {
		this.article3 = article3;
	}

	public Integer getAmountarticle3() {
		return amountarticle3;
	}

	public void setAmountarticle3(Integer amountarticle3) {
		this.amountarticle3 = amountarticle3;
	}

	public Article getArticle4() {
		return article4;
	}

	public void setArticle4(Article article4) {
		this.article4 = article4;
	}

	public Integer getAmountarticle4() {
		return amountarticle4;
	}

	public void setAmountarticle4(Integer amountarticle4) {
		this.amountarticle4 = amountarticle4;
	}

}
