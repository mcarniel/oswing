package demo47.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

@Entity
@EntityListeners({ TablesAuditListener.class })
@Table(name="DOC02_CUSTOMERS")
public class Doc02Customers extends ValueObjectImpl implements Serializable,DefaultTableFields {
	@Id
	@Column(name="CUSTOMER_ID")
	private String customerId;

	private String description;

	private String status;

	private String address;

	@Column(name="COD_UTENTE_INS")
	private String codUtenteIns;

	@Column(name="COD_UTENTE_AGG")
	private String codUtenteAgg;

	@Column(name="DATA_INS")
	private Timestamp dataIns;

	@Column(name="DATA_AGG")
	private Timestamp dataAgg;

	@Version
	private Long timestamp;

	@OneToMany(mappedBy="customerIdDoc02")
	private Set<Doc03Pricelists> doc03PricelistsCollection;

	@OneToMany(mappedBy="customerId")
	private Set<Doc01Orders> doc01OrdersCollection;

	private static final long serialVersionUID = 1L;

	public Doc02Customers() {
		super();
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCodUtenteIns() {
		return this.codUtenteIns;
	}

	public void setCodUtenteIns(String codUtenteIns) {
		this.codUtenteIns = codUtenteIns;
	}

	public String getCodUtenteAgg() {
		return this.codUtenteAgg;
	}

	public void setCodUtenteAgg(String codUtenteAgg) {
		this.codUtenteAgg = codUtenteAgg;
	}

	public Timestamp getDataIns() {
		return this.dataIns;
	}

	public void setDataIns(Timestamp dataIns) {
		this.dataIns = dataIns;
	}

	public Timestamp getDataAgg() {
		return this.dataAgg;
	}

	public void setDataAgg(Timestamp dataAgg) {
		this.dataAgg = dataAgg;
	}

	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Set<Doc03Pricelists> getDoc03PricelistsCollection() {
		return this.doc03PricelistsCollection;
	}

	public void setDoc03PricelistsCollection(Set<Doc03Pricelists> doc03PricelistsCollection) {
		this.doc03PricelistsCollection = doc03PricelistsCollection;
	}

	public Set<Doc01Orders> getDoc01OrdersCollection() {
		return this.doc01OrdersCollection;
	}

	public void setDoc01OrdersCollection(Set<Doc01Orders> doc01OrdersCollection) {
		this.doc01OrdersCollection = doc01OrdersCollection;
	}

}
