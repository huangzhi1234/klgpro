package exception;

/**
 * 类的说明：业务异常
 * <li>继承RuntimeException，用于回滚事务，并在前台弹出提示信息
 * @author Ou
 */
public class IbbMgrException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public static String info="系统异常，请联系管理员：";

	public IbbMgrException(){
		super();
		
	}
	public IbbMgrException(String message, Throwable cause) {
		super(message, cause);
	}

	public IbbMgrException(String message) {
		super(message);
	}

	public IbbMgrException(Throwable cause) {
		super(cause);
	}
}
