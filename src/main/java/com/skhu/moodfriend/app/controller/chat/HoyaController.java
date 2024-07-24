package com.skhu.moodfriend.app.controller.chat;

import com.skhu.moodfriend.app.dto.chat.reqDto.ChatGPTReqDto;
import com.skhu.moodfriend.app.dto.chat.resDto.ChatGPTResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/bot")
public class HoyaController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt") String prompt) {
        ChatGPTReqDto reqDto = new ChatGPTReqDto(model, prompt);
        ChatGPTResDto resDto = template.postForObject(apiURL, reqDto, ChatGPTResDto.class);
        return resDto.choices().get(0).message().content();
    }
}
