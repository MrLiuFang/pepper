package com.pepper.core.constant;

/**
 * 全局参数
 * 
 * @author mrliu
 *
 */
public class GlobalConstant {
	
	/**
	 * 用户资源code，用于将用户资源的code存放到redis，jsp中的auth鉴权使用。
	 */
	public static final String USER_RESOURCE_CODE = "USER_RESOURCE_CODE_";
	
	/**
	 * 登录鉴权TOKEN(用于cookie)
	 */
//	public static final String AUTHORIZE_TOKEN = "AUTHORIZE_TOKEN";
	public static final String AUTHORIZE_TOKEN = "token";
	
	/**
	 * 登录鉴权TOKEN作用域(PC/APP/WEIXIN/CONSOLE)
	 */
	public static final String AUTHORIZE_TOKEN_SCOPE ="AUTHORIZE_TOKEN_SCOPE_";
	
	/**
	 * 保存OPENID
	 */
	public static final String COOKIES_OPENID_KEY = "OPENID";
	
	/**
	 * 后台管理登陆时的title
	 */
	public static final String LOGIN_PAGE_TITLE = "LOGIN_PAGE_TITLE";

	/**
	 * 后台管理登陆后的title
	 */
	public static final String MAIN_PAGE_TITLE = "MAIN_PAGE_TITLE";

	/**
	 * 微信鉴权回调域名
	 */
	public static final String WEIXIN_DOMAIN = "WEIXIN_DOMAIN";

	/**
	 * 微信公众号appid
	 */
	public static final String WEIXIN_APPID = "WEIXIN_APPID";

	/**
	 * 微信公众号AppSecret
	 */
	public static final String WEIXIN_SECRET = "WEIXIN_SECRET";

	/**
	 * 授权中转地址（解决一个微信公众号被多个应用使用的情况，测试环境使用，线上环境清空value）
	 */
	public static final String WEIXIN_AUTH_URL = "WEIXIN_AUTH_URL";

	/**
	 * 后台用户初始密码
	 */
	public static final String ADMIN_USER_INIT_PWD = "ADMIN_USER_INIT_PWD";

	/**
	 * 域名
	 */
	public static final String DOMAIN_NAME = "DOMAIN_NAME";

	/**
	 * 客户定制登录页
	 */
	public static final String CUSTOM_LOGIN_PAGE = "CUSTOM_LOGIN_PAGE";
	
	
	/**
	 * PC端后台用户登录会话token Key
	 */
	public static final String PC_ADMIN_TOKEN_USER_ID = "_PC_ADMIN_TOKEN_USER_ID";
	
	/**
	 * APP端用户登录会话token Key
	 */
	public static final String APP_TOKEN_USER_ID = "_APP_TOKEN_USER_ID";
	
	/**
	 * pc端用户登录会话token Key
	 */
	public static final String PC_TOKEN_USER_ID = "_PC_TOKEN_USER_ID";
	
	/**
	 * 微信端用户登录会话token Key
	 */
	public static final String WEIXIN_TOKEN_USER_ID ="_WEIXIN_TOKEN_USER_ID";
	
	/**
	 * pc端后台用户登录会话超时时长配置文件KEY
	 */
	public static final String PC_ADMIN_SESSION_TIME_OUT = "PC.ADMIN.SESSION.TIME.OUT";
	
	/**
	 * pc端用户登录会话超时时长配置文件KEY
	 */
	public static final String FRONT_SESSION_TIME_OUT = "FRONT.SESSION.TIME.OUT";
	
	/**
	 * APP端后台用户登录会话超时时长配置文件KEY
	 */
	public static final String APP_SESSION_TIME_OUT = "APP.SESSION.TIME.OUT";
	
	/**
	 * 微信端用户登录会话超时时长配置文件KEY
	 */
	public static final String WEIXIN_SESSION_TIME_OUT = "WEIXIN.SESSION.TIME.OUT";
	
	/**
	 * 分页页码大小
	 */
	public static final String PAGE_SIZE = "pageSize";
	
	/**
	 * 分页页码
	 */
	public static final String PAGE_NO = "pageNo";
	
	/**
	 * 用户可用资源URL list key
	 */
	public static final String USER_RESOURCES_KEY = "USER_RESOURCES_KEY";
	
	/**
	 * 表格用户搜索（过滤）的字段的前缀
	 */
	public static final String JPQL_PREFIX_SEARCH = "search_";
	
	/**
	 * 表格用户排序的字段的前缀
	 */
	public static final String JPQL_PREFIX_SORT = "sort_";
	
	/**
	 * 验证码cookie
	 */
	public static final String VERIFICATION_CODE_COOKIE = "VERIFICATIONCODE";

}
