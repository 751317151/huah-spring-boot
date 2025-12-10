package com.huah.huahspringbootspringai.aiAlibaba;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.util.function.BiFunction;

public class AgentExample {

    public static void main(String[] args) throws Exception {
        // 创建模型实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
            .apiKey(System.getenv("aliQwen-api"))
            .build();
        ChatModel chatModel = DashScopeChatModel.builder()
            .dashScopeApi(dashScopeApi)
            .build();
        String chatModelResponse = chatModel.call("今天天气怎么样");
        System.out.println(chatModelResponse);

        AgentExample agentExample = new AgentExample();
        ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", agentExample.new WeatherTool())
                .description("Get weather for a given city")
                .inputType(String.class)
                .build();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(weatherTool)
                .build();
        Prompt prompt = new Prompt("今天天气怎么样", chatOptions);
        ChatResponse chatModelToolResponse = chatModel.call(prompt);
        System.out.println(chatModelToolResponse.getResult().getOutput().getText());

        // 创建 agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .tools(weatherTool)
                .systemPrompt("You are a helpful assistant")
                .saver(new MemorySaver())
                .build();

        // 运行 agent
//        AssistantMessage response = agent.call("今天天气怎么样");
//        AssistantMessage response = agent.call("武汉天气怎么样");
        AssistantMessage response = agent.call("what is the weather in San Francisco");
        System.out.println(response.getText());
    }

    class WeatherTool implements BiFunction<String, ToolContext, String> {
        @Override
        public String apply(String city, ToolContext toolContext) {
            return "It's always sunny in " + city + "!";
        }
    }
}