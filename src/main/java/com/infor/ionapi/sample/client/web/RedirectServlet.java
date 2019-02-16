package com.infor.ionapi.sample.client.web;

import com.infor.ionapi.sample.client.util.OAuth2ClientConfig;
import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import java.util.logging.Logger;

public class RedirectServlet
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(RedirectServlet.class.getName());
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    //System.out.println("Inside Redirect Servlet doGet");
    LOGGER.info("Inside Redirect Servlet doGet");
    try
    {
      OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
      String code = oar.getCode();
      OAuth2ClientConfig.setAuthorization_code(code);
      OAuth2ClientConfig.setAccess_token(null);
      OAuth2ClientConfig.setRefresh_token(null);
      OAuth2ClientConfig.setApi_reply("Step 1 Completed Received Oauthhorization code: " + code);
    }
    catch (OAuthProblemException e)
    {
      OAuth2ClientConfig.setAuthorization_code(null);
      OAuth2ClientConfig.setAccess_token(null);
      OAuth2ClientConfig.setRefresh_token(null);
      OAuth2ClientConfig.setApi_reply(e.getMessage());
      e.printStackTrace();
    }
    response.sendRedirect(request.getContextPath());
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {}
}

