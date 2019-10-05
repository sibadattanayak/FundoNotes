package com.bridgelabz.fundonotes.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.bridgelabz.fundonotes.model.UserNotes;

public class UserNoteLabelValidation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "label_id")
	private Long labelId;

	@Column(nullable = false)
	private String labelName;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "label_id")
	private List<UserNotes> note = new ArrayList<>();

	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public List<UserNotes> getNote() {
		return note;
	}

	public void setNote(List<UserNotes> note) {
		this.note = note;
	}
}
