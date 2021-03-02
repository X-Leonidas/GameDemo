package cn.xy.herostort.cmdhandler;

import cn.xy.herostort.Broadcaster;
import cn.xy.herostort.model.User;
import cn.xy.herostort.model.UserManager;
import cn.xy.herostort.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @author XiangYu
 * @create2021-02-25-10:29
 */
public class UserEntryCmdHandler implements  ICmdHandler<GameMsgProtocol.UserEntryCmd>{
    @Override
    public void handle(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.UserEntryCmd cmd){



        // 获取用户 Id
        Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        User existUser = UserManager.getUserByUserId(userId);

        GameMsgProtocol.UserEntryResult.Builder resultBulider = GameMsgProtocol.UserEntryResult.newBuilder();

        resultBulider.setUserId(userId);
        resultBulider.setHeroAvatar(existUser.getHeroAvatar());
        resultBulider.setUserName(existUser.getUserName());

        GameMsgProtocol.UserEntryResult newResult = resultBulider.build();
        Broadcaster.broadcast(newResult);
    }
}
