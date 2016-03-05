package com.google.glassware;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.mirror.model.Subscription;
import com.google.api.services.mirror.model.TimelineItem;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

public class MainServlet extends HttpServlet {

    private final class BatchCallback extends JsonBatchCallback<TimelineItem> {

        private int success = 0;
        private int failure = 0;

        @Override
        public void onSuccess(TimelineItem item, HttpHeaders headers) throws IOException {
            ++success;
        }

        @Override
        public void onFailure(GoogleJsonError error, HttpHeaders headers) throws IOException {
            ++failure;
            LOG.info("Failed to insert item: " + error.getMessage());
        }
    }

    private static final Logger LOG = Logger.getLogger(MainServlet.class.getSimpleName());
    public static final String CONTACT_ID = "com.google.glassware.contact.java-quick-start";
    public static final String CONTACT_NAME = "";
    private final static String CONSUMER_KEY = "";
    private final static String CONSUMER_KEY_SECRET = "";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String userId = AuthUtil.getUserId(req);
        String message = null;

        if (req.getParameter("operation").equals("saveSettings")) {

            int minute = Integer.parseInt(req.getParameter("selectMinute"));
            int hour = Integer.parseInt(req.getParameter("selectHour"));
            boolean dateCheckbox = req.getParameter("InputBirthdate") != null;
            boolean funnyHolidays_checkbox = req.getParameter("InputFunnyHolidays") != null;
            int month = Integer.parseInt(req.getParameter("bdayMonth"));
            int day = Integer.parseInt(req.getParameter("bdayDay"));

            Database db = new Database();
            int funnyHolidays = 0; //JUST NORMAL

            if(funnyHolidays_checkbox){
                funnyHolidays = 1; //FUNNY
            }

            if (dateCheckbox) {
                db.saveUsrSettings(userId, day, month, 1, hour, minute, funnyHolidays);
            }else{
                db.saveUsrSettings(userId, 0, 0, 1, hour, minute,funnyHolidays);
            }

            message = "Your settings have been saved.";

        } else if (req.getParameter("operation").equals("LoginToTwitter")) {

            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
            req.getSession().setAttribute("twitter", twitter);
            req.getSession().setAttribute("id", userId);

            try {
                StringBuffer callbackURL = req.getRequestURL();
                int index = callbackURL.lastIndexOf("/");
                callbackURL.replace(index, callbackURL.length(), "").append("/callback");
                RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
                req.getSession().setAttribute("requestToken", requestToken);
                res.sendRedirect(requestToken.getAuthenticationURL());
                return ;
            } catch (TwitterException e) {
                e.printStackTrace();
            }

        } else if (req.getParameter("operation").equals("TweetFromSite")) {

            String hldy_id = req.getParameter("hldy");

            PostToTwitter ptt = new PostToTwitter();
            try {
                //ptt.TweetFromSite(hldy_id, userId);
            }catch (Exception e){
                message = "Unable to send tweet.";
            }

            message = "Tweet Sent! "+hldy_id+" "+userId;

        } else if (req.getParameter("operation").equals("LogoutTwitter")) {

            Database db = new Database();
            db.deleteUsrTwitterSettings(userId);

            message = "You have unlinked Twitter.";

        } else if (req.getParameter("operation").equals("stopNotifications")) {

            Database db = new Database();
            db.saveUsrSettings(userId, -1, -1, 0, -1, -1, 0);
            db.callDequeue(userId);
            message = "You have stopped notifications.";

        } else if (req.getParameter("operation").equals("startNotifications")) {

            Database db = new Database();
            db.callEnqueue(userId);
            message = "You have started notifications.";

        } else if (req.getParameter("operation").equals("sendComment")) {

            String contact_name = req.getParameter("contact_name");
            String contact_email = req.getParameter("contact_email");
            String contact_message = req.getParameter("contact_message");

            Database db = new Database();
            db.saveComment(contact_name,contact_email,contact_message);
            message = "Thank you for your comment!";

        }
        else {
            String operation = req.getParameter("operation");
            LOG.warning("Unknown operation specified " + operation);
            message = "I don't know how to do that!!";
        }
        WebUtil.setFlash(req, message);
        res.sendRedirect(WebUtil.buildUrl(req, "/"));
    }

}