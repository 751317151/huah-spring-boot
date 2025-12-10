package com.huah.LangChain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        //2.构建OpenAiChatModel对象
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("API-KEY"))
                .modelName("qwen-plus")
                .logRequests(true)
                .logResponses(true)
                .build();

        //3.调用chat方法,交互
        String result = model.chat("东哥帅不帅?");
        System.out.println(result);
    }
}
