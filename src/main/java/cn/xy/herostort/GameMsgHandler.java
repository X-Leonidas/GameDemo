package cn.xy.herostort;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author XiangYu
 * @create2021-02-21-15:55
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("收到客户端消息, msg = " + o);

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) o;
        ByteBuf byteBuf = frame.content();


        byte[] bytes = new byte[byteBuf.readableBytes()];


        byteBuf.readBytes(bytes);

        System.out.println("收到的字节 = ");


        for (byte aByte : bytes) {
            System.out.print(aByte);
            System.out.print(",");
        }

        System.out.println();
    }
}
