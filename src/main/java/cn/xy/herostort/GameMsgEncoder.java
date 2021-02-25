package cn.xy.herostort;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author XiangYu
 * @create2021-02-22-15:52 消息编码器
 */
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    private static Logger logger = LogManager.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

        if (null == ctx || null == msg) {
            return;
        }
        try {
            if (!(msg instanceof GeneratedMessageV3)) {
                super.write(ctx, msg, promise);
                return;
            }
            int msgCode = GameMsgRecognizer.getMsgCodeByClazz(msg.getClass());

            if (msgCode == -1) {
                logger.error("无法识别的消息类型，msgClazz = {}", msg.getClass().getSimpleName());
                super.write(ctx, msg, promise);
                return;
            }

            byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();

            ByteBuf byteBuf = ctx.alloc().buffer();
            // 消息的长度
            byteBuf.writeShort((short) msgBody.length);
            // 消息编号
            byteBuf.writeShort((short) msgCode);
            // 消息体
            byteBuf.writeBytes(msgBody);

            // 写出 ByteBuf
            BinaryWebSocketFrame outputFrame = new BinaryWebSocketFrame(byteBuf);
            super.write(ctx, outputFrame, promise);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }


    }
}
