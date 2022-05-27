package jasmm.application.business;

import org.springframework.stereotype.Service;

/**
 * Business Logik - Brechnung der benötigten Paletten
 * @author Julia
 *
 */
@Service
public class OrderCalculation {
	
	/* Finale volle Paletten ausrechnen mit Runden */

	public static int calculatePallets(int p1, int p2, int p3, int p4) {
			
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
				double mixedRest4 = 0;

				double spaceP1 = 0;
				double spaceP2 = 0;
				double spaceP3 = 0;
				double spaceP4 = 0;

				double unmixedPalletsFinal = 0;
				double mixedPalletsFinal = 0;
				double finalAll = 0;

				/** Volle Paletten ausrechnen **/

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

				/** RESTEN AUSRECHNEN **/

				double allRest = (restP1 * HIGHT_P1) + (restP2 * HIGHT_P2) + (restP3 * HIGHT_P3) + (restP4 * HIGHT_P4);

				System.out.println("allRest " + allRest);
				System.out.println("*********");

				// wenn alle Restprodukte weniger als 2200 cm Höhe benötigen, können sie auf
				// einander gestappelt werden. Angefangen wird bei der Abfrage P3, da diese
				// Produkte am meisten Platz benötigen, dann p2, p1 und p4
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
				}
				// falls alle Resten gestappelt höher als 2200 cm sind, müssen die "Resten der
				// Resten" gestappelt werden. Dies muss mindestens "2 Palleten" verschiedener
				// Grundflächen ergeben, zb. 1x pallet1 (1.2) + 1x palett2 (2)
				if (allRest > MAX_HIGHT) {
					// Benötigter Platz der Restprodukte ausrechnen
					spaceP3 = (restP3 * HIGHT_P3);
					spaceP2 = (restP2 * HIGHT_P2);
					spaceP1 = (restP1 * HIGHT_P1);
					spaceP4 = (restP4 * HIGHT_P4);

					if (restP3 > 0) {
						double availableSpace3 = MAX_HIGHT;
						availableSpace3 = availableSpace3 - spaceP3;
						mixedPalletP3++; // wenn P3 dann auf jeden Fall 1 Palett hinzufügen
						if (availableSpace3 > 0) {
							availableSpace3 = availableSpace3 - spaceP2;
							if (availableSpace3 < 0) {
								mixedPalletP2++;
								double restSpaceP2 = Math.abs(availableSpace3);
								double availableSpace2 = MAX_HIGHT;
								availableSpace2 = availableSpace2 - restSpaceP2;
								if (availableSpace2 > 0) {
									availableSpace2 = availableSpace2 - spaceP1;
									if (availableSpace2 < 0) {
										mixedPalletP1++;
										double restSpaceP1 = Math.abs(availableSpace2);
										double availableSpace1 = MAX_HIGHT;
										availableSpace1 = availableSpace1 - restSpaceP1;
										if (availableSpace1 > 0) {
											availableSpace1 = availableSpace1 - spaceP4;
											if (availableSpace1 < 0) {
												mixedPalletP4++;
												double restSpaceP4 = Math.abs(availableSpace1);
												double availableSpace4 = MAX_HIGHT;
												availableSpace4 = availableSpace4 - restSpaceP4;

											}
										}
									}
								}

							}
						}
					} else {
						if (restP2 > 0) {
							double availableSpace2 = MAX_HIGHT;
							availableSpace2 = availableSpace2 - spaceP2;
							mixedPalletP2++; // wenn P2 dann auf jeden Fall 1 Palett hinzufügen
							if (availableSpace2 > 0) {
								availableSpace2 = availableSpace2 - spaceP1;
								if (availableSpace2 < 0) {
									mixedPalletP1++;
									double restSpaceP1 = Math.abs(availableSpace2);
									double availableSpace1 = MAX_HIGHT;
									availableSpace1 = availableSpace1 - restSpaceP1;
									if (availableSpace1 > 0) {
										availableSpace1 = availableSpace1 - spaceP4;
										if (availableSpace1 < 0) {
											mixedPalletP4++;
											double restSpaceP4 = Math.abs(availableSpace1);
											double availableSpace4 = MAX_HIGHT;
											availableSpace4 = availableSpace4 - restSpaceP4;

										}
									}

								}
							}
						} else {
							if (restP1 > 0) {
								double availableSpace1 = MAX_HIGHT;
								availableSpace1 = availableSpace1 - spaceP1;
								mixedPalletP1++; // wenn P1 dann auf jeden Fall 1 Palett hinzufügen
								if (availableSpace1 > 0) {
									availableSpace1 = availableSpace1 - spaceP4;
									if (availableSpace1 < 0) {
										mixedPalletP4++;
										double restSpaceP4 = Math.abs(availableSpace1);
										double availableSpace4 = MAX_HIGHT;
										availableSpace4 = availableSpace4 - restSpaceP4;

									} else {
										if (restP4 > 0) {
											mixedPalletP4++; // wenn nur noch P4 dann einfach 1 Palett hinzufügen
											
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
			
				int allFinalPallets = (int) Math.ceil(mixedPalletsFinal + unmixedPalletsFinal);

		return allFinalPallets;

	}

}
