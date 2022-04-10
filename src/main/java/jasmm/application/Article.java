package jasmm.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer articleid;
	private Double spacepersquaremeter;
	
	public Integer getArticleid() {
		return articleid;
	}
	public void setArticleid(Integer articleid) {
		this.articleid = articleid;
	}
	public Double getSpacepersquaremeter() {
		return spacepersquaremeter;
	}
	public void setSpacepersquaremeter(Double spacepersquaremeter) {
		this.spacepersquaremeter = spacepersquaremeter;
	}
	
	
	
}
