package com.infor.ionapi.sample.client.web;

import com.infor.ionapi.sample.client.util.OAuth2ClientConfig;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.AuthenticationRequestBuilder;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest.TokenRequestBuilder;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

public class OAuth2Servlet
  extends HttpServlet
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(OAuth2Servlet.class.getName());
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    // all get APIs routed to post
    LOGGER.info("doGet() " +  response.toString() );
    doPost(request, response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String tenant = request.getParameter("tenant") != null ? request.getParameter("tenant") : "GRANTTHORNTON_DEM";
    LOGGER.info("OAuth Servlet Processing request " + request.toString() + " for Tenant: " + tenant);

    if (request.getParameter("request_code") != null)
    {
      LOGGER.info("Requesting Authorization Code for Tenant: " + tenant);
      try
      {
        OAuthClientRequest oauthrequest = OAuthClientRequest.authorizationLocation(OAuth2ClientConfig.getAuthorization_endpoint(tenant)).setClientId(OAuth2ClientConfig.getClient_id()).setRedirectURI(OAuth2ClientConfig.getRedirect_url()).setResponseType("code").buildQueryMessage();
        LOGGER.info("Redirecting to: " + oauthrequest.getLocationUri());
        response.sendRedirect(oauthrequest.getLocationUri());
      }
      catch (OAuthSystemException e)
      {
        OAuth2ClientConfig.setAuthorization_code(null);
        OAuth2ClientConfig.setAccess_token(null);
        OAuth2ClientConfig.setRefresh_token(null);
        OAuth2ClientConfig.setApi_reply(e.getMessage());
        e.printStackTrace();
      }
    }
    else if (request.getParameter("exchange_code") != null)
    {
      LOGGER.info("Exchanging Authorization Code for Access Token for Tenant: " + tenant);
      try
      {
        OAuthClientRequest oauthrequest = OAuthClientRequest.tokenLocation(OAuth2ClientConfig.getToken_endpoint(tenant)).setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(OAuth2ClientConfig.getClient_id()).setClientSecret(OAuth2ClientConfig.getClient_secret()).setRedirectURI(OAuth2ClientConfig.getRedirect_url()).setCode(OAuth2ClientConfig.getAuthorization_code()).buildBodyMessage();
        LOGGER.info("Exchanging Authorization Code with following param " + oauthrequest.getLocationUri());
        LOGGER.info("\t client- " + OAuth2ClientConfig.getClient_id());
        LOGGER.info("\t client secret- " + OAuth2ClientConfig.getClient_secret());
        LOGGER.info("\t token endpoint- " + OAuth2ClientConfig.getToken_endpoint(tenant));
        
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken(oauthrequest);
        OAuth2ClientConfig.setAccess_token(oauthResponse.getAccessToken());
        OAuth2ClientConfig.setRefresh_token(oauthResponse.getRefreshToken());
        OAuth2ClientConfig.setAuthorization_code(null);
        OAuth2ClientConfig.setApi_reply("Step 2 Completed received access token: " + oauthResponse.getAccessToken());
      }
      catch (Exception e)
      {
        OAuth2ClientConfig.setAccess_token(null);
        OAuth2ClientConfig.setRefresh_token(null);
        OAuth2ClientConfig.setApi_reply("Error Exchanging auth code for token: " + e.getMessage());
        e.printStackTrace();
      }
      response.sendRedirect(request.getContextPath());
    }
    else if (request.getParameter("use_token") != null)
    {
      LOGGER.info("Using Access Token to call ION API for Tenant: " + tenant);
      LOGGER.info("request " + request.toString());
      try
      {
        //String strIonApiResource = "https://mingleinteg01-ionapi.mingledev.infor.com/ACME_AX1/mingle/socialservice.svc/user/detail";
        // set this to grant thronton iu
        //String strIonApiResource = "https://mingle-ionapi.inforcloudsuite.com/GRANTTHORNTON_DEM/mingle/socialservice.svc/user/detail";
        // rwnevinger I can't locate this api call, so let's try ion oneview ping
        //String strIonApiResource = "https://mingle-ionapi.inforcloudsuite.com/GRANTTHORNTON_DEM/mingle/socialservice.svc/user/detail";
        //String strIonApiResource = "https://mingle-ionapi.inforcloudsuite.com/GRANTTHORNTON_DEM/oneviewapi/data/ping";


        LOGGER.info("read util.OAuth2ClientConfig.getIon_api_url()");

        String strIonApiResource = new String();
        // read from index.jsp select list
        strIonApiResource = request.getParameter("ionapi") != null ? request.getParameter("ionapi") : OAuth2ClientConfig.getIon_api_url(tenant);

        //if (OAuth2ClientConfig.getIon_api_url(tenant) != null) {
        //  strIonApiResource = OAuth2ClientConfig.getIon_api_url(tenant);
        //}
        LOGGER.info("IONAPI endpoint: " + strIonApiResource);

        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(strIonApiResource).setAccessToken(OAuth2ClientConfig.getAccess_token()).buildQueryMessage();
        LOGGER.info("OAuthClientRequest bearerClientRequest initialized");
        
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        LOGGER.info("OAuthClient oAuthClient initialized");

        OAuthResourceResponse resourceResponse = (OAuthResourceResponse)oAuthClient.resource(bearerClientRequest, "GET", OAuthResourceResponse.class);

        LOGGER.info("ION API Response: Code-" + resourceResponse.getResponseCode() + " Body- " + resourceResponse.getBody());

        //OAuth2ClientConfig.setApi_reply("Response Code: " + resourceResponse.getResponseCode() + "\n Body:\n\t" + resourceResponse.getBody());
        OAuth2ClientConfig.setApi_reply("Step 3 API Call completed \n" + strIonApiResource + "\n" + "Response Code: " + resourceResponse.getResponseCode() + "\n Body:\n\t" + resourceResponse.getBody());

      }
      catch (Exception e)
      {
        OAuth2ClientConfig.setApi_reply("Error consuming ION API using token: " + e.getMessage());
        e.printStackTrace();
      }
      response.sendRedirect(request.getContextPath());
    }
    else if (request.getParameter("revoke_token") != null)
    {
      LOGGER.info("Revoking Access Token for Tenant: " + tenant);
      try
      {
        OAuthClientRequest oauthrequest = OAuthClientRequest.tokenLocation(OAuth2ClientConfig.getRevocation_endpoint(tenant)).setParameter("token", OAuth2ClientConfig.getRefresh_token()).setParameter("token_type_hint", "refresh_token").buildBodyMessage();
        String authString = OAuth2ClientConfig.getClient_id() + ":" + OAuth2ClientConfig.getClient_secret();
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        oauthrequest.addHeader("Authorization", "Basic " + authStringEnc);
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse resourceResponse = (OAuthResourceResponse)oAuthClient.resource(oauthrequest, "POST", OAuthResourceResponse.class);
        OAuth2ClientConfig.setApi_reply("Response Code: " + resourceResponse.getResponseCode() + "\n Body:Token Revoked\n\t" + resourceResponse.getBody());
        if (resourceResponse.getResponseCode() == 200)
        {
          LOGGER.info("Token revoked. Cleaning up local tokens");
          OAuth2ClientConfig.setAuthorization_code(null);
          OAuth2ClientConfig.setAccess_token(null);
          OAuth2ClientConfig.setRefresh_token(null);
        }
      }
      catch (Exception e)
      {
        OAuth2ClientConfig.setAuthorization_code(null);
        OAuth2ClientConfig.setApi_reply("Error Revoking token: " + e.getMessage());
        e.printStackTrace();
      }
      response.sendRedirect(request.getContextPath());
    }
  }
}


