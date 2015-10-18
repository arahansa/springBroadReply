package com.example.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.SpringBroadReplyApplication;
import com.example.domain.Reply;
import com.example.repository.ReplyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBroadReplyApplication.class)
@WebAppConfiguration
@Transactional
public class ReplyControllerTest {
	
	@Autowired
    private WebApplicationContext wac;
	@Autowired 
	private ObjectMapper objectMapper;
	@Autowired
	private ReplyRepository replyRepository;
   
    private MockMvc mockMvc;
    
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    
    @Test
	public void createReply() throws Exception {
    	Reply reply = new Reply();
    	reply.setAuthor("arahansa");
    	reply.setComment("Hello world");
    	
    	mockMvc.perform(post("/comment")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reply)))
    			.andDo(print())
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.author").value("arahansa"))
    			.andExpect(jsonPath("$.comment").value("Hello world"));
	}
    
    @Test
	public void getReplyandDelete() throws Exception {
		Reply saveSampleReply = saveSampleReply();
		mockMvc.perform(get("/comment/"+saveSampleReply.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.id").value(saveSampleReply.getId().intValue()))
    			.andExpect(jsonPath("$.author").value("arahansa"))
    			.andExpect(jsonPath("$.comment").value("Hello world"));
		
		mockMvc.perform(delete("/comment/"+saveSampleReply.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
	}
    
    @Test
	public void simpleUpdateTest() throws Exception {
    	Reply reply = new Reply();
    	reply.setAuthor("arahansa");
    	reply.setComment("niHao");
    	Reply saveSampleReply = saveSampleReply();
    	
    	mockMvc.perform(put("/comment/"+saveSampleReply.getId())
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(reply)))
    			.andDo(print())
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$.author").value("arahansa"))
    			.andExpect(jsonPath("$.comment").value("niHao"));
	}
    
    private Reply saveSampleReply(){
    	Reply reply = new Reply();
    	reply.setAuthor("arahansa");
    	reply.setComment("Hello world");
    	Reply saveReply = replyRepository.save(reply);
    	return saveReply;
    }



}
