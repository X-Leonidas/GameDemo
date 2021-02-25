package cn.xy.herostort;

import cn.xy.herostort.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiangYu
 * @create2021-02-25-11:20
 */
public final class GameMsgRecognizer {
    /**
     * 消息编号 -> 消息对象字典
     */
    static private final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgObjMap = new HashMap<>();

    /**
     * 消息类 -> 消息编号字典
     */
    static private final Map<Class<?>, Integer> _clazzAndMsgCodeMap = new HashMap<>();




    private GameMsgRecognizer(){
    }



    public  static void intit(){
        _msgCodeAndMsgObjMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE, GameMsgProtocol.UserEntryCmd.getDefaultInstance());
        _msgCodeAndMsgObjMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
        _msgCodeAndMsgObjMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE, GameMsgProtocol.UserMoveToCmd.getDefaultInstance());

        _clazzAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class, GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        _clazzAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class, GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        _clazzAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class, GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        _clazzAndMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class, GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);

    }


    /**
     * 根据消息类获取消息编号
     * @param msgClazz
     * @return
     */
    public static int getMsgCodeByClazz(Class<?> msgClazz){
        if(null == msgClazz){
            return  -1;
        }
        Integer msgCode = _clazzAndMsgCodeMap.get(msgClazz);

        if(null == msgCode){
            return  -1;
        }else{
            return msgCode;
        }

    }


    /**
     * 根据消息编号获取消息构建器
     */
    public static Message.Builder getBuilderByMsgCode(int msgCode){
        if(msgCode < 0){
            return  null;
        }


        GeneratedMessageV3 defaultMsg = _msgCodeAndMsgObjMap.get(msgCode);

        if(null == defaultMsg){
            return  null;
        }else {
            return  defaultMsg.newBuilderForType();
        }

    }

}
