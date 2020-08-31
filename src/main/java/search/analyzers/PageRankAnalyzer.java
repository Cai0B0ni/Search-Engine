package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;


    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed. Must be non-null and must not contain
     *                  nulls.
     * @param decay     Represents the "decay" factor when computing page rank (see spec). Must be a
     *                  number between 0 and 1, inclusive.
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating. Must be a non-negative number.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges. Must be a non-negative number. (A limit of 0 should
     *                  simply return the initial page rank values from 'computePageRank'.)
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // TODO: uncomment these lines when you're ready to begin working on this class

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> relationship = new ChainedHashDictionary<>();
        ISet<URI> selfContainLink = new ChainedHashSet<>();
        for (Webpage webpage : webpages){ //store only self-contained links
            selfContainLink.add(webpage.getUri());
        }

        for (Webpage webpage : webpages){
            ISet<URI> edges = new ChainedHashSet<>(); //no duplicate edges
            for (URI link : webpage.getLinks()){
                // checks for links that are not in the webpages, and if link points to itself
                if (selfContainLink.contains(link) && !link.equals(webpage.getUri())){
                    edges.add(link);
                }
            }

            relationship.put(webpage.getUri(), edges);
        }
        return relationship;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {


        // Step 1: The initialize step should go here
        IDictionary<URI, Double> initializedRank = new ChainedHashDictionary<>();
        IDictionary<URI, Double> newValues = new ChainedHashDictionary<>();

        double n = 1.0 / graph.size();
        System.out.println(graph.size());
        for (KVPair<URI, ISet<URI>> pageBank : graph) {
            initializedRank.put(pageBank.getKey(), n);
            System.out.println(pageBank.getKey());
        }

        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here

            for (KVPair<URI, ISet<URI>> pair : graph) {
                newValues.put(pair.getKey(), 0.0);
            }

            //loop through the entire dictionary
            //if that key is not in the internal array
            //return the default value


            for (KVPair<URI, ISet<URI>> links : graph) {
                if (!links.getValue().isEmpty()) {

                    //make a double and do a running total, ecah time you come accross one of these += tp the double
                    ISet<URI> connectingLinks = links.getValue();
                    for (URI connectingLink : connectingLinks) {
                        //we dont have a get or default for chained hash dictionary
                        // newValues does not have that value yet
                        double updateNumber = initializedRank.get(links.getKey()) / links.getValue().size() * decay;
                        newValues.put(connectingLink, newValues.get(connectingLink) + updateNumber);
                    }


                } else {  //there are no links
                    // add old value * decay / size of the set to old value
                    // make a set with all the links for that node
// if we need to optimize, get rid of this code, keep a running total of stuff to be added after surfer
                    for (KVPair<URI, ISet<URI>> pair : graph) {
                        double updatedRank = initializedRank.get(links.getKey()) / graph.size() * decay;
                        newValues.put(pair.getKey(), newValues.get(pair.getKey()) + updatedRank);
                    }
                }
                //after we go through all the values, 1-decay/all pages
                double surfers = (1 - decay) / graph.size(); //add running total of double from above here
                newValues.put(links.getKey(), newValues.get(links.getKey()) + surfers);
            }

            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            boolean d = true;
            for (KVPair<URI, Double> pair : newValues) {

                double diff = initializedRank.get(pair.getKey()) - newValues.get(pair.getKey());
                double absV = Math.abs(diff);
                if (absV > epsilon) { // if any one thing is greater than, keep going
                    d = false;
                    break;
                }

            }
            if (!d) {
                for (KVPair<URI, Double> pair : newValues) {
                    newValues.put(pair.getKey(), 0.0);
                    initializedRank.put(pair.getKey(), pair.getValue());
                }
            } else {
                    return newValues;

            }

        }
        return initializedRank;

    }



    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
