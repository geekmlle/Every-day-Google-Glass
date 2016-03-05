package com.google.glassware;

/**
 * Created by blondieymollo on 12/13/14.
 */



import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class CallbackTwitterServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        String userId = (String) request.getSession().getAttribute("id");

        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");

        String verifier = request.getParameter("oauth_verifier");
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            request.getSession().removeAttribute("requestToken");
            Database db = new Database();
            db.saveUsrTwitterSettings("0", userId, accessToken.getScreenName(),
                    accessToken.getToken(), accessToken.getTokenSecret());

        } catch (TwitterException e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/");

    }
}
