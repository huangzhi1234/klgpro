package conf;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 
 *    
 * 描述：读取配置文件   config.properties
 * @author：zhousiliang   
 * @Create Date：2014-10-29 下午05:55:10   
 * @version
 */
public class ConfigReader {

	private static Properties props;
	
	static{
		
		try {
			props = new Properties();
			ClassLoader classLoader=ConfigReader.class.getClassLoader();
			InputStream inputStream=classLoader.getResourceAsStream("conf/config.properties");
			props.load(inputStream);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * <pre>
	 *根据属性名得到值
	 * </pre>
	 * @param property 属性名
	 * @return
	 */
	public static String getProperty(String property){
		return props.getProperty(property);
	}
}
