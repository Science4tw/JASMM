//package jasmm.application;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToOne;
//
//@Entity(name = "tblorderitem")
//public class OrderItem {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Integer oderitemid;
//
//	private Integer articleid;
//	private Integer amount;
//	
//	@OneToOne
//	private Article article;
//
//	public Integer getOderitemid() {
//		return oderitemid;
//	}
//
//	public void setOderitemid(Integer oderitemid) {
//		this.oderitemid = oderitemid;
//	}
//
//	public Integer getArticleid() {
//		return articleid;
//	}
//
//	public void setArticleid(Integer articleid) {
//		this.articleid = articleid;
//	}
//
//	public Integer getAmount() {
//		return amount;
//	}
//
//	public void setAmount(Integer amount) {
//		this.amount = amount;
//	}
//
//	public Article getArticle() {
//		return article;
//	}
//
//	public void setArticle(Article article) {
//		this.article = article;
//	}
//	
//	
//	
//
//}
