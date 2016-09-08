package com.funtest.analysis.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_resource")
public class Resource {
	private Integer id;
	private String name;
	private String label;
	private List<Permission> permissions=new ArrayList<Permission>();

	public Resource() {
		super();
	}
	
	@Column(name="id")
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="ResourceId")
	public List<Permission> getPermissions() {
		return permissions;
	}

	
	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}



	
	
	

}
