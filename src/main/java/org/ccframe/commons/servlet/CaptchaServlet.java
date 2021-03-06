package org.ccframe.commons.servlet;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SysInitBeanHelper;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.gimpy.BlockGimpyRenderer;
import cn.apiclub.captcha.gimpy.FishEyeGimpyRenderer;
import cn.apiclub.captcha.gimpy.GimpyRenderer;
import cn.apiclub.captcha.servlet.CaptchaServletUtil;
import cn.apiclub.captcha.text.producer.TextProducer;
import cn.apiclub.captcha.text.renderer.ColoredEdgesWordRenderer;
import cn.apiclub.captcha.text.renderer.WordRenderer;

/**
 * 校验码，同时做启动日志输出.
 * @author JIM
 *
 */
public class CaptchaServlet extends HttpServlet {

	private static final long serialVersionUID = -2178175559012074926L;

	private static final Color DEFAULT_COLOR = Color.BLACK;
	
	private float thickness = 1.2f;
	private String avaiableChars = "bcdefghkmnprtuvwxy34678";
	private String colorConfig = "47,94,149|60,108,128|51,51,51";
	private String fontConfig = "Arial";
	
	private TextProducer textProducer;
	private Map<Color, WordRenderer> wordRendererMap = new HashMap<Color, WordRenderer>();
	private Map<Color, GimpyRenderer> gimpyRendererMap = new HashMap<Color, GimpyRenderer>();
	private BlockGimpyRenderer blockGimpyRenderer = new BlockGimpyRenderer(2);
	private GradiatedBackgroundProducer bradiatedBackgroundProducer;

	private Logger log = Logger.getLogger(this.getClass());
	private static final String SYS_START_STR = "系统启动模式：";

	private void initParameter(){
		if(getServletConfig().getInitParameter("thickness") != null){
			thickness = Float.parseFloat(getServletConfig().getInitParameter("thickness"));
		}
		if(getServletConfig().getInitParameter("avaiableChars") != null){
			avaiableChars = getServletConfig().getInitParameter("avaiableChars");		
		}
		if(getServletConfig().getInitParameter("colorConfig") != null){
			colorConfig = getServletConfig().getInitParameter("colorConfig");
		}
		if(getServletConfig().getInitParameter("font") != null){
			fontConfig = getServletConfig().getInitParameter("font");
		}
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		checkSysIntMode();
		initParameter();
		
		textProducer = new WordProducer(4, avaiableChars); //有兴趣可以试试chinease的producer

		Set<Color> colorSet = new HashSet<Color>();
		colorSet.add(DEFAULT_COLOR);
		String[] colorStrs = colorConfig.split("\\|");
		for(String colorStr: colorStrs){
			String[] rgb = colorStr.split(",");
			colorSet.add(new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2])));
		}
		for(Color color: colorSet){
			wordRendererMap.put(
				color,
				new ColoredEdgesWordRenderer(
					Arrays.asList(new Color[]{color}), 
					Arrays.asList(new Font[]{new Font(fontConfig, Font.ITALIC, 38)}), //Arial的1太难看了，换成了Times New Roman
					thickness
				)
			);
			gimpyRendererMap.put(
				color, 
				new FishEyeGimpyRenderer(color, color) //同类的还有DropShadowGimpyRenderer(), 同interface实现的还有DropShadowGimpyRenderer等
			);
		}
		bradiatedBackgroundProducer=new GradiatedBackgroundProducer();  
		bradiatedBackgroundProducer.setFromColor(new Color(200, 200, 200));  
		bradiatedBackgroundProducer.setToColor(new Color(251, 251, 251)); 
	}

    private void checkSysIntMode() {
    	String forceInitMode = System.getProperty("forceInitMode", null); //自动发布时，通过环境变量-D强制指定是重置还是不处理
    	String modeStr = null;
    	if(forceInitMode != null){
    		modeStr = forceInitMode;
    	}else{
        	try(
        		FileInputStream finput = new FileInputStream(new File(getClass().getResource(SysInitBeanHelper.APP_CONFIG_FILE_NAME).toURI()));
       	    	BufferedReader reader = new BufferedReader(new InputStreamReader(finput));
        	){
    	        String line = null;
    	        while((line = reader.readLine()) != null){
    	        	if(line.trim().startsWith(SysInitBeanHelper.INIT_MODE_PARAM_NAME)){
    	        		modeStr = line.split("=")[1].trim();
    	        		break;
    	        	}
    	        }
    	        if(modeStr == null){ //系统创建模式被注释或删除，默认模式模式就是none
    	        	modeStr = "none";
    	        }
        	}catch(Exception e){
        		e.printStackTrace();
        	}
    	}
    	switch(modeStr){
    		case "none":
    			log.info(SYS_START_STR + "标准模式...");
    			break;
    		case "create":
    			log.info(SYS_START_STR + "数据重新创建模式...");
    			break;
    		case "create-once":
    			log.info(SYS_START_STR + "数据安全创建模式...");
    			break;
    		default: 
    			log.info(SysInitBeanHelper.INIT_MODE_PARAM_NAME + "参数配置有误...");
    	}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //设置页面不缓存
    	Color color = DEFAULT_COLOR;
    	if(request.getParameter("c") != null){
    		String[] rgb = request.getParameter("c").split(","); //color
    		color = new Color(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
    	}
    	if(!wordRendererMap.containsKey(color)){
    		color = DEFAULT_COLOR;
    	}
    	
    	response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        Captcha captcha = new Captcha.Builder(100, 35)
        	.addText(textProducer, wordRendererMap.get(color))
        	.gimp(blockGimpyRenderer)
        	.gimp(gimpyRendererMap.get(color))
        	.addBackground(bradiatedBackgroundProducer)
        	.build();
        CaptchaServletUtil.writeImage(response, captcha.getImage());
        // 将认证码存入SESSION
        request.getSession().setAttribute(Global.SESSION_VALIDATE_CODE, captcha.getAnswer().toLowerCase());
    }
    
    private static final class WordProducer implements TextProducer{
    	
    	private int size;
    	
    	private static char[] avaiableChars;
    	
    	public WordProducer(int size, String avableChars){
    		this.size = size;
    		avaiableChars = avableChars.toCharArray();
    	}
    	
		@Override
		public String getText() {
			Random random = new Random(System.currentTimeMillis());
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < size; i ++){
				sb.append(avaiableChars[random.nextInt(avaiableChars.length)]);
			}
			return sb.toString();
		}
    }
}