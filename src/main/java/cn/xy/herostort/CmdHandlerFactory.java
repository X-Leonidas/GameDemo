package cn.xy.herostort;

import cn.xy.herostort.cmdhandler.ICmdHandler;
import cn.xy.herostort.cmdhandler.UserEntryCmdHandler;
import cn.xy.herostort.cmdhandler.UserMoveToCmdHandler;
import cn.xy.herostort.cmdhandler.WhoElseIsHereCmdHandler;
import cn.xy.herostort.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiangYu
 * @create2021-02-25-10:53
 */
public final class CmdHandlerFactory {

    static private Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handlerMap = new HashMap<>();






    private  CmdHandlerFactory(){
    }



    static public void init(){
        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        _handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        _handlerMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
    }



    public  static ICmdHandler<? extends GeneratedMessageV3> creat(Class<?> myClass){
        if(null == myClass){
            return  null;
        }
       return  _handlerMap.get(myClass);
    }
}
