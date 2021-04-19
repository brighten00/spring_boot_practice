package com.wei.spring_boot_practice;

import org.json.JSONObject;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//might need take reference on https://www.baeldung.com/spring-boot-testing

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SpringBootPracticeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testCreateProduct() throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type","application/json");
		JSONObject request = new JSONObject();
		request.put("name", "Harry Potter");
		request.put("price", 450);

		RequestBuilder requestBuilder =
				MockMvcRequestBuilders.post("/products").headers(httpHeaders).content(request.toString());
//reference : https://medium.com/chikuwa-tech-study/spring-boot-%E7%AC%AC9%E8%AA%B2-%E4%BD%BF%E7%94%A8mockmvc%E8%87%AA%E5%8B%95%E5%8C%96%E6%B8%AC%E8%A9%A6-%E4%B8%80-3e3d031f8d68
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").hasJsonPath())
				.andExpect(jsonPath("$.name").value(request.getString("name")))
				.andExpect(jsonPath("$.price").value(request.getInt("price")))
				.andExpect(header().exists("Location"))
				.andExpect(header().string("Content-Type", "application/json"));
	}


}
