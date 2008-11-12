package demo47.java;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

@Embeddable
public class Doc04OrderRowsPK extends ValueObjectImpl implements Serializable {
	@Column(name="DOC_YEAR", insertable=false, updatable=false)
	private long docYear;

	@Column(name="DOC_NUMBER", insertable=false, updatable=false)
	private long docNumber;

	@Column(name="ROW_NUMBER")
	private long rowNumber;

	private static final long serialVersionUID = 1L;

	public Doc04OrderRowsPK() {
		super();
	}

	public long getDocYear() {
		return this.docYear;
	}

	public void setDocYear(long docYear) {
		this.docYear = docYear;
	}

	public long getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(long docNumber) {
		this.docNumber = docNumber;
	}

	public long getRowNumber() {
		return this.rowNumber;
	}

	public void setRowNumber(long rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof Doc04OrderRowsPK)) {
			return false;
		}
		Doc04OrderRowsPK other = (Doc04OrderRowsPK) o;
		return (this.docYear == other.docYear)
			&& (this.docNumber == other.docNumber)
			&& (this.rowNumber == other.rowNumber);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.docYear ^ (this.docYear >>> 32)));
		hash = hash * prime + ((int) (this.docNumber ^ (this.docNumber >>> 32)));
		hash = hash * prime + ((int) (this.rowNumber ^ (this.rowNumber >>> 32)));
		return hash;
	}

}
