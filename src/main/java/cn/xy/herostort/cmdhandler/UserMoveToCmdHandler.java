package cn.xy.herostort.cmdhandler;

import cn.xy.herostort.Broadcaster;
import cn.xy.herostort.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @author XiangYu
 * @create2021-02-25-11:00
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd cmd) {

        if (null == ctx ||
                null == cmd) {
            return;
        }

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        GameMsgProtocol.UserMoveToResult.Builder requestBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();


        requestBuilder.setMoveUserId(userId);
        requestBuilder.setMoveToPosX(cmd.getMoveToPosX());
        requestBuilder.setMoveToPosY(cmd.getMoveToPosY());

        GameMsgProtocol.UserMoveToResult newRequest = requestBuilder.build();
        Broadcaster.broadcast(newRequest);
    }
}
