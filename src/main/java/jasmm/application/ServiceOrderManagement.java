package jasmm.application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
			// Logging
			// logger.info("Artikel konnte der Bestellung nicht hinzugefügt werden");

//			// TEST ORDERITEM
//			Optional<Order> or = orderRepository.findById(orderid);
//			if (or.isPresent()) {
//				OrderItem oi = new OrderItem();
//				oi.setAmount(message.getAmount());
//				oi.setArticleid(message.getArticleid());
//				Order o = or.get();
//				o.getOrderitems().add(oi);
//				// das OrderItem wird automatisch mit gespeichert da es in der Liste vorhanden
//				// ist
//				orderRepository.save(o);
//////				logger.info(
//////						"TEST ORDERITEM - ERFOLGREICH -  Artikel der Bestellung als Orderitem hinzugefügt - Artikel ID = "
//////								+ articleid + " - Order ID = " + orderid);
////				return true;
////			} else {
//////				logger.info(
//////						"TEST ORDERITEM - FEHLGESCHLAGEN - Artikel der Bestellung als Orderitem hinzugefügt - Artikel ID = "
//////								+ articleid + " - Order ID = " + orderid);
//				return false;
//			}
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

//	@ResponseBody
	@GetMapping(path = "/order/{orderid}/calculateCostOfOrder/", produces = "application/json")
	//public MessageCalculatedOrder calculateCostOfOrder(@PathVariable int orderid) { //alte Variante
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
		anzahlPaletten = calculatePallets(p1, p2, p3, p4);

		// customerid aus Order ziehen
		int customerid = order.getCustomerid();
		// Kunde aus Repo holen
		Customer customer = customerRepository.getById(customerid);

		// In City Tabelle die Zone zur Distanz speichern
		City city = cityRepository.findByZipcode(customer.getZipCode());
		Integer zone = city.getZone();
		//logger.info("Zone: " + zone);

		// Aus der Shipping Tabelle das Objekt mit der korrekten Zone speichern
		Shipping shippingObject = shippingRepository.findByKm(zone);

		// Bassierend auf Anzahl Paletten die Kosten speichern
		Float shippingCost = null;
		switch (anzahlPaletten) {
		case 1:
			anzahlPaletten = 1;
			shippingCost = shippingObject.getPal1();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			//"Anzahl Paletten: " + anzahlPaletten + " | Transportkosten: " + shippingCost + " | Zone: " + zone);
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 2:
			anzahlPaletten = 2;
			shippingCost = shippingObject.getPal2();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 3:
			anzahlPaletten = 3;
			shippingCost = shippingObject.getPal3();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 4:
			anzahlPaletten = 4;
			shippingCost = shippingObject.getPal4();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 5:
			anzahlPaletten = 5;
			shippingCost = shippingObject.getPal5();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 6:
			anzahlPaletten = 6;
			shippingCost = shippingObject.getPal6();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 7:
			anzahlPaletten = 7;
			shippingCost = shippingObject.getPal7();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 8:
			anzahlPaletten = 8;
			shippingCost = shippingObject.getPal8();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 9:
			anzahlPaletten = 9;
			shippingCost = shippingObject.getPal9();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 10:
			anzahlPaletten = 10;
			shippingCost = shippingObject.getPal10();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 11:
			anzahlPaletten = 11;
			shippingCost = shippingObject.getPal11();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		case 12:
			anzahlPaletten = 12;
			shippingCost = shippingObject.getPal12();
			logger.info(
					"Transportkosten für Bestellung " + orderid + ": CHF " + shippingCost + " (Anzahl Paletten: " + anzahlPaletten + " | km-Zone: " + zone +")"); 
			order.setShippingcost(shippingCost);
			order = orderRepository.save(order);
			return shippingCost;
			//break;
		default:
			//Bestellmenge übersteigt die max. Anzahl von 12 Paletten
			logger.info(
					"Bestellnummer: " + orderid + ". Anzahl Paletten > 12. Kunde wird aufgefordert die Bestellmenge zu reduzieren.");
			return -1;
			//break;

		}


	}

	
	/* Finale volle Paletten ausrechnen mit Runden */

	public int calculatePallets(int p1, int p2, int p3, int p4) {
			
	// FINALE WERTE der Produkte/Paletten
		final int MAX_P1 = 25;
		final int MAX_P2 = 10;
		final int MAX_P3 = 15;
		final int MAX_P4 = 100;

		final int MAX_HIGHT = 2200;

		final double HIGHT_P1 = MAX_HIGHT / MAX_P1;
		final double HIGHT_P2 = MAX_HIGHT / MAX_P2;
		final double HIGHT_P3 = MAX_HIGHT / MAX_P3;
		final double HIGHT_P4 = MAX_HIGHT / MAX_P4;

		final double PALLET_P1 = 1.2;
		final double PALLET_P2 = 2;
		final double PALLET_P3 = 2.5;
		final double PALLET_P4 = 0.8;
		
		// double für gespeicherte Paletteanzahl
		double palletP1 = 0;
		double palletP2 = 0;
		double palletP3 = 0;
		double palletP4 = 0;

		// int für gespeicherte volle Paletteanzahl
		double fullPalletP1 = 0;
		double fullPalletP2 = 0;
		double fullPalletP3 = 0;
		double fullPalletP4 = 0;

		double mixedPalletP1 = 0;
		double mixedPalletP2 = 0;
		double mixedPalletP3 = 0;
		double mixedPalletP4 = 0;

		// int für Restproduktanzahl
		double restP1 = 0;
		double restP2 = 0;
		double restP3 = 0;
		double restP4 = 0;

		double mixedRest = 0;
		double mixedRest2 = 0;
		double mixedRest3 = 0;

		double unmixedPalletsFinal = 0;
		double mixedPalletsFinal = 0;

		int allFinalPallets = 0;

		/* Volle Paletten ausrechnen */

		// Abfrage, ob P1 ganze Paletten ausschöpfen oder Restprodukte bleibt
		if (p1 > 0) {
			int p1m = p1 % MAX_P1;
			// wenn Restprodukte von P1, dann Anzahl Produkte in restP1 speichern
			if (p1m > 0) {
				double p1x = p1 / MAX_P1;
				restP1 = p1 % MAX_P1;
				// Kommastellen abschneiden mit Cast und Anzahl Palette in volleP1 speichern
				fullPalletP1 = (int) p1x;
			}
			// wenn keine Restprodukte von P1, dann Anzahl Palette in volleP1 speichern
			if (p1m == 0) {
				fullPalletP1 = p1 / MAX_P1;
			}
		}

		// Abfrage, ob P2 ganze Paletten ausschöpfen oder Restprodukte bleibt
		if (p2 > 0) {
			int p2m = p2 % MAX_P2;
			// wenn Restprodukte von P2, dann Anzahl Produkte in restP2 speichern
			if (p2m > 0) {
				double p2x = p2 / MAX_P2;
				restP2 = p2 % MAX_P2;
				// Kommastellen abschneiden mit Cast und Anzahl Palette in volleP2 speichern
				fullPalletP2 = (int) p2x;
			}
			// wenn keine Restprodukte von P2, dann Anzahl Palette in volleP2 speichern
			if (p2m == 0) {
				fullPalletP2 = p2 / MAX_P2;
			}
		}

		// Abfrage, ob P3 ganze Paletten ausschöpfen oder Restprodukte bleibt
		if (p3 > 0) {
			int p3m = p3 % MAX_P3;
			// wenn Restprodukte von P3, dann Anzahl Produkte in restP3 speichern
			if (p3m > 0) {
				double p3x = p3 / MAX_P3;
				restP3 = p3 % MAX_P3;
				// Kommastellen abschneiden mit Cast und Anzahl Palette in volleP3 speichern
				fullPalletP3 = (int) p3x;
			}
			// wenn keine Restprodukte von P3, dann Anzahl Palette in volleP3 speichern
			if (p3m == 0) {
				fullPalletP3 = p3 / MAX_P3;
			}
		}

		// Abfrage, ob P4 ganze Paletten ausschöpfen oder Restprodukte bleibt
		if (p4 > 0) {
			int p4m = p4 % MAX_P4;
			// wenn Restprodukte von P4, dann Anzahl Produkte in restP4 speichern
			if (p4m > 0) {
				double p4x = p4 / MAX_P4;
				restP4 = p4 % MAX_P4;
				// Kommastellen abschneiden mit Cast und Anzahl Palette in volleP4 speichern
				fullPalletP4 = (int) p4x;
			}
			// wenn keine Restprodukte von P4, dann Anzahl Palette in volleP4 speichern
			if (p4m == 0) {
				fullPalletP4 = p4 / MAX_P4;
			}
		}

		// Tatsächliche Paletten ohne Restprodukte ausrechnen

		palletP1 = (fullPalletP1 * PALLET_P1);
		palletP2 = (fullPalletP2 * PALLET_P2);
		palletP3 = (fullPalletP3 * PALLET_P3);
		palletP4 = (fullPalletP4 * PALLET_P4);

		/* RESTEN AUSRECHNEN */

		double allRest = restP1 * HIGHT_P1 + restP2 * HIGHT_P2 + restP3 * HIGHT_P3 + restP4 * HIGHT_P4;

		if (allRest < MAX_HIGHT) {
			if (restP3 > 0) {
				mixedPalletP3++;
			} else {
				if (restP2 > 0) {
					mixedPalletP2++;
				} else {
					if (restP1 > 0) {
						mixedPalletP1++;
					} else {
						if (restP4 > 0) {
							mixedPalletP4++;
						}
					}
				}
			}
		} else {
			if (allRest > MAX_HIGHT) {
				mixedRest = (MAX_HIGHT - allRest);
				if (restP3 > 0) {
					mixedPalletP3++;
				} else {
					if (restP2 > 0) {
						mixedPalletP2++;
					} else {
						if (restP1 > 0) {
							mixedPalletP1++;
						} else {
							if (restP4 > 0) {
								mixedPalletP4++;
							}
						}
					}
				}

			}
			if (mixedRest > 0 && restP2 > 0) {
				mixedPalletP2++;
				mixedRest2 = MAX_HIGHT - mixedRest;

			} else {
				if (mixedRest > 0 && restP1 > 0) {
					mixedPalletP1++;
					mixedRest2 = MAX_HIGHT - mixedRest;

				} else {
					if (mixedRest > 0 && restP4 > 0) {
						mixedPalletP4++;
						mixedRest2 = MAX_HIGHT - mixedRest;

					} else {

						if (mixedRest2 > 0 && restP1 > 0) {
							mixedPalletP1++;
							mixedRest3 = MAX_HIGHT - mixedRest2;

						} else {
							if (mixedRest2 > 0 && restP4 > 0) {
								mixedPalletP4++;
								mixedRest3 = MAX_HIGHT - mixedRest2;

							} else {
								if (mixedRest3 > 0 && restP4 > 0) {
									mixedPalletP4++;
								}
							}
						}
					}
				}
			}
		}

		/** Finale Paletten ausrechnen **/
		mixedPalletP1 = (mixedPalletP1 * PALLET_P1);
		mixedPalletP2 = (mixedPalletP2 * PALLET_P2);
		mixedPalletP3 = (mixedPalletP3 * PALLET_P3);
		mixedPalletP4 = (mixedPalletP4 * PALLET_P4);

		unmixedPalletsFinal = palletP1 + palletP2 + palletP3 + palletP4;
		mixedPalletsFinal = mixedPalletP1 + mixedPalletP2 + mixedPalletP3 + mixedPalletP4;

		

		allFinalPallets = (int) Math.ceil(mixedPalletsFinal + unmixedPalletsFinal);
		

		return allFinalPallets;

	}

}
