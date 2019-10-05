package com.bridgelabz.fundonotes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NotNull
@Entity
@Table(name = "User_Note_Label")

public class UserNoteLabel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "Label_Id")
	private Long labelId;
	@Column(nullable = false)
	private Long nodeId;
	@Column(nullable = false)
	private String labelName;

	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}

	public Long getNodeId() {
		return nodeId;
	}

	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

}
