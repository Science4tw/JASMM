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

	// Bestellung anlegen
	@PostMapping(path = "/api/createOrder/", produces = "application/json")
	public int createOrder(@RequestBody MessageNewOrder message) {
		Order order = new Order();

		order.setCustomerid(message.getCustomerid());
		order.setTestname("Testname");

		Calendar today = Calendar.getInstance();
		order.setOrderdate(today.getTime());

		order = orderRepository.save(order);

		return order.getOrderid();
	}

	// Orderitem zur Bestellung hinzufügen
	@PutMapping(path = "/api/order/{orderid}/addArticle", 
			produces = "application/json")
	public boolean addArticleToOrder(@PathVariable int orderid, 
			@RequestBody MessageAddArticleToOrder message) {

		Optional<Order> order = orderRepository.findById(orderid);
		
		System.out.println("Order ID: " + orderid);
		System.out.println("Article ID: " + message.getArticleid());
		System.out.println("Amount: " + message.getAmount());

		
		if (order.isPresent()) {
			OrderItem orderitem = new OrderItem();
			orderitem.setAmount(message.getAmount());
			orderitem.setArticleid(message.getArticleid());
			orderitem.setOderid(orderid);
			Order o = order.get();
			o.getItems().add(orderitem);

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
			
			for ( OrderItem oderitem : o.getItems()) {
				MessageOrderItem messageOrderItem = new MessageOrderItem();
				messageOrderItem.setAmount(oderitem.getAmount());
				messageOrderItem.setArticleid(oderitem.getArticleid());
				message.getItems().add(messageOrderItem);
			}
			
			return message;
		} else {
			return null;
		}
		
	}

}
