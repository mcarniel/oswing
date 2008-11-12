package demo47.java;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.persistence.NamedQuery;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

@NamedQuery(name="Doc04OrderRows.getMaxRowNumber",
		query="SELECT max(r.pk.rowNumber)+1 " +
		"FROM Doc04OrderRows r " +
		"WHERE r.pk.docYear=?1 and r.pk.docNumber=?2 ")
@Entity
@EntityListeners({ TablesAuditListener.class })
@Table(name="DOC04_ORDER_ROWS")
public class Doc04OrderRows extends ValueObjectImpl implements Serializable,DefaultTableFields {
	@EmbeddedId
	private Doc04OrderRowsPK pk;

	private BigDecimal qty;

	@Column(name="UNIT_PRICE")
	private BigDecimal unitPrice;

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

	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="DOC_YEAR", referencedColumnName="DOC_YEAR",insertable=false,updatable=false),
		@JoinColumn(name="DOC_NUMBER", referencedColumnName="DOC_NUMBER",insertable=false,updatable=false)
		
	})
	private Doc01Orders doc01Orders;

	@ManyToOne
	@JoinColumn(name="ITEM_ID",updatable=false)
	private Doc05Articles itemId;

	private static final long serialVersionUID = 1L;

	public Doc04OrderRows() {
		super();
	}

	public Doc04OrderRowsPK getPk() {
		return this.pk;
	}

	public void setPk(Doc04OrderRowsPK pk) {
		this.pk = pk;
	}

	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
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

	public Doc01Orders getDoc01Orders() {
		return this.doc01Orders;
	}

	public void setDoc01Orders(Doc01Orders doc01Orders) {
		this.doc01Orders = doc01Orders;
	}

	public Doc05Articles getItemId() {
		return this.itemId;
	}

	public void setItemId(Doc05Articles itemId) {
		this.itemId = itemId;
	}

}
