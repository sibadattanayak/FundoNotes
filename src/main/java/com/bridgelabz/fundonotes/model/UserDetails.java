package com.bridgelabz.fundonotes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.bridgelabz.fundonotes.dto.UserNoteValidation;

@NotNull
@Entity
@Table(name = "User_Details")
public class UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "User_Id")
	private Long userId;

	@Column(name = "FirstName", nullable = false)
	private String firstName;

	@Column(name = "LastName")
	private String lastName;

	@Column(name = "Email", unique = true, nullable = false)
	private String email;

	@Column(name = "Password", nullable = false)
	private String password;

	@Column(name = "PasswordCreationTime")
	private LocalDateTime createTime;

	@Column(name = "PasswordUpdationTime")
	private LocalDateTime updateTime;

	@Column(name = "isVarified")
	private boolean isVarified;

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getUserId() {
		return userId;
	}

	public void setId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isVarified() {
		return isVarified;
	}

	public void setVarified(boolean isVarified) {
		this.isVarified = isVarified;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Column(nullable = false)
	@JoinColumn(name = "User_Id")
	private List<UserNotes> notes = new ArrayList<UserNotes>();

	public List<UserNotes> getNotes() {
		// TODO Auto-generated method stub
		return notes;
	}

}
