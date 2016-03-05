package com.google.glassware;

/**
 * Created by blondieymollo on 12/13/14.
 */

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class PostToTwitter {

    public PostToTwitter(){}

    private final static String CONSUMER_KEY = "";
    private final static String CONSUMER_KEY_SECRET = "";

    public void Tweet(String queue_id){

        try {
            Database db = new Database();

            String[] queueItem = db.readQueueItem(queue_id);

            String[] twitterSettings = db.readUserTwitterSettings(queueItem[0]);

            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);

            AccessToken oathAccessToken = new AccessToken(twitterSettings[0],
                    twitterSettings[1]);

            twitter.setOAuthAccessToken(oathAccessToken);
            String tweetStatus = db.gethldytweet(queueItem[1], queueItem[0]);

            if(!tweetStatus.isEmpty())
            twitter.updateStatus(tweetStatus);

        }catch(TwitterException e){
            e.printStackTrace();
        }

    }

    public void TweetFromSite(String hldy_id, String user_id){

        try {
            Database db = new Database();

            String[] twitterSettings = db.readUserTwitterSettings(user_id);

            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);

            AccessToken oathAccessToken = new AccessToken(twitterSettings[0],
                    twitterSettings[1]);

            twitter.setOAuthAccessToken(oathAccessToken);
            String tweetStatus = db.gethldytweet(hldy_id, user_id);

            if(!tweetStatus.isEmpty())
                twitter.updateStatus(tweetStatus);

        }catch(TwitterException e){
            e.printStackTrace();
        }

    }


}
