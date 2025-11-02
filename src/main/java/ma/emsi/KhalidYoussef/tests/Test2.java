package ma.emsi.KhalidYoussef.tests;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.Map;

public class Test2 {
    public static void main(String[] args) {
        String Key = System.getenv("GEMINI_KEY");
        if (Key == null || Key.isBlank()) {
            System.err.println("Erreur : La variable d'environnement 'GEMINI_KEY' n'est pas définie.");
            return;
        }
        ChatModel model = GoogleAiGeminiChatModel
                .builder()
                .apiKey(Key) // Pour Fournir la clé API
                .modelName("gemini-2.5-flash") // Modèle à utiliser
                .temperature(0.7) // la créativité
                .build();
        String textToTranslate = "Bonjour, je suis un ingénieur en génie logiciel et en IA.";

        PromptTemplate template = PromptTemplate.from("""
                Traduit ce texte en anglais : {{texte}}""");

        Map<String, Object> variables = Map.of("texte", textToTranslate);
        Prompt prompt = template.apply(Map.of("texte", textToTranslate));

        String reponse = model.chat(prompt.text());
        System.out.println("Texte à traduire : " + textToTranslate);
        System.out.println("Traduction : " + reponse);
    }
}
