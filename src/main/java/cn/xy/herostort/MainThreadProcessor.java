package cn.xy.herostort;

import cn.xy.herostort.cmdhandler.CmdHandlerFactory;
import cn.xy.herostort.cmdhandler.ICmdHandler;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author XiangYu
 * @create2021-03-02-10:21
 *
 */
public final class MainThreadProcessor {


    private static final Logger LOGGER = LogManager.getLogger(MainThreadProcessor.class);

    private static final MainThreadProcessor _instance = new MainThreadProcessor();



    public final ExecutorService _es  = Executors.newSingleThreadExecutor((newRuanable)->{
        Thread newThread = new Thread(newRuanable);

        newThread.setName("MainMsgProcessor");
        return  newThread;
    });


    private MainThreadProcessor() {

    }





    public void process(ChannelHandlerContext channelHandlerContext, Object msg){
        if (null == channelHandlerContext ||
                null == msg) {
            return;
        }

        LOGGER.info("收到客户端消息, msgClazz = {}, msgBody = {}",
                msg.getClass().getSimpleName(),
                msg);

        _es.submit(()->{
            try {

                ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.creat(msg.getClass());
                if(null != cmdHandler){
                    cmdHandler.handle(channelHandlerContext,cast(msg));
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });

    }


    /**
     * 处理 Runnable 实例
     *
     * @param r Runnable 实例
     */
    public void process(Runnable r) {
        if (null != r) {
            _es.submit(r);
        }
    }


    public static MainThreadProcessor getInstance() {
        return _instance;
    }




    static private <Tcmd extends GeneratedMessageV3> Tcmd cast(Object object){
        if(null == object){
            return  null;
        }

        return  (Tcmd)object;
    }

}
