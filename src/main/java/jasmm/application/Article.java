package jasmm.application;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Repräsentiert die Artikel
 * @author Matthias
 */
@Entity(name = "tblarticle")
public class Article {

	@Id
	private Integer articleid;
	
	private String articlename;
	
	private Integer maxprostellplatz;
	private Double palettenstellplaetze;
	
	public Integer getArticleid() {
		return articleid;
	}
	public void setArticleid(Integer articleid) {
		this.articleid = articleid;
	}
	public String getArticlename() {
		return articlename;
	}
	public void setArticlename(String articlename) {
		this.articlename = articlename;
	}
	public Integer getMaxprostellplatz() {
		return maxprostellplatz;
	}
	public void setMaxprostellplatz(Integer maxprostellplatz) {
		this.maxprostellplatz = maxprostellplatz;
	}
	public Double getPalettenstellplaetze() {
		return palettenstellplaetze;
	}
	public void setPalettenstellplaetze(Double palettenstellplaetze) {
		this.palettenstellplaetze = palettenstellplaetze;
	}
	


	
	


}
