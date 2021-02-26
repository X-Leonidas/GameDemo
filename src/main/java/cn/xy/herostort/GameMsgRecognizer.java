package cn.xy.herostort;

import cn.xy.herostort.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XiangYu
 * @create2021-02-25-11:20
 */
public final class GameMsgRecognizer {


    private static  Logger LOGGER = LogManager.getLogger(GameMsgRecognizer.class);


    /**
     * 消息编号 -> 消息对象字典
     */
    static private final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgObjMap = new HashMap<>();

    /**
     * 消息类 -> 消息编号字典
     */
    static private final Map<Class<?>, Integer> _clazzAndMsgCodeMap = new HashMap<>();


    private GameMsgRecognizer() {
    }


    public static void intit() {
        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();


        for (Class<?> innerClass : innerClazzArray) {
            if (null == innerClass ||
                    !GeneratedMessageV3.class.isAssignableFrom(innerClass)) {
                continue;
            }
            //获得内部类名
            String innerClazzName = innerClass.getSimpleName().toLowerCase();


            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
                if (null == msgCode) {
                    continue;
                }

                //获得消息编码
                String msgName = msgCode.name();

                msgName = msgName.replaceAll("_", "").toLowerCase();

                //判断包含的类
                if (!msgName.startsWith(innerClazzName)) {
                    continue;
                }

                try {
                    Object returnObject = innerClass.getDeclaredMethod("getDefaultInstance").invoke(innerClass);

                    LOGGER.info(
                            "{} <==> {}",
                            innerClass.getName(),
                            msgCode.getNumber()
                    );

                    _msgCodeAndMsgObjMap.put(
                            msgCode.getNumber(),
                            (GeneratedMessageV3) returnObject
                    );

                    _clazzAndMsgCodeMap.put(
                            innerClass,
                            msgCode.getNumber()
                    );


                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                    LOGGER.error(ex.getMessage(), ex);
                }

            }


        }

    }


    /**
     * 根据消息类获取消息编号
     *
     * @param msgClazz
     * @return
     */
    public static int getMsgCodeByClazz(Class<?> msgClazz) {
        if (null == msgClazz) {
            return -1;
        }
        Integer msgCode = _clazzAndMsgCodeMap.get(msgClazz);

        if (null == msgCode) {
            return -1;
        } else {
            return msgCode;
        }

    }


    /**
     * 根据消息编号获取消息构建器
     */
    public static Message.Builder getBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }


        GeneratedMessageV3 defaultMsg = _msgCodeAndMsgObjMap.get(msgCode);

        if (null == defaultMsg) {
            return null;
        } else {
            return defaultMsg.newBuilderForType();
        }

    }

}
