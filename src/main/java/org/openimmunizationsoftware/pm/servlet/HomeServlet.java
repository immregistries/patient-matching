package org.openimmunizationsoftware.pm.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.openimmunizationsoftware.pm.SoftwareVersion;
import org.openimmunizationsoftware.pm.model.MatchItem;
import org.openimmunizationsoftware.pm.model.MatchSet;
import org.openimmunizationsoftware.pm.model.Patient;
import org.openimmunizationsoftware.pm.model.PatientCompare;
import org.openimmunizationsoftware.pm.model.User;

/**
 * This servlet tests a set of match test cases against a given script to give a
 * summary of how well the weights work.
 * 
 * @author Nathan Bunker
 * 
 */
public class HomeServlet extends HttpServlet
{
  public static final String ACTION_LOGIN = "Login";
  public static final String ACTION_LOGOUT = "Logout";

  public static final String PARAM_NAME = "name";
  public static final String PARAM_PASSWORD = "password";
  public static final String PARAM_MESSAGE = "message";

  public static final String PARAM_ACTION = "action";
  public static final String ATTRIBUTE_USER = "user";
  public static final String ATTRIUBTE_DATA_SESSION = "dataSession";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter out = new PrintWriter(resp.getOutputStream());
    try {
      HttpSession session = req.getSession(true);
      User user = (User) session.getAttribute(ATTRIBUTE_USER);
      Session dataSession = (Session) session.getAttribute(ATTRIUBTE_DATA_SESSION);
      String message = req.getParameter(PARAM_MESSAGE);
      if (dataSession == null) {
        getSessionFactory();
        dataSession = factory.openSession();
        session.setAttribute(ATTRIUBTE_DATA_SESSION, dataSession);
      }

      String action = req.getParameter(PARAM_ACTION);
      if (action != null) {
        if (action.equals(ACTION_LOGIN)) {
          String name = req.getParameter(PARAM_NAME);
          String password = req.getParameter(PARAM_PASSWORD);
          Query query = dataSession.createQuery("from User where name = ? and password = ?");
          query.setParameter(0, name);
          query.setParameter(1, password);
          List<User> userList = query.list();
          if (userList.size() > 0) {
            user = userList.get(0);
            session.setAttribute(ATTRIBUTE_USER, user);
            message = "Welcome " + user.getName() + "!";
          } else {
            message = "Unrecognized name or password, unable to login.";
          }
        } else if (action.equals(ACTION_LOGOUT)) {
          dataSession.close();
          session.invalidate();
          user = null;
          return;
        }
      }

      out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> ");
      out.println("<html>");
      out.println("  <head>");
      out.println("    <title>Match Patient</title>");
      out.println("    <link rel=\"stylesheet\" type=\"text/css\" href=\"index.css\" />");
      out.println("  </head>");
      out.println("  <body>");
      makeMenu(out, user);
      if (message != null) {
        out.println("<p>" + message + "</p>");
      }
      out.println("  <h1>OIS Patient Matcher</h1>");
      out.println("  <p>Software Version" + SoftwareVersion.VERSION + " </p>");
      if (user == null) {
        out.println("    <form action=\"HomeServlet\" method=\"POST\"> ");
        out.println("    <table>");
        out.println("      <tr>");
        out.println("        <td>Name</td>");
        out.println("        <td><input type=\"text\" size=\"20\" name=\"" + PARAM_NAME + "\" value=\"\"/></td>");
        out.println("      </tr>");
        out.println("      <tr>");
        out.println("        <td>Password</td>");
        out.println("        <td><input type=\"password\" size=\"20\" name=\"" + PARAM_PASSWORD
            + "\" value=\"\"/></td>");
        out.println("      </tr>");
        out.println("      <tr>");
        out.println("        <td colspan=\"2\" align=\"right\"><input type=\"submit\" name=\"" + PARAM_ACTION
            + "\" value=\"" + ACTION_LOGIN + "\"/></td>");
        out.println("      </tr>");
        out.println("    </form>");
      } else {
        out.println("  <h3>Primary Tools</h3>");
        out.println("  <ul>");
        out.println("    <li><a href=\"CentralServlet\">Central</a>: Shows the status of the central server that is responsible for listening to remote Island processes and reporting on the progress of these optimizations.</li>");
        out.println("    <li><a href=\"WeightSetServlet\">Test Set</a>: Allows for viewing and updating the currently selected weight set.</li>");
        out.println("    <li><a href=\"TestSetServlet\">Test Set</a>: Allows for entry and management of test sets.</li>");
        out.println("    <li><a href=\"TestSetServlet\">Review</a>: Review tests that fail in context of similar tests. </li>");
        out.println("    <li><a href=\"HomeServlet?" + PARAM_ACTION + "=" + ACTION_LOGOUT + "\">Logout</a></li>");
        out.println("  </ul>");
        out.println("  <h3>Other Tools</h3>");
        out.println("  <ul>");
        out.println("    <li><a href=\"TestMatchingServlet\">Test Matching</a>: Shows the results of how well a particular matching set works.</li>");
        out.println("    <li><a href=\"MatchPatientServlet\">Match Patient</a>: Shows how a single patient is matched using the weighting system.</li>");
        out.println("    <li><a href=\"ConvertDataServlet\">Convert Data to Match Format</a></li>");
        out.println("    <li><a href=\"AddressTestServlet\">Address Test</a>: Allows for looking at how addresses are read.</li>");
        out.println("    <li><a href=\"DownloadHL7Servlet\">Download HL7</a></li>");
        out.println("    <li><a href=\"GenerateWeightsServlet\">Generate Weights</a>: Starts evolutionary algorithm that hunts for best weights. Do not click unless you are ready for generator start.</li>");
        out.println("    <li><a href=\"RandomServlet\">Random</a>: Supports creating a set of three random patients, the second matching with the first and the third having similar characteristics but not being a match.</li>");
        out.println("    <li><a href=\"RandomScriptServlet\">Random Script</a>: Creates script with lots of example data.</li>");
        out.println("    <li><a href=\"RandomForCDCServlet\">Random for CDC Servlet</a>:  Creates data in a spreadsheet that was requested by the CDC deduplication project.</li>");
        out.println("  </ul>");
      }
      out.println("  </body>");
      out.println("</html>");
    } catch (Exception e) {
      e.printStackTrace(out);
    }
    out.close();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(req, resp);
  }

  private static SessionFactory factory;

  public static SessionFactory getSessionFactory() {
    if (factory == null) {
      factory = new AnnotationConfiguration().configure().buildSessionFactory();
    }
    return factory;
  }

  public static void makeMenu(PrintWriter out, User user) {
    makeMenu(out, user, "HomeServlet");
  }

  public static void makeMenu(PrintWriter out, User user, String m) {
    out.println("<div class=\"menu\">");
    String ns = "menuLink";
    String s = "menuLinkSelected";
    out.println("<a class=\"" + (m.equals("HomeServlet") ? s : ns) + "\" href=\"HomeServlet\">Home</a>");
    if (user != null) {
      out.println("<a class=\"" + (m.equals("CentralServlet") ? s : ns) + "\" href=\"CentralServlet\">Central</a>");
      out.println("<a class=\"" + (m.equals("WeightSetServlet") ? s : ns)
          + "\" href=\"WeightSetServlet\">Weight Script</a>");
      out.println("<a class=\"" + (m.equals("TestSetServlet") ? s : ns) + "\" href=\"TestSetServlet\">Test Set</a>");
      out.println("<a class=\"" + (m.equals("ReviewServlet") ? s : ns)
          + "\" href=\"ReviewServlet\">Review</a>");
    }
    out.println("</div>");
  }
}
