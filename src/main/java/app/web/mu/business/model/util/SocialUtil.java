package app.web.mu.business.model.util;

import java.io.IOException;

import java.net.URLEncoder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;

import org.brickred.socialauth.exception.ServerDataException;
import org.brickred.socialauth.exception.SocialAuthException;
import org.brickred.socialauth.util.AccessGrant;
import org.brickred.socialauth.util.Constants;
import org.brickred.socialauth.util.HttpUtil;
import org.brickred.socialauth.util.MethodType;
import org.brickred.socialauth.util.Response;

import org.json.JSONException;
import org.json.JSONObject;

import app.web.mu.business.model.User;
import app.web.mu.config.Config;
import app.web.mu.config.Keys;

/**
 * Classe utilitária para realizar a conexão com o Facebook
 * @author santos.rafaelbs
 * @version 1.0.0
 */
public class SocialUtil {
	
	private static Map<String, String> endpoints;
	
	private AccessGrant accessGrant;

	static {
		endpoints = new HashMap<String, String>();
		endpoints.put(Constants.OAUTH_AUTHORIZATION_URL, Config.getBundle().getString(Keys.FACEBOOK_AUTHORIZATION_URL.getValue()));
		endpoints.put(Constants.OAUTH_ACCESS_TOKEN_URL, Config.getBundle().getString(Keys.FACEBOOK_TOKEN_URL.getValue()));
	}

	// Aqui define-se os atributos desejados para a sua classe
	private static final String PROFILE_FIELDS = Config.getBundle().getString(Keys.FACEBOOK_PROFILE_FIELDS.getValue());
	private static final String PROFILE_URL = Config.getBundle().getString(Keys.FACEBOOK_PROFILE_URL.getValue()) + PROFILE_FIELDS;

	/**
	 * Método que retorna a URL de autenticação
	 * @return
	 * @throws Exception
	 */
	public User authFacebookLogin() throws Exception {
		String presp;

		try {
				Response response = executeFeed(PROFILE_URL);
				presp = response.getResponseBodyAsString(Constants.ENCODING);
				
		} catch (Exception e) {
			throw new SocialAuthException(e);
		}
		
		try {
				JSONObject json = new JSONObject(presp);
	
				User user = this.parseUserFromJSON(json);
				
				//Armazena o usuário na sessão
				FacesContext fc = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
				session.setAttribute("USER", user);
				user.setLogged(true);
	
				return user;

		} catch (Exception error) {
			throw new ServerDataException(error.getMessage(), error);
		}
	}

	/**
	 * Método que faz o encoding da mensagem, converte o objeto JSON retornado em uma instância User e armazena na sessão
	 * @param code
	 * @param methodType
	 * @throws Exception
	 */
	public void encode(String code, String methodType) throws Exception {

		String acode;
		String accessToken = null;
		
		try {
			acode = URLEncoder.encode(code, "UTF-8");
		} catch (Exception e) {
			throw e;
		}
		
		StringBuffer sb = new StringBuffer();
		
		if (MethodType.GET.toString().equals(methodType)) {
			sb.append(endpoints.get(Constants.OAUTH_ACCESS_TOKEN_URL));
			
			char separator = endpoints.get(Constants.OAUTH_ACCESS_TOKEN_URL).indexOf('?') == -1 ? '?' : '&';
			sb.append(separator);
		}
		
		sb.append(Config.getBundle().getString(Keys.FACEBOOK_CLIENT_ID_PARAM.getValue())).append(Config.FACEBOOK_APP_ID);
		sb.append(Config.getBundle().getString(Keys.FACEBOOK_REDIRECT_URI_PARAM.getValue())).append(Config.REDIRECT_TO_CURRENT_URL);
		sb.append(Config.getBundle().getString(Keys.FACEBOOK_CLIENT_SECRET_PARAM.getValue())).append(Config.FACEBOOK_APP_SECRET);
		sb.append(Config.getBundle().getString(Keys.FACEBOOK_CODE_PARAM.getValue())).append(acode);
		sb.append(Config.getBundle().getString(Keys.FACEBOOK_GRANT_TYPE_PARAM.getValue()));

		Response response;
		String authURL = null;
		
		try {
				if (MethodType.GET.toString().equals(methodType)) {
					authURL = sb.toString();
					
					response = HttpUtil.doHttpRequest(authURL, methodType, null, null);

			} else {
				authURL = endpoints.get(Constants.OAUTH_ACCESS_TOKEN_URL);
				response = HttpUtil.doHttpRequest(authURL, methodType, sb.toString(), null);
			}
				
		} catch (Exception e) {
			throw new SocialAuthException(e);
		}
		
		String result;
		
		try {
				result = response.getResponseBodyAsString(Constants.ENCODING);
		} catch (IOException io) {
			throw new SocialAuthException(io);
		}
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		Integer expires = null;
		
		if (result.indexOf("{") < 0) {
			String[] pairs = result.split("&");
			
			for (String pair : pairs) {
				String[] kv = pair.split("=");
				
				if (kv.length != 2) {
					throw new SocialAuthException("Unexpected auth response from " + authURL);
				} else {
					if (kv[0].equals("access_token"))
						accessToken = kv[1];
					else if (kv[0].equals("expires"))
						expires = Integer.valueOf(kv[1]);
					else if (kv[0].equals("expires_in"))
						expires = Integer.valueOf(kv[1]);
					else
						attributes.put(kv[0], kv[1]);
				}
			}
		} else {
			try {
					JSONObject jObj = new JSONObject(result);
					
					if (jObj.has("access_token"))
						accessToken = jObj.getString("access_token");

				if (jObj.has("expires_in") && jObj.opt("expires_in") != null) {
					String str = jObj.get("expires_in").toString();
					
					if (str != null && str.length() > 0)
						expires = Integer.valueOf(str);
				}
				
				if (accessToken != null) {
					Iterator<String> keyItr = jObj.keys();
					
					while (keyItr.hasNext()) {
						String key = keyItr.next();
						
						if (!"access_token".equals(key) && !"expires_in".equals(key) && jObj.opt(key) != null)
							attributes.put(key, jObj.opt(key).toString());
					}
				}
			} catch (JSONException je) {
				throw new SocialAuthException(je);
			}
		}
		
		if (accessToken != null) {
			accessGrant = new AccessGrant();
			accessGrant.setKey(accessToken);
			accessGrant.setAttribute(Constants.EXPIRES, expires);
			
			if (attributes.size() > 0)
				accessGrant.setAttributes(attributes);

			accessGrant.setProviderId(Config.FACEBOOK_APP_ID);
		}
	}

	public Response executeFeed(final String url) throws Exception {
		if (accessGrant == null)
			throw new SocialAuthException("Please call verifyResponse function first to get Access Token");


		char separator = url.indexOf('?') == -1 ? '?' : '&';
		String urlStr = url + separator + Constants.ACCESS_TOKEN_PARAMETER_NAME + "=" + accessGrant.getKey();
		
		return HttpUtil.doHttpRequest(urlStr, MethodType.GET.toString(), null, null);
	}
	
	private User parseUserFromJSON(JSONObject json) {
		User user = new User();
		user.setId(json.getString("id"));
		user.setName((json.getString("first_name")));
		user.setEmail(json.getString("email"));
		user.setProfileImageUrl(json.getJSONObject("picture").getJSONObject("data").getString("url"));
		user.setGender(json.getString("gender"));
		user.setAge(json.getJSONObject("age_range").getInt("min"));
		
		return user;
	}
}