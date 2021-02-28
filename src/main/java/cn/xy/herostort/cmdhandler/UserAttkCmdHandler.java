package cn.xy.herostort.cmdhandler;

import cn.xy.herostort.Broadcaster;
import cn.xy.herostort.model.User;
import cn.xy.herostort.model.UserManager;
import cn.xy.herostort.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author XiangYu
 * @create2021-02-28-21:40
 */
public class UserAttkCmdHandler  implements  ICmdHandler<GameMsgProtocol.UserAttkCmd> {

    static private  final Logger LOGGER  = LogManager.getLogger(UserAttkCmdHandler.class);


    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd cmd) {
        if (null == ctx ||
                null == cmd) {
            return;
        }

        // 获取攻击用户 Id
        Integer attkUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if (null == attkUserId) {
            return;
        }

        // 获取目标用户 Id
        int targetUserId = cmd.getTargetUserId();
        // 获取目标用户
        User targetUser = UserManager.getUserByUserId(targetUserId);

        if (null == targetUser) {
            broadcastAttkResult(attkUserId, -1);
            return;
        }

        LOGGER.info("当前线程 = {}", Thread.currentThread().getName());

        final int dmgPoint = 1;
        targetUser.setCurrHp(targetUser.getCurrHp() - dmgPoint);

        // 广播攻击结果
        broadcastAttkResult(attkUserId, targetUserId);
        // 广播减血结果
        broadcastSubtractHpResult(targetUserId, dmgPoint);

        if (targetUser.getCurrHp() <= 0) {
            // 广播死亡结果
            broadcastDieResult(targetUserId);
        }
    }



    /**
     * 广播攻击结果
     *
     * @param attkUserId
     * @param targetUserId
     */
    static private void broadcastAttkResult(int attkUserId, int targetUserId) {
        if (attkUserId <= 0) {
            return;
        }

        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(attkUserId);
        resultBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserAttkResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    /**
     * 广播减血结果
     *
     * @param targetUserId
     * @param subtractHp
     */
    static private void broadcastSubtractHpResult(int targetUserId, int subtractHp) {
        if (targetUserId <= 0 ||
                subtractHp <= 0) {
            return;
        }

        GameMsgProtocol.UserSubtractHpResult.Builder resultBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);
        resultBuilder.setSubtractHp(subtractHp);

        GameMsgProtocol.UserSubtractHpResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    /**
     * 广播死亡结果
     *
     * @param targetUserId
     */
    static private void broadcastDieResult(int targetUserId) {
        if (targetUserId <= 0) {
            return;
        }

        GameMsgProtocol.UserDieResult.Builder resultBuilder = GameMsgProtocol.UserDieResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserDieResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
