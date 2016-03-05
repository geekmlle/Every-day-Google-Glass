/*
 * Copyright (C) 2013 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.glassware;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.mirror.Mirror;
import com.google.api.services.mirror.model.Notification;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.MenuValue;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.api.services.mirror.model.MenuItem;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles the notifications sent back from subscriptions
 */

public class NotifyServlet extends HttpServlet {

  private static final Logger LOG = Logger.getLogger(NotifyServlet.class.getSimpleName());

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

        LOG.info("Received subscription notification");

        // Read the string & respond with OK and status 200 in a timely fashion to prevent redelivery

        JsonFactory jsonFactory = new JacksonFactory();
        Notification notification = jsonFactory.fromInputStream(request.getInputStream(), Notification.class);

        System.out.println(notification.toString());

        response.setContentType("text/html");
        Writer writer = response.getWriter();
        writer.append("OK");
        writer.close();

      if (notification.getCollection().equals("timeline"))  {

          String Payload = notification.getUserActions().get(0).getPayload();

          if (notification.getUserActions().get(0).getType().equals("CUSTOM")
                  && notification.getVerifyToken().equals("everydaygoogleglasstweettimelineitem")) {

              new PostToTwitter().Tweet(Payload);

              //Update the timelineItem so it never tweets again to prevent double tweets

              String userId = notification.getUserToken();
              Credential credential = AuthUtil.getCredential(userId);
              Mirror mirrorClient = MirrorClient.getMirror(credential);

              TimelineItem timelineItem = mirrorClient.timeline().get(notification.getItemId()).execute();

              TimelineItem updatedtimelineItem = new TimelineItem();
              updatedtimelineItem.setHtml(timelineItem.getHtml());

              List<MenuItem> menuItemList = new ArrayList<MenuItem>();
              menuItemList.add(new MenuItem().setAction("SHARE"));
              menuItemList.add(new MenuItem().setAction("DELETE"));
              updatedtimelineItem.setMenuItems(menuItemList);

              mirrorClient.timeline().update(timelineItem.getId(), updatedtimelineItem).execute();

          } else {
            LOG.warning("Not an official glass POST request.");
          }
        }
      else{
           LOG.info("I have no idea what I just tried to handle.");
       }
      }

}
