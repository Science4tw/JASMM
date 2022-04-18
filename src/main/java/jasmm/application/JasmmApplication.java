package jasmm.application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JasmmApplication {

	// @Autowired
	// private CustomerRepository customerRepository;
	
//	private Article article1;
//	private Article article2;
//	private Article article3;
//	private Article article4;
//		
//	@Autowired
//	private ArticleRepository articleRepository;

	public static void main(String[] args) {
		SpringApplication.run(JasmmApplication.class, args);

	}

//	@PostConstruct
//	public void createArticles() {
//
//		article1 = new Article();
//		article2 = new Article();
//		article3 = new Article();
//		article4 = new Article();
//
//		// Attribute Artikel 1
//		article1.setArticleid(1);
//		article1.setArticlename("Produkt 1");
//		article1.setMaxprostellplatz(10);
//		article1.setPalettenstellplaetze(1.0);
//		articleRepository.save(article1);
//				
//		// Attribute Artikel 2
//		article2.setArticleid(2);
//		article2.setArticlename("Produkt 2");
//		article2.setMaxprostellplatz(20);
//		article2.setPalettenstellplaetze(2.0);
//		articleRepository.save(article2);
//		
//		// Attribute Artikel 3
//		article3.setArticleid(3);
//		article3.setArticlename("Produkt 3");
//		article3.setMaxprostellplatz(30);
//		article3.setPalettenstellplaetze(3.0);
//		articleRepository.save(article3);
//
//		// Attribute Artikel 4
//		article4.setArticleid(4);
//		article4.setArticlename("Produkt 4");
//		article4.setMaxprostellplatz(15);
//		article4.setPalettenstellplaetze(4.0);
//		articleRepository.save(article4);
//
//	}


}
