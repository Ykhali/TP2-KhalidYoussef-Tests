package ma.emsi.KhalidYoussef.tests;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.CosineSimilarity;

import java.time.Duration;

public class Test3 {
    public static void main(String[] args) {
        String Key = System.getenv("GEMINI_KEY");

        EmbeddingModel model = GoogleAiEmbeddingModel.builder()
                .apiKey(Key)
                .modelName("gemini-embedding-001")
                .taskType(GoogleAiEmbeddingModel.TaskType.SEMANTIC_SIMILARITY)
                .outputDimensionality(300)
                .timeout(Duration.ofSeconds(2))
                .build();

        String phrase1 = "J'adore programmer en Java";
        String phrase2 = "Je trouve la programmation en Java passionnante";

        Response<Embedding> response1 = model.embed(phrase1);
        Response<Embedding> response2 = model.embed(phrase2);

        Embedding emb1 = response1.content();
        Embedding emb2 = response2.content();
        //Calcul de la similarité:
        double similarity = CosineSimilarity.between(emb1, emb2);
        System.out.println("Similarité cosinus : " + similarity);
    }
}
