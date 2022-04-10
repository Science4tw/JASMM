package jasmm.application;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controller für das Order Handling
 * Matthias
 */
@RestController
public class ServiceOrderManagement {

	// Verknüpfung zur Order Repo
	@Autowired
	private OrderRepository orderRepository;

	// Verknüpfung zur Artikel Repo
	@Autowired
	private ArticleRepository articleRepository;

	// Bestellung anlegen
	@PostMapping(path = "/api/createOrder/", produces = "application/json")
	public int createOrder(@RequestBody MessageNewOrder message) {
		Order order = new Order();

		order.setCustomerid(message.getCustomerid());

		order.setAmountarticle1(0);
		order.setAmountarticle2(0);
		order.setAmountarticle3(0);
		order.setAmountarticle4(0);

		// ATTENTION HARD CODED ID
		order.setArticle1(articleRepository.getById(1));
		order.setArticle2(articleRepository.getById(2));
		order.setArticle3(articleRepository.getById(3));
		order.setArticle4(articleRepository.getById(4));

		Calendar today = Calendar.getInstance();
		order.setOrderdate(today.getTime());

		order = orderRepository.save(order);

		return order.getOrderid();
	}

	// Orderitem zur Bestellung hinzufügen
	@PutMapping(path = "/api/order/{orderid}/addArticle", produces = "application/json")
	public boolean addArticleToOrder(@PathVariable int orderid, @RequestBody MessageAddArticleToOrder message) {

		// Order suchen mittels ID
		Optional<Order> order = orderRepository.findById(orderid);

		// Varialbe für Article ID um zu prüfen, bei welchem Artikel Mengen hinzugefügt
		// werden
		int articleid = message.getArticleid();

		// Konsolen Test
		System.out.println("Order ID: " + orderid);
		System.out.println("Article ID: " + message.getArticleid());
		System.out.println("Amount: " + message.getAmount());

		// Prüfung ob Order vorhanden ist
		if (order.isPresent()) {
			Order o = order.get();

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

			orderRepository.save(o);

			return true;
		}

		return false;

	}

	@GetMapping(path = "/api/order/{orderid}", produces = "application/json")
	public MessageOrderDetails getOrder(@PathVariable int orderid) {

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

//			for ( OrderItem oderitem : o.getItems()) {
//				MessageOrderItem messageOrderItem = new MessageOrderItem();
//				messageOrderItem.setAmount(oderitem.getAmount());
//				messageOrderItem.setArticleid(oderitem.getArticleid());
//				message.getItems().add(messageOrderItem);
//			}

			return message;
		} else {
			return null;
		}

	}

}
