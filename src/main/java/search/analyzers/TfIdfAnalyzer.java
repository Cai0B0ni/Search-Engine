package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;
import sun.nio.cs.ext.MacThai;

import java.net.URI;
import java.util.HashSet;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.


    /**
     * @param webpages  A set of all webpages we have parsed. Must be non-null and
     *                  must not contain nulls.
     */
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> idfScore = new ChainedHashDictionary<>();
        IDictionary<String, Double> documentCount = new ChainedHashDictionary<>();

        for (Webpage webpage : pages) {
            IList<String> webWords = webpage.getWords();
            ISet<String> uniqueWords = new ChainedHashSet<>();

            for (String word : webWords) {
                if (!uniqueWords.contains(word)){
                    uniqueWords.add(word);

                    if (documentCount.containsKey(word)){
                        documentCount.put(word, documentCount.get(word) + 1);

                    }else {
                        documentCount.put(word, 1.0);
                    }
                }
            }
        }

        for (KVPair<String, Double> pair : documentCount) {
            double docFreq = pages.size() / pair.getValue();


            idfScore.put(pair.getKey(), Math.log(docFreq));
        }

        return idfScore;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {

        IDictionary<String, Double> frequency = new ChainedHashDictionary<>();
        IDictionary<String, Double> wordCount = new ChainedHashDictionary<>();
        for (String word : words){
            //System.out.println(word);

            if (wordCount.containsKey(word)){
                wordCount.put(word, wordCount.get(word) + 1);
            } else {
                wordCount.put(word, 1.0);
            }
        }

        for (KVPair<String, Double> pair : wordCount){
            double freq = pair.getValue() / words.size();
            //System.out.println("TD" + pair.getKey() + ": " + pair.getValue());

            frequency.put(pair.getKey(), freq);
        }
        //System.out.println("end");

        return frequency;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> tfIdfVector = new ChainedHashDictionary<>();
        for (Webpage webpage : pages){
            IDictionary<String, Double> tfScores = computeTfScores(webpage.getWords());
            IDictionary<String, Double> tfIdfScore = new ChainedHashDictionary<>();

            /*ISet<String> setWords = new ChainedHashSet<>();
            for(String word :words){
                setWords.add(word);
            }*/
            for (KVPair<String, Double> word: tfScores){
                double score = 0.0;

                if (idfScores.containsKey(word.getKey())){
                    score = word.getValue() * idfScores.get(word.getKey());

                }

                tfIdfScore.put(word.getKey(), score);

            }
            tfIdfVector.put(webpage.getUri(), tfIdfScore);

        }
        return tfIdfVector;
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.

        IDictionary<String, Double> queryTf = computeTfScores(query);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();
        IDictionary<String, Double> documentVector  = this.documentTfIdfVectors.get(pageUri);

        double numerator  = 0.0;

        for (String word : query) {
            double docWordScore  = 0.0;
            if (idfScores.containsKey(word)) {
                docWordScore  = this.idfScores.get(word);
            }
            double queryWordScore = docWordScore  * queryTf.get(word);
            if (documentVector .containsKey(word)) {
                numerator  += queryWordScore * documentVector .get(word);
            }

            queryVector.put(word, queryWordScore);
        }

        double denominator = norm(queryVector) * norm(documentVector);
        if (denominator != 0.0) {
            return numerator  / denominator;
        } else {
            return 0.0;
        }
    }

    private double norm(IDictionary<String, Double> vector){
        double output = 0.0;
        // need to get key,value pair
        //extract key only to then iterate over it
        for (KVPair<String, Double> pair : vector){
            double score = pair.getValue();
            //System.out.println(pair.getKey() + " " + score);
            output += (score * score);

        }
        return Math.sqrt(output);
    }
}
