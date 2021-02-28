package cn.xy.herostort.cmdhandler;

import cn.xy.herostort.Broadcaster;
import cn.xy.herostort.model.User;
import cn.xy.herostort.model.UserManager;
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
        // 获取已有用户
        User existUser = UserManager.getUserByUserId(userId);

        long nowTime = System.currentTimeMillis();

        existUser.getMoveState().fromPosX = cmd.getMoveFromPosX();
        existUser.getMoveState().fromPosY = cmd.getMoveFromPosY();
        existUser.getMoveState().toPosX = cmd.getMoveToPosX();
        existUser.getMoveState().toPosY = cmd.getMoveToPosY();
        existUser.getMoveState().startTime = nowTime;


        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();


        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
        resultBuilder.setMoveStartTime(nowTime);

        GameMsgProtocol.UserMoveToResult newRequest = resultBuilder.build();
        Broadcaster.broadcast(newRequest);
    }
}
