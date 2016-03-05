package com.google.glassware;

/**
 * Created by blondieymollo on 11/6/14.
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.MenuValue;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.api.services.mirror.model.MenuItem;
import java.io.*;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;


public class RestServlet extends HttpServlet{

    private static final Logger LOG = Logger.getLogger(RestServlet.class.getSimpleName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException{

        Database db = new Database();
        ArrayList<String[]> temp = db.readQueue();

        for(int i = 0; i< temp.size(); i++){

            String[] work = temp.get(i);

            Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(work[1]);

            TimelineItem timelineItem = new TimelineItem();
            timelineItem.setHtml(work[0]);

            List<MenuItem> menuItemList = new ArrayList<MenuItem>();
            menuItemList.add(new MenuItem().setAction("SHARE"));

            boolean canTweet = db.usrCanTweet(work[1]);
            boolean isLinked = db.usrHasTwitterLinked(work[1]);

            //Check if user can tweet and if he/she has a linked twitter account
            if(canTweet && isLinked){

                List<MenuValue> menuValues = new ArrayList<MenuValue>();
                menuValues.add(
                        new MenuValue()
                                .setState("DEFAULT")
                                .setDisplayName("Tweet")
                                .setIconUrl("https://dl.dropboxusercontent.com/u/6410085/icon_twitter.png"));
                menuValues.add(new MenuValue()
                                .setState("PENDING")
                                .setDisplayName("Tweeting"));
                menuValues.add(
                        new MenuValue()
                                .setState("CONFIRMED")
                                .setDisplayName("Tweeted")
                                .setIconUrl("https://dl.dropboxusercontent.com/u/6410085/ic_done_50.png")
                );

                menuItemList.add( new MenuItem()
                        .setAction("CUSTOM")
                        .setId(work[2])
                        .setPayload("yo")
                        .setValues(menuValues)
                );
            }

            menuItemList.add(new MenuItem().setAction("DELETE"));
            timelineItem.setMenuItems(menuItemList);
            timelineItem.setNotification(new NotificationConfig().setLevel("DEFAULT"));

            try{
                MirrorClient.insertTimelineItem(credential, timelineItem);
                db.insertPushedTimelineItem(work[2]);
                db.updatequeueItem(work[2]);
            }
            catch(Exception e){
                e.printStackTrace(); }
        }

    }

}
