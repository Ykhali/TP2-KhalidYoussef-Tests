package ma.emsi.KhalidYoussef.tests;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.time.Duration;
import java.util.Scanner;

public class Test5 {
    interface Assistant {
        String chat(String userMessage);
    }
    public static void main(String[] args) {
        String Key = System.getenv("GEMINI_KEY");
        if (Key == null || Key.isBlank()) {
            System.err.println("Erreur : La variable d'environnement 'GEMINI_KEY' n'est pas définie.");
            return;
        }
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(Key) // Pour Fournir la clé API
                .modelName("gemini-2.5-flash") // Modèle à utiliser
                .temperature(0.2) // la créativité
                .build();

        ChatMemory memoire =
                MessageWindowChatMemory.withMaxMessages(20);

        String myDoc = "ML.pdf";
        System.out.println("📄 Chargement du document : " + myDoc);
        Document document;
        try {
            document = FileSystemDocumentLoader.loadDocument(myDoc);
            System.out.println("Document chargé avec succès!");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du document : " + e.getMessage());
            return;
        }
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        Assistant assistant =
                AiServices.builder(Assistant.class)
                        .chatModel(model)
                        .chatMemory(memoire)
                        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                        .build();

        conversationAvec(assistant);

    }
    private static void conversationAvec(Assistant assistant){
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
