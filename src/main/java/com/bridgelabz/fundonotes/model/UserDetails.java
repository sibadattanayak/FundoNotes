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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	//@Email
	@Column(name = "Email", unique = true, nullable = false)
	private String email;

	/*
	 * @Pattern.List({ @Pattern(regexp = "(?=.*[0-9])", message =
	 * "Password must contain one digit.")
	 */

	//@Size(min = 3, max = 10, message = "Password Length must be between 3 to 10")
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
	private List<UserNotes> notesList = new ArrayList<UserNotes>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Column(nullable = false)
	@JoinColumn(name = "User_Id")
	private List<UserNoteLabel> labelList = new ArrayList<UserNoteLabel>();

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "collabratorUserList")
	@JsonIgnore
	private List<UserNotes> collabratorNoteList;

	public List<UserNotes> getCollabratorNoteList() {
		return collabratorNoteList;
	}

	public void setCollabratorNoteList(List<UserNotes> collabratorNoteList) {
		this.collabratorNoteList = collabratorNoteList;
	}

	public List<UserNotes> getNotes() {
		return notesList;
	}

	public List<UserNotes> getNotesList() {
		return notesList;
	}

	public void setNotesList(List<UserNotes> notesList) {
		this.notesList = notesList;
	}

	public List<UserNoteLabel> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<UserNoteLabel> labelList) {
		this.labelList = labelList;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
