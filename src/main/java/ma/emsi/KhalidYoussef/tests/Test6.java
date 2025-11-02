package ma.emsi.KhalidYoussef.tests;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import ma.emsi.KhalidYoussef.tools.meteo.MeteoTool;
import ma.emsi.KhalidYoussef.interfaces.AssitantMeteo;

import java.util.Scanner;


public class Test6 {
    public static void main(String[] args) {
        String Key = System.getenv("GEMINI_KEY");
        if (Key == null || Key.isBlank()) {
            System.err.println("Erreur : La variable d'environnement 'GEMINI_KEY' n'est pas définie.");
            return;
        }
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(Key)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .build();
        AssitantMeteo assistant = AiServices.builder(AssitantMeteo.class)
                .chatModel(model)
                .tools(new MeteoTool())                             // <-- outil météo branché
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))  // <-- contexte multi-tours
                .build();

        conversationAvec(assistant);
    }
    private static void conversationAvec(AssitantMeteo assistant){
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("==================================================");
                System.out.println("Posez votre question : ");
                String question = scanner.nextLine();
                if (question.isBlank()) {
                    continue;
                }
                System.out.println("==================================================");
                if ("fin".equalsIgnoreCase(question)) {
                    break;
                }
                String reponse = assistant.chat(question);
                System.out.println("Assistant : " + reponse);
                System.out.println("==================================================");
            }
        }
    }
}
