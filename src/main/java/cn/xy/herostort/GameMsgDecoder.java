package cn.xy.herostort;

import cn.xy.herostort.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author XiangYu
 * @create2021-02-22-15:10 消息解码器
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    private static Logger logger = LogManager.getLogger(GameMsgDecoder.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (null == ctx ||
                null == msg) {
            return;
        }


        //如果当前消息流不符合格式要求，
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }


        try {

            //解析消息
            BinaryWebSocketFrame inputFrame = (BinaryWebSocketFrame) msg;
            //字节缓存
            ByteBuf byteBuff = inputFrame.content();
            //读取长度
            byteBuff.readShort();
            //读取编号
            int msgCode = byteBuff.readShort();

            //拿到消息体
            byte[] msgBody = new byte[byteBuff.readableBytes()];
            byteBuff.readBytes(msgBody);

            Message.Builder msgBuild = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
            Message cmd = null;

            if(null != msgBuild){
                msgBuild.clear();
                msgBuild.mergeFrom(msgBody);
                cmd  =  msgBuild.build();
            }


//            switch (msgCode) {
//                //用户进入发起向其他用户发起广播
//                case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
//                    cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
//                    break;
//                case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
//                    cmd = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
//                    break;
//                case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
//                    cmd = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
//                default:
//                    break;
//            }

            if (null != cmd) {
                ctx.fireChannelRead(cmd);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
