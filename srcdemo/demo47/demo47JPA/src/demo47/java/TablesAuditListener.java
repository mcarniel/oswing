package demo47.java;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Listener che si occupa di valorizzare i campi standard delle tabelle
 */
public class TablesAuditListener {

	
	@PrePersist
	public void insert(DefaultTableFields valueObject) {
		valueObject.setDataIns(new java.sql.Timestamp(System.currentTimeMillis()));
		valueObject.setStatus("E");
	}
	
	
	@PreUpdate
	public void update(DefaultTableFields valueObject) {
		valueObject.setDataAgg(new java.sql.Timestamp(System.currentTimeMillis()));
	}	
	
}
