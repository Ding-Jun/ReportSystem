package com.funtest.analysis.bean;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_role")
public class Role {
	private Integer id;
	private String name;
	private Set<Permission> permissionSet=new HashSet<Permission>();

	public Role() {
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

	@ManyToMany(fetch=FetchType.EAGER)
	@Cascade(CascadeType.ALL)
    @JoinTable(name="t_role_permission",
            joinColumns={@JoinColumn(name="roleId")},
            inverseJoinColumns={@JoinColumn(name="permissionId")})
	public Set<Permission> getPermissionSet() {
		return permissionSet;
	}

	
	public void setPermissionSet(Set<Permission> permissionSet) {
		this.permissionSet = permissionSet;
	}



	
	
	

}
