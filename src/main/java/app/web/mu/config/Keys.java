package app.web.mu.config;

public enum Keys {
	
	FACEBOOK_APP_ID("facebook.app.id"),
	FACEBOOK_APP_SECRET("facebook.app.secret"),
	FACEBOOK_APP_ID_URL("facebook.app.id.url"),
	FACEBOOK_APP_SECRET_URL("facebook.app.secret.url"),
	FACEBOOK_PERMISSIONS_URL("facebook.permissions.url"),
	FACEBOOK_PERMISSIONS("facebook.permissions"),
	FACEBOOK_AUTHORIZATION_URL("facebook.authorization.url"),
	FACEBOOK_TOKEN_URL("facebook.token.url"),
	FACEBOOK_PROFILE_FIELDS("facebook.profile.fields"),
	FACEBOOK_PROFILE_URL("facebook.profile.url"),
	FACEBOOK_CLIENT_ID_PARAM("facebook.client.id.param"),
	FACEBOOK_REDIRECT_URI_PARAM("facebook.redirect.uri.param"),
	FACEBOOK_CLIENT_SECRET_PARAM("facebook.client.secret.param"),
	FACEBOOK_CODE_PARAM("facebook.code.param"),
	FACEBOOK_GRANT_TYPE_PARAM("facebook.grant.type.param"),
	
	MESSAGE_MARKER_ADD_SUCCESS("message.marker.add.success"),
	MESSAGE_MARKER_ADD_ERROR("message.marker.add.error"),
	MESSAGE_MARKER_REMOVE_SUCCESS("message.marker.remove.success"),
	MESSAGE_MARKER_REMOVE_ERROR("message.marker.remove.error"),
	
	MENU_ITEM_REMOVE("menu.item.remove"),
	MENU_ITEM_UPDATE("menu.item.update"),
	
	HOME_ENUM_VALUE("moradia.enum.value"),
	RESTAURANT_ENUM_VALUE("restaurant.enum.value"),
	MORADIA_ENUM_VALUE("moradia.enum.value"),
	
	HOME_OUTCOME_REDIRECT("home.outcome.redirect"),
	
	BASE_DOMAIN_URL("base.domain.url"),
	API_CONTEXT_NAME("api.context.name"),
	
	MENU_LOGIN("menu.login"),
	MENU_FACEBOOK_LOGIN("menu.facebook.login");
	
	Keys(String key) {
		this.key = key;
	}
	
	private String key;
	
	public String getValue() {
		return this.key;
	}
}
