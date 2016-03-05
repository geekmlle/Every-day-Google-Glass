package com.google.glassware;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by blondieymollo on 11/2/14.
 */
public class Database {

    private String[] result;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/glass";
    static final String USER = "";
    static final String PASS = "";

    public  void insertUser(String accessToken, String refreshToken, long expirationTimeMillis,String userID) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            int id = get_USER_ID(userID);

            String sql;

            if (id>0){
                sql = "UPDATE usr SET accss_tok = '"+accessToken+"' , rfrsh_tok = '"+refreshToken+
                      "', expiratn_tm_mill = '"+expirationTimeMillis+
                      "' WHERE usr_id="+id;

            }else{
                System.out.println("User Insert");
                sql = "INSERT INTO usr(usr_nm, accss_tok, rfrsh_tok,expiratn_tm_mill, enqueued) " +
                        "VALUES ('" + userID + "', '" + accessToken + "', '" + refreshToken + "', '" + expirationTimeMillis + "',0)";
            }


            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public String[] readUserCredentials (String userID){
        Connection conn = null;
        Statement stmt = null;
        String sql = "";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            sql = "SELECT accss_tok, rfrsh_tok, expiratn_tm_mill FROM usr WHERE usr_nm='"+userID+"'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = new String[]{rs.getString("accss_tok"),
                                      rs.getString("rfrsh_tok"),
                                      rs.getString("expiratn_tm_mill")};

            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            System.out.println("Error 1 "+userID);
            System.out.println(sql);
            se.printStackTrace();
        }catch(Exception e){
            System.out.println("Error 2");
            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                System.out.println("Error 3");
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                System.out.println("Error 4");
                se.printStackTrace();
            }
        }

        return result;
    }

    public  void deleteNewUser(String userID) {
        Connection conn = null;
        Statement stmt = null;
        if (count(userID) == 0) {

            try {

                Class.forName("com.mysql.jdbc.Driver");

                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL, USER, PASS);

                stmt = conn.createStatement();

                String sql = "DELETE FROM usr_sttngs WHERE usr_id = (SELECT usr_id FROM usr WHERE usr_nm = '"+userID+"')";

                stmt.executeUpdate(sql);

                sql = "DELETE FROM usr WHERE usr_nm = '"+userID+"'";

                stmt.executeUpdate(sql);


                stmt.close();
                conn.close();
            } catch (SQLException se) {

                se.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            } finally {

                try {
                    if (stmt != null)
                        stmt.close();
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    }

    public int count (String user){
        Connection conn = null;
        Statement stmt = null;
        int result = 0;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query

            stmt = conn.createStatement();
            String sql;

            sql = "SELECT count(usr_nm) usr FROM usr where usr_nm='"+user+"'";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = rs.getInt("usr");
                break;
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;
    }

    public int usr_sttngs_exist(int user){
        Connection conn = null;
        Statement stmt = null;
        int result = 0;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query

            stmt = conn.createStatement();
            String sql;

            sql = "SELECT count(usr_id) usr FROM usr_sttngs where usr_id="+user;

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = rs.getInt("usr");
                break;
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;

    }

    public int get_USER_ID (String user){
        Connection conn = null;
        Statement stmt = null;

        int result = 0;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT usr_id usr FROM usr where usr_nm='"+user+"'";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = rs.getInt("usr");
                break;
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;
    }

    public void saveUsrSettings(String usr_nm, int brthday, int brthmth, int actv, int notf_hr, int notf_min, int funnyHolidays){

        Connection conn = null;
        Statement stmt = null;

        String sql = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            int id = get_USER_ID(usr_nm);

            if (usr_sttngs_exist(id)>0){
                if(brthday == -1 && brthmth == -1 && notf_hr == -1 && notf_min == -1){
                    sql = "UPDATE usr_sttngs SET actv = "+actv+" WHERE usr_id="+id;
                }else{
                    sql = "UPDATE usr_sttngs SET " +
                            " brthdy_day = "+brthday+" , brthdy_month = "+brthmth+", actv = "+actv+
                            ", notf_hr = "+notf_hr+", notf_min = " +notf_min+ ", funny_hldys = "+funnyHolidays+
                            " WHERE usr_id="+id;
                }
            }else{
                sql = "INSERT INTO usr_sttngs(usr_id, brthdy_day, brthdy_month, actv, notf_hr,notf_min,funny_hldys) " +
                        "VALUES ("+id+","+brthday+","+brthmth+","+actv+","+notf_hr+","+notf_min+","+funnyHolidays+")";
            }

            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public  void callEnqueue(String userNm) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "CALL enqueue('"+userNm+"')";

            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public  void callDequeue(String userNm) {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "CALL dequeue('"+userNm+"')";

            stmt = conn.createStatement();

            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public boolean isEnqueued (String user){
        Connection conn = null;
        Statement stmt = null;

        int result = 0;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT enqueued FROM usr where usr_nm='"+user+"'";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = rs.getInt("enqueued");
                break;
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        if(result == 0){
            return false;
        }
        else{
            return true;
        }

    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public ArrayList<String[]> readQueue (){

        Connection conn = null;
        Statement stmt = null;
        ArrayList<String[]> temp = new ArrayList<String[]>();

        Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        int currentDay = localCalendar.get(Calendar.DATE);
        int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        int year = localCalendar.get(Calendar.YEAR);
        int currentHour = localCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = localCalendar.get(Calendar.MINUTE);

        String minute="";
        String hour = "";
        String day = "";
        String month = "";

        if (isBetween(currentMinute, 0, 4)) {
            minute = "00";
        } else if (isBetween(currentMinute, 5, 9)) {
            minute = "05";
        } else if (isBetween(currentMinute, 10, 14)) {
            minute = "10";
        } else if (isBetween(currentMinute, 15, 19)) {
            minute = "15";
        } else if (isBetween(currentMinute, 20, 24)) {
            minute = "20";
        } else if (isBetween(currentMinute, 25, 29)) {
            minute = "25";
        } else if (isBetween(currentMinute, 30, 34)) {
            minute = "30";
        } else if (isBetween(currentMinute, 35, 39)) {
            minute = "35";
        } else if (isBetween(currentMinute, 40, 44)) {
            minute = "40";
        } else if (isBetween(currentMinute, 45, 49)) {
            minute = "45";
        } else if (isBetween(currentMinute, 50, 54)) {
            minute = "50";
        } else if (isBetween(currentMinute, 55, 59)) {
            minute = "55";
        }

        if (isBetween(currentHour, 0, 9)) {
            hour = "0"+currentHour;
        }else{
            hour = Integer.toString(currentHour);
        }

        if (isBetween(currentMonth, 0, 9)) {
            month = "0"+currentMonth;
        }else{
            month = Integer.toString(currentMonth);
        }

        if (isBetween(currentDay, 0, 9)) {
            day = "0"+currentDay;
        }else{
            day = Integer.toString(currentDay);
        }

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = "SELECT q.push_html push_html, u.usr_nm usr_nm, q.queue_id queue_id " +
                    "FROM queue q, usr u " +
                    "WHERE u.usr_id = q.usr_id AND " +
                    " q.sent = 0 AND "+
                    "q.snd_dt = '"+year+"-"+month+"-"+day+"' AND " +
                    "q.snd_tm = '"+hour+":"+minute+":00';";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                temp.add( new String[]{rs.getString("push_html"), //0
                        rs.getString("usr_nm"), //1
                        rs.getString("queue_id")} ); //2
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                se2.printStackTrace();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        return temp;
    }

    public String[] readUserSettings (String userID){

        Connection conn = null;
        Statement stmt = null;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();

            String sql = "SELECT s.usr_id, s.brthdy_day, s.brthdy_month, s.actv, s.notf_hr, s. notf_min, s.funny_hldys, u.canTweet " +
                    "FROM usr_sttngs s, usr u " +
                    "WHERE u.usr_id = s.usr_id AND u.usr_nm = '"+userID+"'";

            ResultSet rs = stmt.executeQuery(sql);


            while(rs.next()){

                result = new String[]{
                        rs.getString("usr_id"), //0
                        rs.getString("brthdy_day"), //1
                        rs.getString("brthdy_month"), //2
                        rs.getString("actv"), //3
                        rs.getString("notf_hr"), //4
                        rs.getString("notf_min"), //5
                        rs.getString("funny_hldys"), //6
                        rs.getString("canTweet") //7
                };

            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;
    }

    public String[] getTodaysHolidays (){

        Connection conn = null;
        Statement stmt = null;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();

            Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
            int currentDay = localCalendar.get(Calendar.DATE);
            int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
            int currentYear = localCalendar.get(Calendar.YEAR);

            String sql = "SELECT site_html html, hldy_id id FROM hldy " +
                    "WHERE dt_day = "+currentDay+" and dt_month = "+currentMonth+" and dt_year = "+currentYear;

            ResultSet rs = stmt.executeQuery(sql);


            while(rs.next()){

                result = new String[]{
                        rs.getString("html"), //0
                        rs.getString("id") //1
                };

            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;
    }

    public void insertPushedTimelineItem(String queue_id){
        Connection conn = null;
        Statement stmt = null;
        String sql = null;

        Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        int currentHour = localCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = localCalendar.get(Calendar.MINUTE);

        String minute;
        String hour;


        if (isBetween(currentHour, 0, 9)) {
            hour = "0"+currentHour;
        }else{
            hour = Integer.toString(currentHour);
        }

        if (isBetween(currentMinute, 0, 9)) {
            minute = "0"+currentMinute;
        }else{
            minute = Integer.toString(currentMinute);
        }


        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            sql = "INSERT INTO pushed(queue_id, dt_tm) " +
                    "VALUES ("+queue_id+", STR_TO_DATE('"+hour+":"+minute+"','%H:%i'))";

            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {}
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void saveComment(String contact_name,String contact_email,String contact_message){
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO usr_cmmnts(name,email,message) VALUES (?,?,?)");
            pstmt.setString(1, contact_name);
            pstmt.setString(2, contact_email);
            pstmt.setString(3, contact_message);

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {}
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void updatequeueItem(String queue_id){
        Connection conn = null;
        Statement stmt = null;
        String sql = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            sql = "UPDATE queue SET sent = 1 WHERE queue_id= " + queue_id;

            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {}
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void saveUsrTwitterSettings(String pin, String usr_nm, String twitter_handle, String accessToken, String accessTokenSecret){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "CALL sv_twttr_usr_sttigs('"+pin+"','"+usr_nm+"','"+twitter_handle
                    +"','"+accessToken+"','"+accessTokenSecret+"')";

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public String[] readUserTwitterSettings (String userID){
        Connection conn = null;
        Statement stmt = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = "SELECT t.svd_accss_tkn, t.svd_accss_tkn_scrt, t.scrn_nm " +
                    "FROM usr_twitter_sttngs t " +
                    "WHERE t.usr_id = "+userID;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                result = new String[]{
                        rs.getString("svd_accss_tkn"), //0
                        rs.getString("svd_accss_tkn_scrt"), //1
                        rs.getString("scrn_nm") //2
                };
            }
            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;
    }

    public String[] readQueueItem (String queue_id){
        Connection conn = null;
        Statement stmt = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = "SELECT q.usr_id, h.hldy_id " +
                    "FROM queue q, hldy h " +
                    "WHERE q.hldy_id = h.hldy_id AND " +
                    "q.queue_id = "+queue_id;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                result = new String[]{
                        rs.getString("usr_id"), //0
                        rs.getString("hldy_id")//1
                };
            }
            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;
    }

    public String gethldytweet (String hldy_id, String userId){
        Connection conn = null;
        Statement stmt = null;
        StringBuilder result = new StringBuilder("");
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;

            String month= "", day="", nm="";

            String [] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

            sql = "Select h.dt_month , h.dt_day , h.hldy_nm " +
                    "FROM hldy h WHERE " +
                    "dt_day > 0 AND dt_month > 0 AND hldy_id = "+hldy_id;

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                month = rs.getString("dt_month");
                day = rs.getString("dt_day");
                nm = rs.getString("hldy_nm");
            }

            if(!month.equals("0") && !day.equals("0")) {
                result.append("Did u know ")
                        .append(months[Integer.parseInt(month) - 1])
                        .append(" ")
                        .append(day)
                        .append(" is ")
                        .append(nm)
                        .append("? #everydayGlass");
            }else{
                String [] temp = readUserSettings(userId);
                result.append("Did u know ")
                        .append(months[Integer.parseInt( temp [2]) - 1])
                        .append(" ")
                        .append(temp [1])
                        .append(" is my #birthday? You better get me a #gift! #everydayGlass");
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        System.out.println(result.toString());
        return result.toString();
    }

    public void deleteUsrTwitterSettings(String usr_nm){
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "CALL del_twttr_usr_sttigs('"+usr_nm+"')";

            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }

    public String getUserTwitterHandle (String user){
        Connection conn = null;
        Statement stmt = null;

        String result = "";
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT t.scrn_nm twitter_handle " +
                    "FROM usr_twitter_sttngs t, usr u " +
                    "WHERE u.usr_id = t.usr_id " +
                    "AND u.usr_nm = '"+user+"';";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = rs.getString("twitter_handle");
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        return result;
    }

    public boolean usrCanTweet (String user){
        Connection conn = null;
        Statement stmt = null;

        int result = 0;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;

            sql = "SELECT canTweet FROM usr where usr_nm='"+user+"'";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = rs.getInt("canTweet");
                break;
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        if(result == 0){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean usrHasTwitterLinked (String user){
        Connection conn = null;
        Statement stmt = null;

        int result = 0;
        try{

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql;

            sql = "select count(t.usr_id) canTweet from usr_twitter_sttngs t, usr u where u.usr_id = t.usr_id and u.usr_nm ='"+user+"'";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                result = rs.getInt("canTweet");
            }

            rs.close();
            stmt.close();
            conn.close();

        }catch(SQLException se){

            se.printStackTrace();
        }catch(Exception e){

            e.printStackTrace();
        }finally{

            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

        if(result < 1){
            return false;
        }
        return true;

    }

}
