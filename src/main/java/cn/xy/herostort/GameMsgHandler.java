package cn.xy.herostort;

import cn.xy.herostort.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XiangYu
 * @create2021-02-21-15:55
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger logger = LogManager.getLogger(GameMsgHandler.class);


    /**
     * 信道组, 注意这里一定要用 static,
     * 否则无法实现群发
     */
    static private final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);



    /**
     * 用户字典
     */
    static private final Map<Integer, User> _userMap = new HashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (null == ctx) {
            return;
        }

        try {
            super.channelActive(ctx);
            _channelGroup.add(ctx.channel());
        } catch (Exception ex) {
            // 记录错误日志
            logger.error(ex.getMessage(), ex);
        }
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) {

        if (msg == null || channelHandlerContext == null) {
            logger.debug("为空");
            return;
        }

        logger.info("收到客户端消息, msgClazz = {}, msgBody = {}",
                msg.getClass().getSimpleName(),
                msg);
        try {


            if (msg instanceof GameMsgProtocol.UserEntryCmd) {
                GameMsgProtocol.UserEntryCmd cmd = (GameMsgProtocol.UserEntryCmd) msg;


                int userId = cmd.getUserId();
                String heroAvatar = cmd.getHeroAvatar();

                User newUser = new User();
                newUser.setUserId(userId);
                newUser.setHeroAvatar(heroAvatar);
                _userMap.putIfAbsent(userId, newUser);


                GameMsgProtocol.UserEntryResult.Builder resultBulider = GameMsgProtocol.UserEntryResult.newBuilder();

                resultBulider.setUserId(userId);
                resultBulider.setHeroAvatar(heroAvatar);

                GameMsgProtocol.UserEntryResult newResult = resultBulider.build();

                _channelGroup.writeAndFlush(newResult);

            }else if(msg instanceof  GameMsgProtocol.WhoElseIsHereCmd){
                GameMsgProtocol.WhoElseIsHereResult.Builder resultBulider = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

                for (User currUser : _userMap.values()) {
                    if(null  ==  currUser){
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
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
