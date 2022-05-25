package jasmm.application;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jasmm.application.persistence.Article;
import jasmm.application.persistence.ArticleRepository;

@SpringBootApplication
public class JasmmApplication {

	// Logger
	static Logger logger = ServiceLocator.getServiceLocator().getLogger();	
    private static FileHandler fh;     
    private static SimpleFormatter formatter = new SimpleFormatter();  

	public static void main(String[] args) {
		
		
		// Logger & Filehandler
		// Credit: https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger
	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("MyLogFile.log");  
	        logger.addHandler(fh);        
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }   
		
		SpringApplication.run(JasmmApplication.class, args);
	}

	
	/**
	 * Erstmaliges erstellen der Artikel
	 */
	private Article article1;
	private Article article2;
	private Article article3;
	private Article article4;
	
	@Autowired
	private ArticleRepository articleRepository;
	
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
