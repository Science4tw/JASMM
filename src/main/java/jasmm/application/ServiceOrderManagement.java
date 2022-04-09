package jasmm.application;

import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * 
 */
@RestController
public class ServiceOrderManagement {
	
		
	/*
	 * Verknüpfung zur Order Repo
	 */
	@Autowired
	private OrderRepository orderRepository;
	
	@PostMapping(path = "/api/createOrder/", produces = "application/json")
	public int createOrder(@RequestBody MessageNewOrder message) {
		Order order = new Order();
		
		order.setCustomerid(message.getCustomerid());
		order.setTestname("Testname");
		
		Calendar today = Calendar.getInstance();
		System.out.println(today);
		//today.set(Calendar.HOUR_OF_DAY, 0); // same for minutes and seconds	
		order.setOrderdate(today.getTime());
		System.out.println(today.getTime());
		
		
		order = orderRepository.save(order);
		
		return order.getOrderid();
	}
}
