package com.bridgelabz.fundonotes.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NotNull
@Entity
@Table(name = "User_Notes")

public class UserNotes {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "User_Note_Id")
	private Long noteId;
	@Column(name = "User_Note_Data")
	private String noteData;
	@Column(name = "User_NoteCreation_Time")
	private LocalDateTime noteCreateTime;
	@Column(name = "User_NoteUpdation_Time")
	private LocalDateTime noteUpdateTime;
	@Column(name = "User_Note_isReminder")
	private boolean reminder;
	@Column(name = "User_Note_isTrace")
	private boolean trace;
	@Column(name = "User_Note_isArchive")
	private boolean archive;
	@Column(name = "User_Note_isPinned")
	private boolean isPinned;

	public Long getNoteId() {
		return noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
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

	public boolean isReminder() {
		return reminder;
	}

	public void setReminder(boolean reminder) {
		this.reminder = reminder;
	}

	public boolean isTrace() {
		return trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
}