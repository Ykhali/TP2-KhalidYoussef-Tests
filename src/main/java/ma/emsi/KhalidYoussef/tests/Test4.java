package ma.emsi.KhalidYoussef.tests;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.time.Duration;

public class Test4 {
    // Assistant conversationnel
    interface Assistant {
        // Prend un message de l'utilisateur et retourne une réponse du LLM.
        String chat(String userMessage);
    }
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

        EmbeddingModel embeddingModel = GoogleAiEmbeddingModel.builder()
                .apiKey(Key)
                .modelName("text-embedding-004")
                .taskType(GoogleAiEmbeddingModel.TaskType.RETRIEVAL_DOCUMENT)
                .outputDimensionality(300)
                .timeout(Duration.ofSeconds(10))
                .build();

        ChatMemory memoire =
                MessageWindowChatMemory.withMaxMessages(10);
        String myDoc = "infos.txt";
        System.out.println("📄 Chargement du document : " + myDoc);
        Document document;
        try {
            document = FileSystemDocumentLoader.loadDocument(myDoc);
            System.out.println("Document chargé avec succès!");
            System.out.println("Nombre de caractères : " + document.text().length());
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


        String question = "Pierre appelle son chat. Qu'est-ce qu'il pourrait dire ?";

        String reponse = assistant.chat(question);
        System.out.println(reponse);
    }
}
