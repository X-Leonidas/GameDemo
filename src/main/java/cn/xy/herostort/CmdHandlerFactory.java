package cn.xy.herostort;

import cn.xy.herostort.cmdhandler.ICmdHandler;
import cn.xy.herostort.util.PackageUtil;
import com.google.protobuf.GeneratedMessageV3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author XiangYu
 * @create2021-02-25-10:53
 */
public final class CmdHandlerFactory {

    private static  Logger LOGGER = LogManager.getLogger(CmdHandlerFactory.class);


    static private Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handlerMap = new HashMap<>();


    private CmdHandlerFactory() {
    }


    static public void init() {
        // 获取包名称
        final String packageName = CmdHandlerFactory.class.getPackage().getName();

        // 获取 ICmdHandler 所有的实现类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(
                packageName,
                true,
                ICmdHandler.class
        );


        for (Class<?> handlerClazz : clazzSet) {
            if (null == handlerClazz ||
                    0 != (handlerClazz.getModifiers() & Modifier.ABSTRACT)) {
                continue;
            }

            //获取方法数组
            Method[] methodArray = handlerClazz.getDeclaredMethods();
            // 消息类型
            Class<?> cmdClazz = null;

            for (Method method : methodArray) {

                if (null == method ||
                        !method.getName().equals("handle")) {
                    continue;
                }

                //获取函数参数类型数组
                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes.length < 2 ||
                        parameterTypes[1] == GeneratedMessageV3.class ||
                        !GeneratedMessageV3.class.isAssignableFrom(parameterTypes[1])) {
                    continue;
                }
                cmdClazz = parameterTypes[1];
                break;
            }

            if (null == cmdClazz) {
                continue;
            }

            try {
                // 创建命令处理器实例
                ICmdHandler<?> newHandler = (ICmdHandler<?>) handlerClazz.newInstance();

                LOGGER.info(
                        "{} <==> {}",
                        cmdClazz.getName(),
                        handlerClazz.getName()
                );

                _handlerMap.put(cmdClazz, newHandler);
            } catch (Exception ex) {
                // 记录错误日志
                LOGGER.error(ex.getMessage(), ex);
            }
        }


    }


    public static ICmdHandler<? extends GeneratedMessageV3> creat(Class<?> myClass) {
        if (null == myClass) {
            return null;
        }
        return _handlerMap.get(myClass);
    }
}
