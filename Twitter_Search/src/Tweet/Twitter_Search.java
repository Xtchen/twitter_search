package Tweet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author xiaotongchen
 */
public class Twitter_Search {

    public static void main(String[] args) throws MalformedURLException, IOException, JSONException {
        ArrayList<Tweet>  tweets;// to store the results
        Scanner cin = new Scanner(System.in); 
        String keyword="",temp;
        StringBuffer sb;
        InputStream is;
        BufferedReader br;
        URL url;
        JSONObject json;
        
        System.out.println("**********Tweets Searcher, Code by Xiaotong Chen****************");
        System.out.println("Hi, please type in the keyword. e.g.if you type in #test, ");
        System.out.println("it will display the 100 most recent urls of tweets matched #test");
        System.out.println("However, you should notice that there may not be enough tweets matched your hashtag.");
        System.out.println("The hashtag should be 1,000 characters maximum.");
        System.out.println("The API seams not support symbols well... e.g. a single ','");
        System.out.println("Type in \"exit\" to exit...");
        
        while((keyword=cin.nextLine())!=null){
            if(keyword.equals(""))continue;//empty
            if(keyword.equals("exit")) break;//exit
            if(keyword.length()>1000){//too long
                System.out.println("The hashtag should be 1,000 characters maximum.");
                continue;
            }
            tweets =new ArrayList<Tweet> ();// to store the results
            sb=new StringBuffer();//to store the stream
            keyword = URLEncoder.encode(keyword, "UTF-8");//encode the keyword
            try{
                url=new URL("http://search.twitter.com/search.json?q="+keyword+"&rpp=100");//generate the url 
                is=url.openStream();
            }catch(Exception e){
                System.out.println("Something wrong with the url...");//validate the url
                continue;
            }
            br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
            while((temp=br.readLine())!=null){
                sb.append(temp);//put the json into the stringbuffer
            }
            try{
                json=new JSONObject(sb.toString());
                //json=new JSONObject("{\"errors\":[{\"message\":\"Over capacity\",\"code\":130}]}");
                if(json.has("results")){//if works correctly
                    JSONArray results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        Tweet tweet=new Tweet();
                        JSONObject result=results.getJSONObject(i);
                        tweet.setUser(result.getString("from_user"));
                        tweet.setTweetid(result.getString("id_str"));
                        tweets.add(tweet);
                    }
                }
                else{//if works incorrectly
                    System.out.println("error! Maybe caused by the limit of Twitter API.");
                    continue;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            for(Tweet tweet:tweets){//display all the results
                System.out.println("https://twitter.com/"+tweet.getUser()+"/status/"+tweet.getTweetid());
            }
            System.out.println("Find "+tweets.size()+" tweets.");
            }
        System.out.println("Finish...");
    }
}
