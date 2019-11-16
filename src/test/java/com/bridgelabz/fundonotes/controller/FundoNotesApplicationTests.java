//package com.bridgelabz.fundonotes.controller;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import com.bridgelabz.fundonotes.dto.UserDataValidation;
//import com.bridgelabz.fundonotes.dto.UserLoginValidation;
//import com.bridgelabz.fundonotes.dto.UserNoteLabelValidation;
//import com.bridgelabz.fundonotes.dto.UserNoteValidation;
//import com.bridgelabz.fundonotes.model.UserDetails;
//import com.bridgelabz.fundonotes.model.UserNotes;
//import com.bridgelabz.fundonotes.repository.UserDataRepository;
//import com.bridgelabz.fundonotes.repository.UserNoteLabelRepository;
//import com.bridgelabz.fundonotes.repository.UserNoteRepository;
//import com.bridgelabz.fundonotes.serviceimpl.UserServiceImplementation;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//class FundoNotesApplicationTests {
//	/*
//	 * @Autowired private UserServiceImplementation service;
//	 *
//	 * @MockBean private UserDataRepository repository;
//	 *
//	 * @Autowired private ModelMapper modelMapper;
//	 */
//
//	@MockBean
//	private ModelMapper modelMapper;
//	@MockBean
//	private UserServiceImplementation service;
//	@MockBean
//	private UserNoteRepository reposit;
//	@MockBean
//	private UserNoteLabelRepository repoit;
//
//	@MockBean
//	private UserDataRepository repository;
//	@Autowired
//	private MockMvc mockMvc;
//
//	private static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOjF9.pFrEwSR6gnu-uDiYoj7KLWG4wW03Lxc2eoxVhuTd5P2XrzIgPYsdWGzVB0xNzrgo-vnSX2L543jzu7Nvs-gLAw";
//
//	public static String asJsonString(final Object obj) {
//		try {
//			return new ObjectMapper().writeValueAsString(obj);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	/* ===================== */
//	/* API Testing */
//	/* ===================== */
//
//	@Test
//	public void LoginTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/login")
//				.content(asJsonString(new UserLoginValidation("shibadattanayak@gmail.com", "Siba@123")))
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	public void userForgotPasswordTest() throws Exception {
//		String emailId = "shibadattanayak@gmail.com";
//		mockMvc.perform(put("/fundonotes/forgotpassword", emailId).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON));
//// .andExpect(status().isOk());
//
//	}
//
//	@Test
//	public void getAllUserListTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/fundonotes/showallusers").accept(MediaType.APPLICATION_JSON))
//				.andDo(print())
//// .andExpect(status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
//	}
//
//	@Test
//	public void createNoteTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/createnote").header("token", token)
//				.content(asJsonString(
//						new UserNoteValidation("Note Description", "Note Title", null, false, false, false, "Red")))
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
//				.andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	public void deleteNoteTest() throws Exception {
//		mockMvc.perform(delete("/fundonotes/deletenote/{noteId}", 111).header("token", token));
//		// .andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	public void updateNoteTest() throws Exception {
//		mockMvc.perform(put("/fundonotes/updatenote/{noteId}", 112).header("token", token)
//				.content(asJsonString(new UserNotes(112L, "Red", "Note Description", "Note Title", null, null, null,
//						false, false, false, null, null)))
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//		// .andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	public void getAllNoteTest() throws Exception {
//		mockMvc.perform(get("/fundonotes/showallnotes").header("token", token).contentType(MediaType.APPLICATION_JSON))
//				.andDo(print());
//// .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
//	}
//
//	@Test
//	public void getAllLabelTest() throws Exception {
//		mockMvc.perform(get("/fundonotes/alllabel").header("token", token).contentType(MediaType.APPLICATION_JSON))
//				.andDo(print());
//// .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.*").exists());
//	}
//
//	@Test
//	public void updateLabelTest() throws Exception {
//		mockMvc.perform(put("/fundonotes/updatelabel").header("token", token).param("label2", "labelName")
//				.content(asJsonString(new UserNoteLabelValidation("label2"))).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON));
//// .andExpect(status().isOk());
//	}
//
////   @Test public void deleteLabelTest() throws Exception { ((ResultActions)
////  ((MockHttpServletRequestBuilder) mockMvc
////  .perform(delete("/fundonotes/deletelabel").header("token",
//// token))).param("label2", "labelName")).andExpect(status().isOk()); }
//
//	@Test
//	public void createLabelTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/createlabel").header("token", token).param("label3", "labelName")
//				.content(asJsonString(new UserNoteLabelValidation("label7"))).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON));
//		// .andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	public void ifArchiveTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/archive").header("token", token).param("noteId", "113L")
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//		// .andExpect(status().is2xxSuccessful());
//	}
//
//	@Test
//	public void ifPinnedTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/pinned").header("token", token).param("noteId", "113L")
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//// .andExpect(status().isOk());
//	}
//
//	@Test
//	public void ifTrashTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/trash").header("token", token).param("noteId", "113L")
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//// .andExpect(status().isOk());
//	}
//
//	@Test
//	public void addCollaboratorTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/addcollaborator").header("token", token)
//				.param("email", "shibadattanayak@gmail.com").param("noteId", "113L")
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//// .andExpect(status().isOk());
//	}
//
//	@Test
//	
//	public void removeCollaboratorTest() throws Exception {
//		mockMvc.perform(post("/fundonotes/removecollaborator").header("token", token)
//				.param("email", "shibadattanayak@gmail.com").param("noteId", "113L")
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//// .andExpect(status().isOk());
//	}
//
////	@Test
////	@Ignore
////	public void testUserRegistration() {
////		UserDataValidation userDto = new UserDataValidation("Sibadatta", "Nayak", "shibadattanayak@gmail.com",
////				"Siba@123");
////		UserDetails userDetails = modelMapper.map(userDto, UserDetails.class);
////		userDetails.setUserId(101L);
////		userDetails.setCreateTime(LocalDateTime.now());
////		userDetails.setUpdateTime(LocalDateTime.now());
////		userDetails.setIsVarified(false);
////		UserDetails dt = null;
////		Optional<UserDetails> user1 = Optional.ofNullable(dt);
////		when(repository.findByEmail(userDto.getEmail())).thenReturn(user1);
////		when(repository.save(userDetails)).thenReturn(userDetails);
////		assertTrue(userDetails.equals(service.userRegistration(userDto)));
////	}
////
////	@SuppressWarnings("unlikely-arg-type")
////	@Test
////	public void testUserLogin() {
////		UserLoginValidation loginDto = new UserLoginValidation("shibadattanayak@gmail.com", "Siba@123");
////		UserDetails userDetails = modelMapper.map(loginDto, UserDetails.class);
////		userDetails.setIsVarified(true);
////		UserDetails dt = null;
////		Optional<UserDetails> user1 = Optional.ofNullable(dt);
////		when(repository.findByEmail(loginDto.getUserEmail())).thenReturn(user1);
////		when(repository.save(userDetails)).thenReturn(userDetails);
////		assertTrue(userDetails.equals(service.userLogin(loginDto)));
////	}
//
//}