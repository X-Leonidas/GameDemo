package cn.xy.herostort;

import cn.xy.herostort.cmdhandler.CmdHandlerFactory;
import cn.xy.herostort.cmdhandler.ICmdHandler;
import cn.xy.herostort.model.UserManager;
import cn.xy.herostort.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author XiangYu
 * @create2021-02-21-15:55
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger logger = LogManager.getLogger(GameMsgHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        try {
            super.channelActive(ctx);
            Broadcaster.addChannel(ctx.channel());
        } catch (Exception ex) {
            // 记录错误日志
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (null == ctx) {
            return;
        }
        try {
            super.handlerRemoved(ctx);
            Broadcaster.removeChannel(ctx.channel());

            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
            if (null == userId) {
                return;
            }
            UserManager.removeUserBuUserId(userId);

            GameMsgProtocol.UserQuitResult.Builder requestBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
            requestBuilder.setQuitUserId(userId);

            GameMsgProtocol.UserQuitResult newResult = requestBuilder.build();

            Broadcaster.broadcast(newResult);
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }


    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {

        if (msg == null || channelHandlerContext == null) {
            logger.debug("为空");
            return;
        }
       MainThreadProcessor.getInstance().process(channelHandlerContext,msg);
    }

}
