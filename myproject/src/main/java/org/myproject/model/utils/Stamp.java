package org.myproject.model.utils;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*
 *    https://www.youtube.com/watch?v=CV-qLwYKajI - Kevin Bowersox
 */

@Embeddable
public class Stamp {

    @Column(name = "CREATION_USER")
    private String creationUser;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "MODIFICATION_USER")
    private String modificationUser;

    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

	public String getCreationUser() {
		return creationUser;
	}

	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getModificationUser() {
		return modificationUser;
	}

	public void setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

}
