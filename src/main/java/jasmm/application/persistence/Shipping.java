package jasmm.application.persistence;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * Repräsentiert die Artikel
 * @author Matthias & Julia
 */
@Entity(name = "shipping")
public class Shipping {

	@Id
	private int shippingid;

	private Integer km;

	private Float pal1;
	private Float pal2;
	private Float pal3;
	private Float pal4;
	private Float pal5;
	private Float pal6;
	private Float pal7;
	private Float pal8;
	private Float pal9;
	private Float pal10;
	private Float pal11;
	private Float pal12;

	public int getShippingid() {
		return shippingid;
	}

	public void setShippingid(int shippingid) {
		this.shippingid = shippingid;
	}

	public Integer getKm() {
		return km;
	}

	public void setKm(Integer km) {
		this.km = km;
	}

	public Float getPal1() {
		return pal1;
	}

	public void setPal1(Float pal1) {
		this.pal1 = pal1;
	}

	public Float getPal2() {
		return pal2;
	}

	public void setPal2(Float pal2) {
		this.pal2 = pal2;
	}

	public Float getPal3() {
		return pal3;
	}

	public void setPal3(Float pal3) {
		this.pal3 = pal3;
	}

	public Float getPal4() {
		return pal4;
	}

	public void setPal4(Float pal4) {
		this.pal4 = pal4;
	}

	public Float getPal5() {
		return pal5;
	}

	public void setPal5(Float pal5) {
		this.pal5 = pal5;
	}

	public Float getPal6() {
		return pal6;
	}

	public void setPal6(Float pal6) {
		this.pal6 = pal6;
	}

	public Float getPal7() {
		return pal7;
	}

	public void setPal7(Float pal7) {
		this.pal7 = pal7;
	}

	public Float getPal8() {
		return pal8;
	}

	public void setPal8(Float pal8) {
		this.pal8 = pal8;
	}

	public Float getPal9() {
		return pal9;
	}

	public void setPal9(Float pal9) {
		this.pal9 = pal9;
	}

	public Float getPal10() {
		return pal10;
	}

	public void setPal10(Float pal10) {
		this.pal10 = pal10;
	}

	public Float getPal11() {
		return pal11;
	}

	public void setPal11(Float pal11) {
		this.pal11 = pal11;
	}

	public Float getPal12() {
		return pal12;
	}

	public void setPal12(Float pal12) {
		this.pal12 = pal12;
	}

}
