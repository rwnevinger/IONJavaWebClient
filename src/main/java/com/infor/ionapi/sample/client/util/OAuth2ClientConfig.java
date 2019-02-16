package com.infor.ionapi.sample.client.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

public class OAuth2ClientConfig
{
  private static final Logger LOGGER = Logger.getLogger(OAuth2ClientConfig.class.getName());
  private static final String modeCE = "CE";
  private static final String modeOP = "OP";
  private static String mode;
  private static String authorization_endpoint;
  private static String token_endpoint;
  private static String revocation_endpoint;
  private static String client_id;
  private static String client_secret;
  private static String redirect_url;
  private static String authorization_code;
  private static String access_token;
  private static String refresh_token;
  private static String api_reply;
  private static String ion_api_url;
  
  public static void InitProperties()
  {
    authorization_code = null;
    access_token = null;
    refresh_token = null;
    try
    {
      Properties prop = new Properties();
      
      InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ifsceoauth.properties");
      prop.load(is);
      setMode(prop.getProperty("mode"));
      setAuthorization_endpoint(prop.getProperty("authorization_endpoint"));
      setToken_endpoint(prop.getProperty("token_endpoint"));
      setRevocation_endpoint(prop.getProperty("revocation_endpoint"));
      setClient_id(prop.getProperty("client_id"));
      setClient_secret(prop.getProperty("client_secret"));
      setRedirect_url(prop.getProperty("redirect_url"));
      setIon_api_url(prop.getProperty("ion_api_url"));
      setApi_reply("");
      LOGGER.info("Set properties from file:" + Thread.currentThread().getContextClassLoader().getResource("ifsceoauth.properties").getPath());
      LOGGER.info(prop.getProperty("mode"));
      LOGGER.info(prop.getProperty("authorization_endpoint"));
      LOGGER.info(prop.getProperty("token_endpoint"));
      LOGGER.info(prop.getProperty("revocation_endpoint"));
      LOGGER.info(prop.getProperty("client_id"));
      LOGGER.info(prop.getProperty("client_secret"));
      LOGGER.info(prop.getProperty("redirect_url"));
      LOGGER.info(prop.getProperty("ion_api_url"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
      LOGGER.warning("Error reading properties. Setting default values");
      //setMode("CE");
      //setAuthorization_endpoint("https://mingleinteg01-sso.mingledev.infor.com:443/{TENANT_ID}/as/authorization.oauth2");
      //setToken_endpoint("https://mingleinteg01-sso.mingledev.infor.com:443/{TENANT_ID}/as/token.oauth2");
      //setRevocation_endpoint("https://mingleinteg01-sso.mingledev.infor.com:443/{TENANT_ID}/as/revoke_token.oauth2");
      //setClient_id("ACME_PRD~yJPOo80cO59KXl8dqne6UGShRIP5K1OTld7wJaJzDtA");
      //setClient_secret("IucCjwBRyj_SHOvrpd3FVgVDPbJQ8Dm56DNstSTdFo8R1nnHRdOSc3YM8wrtC_hAe5Fd4lVYGvGV59xJ0K__Lg");
      //setRedirect_url("http://sample-oauth2-client.infor.com:8080/SampleAppOAuth2/RedirectServlet");
      //setIon_api_url("https://mingleinteg01-ionapi.mingledev.infor.com/{TENANT_ID}/{RESOURCE}");
    }
  }
  
  public static String getMode()
  {
    return mode;
  }
  
  public static void setMode(String _mode)
  {
    mode = _mode;
  }
  
  public static String getAuthorization_endpoint(String _tenant)
  {
    if (getMode().equals("CE")) {
      return authorization_endpoint.replace("{TENANT_ID}", _tenant);
    }
    return authorization_endpoint;
  }
  
  public static void setAuthorization_endpoint(String _authorization_endpoint)
  {
    authorization_endpoint = _authorization_endpoint;
  }
  
  public static String getToken_endpoint(String _tenant)
  {
    if (getMode().equals("CE")) {
      return token_endpoint.replace("{TENANT_ID}", _tenant);
    }
    return token_endpoint;
  }
  
  public static void setToken_endpoint(String _token_endpoint)
  {
    token_endpoint = _token_endpoint;
  }
  
  public static String getRevocation_endpoint(String _tenant)
  {
    if (getMode().equals("CE")) {
      return revocation_endpoint.replace("{TENANT_ID}", _tenant);
    }
    return revocation_endpoint;
  }
  
  public static void setRevocation_endpoint(String _revocation_endpoint)
  {
    revocation_endpoint = _revocation_endpoint;
  }
  
  public static String getClient_id()
  {
    return client_id;
  }
  
  public static void setClient_id(String _client_id)
  {
    client_id = _client_id;
  }
  
  public static String getClient_secret()
  {
    return client_secret;
  }
  
  public static void setClient_secret(String _client_secret)
  {
    client_secret = _client_secret;
  }
  
  public static String getAuthorization_code()
  {
    return authorization_code;
  }
  
  public static void setAuthorization_code(String _authorization_code)
  {
    authorization_code = _authorization_code;
  }
  
  public static String getAccess_token()
  {
    return access_token;
  }
  
  public static void setAccess_token(String _access_token)
  {
    access_token = _access_token;
  }
  
  public static String getRefresh_token()
  {
    return refresh_token;
  }
  
  public static void setRefresh_token(String _refresh_token)
  {
    refresh_token = _refresh_token;
  }
  
  public static String getRedirect_url()
  {
    return redirect_url;
  }
  
  public static void setRedirect_url(String _redirect_url)
  {
    redirect_url = _redirect_url;
  }
  
  public static String getApi_reply()
  {
    return api_reply;
  }
  
  public static void setApi_reply(String _api_reply)
  {
    api_reply = _api_reply;
  }
  
  public static String getIon_api_url(String _tenant)
  {
    if (getMode().equals("CE")) {
      return ion_api_url.replace("{TENANT_ID}", _tenant);
    }
    return ion_api_url;
  }
  
  public static void setIon_api_url(String _ion_api_url)
  {
    ion_api_url = _ion_api_url;
  }
}

