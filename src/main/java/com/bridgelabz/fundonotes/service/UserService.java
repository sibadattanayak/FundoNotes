package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.ForgotPasswordDTO;
import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.NoteDTO;
import com.bridgelabz.fundonotes.dto.NoteLabelDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;

public interface UserService {

	String userRegistration(ValidateUser validateUser);

	String userLogin(LoginDTO loginDto);

	String userForgotPassword(String email);

	String userResetPassword(ForgotPasswordDTO password, String token);

	List<ValidateUser> showUserList(ValidateUser validateUser);

	List<NoteDTO> showNoteList(NoteDTO validateNote);

	List<NoteLabelDTO> showNoteLabelList(NoteLabelDTO validateNoteLabel);

	List<ValidateUser> showNoteColabratorList(ValidateUser validateUser);

	String createNote(NoteDTO validateNote);

	String updateNote(NoteDTO validateNote);

	String deleteNote(NoteDTO validateNote);

}
