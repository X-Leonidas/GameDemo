package cn.xy.herostort.cmdhandler;

import cn.xy.herostort.login.LoginService;
import cn.xy.herostort.model.User;
import cn.xy.herostort.model.UserManager;
import cn.xy.herostort.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户登陆
 */
public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd> {
    /**
     * 日志对象
     */
    static private final Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd cmd) {
        if (null == ctx ||
            null == cmd) {
            return;
        }

        String userName = cmd.getUserName();
        String password = cmd.getPassword();

        if (null == userName ||
            null == password) {
            return;
        }

        LOGGER.info("当前线程 = {}", Thread.currentThread().getName());

        // 获取用户实体
        LoginService.getInstance().userLogin(userName, password, (userEntity) -> {
            GameMsgProtocol.UserLoginResult.Builder resultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();

            LOGGER.info("当前线程 = {}", Thread.currentThread().getName());

            if (null == userEntity) {
                resultBuilder.setUserId(-1);
                resultBuilder.setUserName("");
                resultBuilder.setHeroAvatar("");
            }  else {
                User newUser = new User();
                newUser.setUserId(userEntity.userId);
                newUser.setUserName(userEntity.userName);
                newUser.setHeroAvatar(userEntity.heroAvatar);
                newUser.setCurrHp(100);
                UserManager.addUser(newUser);

                // 将用户 Id 保存至 Session
                ctx.channel().attr(AttributeKey.valueOf("userId")).set(newUser.getUserId());

                resultBuilder.setUserId(userEntity.userId);
                resultBuilder.setUserName(userEntity.userName);
                resultBuilder.setHeroAvatar(userEntity.heroAvatar);
            }

            GameMsgProtocol.UserLoginResult newResult = resultBuilder.build();
            ctx.writeAndFlush(newResult);

            return null;
        });
    }
}
