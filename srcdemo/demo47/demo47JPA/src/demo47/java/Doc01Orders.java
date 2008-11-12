package demo47.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

@Entity
@EntityListeners({ TablesAuditListener.class })
@Table(name="DOC01_ORDERS")
public class Doc01Orders extends ValueObjectImpl implements Serializable,DefaultTableFields {
	@EmbeddedId
	private Doc01OrdersPK pk;

	@Column(name="DOC_DATE")
	private Date docDate;

	private BigDecimal total;

	private String note;

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

	private String state;

	@ManyToOne
	@JoinColumn(name="CUSTOMER_ID",insertable=false,updatable=false)
	private Doc02Customers customerId;

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="CUSTOMER_ID", referencedColumnName="CUSTOMER_ID_DOC02"),
		@JoinColumn(name="PRICELIST_ID", referencedColumnName="PRICELIST_ID")
	})
	private Doc03Pricelists doc03Pricelists;

	@OneToMany(mappedBy="doc01Orders")
	private List<Doc04OrderRows> doc04OrderRowsCollection;

	private static final long serialVersionUID = 1L;

	public Doc01Orders() {
		super();
	}

	public Doc01OrdersPK getPk() {
		return this.pk;
	}

	public void setPk(Doc01OrdersPK pk) {
		this.pk = pk;
	}

	public Date getDocDate() {
		return this.docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Doc02Customers getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Doc02Customers customerId) {
		this.customerId = customerId;
	}

	public Doc03Pricelists getDoc03Pricelists() {
		return this.doc03Pricelists;
	}

	public void setDoc03Pricelists(Doc03Pricelists doc03Pricelists) {
		this.doc03Pricelists = doc03Pricelists;
	}

	public List<Doc04OrderRows> getDoc04OrderRowsCollection() {
		return this.doc04OrderRowsCollection;
	}

	public void setDoc04OrderRowsCollection(List<Doc04OrderRows> doc04OrderRowsCollection) {
		this.doc04OrderRowsCollection = doc04OrderRowsCollection;
	}

}
