package demo47.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

@Entity
@EntityListeners({ TablesAuditListener.class })
@Table(name="DOC03_PRICELISTS")
public class Doc03Pricelists extends ValueObjectImpl implements Serializable,DefaultTableFields {
	@EmbeddedId
	private Doc03PricelistsPK pk;

	@Column(name="DESCRIPTION")
	private String description;

	private String status;

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

	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID_DOC02",insertable=false,updatable=false)
	private Doc02Customers customerIdDoc02;

	@OneToMany(mappedBy="doc03Pricelists")
	private Set<Doc01Orders> doc01OrdersCollection;

	private static final long serialVersionUID = 1L;

	public Doc03Pricelists() {
		super();
	}

	public Doc03PricelistsPK getPk() {
		return this.pk;
	}

	public void setPk(Doc03PricelistsPK pk) {
		this.pk = pk;
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

	@Version
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Doc02Customers getCustomerIdDoc02() {
		return this.customerIdDoc02;
	}

	public void setCustomerIdDoc02(Doc02Customers customerIdDoc02) {
		this.customerIdDoc02 = customerIdDoc02;
	}

	public Set<Doc01Orders> getDoc01OrdersCollection() {
		return this.doc01OrdersCollection;
	}

	public void setDoc01OrdersCollection(Set<Doc01Orders> doc01OrdersCollection) {
		this.doc01OrdersCollection = doc01OrdersCollection;
	}

}
