package similartweets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


public class Vector {
    final String SPLIT = " |\\.|\\,|\\?|\\!|\\:|\\...|\\)|\\(|\\;|\\]|\\[|\\/|\\-|\\+";
    
    private final String TWEET;
    private String[] TWEET_WORDS;
    private ArrayList<String> vectorTerms;
    private double[] idfArray;
    private HashMap<String, Double> WORD_WEIGHT;
    
    public Vector(String tweet, ArrayList<String> terms, double[] idfArray) {
        this.TWEET = tweet;
        this.TWEET_WORDS = getWordsFromTweet(tweet);
        this.vectorTerms = terms;
        this.idfArray = idfArray;
        WORD_WEIGHT = fillWeightList();
    }

    public HashMap<String, Double> getWeight() {
        return this.WORD_WEIGHT;
    }
    
    private HashMap<String, Double> fillWeightList() {
        HashMap<String, Double> word_weight = new HashMap<>();
        
        for (int cur_term = 0; cur_term < vectorTerms.size(); cur_term++) {
            String term = vectorTerms.get(cur_term);
            
            int cur_tf = Collections.frequency(Arrays.asList(TWEET_WORDS), term);
            
            double weight = Math.log10(1 + cur_tf) * idfArray[cur_term];
            
            if (weight != 0.0) {
                word_weight.put(term, weight);
            }
        }
        
        return word_weight;
    }
    
    private String[] getWordsFromTweet(String tweet) {
        return tweet.split(SPLIT);
    }
    
    
     
/*   alternative method for Collections.frequency().
 *   
 *   for (String current_word : TWEET_WORDS) {
 *       if(term.equals(current_word)){
 *           cur_tf++;
 *       }
 *   }
 */  
}