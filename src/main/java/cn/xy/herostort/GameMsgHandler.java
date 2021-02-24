package cn.xy.herostort;

import cn.xy.herostort.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.Collection;

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

            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();


            if (null == userId) {
                return;
            }

            UserManager.removeUserBuUserId(userId);
            Broadcaster.removeChannel(ctx.channel());

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

                UserManager.addUser(newUser);
                
                // 将用户 Id 保存至 Session
                channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).set(userId);


                GameMsgProtocol.UserEntryResult.Builder resultBulider = GameMsgProtocol.UserEntryResult.newBuilder();

                resultBulider.setUserId(userId);
                resultBulider.setHeroAvatar(heroAvatar);

                GameMsgProtocol.UserEntryResult newResult = resultBulider.build();

                Broadcaster.broadcast(newResult);

            } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
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
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
