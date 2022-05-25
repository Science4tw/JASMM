package jasmm.application.service;

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

import jasmm.application.ServiceLocator;
import jasmm.application.persistence.ArticleRepository;
import jasmm.application.persistence.City;
import jasmm.application.persistence.CityRepository;
import jasmm.application.persistence.Customer;
import jasmm.application.persistence.CustomerRepository;
import jasmm.application.persistence.Order;
import jasmm.application.persistence.OrderRepository;
import jasmm.application.persistence.Shipping;
import jasmm.application.persistence.ShippingRepository;
import jasmm.application.business.OrderCalculation;

/* Controller für das Order Handling
 * author Matthias & Julia
 */
@RestController
public class ServiceOrderManagement {

	// Verknüpfung zur Order Repo
	@Autowired
	private OrderRepository orderRepository;

	// Verknüpfung zur Artikel Repo
	@Autowired
	private ArticleRepository articleRepository;

	// Verknüpfung zur Customer Repo
	@Autowired
	private CustomerRepository customerRepository;

	// Verknüpfung zur Shipping Repo
	@Autowired
	private ShippingRepository shippingRepository;

	// Verknüpfung zur City Repo
	@Autowired
	private CityRepository cityRepository;

	// Logger
	Logger logger = ServiceLocator.getServiceLocator().getLogger();

	// Bestellung anlegen
	@PostMapping(path = "/createOrder/", produces = "application/json")
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
			logger.info("Neue Bestellung erfolgreich angelegt. Bestell-ID: " + order.getOrderid() + ". Kunden-ID: "
					+ message.getCustomerid());

			return order.getOrderid();

		} catch (Exception e) {
			logger.warning(e.toString());
			return -1;
		}

	}

	/*
	 * Menge eines Artikels der Bestellung author Matthias
	 */
	@PutMapping(path = "/order/{orderid}/addArticle", produces = "application/json")
	public boolean addArticleToOrder(@PathVariable int orderid, @RequestBody MessageAddArticleToOrder message) {

		try {

			// Order suchen mittels ID
			Optional<Order> order = orderRepository.findById(orderid);

			// Varialbe für Article ID um folgend zu prüfen, bei welchem Artikel Mengen
			// hinzugefügt werden
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
				logger.info("Produkt P" + articleid + " mit der Menge " + message.getAmount()
						+ " erfolgreich der Bestellung " + o.getOrderid() + " hinzugefügt. Kunden-ID: "
						+ message.getCustomerid());

				return true;
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

	@GetMapping(path = "/order/{orderid}/calculateCostOfOrder/", produces = "application/json")
	public float calculateCostOfOrder(@PathVariable int orderid) {

		// Order suchen und speichern
		Optional<Order> o = orderRepository.findById(orderid);
		// "richtige" Order
		Order order = o.get();

		// Mengen der jeweiligen Produkte
		int p1 = order.getAmountarticle1();
		int p2 = order.getAmountarticle2();
		int p3 = order.getAmountarticle3();
		int p4 = order.getAmountarticle4();

		// Berechnung der Anzahl Paletten
		int anzahlPaletten = 0;
		anzahlPaletten = OrderCalculation.calculatePallets(p1, p2, p3, p4);

		// customerid aus Order ziehen
		int customerid = order.getCustomerid();
		// Kunde aus Repo holen
		Customer customer = customerRepository.getById(customerid);

		// In City Tabelle die Zone zur Distanz speichern
		City city = cityRepository.findByZipcode(customer.getZipCode());
		Integer zone = city.getZone();
		// logger.info("Zone: " + zone);

		// Aus der Shipping Tabelle das Objekt mit der korrekten Zone speichern
		Shipping shippingObject = shippingRepository.findByKm(zone);

		// Bassierend auf Anzahl Paletten die Kosten speichern
		Float shippingCost = null;
		switch (anzahlPaletten) {
		case 1:
			anzahlPaletten = 1;
			shippingCost = shippingObject.getPal1();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			// "Anzahl Paletten: " + anzahlPaletten + " | Transportkosten: " + shippingCost
			// + " | Zone: " + zone);
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 2:
			anzahlPaletten = 2;
			shippingCost = shippingObject.getPal2();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 3:
			anzahlPaletten = 3;
			shippingCost = shippingObject.getPal3();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 4:
			anzahlPaletten = 4;
			shippingCost = shippingObject.getPal4();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 5:
			anzahlPaletten = 5;
			shippingCost = shippingObject.getPal5();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 6:
			anzahlPaletten = 6;
			shippingCost = shippingObject.getPal6();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 7:
			anzahlPaletten = 7;
			shippingCost = shippingObject.getPal7();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 8:
			anzahlPaletten = 8;
			shippingCost = shippingObject.getPal8();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 9:
			anzahlPaletten = 9;
			shippingCost = shippingObject.getPal9();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 10:
			anzahlPaletten = 10;
			shippingCost = shippingObject.getPal10();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 11:
			anzahlPaletten = 11;
			shippingCost = shippingObject.getPal11();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		case 12:
			anzahlPaletten = 12;
			shippingCost = shippingObject.getPal12();
			logger.info("Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: "
					+ anzahlPaletten + " | km-Zone: " + zone + ")");
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
		// break;
		default:
			// Bestellmenge übersteigt die max. Anzahl von 12 Paletten
			logger.info("Bestellnummer: " + orderid
					+ ". Anzahl Paletten > 12. Kunde wird aufgefordert die Bestellmenge zu reduzieren.");
			return -1;
		// break;

		}

	}

}
