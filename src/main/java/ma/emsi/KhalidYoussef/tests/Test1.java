package ma.emsi.KhalidYoussef.tests;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;

public class Test1 {

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
        String question = "Bonjour, peux-tu Résumer l'histoire du Maroc en 5 points clés ?";
        System.out.println("Question : " + question);
        String reponse = model.chat(question);
        System.out.println("Réponse: ");
        System.out.println(reponse);
    }
}