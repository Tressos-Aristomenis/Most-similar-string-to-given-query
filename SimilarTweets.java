package similartweets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JOptionPane;

public class SimilarTweets {
    private static ArrayList<String> tweetList;
    private static ArrayList<String> termList;
    private static double[] idfArray;
    final static String SPLIT = " |\\.|\\,|\\?|\\!|\\:|\\...|\\)|\\(|\\;|\\]|\\[|\\/|\\-|\\+";
    
    public static void main(String[] args) {
        
        
        final String filename = "2K-Tweets.txt";
        tweetList = new ArrayList<>();
        termList = new ArrayList<>();
        
        readTweetsFromFile(filename);
        idfArray = getIdfList();
        
        String answer = JOptionPane.showInputDialog(null, "Type 'random' to generate random tweet as query.\n\nAnything else you type will be your query: ");
        long startTime = System.nanoTime();
        
        if (answer != null) {
            String query;
            
            if (answer.equals("random")) {
                int random = new Random().nextInt(2000);
                query = tweetList.get(random);
            }
            else {
                query = answer;
            }
            
            String topTweet = getMostSimilarTweet(query);
            
            JOptionPane.showMessageDialog(null, "QUERY: " + query + "\n\n" + (topTweet != null ? "TOP TWEET: "+ topTweet : "No result found matching your query!"));
            
            long endTime   = System.nanoTime();
            long totalTime = endTime - startTime;
            
            System.out.println("Compilation time: " + totalTime / 1000000 + " ms");
        }
        
        
    }
    
    
    private static String getMostSimilarTweet(String queryString) {
        Vector query = new Vector(queryString, termList, idfArray);
        String topTweet = null;
        double maxCos = 0;
        
        for (String tweet : tweetList) {
            Vector currentTweet = new Vector(tweet, termList, idfArray);
            double cos = cosine(query, currentTweet);
            
            
            if (cos > maxCos) {
                maxCos = cos;
                topTweet = tweet;
            }
        }
        
        return topTweet;
    }
    
    private static double cosine(Vector query, Vector tweet) {
        HashMap<String, Double> queryWeight = query.getWeight();
        HashMap<String, Double> tweetWeight = tweet.getWeight();
        
        double cos = 0;
        
        for (String queryWord : queryWeight.keySet()) {
            if (tweetWeight.containsKey(queryWord))
                cos += queryWeight.get(queryWord) * tweetWeight.get(queryWord);
        }
        
        return cos;
    }
    
    private static double[] getIdfList() {
        double[] idfArray = new double[termList.size()];
        
        for (int curr_word = 0; curr_word < termList.size(); curr_word++) {
            int cur_df = 0;
            for (String tweet : tweetList) {
                if (tweet.contains(termList.get(curr_word))) {
                    cur_df++;
                }
            }
            
            idfArray[curr_word] = Math.log10(tweetList.size() / cur_df);
        }
        
        return idfArray;
    }
    
    private static void readTweetsFromFile(String filename) {
        java.io.BufferedReader br = null;
        
        try {
            br = new java.io.BufferedReader(new java.io.FileReader(filename));
            String line = "";
            
            while((line = br.readLine()) != null) {
            	tweetList.add(line);
                String[] tweetWords = getWordsFromTweet(line);
                
                for (String word : tweetWords) {
                    if (!termList.contains(word)) {
                        termList.add(word);
                    }
                }
            }
            
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
            	if (br != null) br.close();
            }
            catch (java.io.IOException ex) {
            	ex.printStackTrace();
            }
        }
    }
    
    private static String[] getWordsFromTweet(String tweet) {
        return tweet.split(SPLIT);
    }
}