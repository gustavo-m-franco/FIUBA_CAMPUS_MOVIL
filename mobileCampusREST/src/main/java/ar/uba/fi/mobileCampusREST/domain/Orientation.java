package ar.uba.fi.mobileCampusREST.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Orientation {
		
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
