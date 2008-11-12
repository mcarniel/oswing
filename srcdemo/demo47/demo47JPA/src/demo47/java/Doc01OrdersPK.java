package demo47.java;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

@Embeddable
public class Doc01OrdersPK  extends ValueObjectImpl implements Serializable {
	@Column(name="DOC_YEAR")
	private long docYear;

	@Column(name="DOC_NUMBER")
	private long docNumber;

	private static final long serialVersionUID = 1L;

	public Doc01OrdersPK() {
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

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof Doc01OrdersPK)) {
			return false;
		}
		Doc01OrdersPK other = (Doc01OrdersPK) o;
		return (this.docYear == other.docYear)
			&& (this.docNumber == other.docNumber);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.docYear ^ (this.docYear >>> 32)));
		hash = hash * prime + ((int) (this.docNumber ^ (this.docNumber >>> 32)));
		return hash;
	}

}
