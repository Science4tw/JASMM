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
}
