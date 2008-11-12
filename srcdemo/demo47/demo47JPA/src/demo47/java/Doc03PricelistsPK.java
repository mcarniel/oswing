package demo47.java;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

@Embeddable
public class Doc03PricelistsPK extends ValueObjectImpl implements Serializable {
	@Column(name="CUSTOMER_ID_DOC02", insertable=false, updatable=false)
	private String customerIdDoc022;

	@Column(name="PRICELIST_ID")
	private String pricelistId;

	private static final long serialVersionUID = 1L;

	public Doc03PricelistsPK() {
		super();
	}

	public String getCustomerIdDoc022() {
		return this.customerIdDoc022;
	}

	public void setCustomerIdDoc022(String customerIdDoc022) {
		this.customerIdDoc022 = customerIdDoc022;
	}

	public String getPricelistId() {
		return this.pricelistId;
	}

	public void setPricelistId(String pricelistId) {
		this.pricelistId = pricelistId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof Doc03PricelistsPK)) {
			return false;
		}
		Doc03PricelistsPK other = (Doc03PricelistsPK) o;
		return this.customerIdDoc022.equals(other.customerIdDoc022)
			&& this.pricelistId.equals(other.pricelistId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.customerIdDoc022.hashCode();
		hash = hash * prime + this.pricelistId.hashCode();
		return hash;
	}

}
