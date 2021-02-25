package cn.xy.herostort.cmdhandler;

import cn.xy.herostort.User;
import cn.xy.herostort.UserManager;
import cn.xy.herostort.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collection;

/**
 * @author XiangYu
 * @create2021-02-25-10:46
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{

    @Override
    public void handle(ChannelHandlerContext channelHandlerContext,GameMsgProtocol.WhoElseIsHereCmd cmd){
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBulider = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        Collection<User> listUser = UserManager.listUser();

        for (User currUser : listUser) {
            if (null == currUser) {
                continue;
            }
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();

            userInfoBuilder.setUserId(currUser.getUserId());
            userInfoBuilder.setHeroAvatar(currUser.getHeroAvatar());

            resultBulider.addUserInfo(userInfoBuilder);

        }


        GameMsgProtocol.WhoElseIsHereResult newResult = resultBulider.build();

        channelHandlerContext.writeAndFlush(newResult);
    }
}
