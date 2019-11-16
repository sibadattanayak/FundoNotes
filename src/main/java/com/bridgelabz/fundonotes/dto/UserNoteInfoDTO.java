package com.bridgelabz.fundonotes.dto;

import java.time.LocalDateTime;

public class UserNoteInfoDTO {

	private String noteDescription;
	private String noteTitle;
	private LocalDateTime reminder;
	private boolean isTrace;
	private boolean isArcive;
	private boolean isPinned;
	private String color;
	
	

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteDescription() {
		return noteDescription;
	}

	public void setNoteDescription(String noteDescription) {
		this.noteDescription = noteDescription;
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

	public boolean isArcive() {
		return isArcive;
	}

	public void setArcive(boolean isArcive) {
		this.isArcive = isArcive;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}
	
	
	public UserNoteInfoDTO() {
		
	}
	
	public UserNoteInfoDTO(String noteDescription, String noteTitle, LocalDateTime reminder, boolean isTrace,
			boolean isArcive, boolean isPinned, String color) {
		
		this.noteDescription = noteDescription;
		this.noteTitle = noteTitle;
		this.reminder = reminder;
		this.isTrace = isTrace;
		this.isArcive = isArcive;
		this.isPinned = isPinned;
		this.color = color;
	}

}
