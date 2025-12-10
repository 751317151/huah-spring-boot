package com.huah.LangChain4j;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @Author BlackStar
 * @Date 2025-12-04 16:23:59
 */
public class LangChain4jTest {
    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();

        String answer = model.chat("你是谁");
        System.out.println(answer); // Hello World

        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("content-type", "text/event-stream;charset=utf-8");
        StreamingChatModel streamingChatModel = OpenAiStreamingChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .customHeaders(customHeaders)
                .modelName("gpt-4o-mini")
                .logRequests(true)
                .logResponses(true)
                .build();
        streamingChatModel.chat("你是谁 ", new StreamingChatResponseHandler() {

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                System.out.println("onCompleteResponse: " + completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
        });
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String question = scanner.nextLine();
            String answer1 = model.chat(question);
            System.out.println(answer1);
        }
    }
}
