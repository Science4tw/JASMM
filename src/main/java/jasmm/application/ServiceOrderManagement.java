package jasmm.application;

import java.util.Calendar;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/* Controller für das Order Handling
 * author Matthias
 */
@RestController
public class ServiceOrderManagement {

	// Verknüpfung zur Order Repo
	@Autowired
	private OrderRepository orderRepository;

	// Verknüpfung zur Artikel Repo
	@Autowired
	private ArticleRepository articleRepository;

	// Logger
	Logger logger = ServiceLocator.getServiceLocator().getLogger();

	// Bestellung anlegen
	@PostMapping(path = "/api/createOrder/", produces = "application/json")
	public int createOrder(@RequestBody MessageNewOrder message) {

		try {
			Order order = new Order();

			order.setCustomerid(message.getCustomerid());

			// Mengen auf 0 setzen
			order.setAmountarticle1(0);
			order.setAmountarticle2(0);
			order.setAmountarticle3(0);
			order.setAmountarticle4(0);

			// ATTENTION HARD CODED ID
			order.setArticle1(articleRepository.getById(1));
			order.setArticle2(articleRepository.getById(2));
			order.setArticle3(articleRepository.getById(3));
			order.setArticle4(articleRepository.getById(4));

			// Zeitpunkt der Bestellung
			Calendar today = Calendar.getInstance();
			order.setOrderdate(today.getTime());

			// Order speichern
			order = orderRepository.save(order);

			// Logging
			logger.info("Neue Bestellung erfolgreich angelegt. Bestell-ID: " + order.getOrderid() + ". Kunden-ID: " + message.getCustomerid());

			return order.getOrderid();

		} catch (Exception e) {
			logger.warning(e.toString());
			return -1;
		}

	}

	/*
	 * Menge eines Artikels der Bestellung author Matthias
	 */
	@PutMapping(path = "/api/order/{orderid}/addArticle", produces = "application/json")
	public boolean addArticleToOrder(@PathVariable int orderid, @RequestBody MessageAddArticleToOrder message) {
	
		try {

			// Order suchen mittels ID
			Optional<Order> order = orderRepository.findById(orderid);

			// Varialbe für Article ID um folgend zu prüfen, bei welchem Artikel Mengen hinzugefügt werden
			int articleid = message.getArticleid();

			// Prüfung ob Order vorhanden ist
			if (order.isPresent()) {
				Order o = order.get();

				// Basierend auf der articleid die Menge beim entsprechenden Artikel hinzufügen
				switch (articleid) {
				case 1:
					articleid = 1;
					o.setAmountarticle1(message.getAmount());

					break;
				case 2:
					articleid = 2;
					o.setAmountarticle2(message.getAmount());
					break;
				case 3:
					articleid = 3;
					o.setAmountarticle3(message.getAmount());
					break;
				case 4:
					articleid = 4;
					o.setAmountarticle4(message.getAmount());
					break;
				default:
					break;

				}
				
				// Order speichern
				orderRepository.save(o);

				// Logging
				logger.info("Produkt P" + articleid + " mit der Menge " + message.getAmount() + " erfolgreich der Bestellung " + o.getOrderid() + " hinzugefügt. Kunden-ID: <<fehlt noch>>");

				// return true;
			}
			// Logging
			// logger.info("Artikel konnte der Bestellung nicht hinzugefügt werden");

			// TEST ORDERITEM
			Optional<Order> or = orderRepository.findById(orderid);
			if (or.isPresent()) {
				OrderItem oi = new OrderItem();
				oi.setAmount(message.getAmount());
				oi.setArticleid(message.getArticleid());
				Order o = or.get();
				o.getOrderitems().add(oi);
				// das OrderItem wird automatisch mit gespeichert da es in der Liste vorhanden
				// ist
				orderRepository.save(o);
				logger.info("TEST ORDERITEM - ERFOLGREICH -  Artikel der Bestellung als Orderitem hinzugefügt - Artikel ID = " + articleid
						+ " - Order ID = " + orderid);
				return true;
			} else {
				logger.info("TEST ORDERITEM - FEHLGESCHLAGEN - Artikel der Bestellung als Orderitem hinzugefügt - Artikel ID = "
						+ articleid + " - Order ID = " + orderid);
				return false;
			}
		} catch (Exception e) {
			logger.warning(e.toString());
		}
		return false;
	}

	/*
	 * Zurückgeben einer Order author Matthias
	 */
	@GetMapping(path = "/api/order/{orderid}", produces = "application/json")
	public MessageOrderDetails getOrder(@PathVariable int orderid) {
		try {
			
			// Order suchen und speichern
			Optional<Order> order = orderRepository.findById(orderid);

			// Wenn die Order vorhanden ist, dann...
			if (order.isPresent()) {
				Order o = order.get();

				// Message um den Client Order zurückzugeben
				MessageOrderDetails message = new MessageOrderDetails();

				// Variablen der Order in der Message setzen
				message.setOrderid(o.getOrderid());
				message.setCustomerid(o.getCustomerid());

				// Logging
				logger.info("Order zurückgegeben mit der ID: " + orderid);

				return message;
			} else {
				
				// Logging
				logger.info("Order zurückgegeben fehlgeschlagen mit der ID: " + orderid);
				
				return null;
			}

		} catch (Exception e) {
			logger.info(e.toString());
		}
		return null;
		

	}

}
