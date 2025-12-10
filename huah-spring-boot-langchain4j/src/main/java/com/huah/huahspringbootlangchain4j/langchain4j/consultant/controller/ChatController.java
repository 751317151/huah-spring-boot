package com.huah.huahspringbootlangchain4j.langchain4j.consultant.controller;

import com.huah.huahspringbootlangchain4j.langchain4j.consultant.aiservice.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {
    @Autowired
    private ConsultantService consultantService;

    @RequestMapping(value = "/chat")
    public Flux<String> chat(@RequestParam("memoryId") String memoryId, @RequestParam("message") String message){
        Flux<String> result = consultantService.chat(memoryId,message);
        return result;
    }

    /*@RequestMapping("/chat")
    public String chat(String message){
        String result = consultantService.chat(message);
        return result;
    }*/

    /*@Autowired
    private OpenAiChatModel model;
    @RequestMapping("/chat")
    public String chat(String message){//浏览器传递的用户问题
        String result = model.chat(message);
        return result;
    }*/
}
