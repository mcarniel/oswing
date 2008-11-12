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
@Table(name="DOC05_ARTICLES")
public class Doc05Articles extends ValueObjectImpl implements Serializable,DefaultTableFields {
	@Id
	@Column(name="ITEM_ID")
	private String itemId;

	private String descrizione;

	@Column(name="DEFAULT_UNIT_PRICE")
	private BigDecimal defaultUnitPrice;

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

	@OneToMany(mappedBy="itemId")
	private Set<Doc04OrderRows> doc04OrderRowsCollection;

	private static final long serialVersionUID = 1L;

	public Doc05Articles() {
		super();
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public BigDecimal getDefaultUnitPrice() {
		return this.defaultUnitPrice;
	}

	public void setDefaultUnitPrice(BigDecimal defaultUnitPrice) {
		this.defaultUnitPrice = defaultUnitPrice;
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

	public Set<Doc04OrderRows> getDoc04OrderRowsCollection() {
		return this.doc04OrderRowsCollection;
	}

	public void setDoc04OrderRowsCollection(Set<Doc04OrderRows> doc04OrderRowsCollection) {
		this.doc04OrderRowsCollection = doc04OrderRowsCollection;
	}

}
