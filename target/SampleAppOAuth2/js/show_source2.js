/**
 *
 * https://mingleinteg01-sso.mingledev.infor.com/GRANTTHORNTON_DEM-CxvxOVS3CTGXY/ results in "this site can't be reached"
 * Navigate to Infor ION API;  Available APIs, Ming.le API
 * this is documented as the base URL  mingle-ionapi.inforcloudsuite.com/GRANTTHORNTON_DEM/Mingle
 * https://mingle-sso.inforcloudsuite.com:443/GRANTTHORNTON_DEM/as/
 * 8.27.2018 generated credentils which are delivered as json key value pairs
 * tenant id ti:  GRANTTHORNTON_DEM
 * client id ci: GRANTTHORNTON_DEM~CxvxOVS3CTGXY-RZ4sCF-XWesW3yWxAcL5bjV-06P1w
 * client secret cs:  KDZ16PK9FrskysKGyN22odEa3EZZDpSuxknyMScdVFDm9l99P7iLp6XAGG3aGj7IMsaSH0c4u3E5qbxo93ZdYg
 * authentication providor pu:  https://mingle-sso.inforcloudsuite.com:443/GRANTTHORNTON_DEM/as/
 * oauth2 athorization oa:  authorization.oauth2
 * oauth2 token ot:  token.oauth2
 * oauth2 revoke or :  revoke_token.oauth2
 */
function req_auth_source(){
	var newtext = document.getElementById('source').value;
	document.getElementById('source').value = newtext+'\n\ne.g.\nOAuthClientRequest request = OAuthClientRequest'+ 
		   '.authorizationProvider("https://mingle-sso.inforcloudsuite.com:443/GRANTTHORNTON_DEM/as/authorization.oauth2")'+ 
		   '\n\t\t.setClientId("GRANTTHORNTON_DEM~CxvxOVS3CTGXY-RZ4sCF-XWesW3yWxAcL5bjV-06P1w")'+
		   '\n\t\t.setRedirectURI("http://my.bennyboo.com:8080/SampleAppOAuth2/redirect")'+
		   '\n\t\t.setResponseType("code")'+
		   '\n\t\t.buildQueryMessage();'+
		   '\nservletResponse.sendRedirect(request.getLocationUri());';
}
function exchange_code_for_token(){
	var newtext = document.getElementById('source').value;
	document.getElementById('source').value = newtext+'\n\ne.g.\nOAuthClientRequest request = OAuthClientRequest'+ 
	   '.tokenLocation("https://mingle-sso.inforcloudsuite.com:443/GRANTTHORNTON_DEM/as/token.oauth2")'+
	   '\n\t\t.setGrantType(GrantType.AUTHORIZATION_CODE)'+
	   '\n\t\t.setClientId("GRANTTHORNTON_DEM~CxvxOVS3CTGXY-RZ4sCF-XWesW3yWxAcL5bjV-06P1w")'+
	   '.setClientSecret("KDZ16PK9FrskysKGyN22odEa3EZZDpSuxknyMScdVFDm9l99P7iLp6XAGG3aGj7IMsaSH0c4u3E5qbxo93ZdYg")'+
	   '\n\t\t.setRedirectURI("http://my.bennyboo.com:8080/SampleAppOAuth2/redirect")'+
	   '\n\t\t.setCode(code)'+
	   '.buildQueryMessage();'+
	   '\nOAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());'+
	   '\nOAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken(request);'+
	   '\nString accessToken = oAuthResponse.getAccessToken();'+
	   '\nString expiresIn = oAuthResponse.getExpiresIn();';
}
function use_access_token(){
	var newtext = document.getElementById('source').value;
	document.getElementById('source').value = newtext+'\n\ne.g.\nOAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("https://mingle-ionapi.inforcloudsuite.com/GRANTTHORNTON_DEM/Mingle/weather/geolookup/q/FL/32266.json")'+ 
	   '\n\t\t.setAccessToken(accessToken)'+
	   '\n\t\t.buildQueryMessage();'+
	   '\n//bearerClientRequest.setHeader(OAuth.HeaderType.CONTENT_TYPE, ...);'+
	   '\n//bearerClientRequest.setBody(...);'+
	   '\nOAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);';
}
function revoke_oauth_token(){
	var newtext = document.getElementById('source').value;
	document.getElementById('source').value = newtext+'\n\ne.g.\nString reqParam = "token="+varRefreshToken+"&token_type_hint=refresh_token";'+
	'\nOAuthClientRequest oauthrequest = OAuthClientRequest.tokenLocation(https://mingle-ionapi.inforcloudsuite.com/GRANTTHORNTON_DEM/Mingle/as/revoke_token.oauth2+"?"+reqParam)'+
	'\n\t\t.buildBodyMessage();'+
	'\noauthrequest.addHeader("Authorization", "Basic "+authStringEnc);//use client_id as username, client_secret as password'+
	'\nOAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());'+
	'\nOAuthResourceResponse resourceResponse = oAuthClient.resource(oauthrequest, OAuth.HttpMethod.POST, OAuthResourceResponse.class);';
}
