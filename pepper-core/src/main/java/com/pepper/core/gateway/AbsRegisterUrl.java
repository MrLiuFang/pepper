package com.pepper.core.gateway;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;
import javax.servlet.ServletContext;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.pepper.util.SpringContextUtil;

/**
 * 注册资源URL（zookeeper）
 * 
 * @author mrliu
 *
 */
public abstract class AbsRegisterUrl implements ApplicationListener<ContextRefreshedEvent> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CuratorFramework curatorFramework;

	@Autowired
	private Environment environment;

	@Autowired
	private WebApplicationContext webApplicationConnect;

	private String host;

	private String port;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}

	protected abstract URL getCodeSourcePath();

	public void init() {
		registerUrl();
		curatorFramework.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			@Override
			public void stateChanged(CuratorFramework client, ConnectionState newState) {
				if (newState == ConnectionState.RECONNECTED) {
					registerUrl();
				}
			}
		});
	}

	public void registerUrl() {
		try {
			String ctx = webApplicationConnect.getServletContext().getContextPath();
			webApplicationConnect.getServletContext().setAttribute("ctx", ctx);
			ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
			Map<String, Object> controllerBean = applicationContext.getBeansWithAnnotation(Controller.class);
			if (controllerBean == null || controllerBean.size() <= 0) {
				return;
			}
			List<String> endPoints = getEndPoints();
			//logger.info(endPoints.toString());
			Set<String> resultUrl = new HashSet<String>();
			RequestMappingHandlerMapping bean = webApplicationConnect.getBean(RequestMappingHandlerMapping.class);
			Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
			for (RequestMappingInfo rmi : handlerMethods.keySet()) {
				PatternsRequestCondition pc = rmi.getPatternsCondition();
				Set<String> pSet = pc.getPatterns();
				resultUrl.addAll(pSet);
			}

			for (String url : resultUrl) {
				registerPath(url);
			}
			registerAssets(webApplicationConnect.getServletContext());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("资源注册失败！");
		}

	}

	private void registerPath(String path) throws Exception {

		StringBuffer sb = new StringBuffer("/url");
		if (environment.getProperty("environment", "").toLowerCase().equals("dev")) {
			sb.append("/");
			sb.append(host);
			sb.append(path);
		} else {
			sb.append(path);
		}

		if (path.equals("/")) {
			sb.append("root/");
		} else {
			sb.append("/");
		}
		sb.append(host);
		sb.append(":");
		sb.append(port);
		Stat stat = curatorFramework.checkExists().forPath(sb.toString());
		if (stat != null) {
			curatorFramework.delete().forPath(sb.toString());
		}
		logger.info("注册url： {}", sb.toString());
		curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(sb.toString());
	}

	/**
	 * 获取tomcat web 服务端口和地址
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 * @throws UnknownHostException
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 */
	private List<String> getEndPoints() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
				Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		ArrayList<String> endPoints = new ArrayList<String>();
		host = NetUtils.getLocalHost();
		// 获取端口
		for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
			ObjectName obj = i.next();
			String scheme = mbs.getAttribute(obj, "scheme").toString();
			port = obj.getKeyProperty("port");
			String ep = scheme + "://" + host + ":" + port;
			endPoints.add(ep);
		}
		return endPoints;
	}

	/**
	 * 注册静态资源
	 * 
	 * @throws Exception
	 */
	private void registerAssets(ServletContext servletContext) throws Exception {
		for (String assetsPath : getAssetsList()) {
			registerPath(assetsPath);
		}
	}

	private List<String> getAssetsList() throws Exception {
		List<String> assetsList = new ArrayList<String>();
		URL codeSourcePath = this.getCodeSourcePath();
		if (ResourceUtils.isJarURL(codeSourcePath)) {
			URL url = new URL("jar", null, 0, codeSourcePath.getPath());
			URLConnection con = url.openConnection();
			if (con instanceof JarURLConnection) {
				JarURLConnection result = (JarURLConnection) con;
				JarInputStream jarInputStream = new JarInputStream(result.getInputStream());
				JarEntry entry;
				while ((entry = jarInputStream.getNextJarEntry()) != null) {
					String name = entry.getName();
					if (!entry.isDirectory() && name.indexOf("META-INF/resources/assets") == 0) {
						assetsList.add(name.replaceAll("META-INF/resources", "").replaceAll("\\\\", "/"));
					}
				}
				jarInputStream.close();
			}
		} else if (ResourceUtils.isJarFileURL((codeSourcePath))) {
			JarFile jarFile = new JarFile(ResourceUtils.getFile(codeSourcePath));
			Enumeration<JarEntry> enu = jarFile.entries();
			while (enu.hasMoreElements()) {
				JarEntry element = (JarEntry) enu.nextElement();
				String name = element.getName();
				if (!element.isDirectory() && name.indexOf("META-INF/resources/assets") == 0) {
					assetsList.add(name.replaceAll("META-INF/resources", "").replaceAll("\\\\", "/"));
				}
			}
			jarFile.close();
		} else {
			File file = ResourceUtils.getFile(codeSourcePath);
			if (file.isDirectory()) {
				String assetsPath = codeSourcePath.getPath() + "META-INF/resources/assets";
				traverseFolder(assetsPath, assetsList, codeSourcePath.getPath());
			}
		}
		return assetsList;
	}

	public void traverseFolder(String path, List<String> assetsList, String codeSourcePath) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						traverseFolder(file2.getAbsolutePath(), assetsList, codeSourcePath);
					} else {
						assetsList.add(file2.getAbsolutePath().substring((codeSourcePath + "META-INF/resources").length()));
					}
				}
			}
		} else {
		}
	}
}
