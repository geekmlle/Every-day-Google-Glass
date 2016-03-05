<%@ page import="com.google.api.client.auth.oauth2.Credential" %>
<%@ page import="com.google.api.services.mirror.model.Contact" %>
<%@ page import="com.google.glassware.MirrorClient" %>
<%@ page import="com.google.glassware.Database" %>
<%@ page import="com.google.glassware.WebUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.api.services.mirror.model.TimelineItem" %>
<%@ page import="com.google.api.services.mirror.model.Subscription" %>
<%@ page import="com.google.api.services.mirror.model.Attachment" %>
<%@ page import="com.google.glassware.MainServlet" %>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>


<%

  String userId = com.google.glassware.AuthUtil.getUserId(request);

  String appBaseUrl = WebUtil.buildUrl(request, "/glass/");

  Credential credential = com.google.glassware.AuthUtil.getCredential(userId);

  Contact contact = MirrorClient.getContact(credential, MainServlet.CONTACT_ID);

  List<TimelineItem> timelineItems = MirrorClient.listItems(credential, 3L).getItems();

  List<Subscription> subscriptions = MirrorClient.listSubscriptions(credential).getItems();
  boolean timelineSubscriptionExists = false;
  boolean locationSubscriptionExists = false;

%>

<%
	String [] hours = {"12 AM", "1 AM", "2 AM", "3 AM" , "4 AM", "5 AM", "6 AM",
	   "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM" ,
	   "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM"};

	String [] minutes = {"00","05","10", "15","20","25","30", "35","40", "45","50"};
	String [] months = {"January", "February", "March", "April", "May", "June",
	   "July", "August", "September", "October", "November", "December"};


	Database db = new Database();
	String [] result = db.readUserSettings(userId);
	String [] todaysHolidays = db.getTodaysHolidays();

%>

<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]><html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->

    <head>
        <meta charset="utf-8">
        <!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->
        <!--
        Zoom Template
        http://www.templatemo.com/preview/templatemo_414_zoom
        -->
        <title>Everyday</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">

        <!-- Google Web Font Embed -->
        <link href='http://fonts.googleapis.com/css?family=Raleway:400,600,500,300,700' rel='stylesheet' type='text/css'>

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/font-awesome.min.css">
        <link rel="stylesheet" href="css/templatemo_main.css">
        <link rel="stylesheet" href="css/dynamicGlassCard.css">
        <script src="https://apis.google.com/_/scs/apps-static/_/js/k=oz.gapi.en.KIVJsaxZpFc.O/m=client/rt=j/sv=1/d=1/ed=1/am=AQ/rs=AGLTcCP6KsgHW8D92qmhG2jp88qFj0OkPg/t=zcms/cb=gapi.loaded_0" async=""></script><script src="https://apis.google.com/js/client.js?onload=initAuth" gapi_processed="true"></script>
        <script type="text/javascript" src="assets/js/card_script.js"></script>

        <style>
            #note {
                position: absolute;
                z-index: 6001;
                top: 0;
                left: 0;
                right: 0;
                background: #FFFFFF;
                text-align: center;
                line-height: 2.5;
                overflow: hidden;
                -webkit-box-shadow: 0 0 5px black;
                -moz-box-shadow:    0 0 5px black;
                box-shadow:         0 0 5px black;
            }
            .cssanimations.csstransforms #note {
                -webkit-transform: translateY(-50px);
                -webkit-animation: slideDown 2.5s 1.0s 1 ease forwards;
                -moz-transform:    translateY(-50px);
                -moz-animation:    slideDown 2.5s 1.0s 1 ease forwards;
            }

            #close {
              position: absolute;
              right: 10px;
              top: 9px;
              text-indent: -9999px;
              background: url(images/close.png);
              height: 20px;
              width: 20px;
              cursor: pointer;
            }
            .cssanimations.csstransforms #close {
              display: none;
            }

            @-webkit-keyframes slideDown {
                0%, 100% { -webkit-transform: translateY(-50px); }
                10%, 90% { -webkit-transform: translateY(0px); }
            }
            @-moz-keyframes slideDown {
                0%, 100% { -moz-transform: translateY(-50px); }
                10%, 90% { -moz-transform: translateY(0px); }
            }
            #selectHour {
				color: black;
			}
			#selectMinute {
				color: black;
			}
			#bdayMonth {
				color: black;
			}
			#bdayDay {
				color: black;
			}
        </style>

    </head>
    <body>
        <div id="main-wrapper">
            <!--[if lt IE 7]>
                <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
            <![endif]-->

 		<% String flash = WebUtil.getClearFlash(request);
            	if (flash != null) { %>
                    <div id="note"><h5><%= StringEscapeUtils.escapeHtml4(flash)%></h5><a id="close">close</a></div>
        <% } %>

            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center templatemo-logo margin-top-20">
                <h1 class="templatemo-site-title">
                    <a href="#">Every<span class="pink">d</span>ay</a>
                </h1>
                <h5 class="templatemo-site-title">
                	for Google Glass
                </h5>
                <br />
                <h3 class="templatemo-site-title">
                	Giving you a reason to celebrate Every<span class="pink">d</span>ay
                </h3>
            </div>
            <div class="image-section">
                <div class="image-container">
                    <img src="images/zoom-bg-1.jpg" id="menu-img" class="main-img inactive" alt="Zoom">
                    <img src="images/zoom-bg-2.jpg" id="products-img" class="inactive" alt="Product">
                    <img src="images/zoom-bg-3.jpg" id="services-img"  class="inactive" alt="Services">
                    <img src="images/zoom-bg-4.jpg" id="about-img" class="inactive" alt="About">
                    <img src="images/zoom-bg-5.jpg" id="contact-img" class="inactive" alt="Contact">
                    <img src="images/zoom-bg-6.jpg" id="company-intro-img" class="main-img inactive" alt="Company Intro">
                    <img src="images/zoom-bg-7.jpg" id="testimonials-img" class="main-img inactive" alt="Testimonials">
                </div>
            </div>
            <div class="container">
                <div class="col-xs-12 col-sm-12 col-md-8 col-lg-8 col-md-offset-2 col-lg-offset-2 templatemo-content-wrapper">
                    <div class="templatemo-content">
                        <section id="menu-section" class="active">
                            <div class="row">

								<div class="col-sm-3 col-md-3 col-lg-3 margin-bottom-20">
                                    <a href="#services" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <i class="fa fa-pencil"></i>
                                            <h2>Settings</h2>
                                        </div>
                                    </a>
                                </div>
                                <div class="col-sm-3 col-md-3 col-lg-3 margin-bottom-20">
                                    <a href="#products" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <i class="fa fa-clock-o"></i>
                                            <h2>Start/Stop</h2>
                                        </div>
                                    </a>
                                </div>
                                <div class="col-sm-3 col-md-3 col-lg-3 margin-bottom-20">
                                    <a href="#about" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <i class="fa fa-question-circle"></i>
											<h2>What's today?</h2>
                                        </div>
                                    </a>
                                </div>
                                <div class="col-sm-3 col-md-3 col-lg-3 margin-bottom-20">
                                    <a href="#contact" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <i class="fa fa-envelope"></i>
                                            <h2>Contact</h2>
                                        </div>
                                    </a>
                                </div>

                               <div class="black-bg col-sm-12 col-md-12 col-lg-12">
                                    <a href="#company-intro" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <h2>What is Google Glass?</h2><img src="images/GOOGLE-GLASS-LOGO.png" alt="glass" draggable="false">
                                        </div>
                                    </a>
                                </div>
                             <br />
                             <br />

                            </div>

                        </section><!-- /.menu-section -->
                        <section id="products-section" class="inactive">
                            <div class="row">
                                <div class="black-bg col-sm-12 col-md-12 col-lg-12">
                                    <h2 class="text-center">Start or Stop the Service</h2>
                                    <div class="col-sm-12 col-md-12">
                                    <p>If you have already configured everything, click start.<br />If you are currently receiving the timeline cards, and don't want to receive them anymore, click stop.
									</p>
									</div>
                                    <center>
                                    <table cellpadding="10" cellspacing="10">
                                    	<tr>
                                    	<th>

										<% boolean enqueued = db.isEnqueued(userId);
										if(result == null){%>
<center>
<p>It looks like you have never used the service, please take a minute to go to the <a href="#services" class="change-section"><u>Settings</u></a> page before you can begin. </p></center>

										<%} else{
											if(!enqueued){
											%>
										<form action="<%= WebUtil.buildUrl(request, "/main") %>" method="post">
											<input type="hidden" name="operation" value="startNotifications">
											<button type="submit" class="btn btn-primary">Start</button>
										</form>

										<%} else{%>

										<form action="<%= WebUtil.buildUrl(request, "/main") %>" method="post">
											<input type="hidden" name="operation" value="stopNotifications">
											<button type="submit" class="btn btn-primary">Stop</button>
										</form>

										<%}}%>
										</th>
										</tr>
									</table>
                                      <br />
                                      <br />
                                    </center>
                                </div>
                            </div>
                            <div class="row margin-top-20">
                                <div class="black-bg col-sm-12 col-md-12 col-lg-12">
                                    <a href="#menu" class="change-section">
                                        <div class=" btn-menu">
                                            <h2>Back to menu</h2>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </section><!-- /.product-section -->
                        <section id="services-section" class="inactive">

                            <div class="row">
                                <div class="col-sm-12 col-md-12 col-lg-12">
                                    <div class="col-sm-12 col-md-12 col-lg-12 black-bg">

                                        <h2 class="text-center">Settings</h2>
										<p> The application will send you a timeline card if there is a holiday each day. So we don't send you a card at an inappropriate time, please tell us a good time to notify you.
										<br />
										Although optional, you can sign up to receive a birthday greeting. Click the "Birthday" checkbox, and tell us in what day and month you were born. Don't worry! We don't need to know the year ;)
										</p>

										<form action="<%= WebUtil.buildUrl(request, "/main") %>" method="post">
										<input type="hidden" name="operation" value="saveSettings">
				  						<center>

										<table cellpadding="10" cellspacing="10">
											<tr>
												<td>
													<p>Time: (EST UTC Offset: UTC -5:00)</p>
												</td>
												<td>
													<p class="select">
													<select id="selectHour" name="selectHour">
                                                    <% int i;
                                                    if(result!=null){
                                                    for (i = 0; i< 24; i++){
                                                    if(Integer.parseInt(result[4])==i){
                                                    %>
                                                    <option value="<%out.print(String.valueOf(i));%>"
                                                    selected="true"><%out.print(hours[i]);%> </option>
                                                    <%} else{ %>
                                                    <option value="<%out.print(String.valueOf(i));%>">
                                                    <%out.print(hours[i]);%></option>
                                                    <%}

                                                    }
                                                    } else{
                                                    	for(i = 0; i<24; i++){%>
                                                    	<option value="<%out.print(String.valueOf(i));%>">
                                                        <%out.print(hours[i]);%></option>
                                                    <%}}%>
                                                </select>

											    <select id="selectMinute" name="selectMinute">
                                                <%
                                                int j;
                                                if(result!=null){
                                                for(j = 0; j < 11; j++){
                                                if(result[5].equals(minutes[j])){%>
                                                    <option value="<%out.print(minutes[j]);%>" selected="true">
                                                        <%out.print(minutes[j]);%></option>
                                                <%} else {%>
                                                <option value="<%out.print(minutes[j]);%>">
                                                        <%out.print(minutes[j]);%></option>
                                                <%} }
                                                }else {
                                                	for(j = 0; j<11; j++){ %>
                                                	<option value="<%out.print(minutes[j]);%>">
                                                        <%out.print(minutes[j]);%></option>
                                                <%}
                                                }%>
                                            </select>
											</p>
												</td>
											</tr>
											<tr>
												<td>
													<p>Include Funny Holidays:</p>
												</td>
												<td>
                                                    <p>
                                                    <input type="checkbox" name="InputFunnyHolidays" id="InputFunnyHolidays" value=y
                                                    <% if(result!=null){
                                                    if ( Integer.parseInt(result[6]) == 1 ){
                                                    out.print("checked");
                                                    }
                                                    }%> >
                                                     </p>
                                                 </td>
											</tr>
											<tr>
												<td>
													<p>Include Birthday:</p>
												</td>
												<td>
                                                    <p>
                                                    <input type="checkbox" name="InputBirthdate" id="InputBirthdate" value=y
                                                    <% if(result!=null){
                                                    if (Integer.parseInt(result[1]) > 0 && Integer.parseInt(result[2]) > 0 ){
                                                    out.print("checked");
                                                    }
                                                    }%> >
                                                     </p>
                                                 </td>
											</tr>
											<tr>
												<td>
													<p>Month: </p>
												</td>
												<td><p class="select">
													<select id="bdayMonth" name="bdayMonth">
                                                    <% int h;
                                                    if(result!=null){
                                                    for ( h = 0; h<12; h++){%>
                                                    <option value="<%out.print(h+1);%>"
                                                    <%
                                                        if(Integer.parseInt(result[2])==(h+1)){
                                                        out.print("selected=\"true\"");
                                                        }
                                                    %>
                                                    ><%out.print(months[h]);%></option>
                                                    <%}
                                                    } else {
                                                	for ( h = 0; h<12; h++){%>
                                                	<option value="<%out.print(h+1);%>"><%out.print(months[h]);%></option>
                                                <%}
                                                }%>
                                                </select>
												</p></td>
											</tr>
											<tr>
												<td>
													<p>Day: </p>
												</td>
												<td><p class="select">
													<select id="bdayDay" name="bdayDay">
                                                        <%
                                                        int k;
                                                        if(result!=null){
                                                        for (k = 0; k<31; k++){%>
                                                            <option value="<%out.print(k+1);%>" <%
                                                                if(Integer.parseInt(result[1])==(k+1)){
                                                                out.print("selected=\"true\"");
                                                                }
                                                            %>
                                                            ><%out.print(k+1);%></option>
                                                        <%}
                                                        }else {
                                                        for(k = 0; k<31; k++){
                                                        %>
                                                        <option value="<%out.print(k+1);%>">
                                                         <%out.print(k+1);%></option>
                                                        <%}}%>
                                                    </select>
												   </p>
												</td>
											</tr>
										</table>
										<br />

										<button type="submit" class="btn btn-primary">Save Settings</button>
										</center>
										<br /> <br />
										</form>

<% if(result!=null){
if(Integer.parseInt(result[7])==1){

 String twitter_handle = db.getUserTwitterHandle(userId);
	if(twitter_handle.isEmpty()){%>
<p>If you want to share to Twitter from Google Glass, link your Twitter account here: <br />
<br /></p>
	<center>
	   <form action="<%= WebUtil.buildUrl(request, "/main") %>" method="post">
        <input type="hidden" name="operation" value="LoginToTwitter">
        <button class="btn btn-primary" type="submit">
          Link Twitter</button>
      </form>
	</center>

	</p>
	<%} else {%>
	<center>
	<table cellpadding="10" cellspacing="10">
	<tr>
		<td><p>Connected to Twitter as:</p></td>
		<td><p><a href="http://twitter.com/<%out.print(twitter_handle);%>">
			<%out.print("@"+twitter_handle);%>
			</a>
			</p>
		</td>
	<tr>
	</table>
	   <form action="<%= WebUtil.buildUrl(request, "/main") %>" method="post">
        <input type="hidden" name="operation" value="LogoutTwitter">
        <button class="btn btn-primary" type="submit">
          Unlink Twitter</button>
      </form>
	</center>
	<br />
	<br />
<%}%>
<%}}%>

                                    </div>
                                </div>
                            </div>
                             <div class="row margin-top-20">
                                <div class="col-sm-12 col-md-12 col-lg-12">
                                    <a href="#menu" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <h2>Back to menu</h2>
                                        </div>
                                    </a>
                                </div>
                            </div>

                        </section><!-- /.services-section -->
                        <section id="about-section" class="inactive">
                             <div class="row">
                                <div class="black-bg col-sm-12 col-md-12 col-lg-12">
                                    <h2 class="text-center">What is Celebrated Today?</h2>
                                    <div class="col-sm-12 col-md-12">

<% if (todaysHolidays == null) {%>
    Unfortunately, there are no holidays (that we know of!) today!
    <br />
    Please check back tomorrow!
<%} else { for(int counter = 0; counter<todaysHolidays.length; counter++){%>
                                   <p>
                                    <br /><br /><br /><br /><br /><br />
                                   <div>
                                      <%out.print(todaysHolidays[counter]);%>
                                   	</div>
                                   	</p>

<%}
}%>

                                    </div>
                                </div>
                            </div>

                            <div class="row margin-top-20">
                                <div class="black-bg col-sm-12 col-md-12 col-lg-12">
                                    <a href="#menu" class="change-section">
                                        <div class="btn-menu">
                                            <h2>Back to menu</h2>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </section><!-- /.about-section -->
                        <section id="contact-section" class="inactive">
                            <div class="row">
                                <div class="black-bg col-sm-12 col-md-12 col-lg-12">

                                     <h2 class="text-center">What is Every<span class="pink">d</span>ay?</h2>
                                    <div class="col-sm-12 col-md-12">
                                        <p>
                                        Every<span class="pink">d</span>ay is an app that let's you know about holidays every day. It sends a card to your Google Glass device so you never miss another holiday. You can add your birthday, and get a birthday greeting for your special day too! We have funny and normal holidays, pick the ones you want, or all of them! <br /> The best part, it's all free of charge!
                                        </p>
                                    </div>


                                    <h2 class="text-center">Contact Me</h2>
                                    <div class="col-sm-12 col-md-12">
                                        <p>My name is <span class="pink">Diana</span> and I am from Honduras. I got my bachelor's degree in Computer Systems Engineering and I have completed my Master's Degree in Computer Science at <a href="http://www.pace.edu/academics/graduate-students/degrees/computer-science-ms" target="_blank">Pace University in NYC</a>. I am very passionate about my work and I am always looking for a new challenge. This was my graduation project, which became a little bigger than expected. This project tested my ability to learn quick, adapt, and apply the knowledge I learned to finish it on time. This project started with me knowing nothing of Google Glass, and I'm more than ecstatic it's finally coming true. Don't hesitate to leave me a message below! </p>
                                    </div>

                                    <div class="col-sm-12 col-md-12">
                                        <form name="myCommentsForm" action="<%= WebUtil.buildUrl(request, "/main") %>" onsubmit="return validateForm()" method="post">
                                        <input type="hidden" name="operation" value="sendComment">

                                                <div class="form-group">
                                                    <!--<label for="contact_name">Name</label>-->
                                                    <input type="text" name = "contact_name" id="contact_name" class="form-control" placeholder="Name" />
                                                </div>
                                                <div class="form-group">
                                                    <!--<label for="contact_email">Email Address</label>-->
                                                    <input type="text" name="contact_email" id="contact_email" class="form-control" placeholder="Email Address" />
                                                </div>
                                                <div class="form-group">
                                                    <!--<label for="contact_message">Message</label>-->
                                                    <textarea name="contact_message" id="contact_message" class="form-control" rows="7" placeholder="Write a message"></textarea>
                                                </div>
                                                <button type="submit" class="btn btn-primary">Send</button>

                                        </form>
                                        <br />
                                        <br />
                                    </div>
                                    <div class="clearfix"></div>
                                </div>
                            </div>
                            <div class="row margin-top-20">
                                <div class="col-sm-12 col-md-12 col-lg-12">
                                    <a href="#menu" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <h2>Back to menu</h2>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </section><!-- /.contact-section -->
                        <section id="company-intro-section" class="inactive">
                            <div class="row">
                                <div class="black-bg col-sm-12 col-md-12 col-lg-12">
                                    <h2 class="text-center">What is Google Glass?</h2>
                                    <div class="col-sm-12 col-md-12">

                                    <p> Google Glass is a type of wearable technology with an optical head-mounted display (OHMD). It was developed by Google with the mission of producing a mass-market ubiquitous computer. Google Glass displays information in a smartphone-like hands-free format.</p>
                                    <br />
                                    <center>
                                    	<img src="images/google_glass.jpg" alt="glass" draggable="false" width="50%" height="50%">
                                    </center>
                                    <br />
                                    <br />

                                    <p>
                                	Google Glass syncs with your phone through bluetooth and allows you to receive your notifications directly on the screen above your right eye, instead of fetching your phone to respond. Glass is able to take pictures, video, answer calls, send text messages, post to Twitter, post to Facebook and many more things without the necessity of picking up your phone.
                                    </p>

                                    <br />
                                    <center>
                                    	<img src="images/google_glass2.jpg" alt="glass" draggable="false" width="50%" height="50%">
                                    </center>
                                    <br />

                                    <p>
                                    Contrary to popular belief, Google Glass cannot operate on its own and it cannot record video or take pictures without the crystal lighting up. If the crystal is off, the device isn't recording.
                                    </p>

                                    <p>As of now, Google has stopped selling the devices and is attempting to change the target users for it. Google has recently posted job listings for software engineers in the Google Glass department. We are hopeful Glass will make a full recovery and come back better than before! </p>

                                    </div>
                                </div>
                            </div>
                            <div class="row margin-top-20">
                                <div class="col-sm-12 col-md-12 col-lg-12">
                                    <a href="#menu" class="change-section">
                                        <div class="black-bg btn-menu">
                                            <h2>Back to menu</h2>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </section><!-- /.company-intro-section -->
                    </div><!-- /.templatemo-content -->
                </div><!-- /.templatemo-content-wrapper -->
            </div><!-- /.row -->

            <div class="row">
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 footer">
                    <p class="footer-text">
                    	Created by Diana <span class="pink">Michelle</span> <br />
                    	Copyright &copy; 2014 <span class="pink">Pink Michelle</span>
                    </p>
                </div><!-- /.footer -->
            </div>

		</div><!-- /#main-wrapper -->

        <div id="preloader">
            <div id="status">&nbsp;</div>
        </div><!-- /#preloader -->

        <script src="js/jquery.min.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/jquery.backstretch.min.js"></script>
        <script src="js/templatemo_script.js"></script>

         <script >
           close = document.getElementById("close");
           if(close != null){
           close.addEventListener('click', function() {
             note = document.getElementById("note");
             note.style.display = 'none';
           }, false);
           }
          </script>

          <script>
			function validateForm() {
				var x = document.forms["myCommentsForm"]["contact_name"].value;
				var y = document.forms["myCommentsForm"]["contact_email"].value;
				var z = document.forms["myCommentsForm"]["contact_message"].value;

				var len1 = x.length;
				var len2 = y.length;
				var len3 = z.length;

				if (len1 == 0 || len2 == 0 || len3 == 0) {
					alert("All three fields must be filled before submitting the form.");
					return false;
				}
				if (len1 >= 200 ){
					alert("Name can't be bigger than 200 characters");
					return false;
				}
				if (len2 >= 200 ){
					alert("Email can't be bigger than 200 characters");
					return false;
				}
				if (len3 >= 10000) {
					alert("The message is too big, please make it smaller.");
					return false;
				}
			}
			</script>

    </body>
</html>