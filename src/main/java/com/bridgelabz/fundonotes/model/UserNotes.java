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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NotNull
@Entity
@Table(name = "User_Notes")
public class UserNotes {
	@Id
	@Column(name = "UserNote_Id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "UserNote_Description")
	private String noteDescription;

	@Column(name = "UserNote_Title")
	private String noteTitle;

	@Column(name = "UserNote_CreationTime")
	private LocalDateTime noteCreateTime;

	@Column(name = "UserNote_UpdationTime")
	private LocalDateTime noteUpdateTime;

	@Column(name = "UserNote_Reminder")
	private LocalDateTime reminder;

	@Column(name = "UserNote_isTrace")
	private boolean isTrace;

	@Column(name = "UserNote_isArchive")
	private boolean isArchive;

	@Column(name = "UserNote_isPinned")
	private boolean isPinned;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNoteDescription() {
		return noteDescription;
	}

	public void setNoteDescription(String noteDescription) {
		this.noteDescription = noteDescription;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public LocalDateTime getNoteCreateTime() {
		return noteCreateTime;
	}

	public void setNoteCreateTime(LocalDateTime noteCreateTime) {
		this.noteCreateTime = noteCreateTime;
	}

	public LocalDateTime getNoteUpdateTime() {
		return noteUpdateTime;
	}

	public void setNoteUpdateTime(LocalDateTime noteUpdateTime) {
		this.noteUpdateTime = noteUpdateTime;
	}

	public LocalDateTime getReminder() {
		return reminder;
	}

	public void setReminder(LocalDateTime reminder) {
		this.reminder = reminder;
	}

	public boolean isTrace() {
		return isTrace;
	}

	public void setTrace(boolean isTrace) {
		this.isTrace = isTrace;
	}

	public boolean isArchive() {
		return isArchive;
	}

	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public List<UserNoteLabel> getLabel() {
		return label;
	}

	public void setLabel(List<UserNoteLabel> label) {
		this.label = label;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "UserNote_Id")
	private List<UserNoteLabel> label = new ArrayList<UserNoteLabel>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Column(nullable = false)
	@JoinColumn(name = "UserNote_Id")
	private List<UserDetails> notesList = new ArrayList<UserDetails>();

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<UserDetails> collabratorUserList;

	public List<UserDetails> getCollabratorUserList() {
		return collabratorUserList;
	}

	public void setCollabratorUserList(List<UserDetails> collabratorUserList) {
		this.collabratorUserList = collabratorUserList;
	}

	public List<UserDetails> getNotesList() {
		return notesList;
	}

	public void setNotesList(List<UserDetails> notesList) {
		this.notesList = notesList;
	}
}
