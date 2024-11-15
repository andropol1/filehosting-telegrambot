package ru.andropol1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.andropol1.service.UpdateService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(WebHookController.class)
class WebHookControllerTest {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	UpdateService updateService;
	@Test
	void onUpdateReceivedSuccess() throws Exception {
		Update update = new Update();
		update.setUpdateId(1);
		String updateJson = new ObjectMapper().writeValueAsString(update);
		mockMvc.perform(post("/callback/update")
				.contentType("application/json")
				.content(updateJson))
				.andExpect(status().isOk());
		verify(updateService, times(1)).processUpdate(any(Update.class));
	}

}