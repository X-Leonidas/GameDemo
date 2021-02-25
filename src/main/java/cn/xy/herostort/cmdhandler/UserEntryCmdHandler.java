package cn.xy.herostort.cmdhandler;

import cn.xy.herostort.Broadcaster;
import cn.xy.herostort.User;
import cn.xy.herostort.UserManager;
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

        //用户登录
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();

        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setHeroAvatar(heroAvatar);
        UserManager.addUser(newUser);

        // 将用户 Id 保存至 Session
        channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        GameMsgProtocol.UserEntryResult.Builder resultBulider = GameMsgProtocol.UserEntryResult.newBuilder();

        resultBulider.setUserId(userId);
        resultBulider.setHeroAvatar(heroAvatar);

        GameMsgProtocol.UserEntryResult newResult = resultBulider.build();
        Broadcaster.broadcast(newResult);
    }
}
