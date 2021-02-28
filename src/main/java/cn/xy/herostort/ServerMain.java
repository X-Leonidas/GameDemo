package cn.xy.herostort;


import cn.xy.herostort.cmdhandler.CmdHandlerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author XiangYu
 * @create2021-02-20-21:21
 */
public class ServerMain {

    private static Logger logger = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) {

        //初始化
        CmdHandlerFactory.init();
        GameMsgRecognizer.intit();

        //创建group
        EventLoopGroup boos = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();


        bootstrap.group(boos, work);

        //配置通道
        bootstrap.channel(NioServerSocketChannel.class);

        //通道处理器
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(
                        new HttpServerCodec(),
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),
                        new GameMsgDecoder(),
                        new GameMsgEncoder(),
                        new GameMsgHandler()
                );
            }
        });

        try {
            //绑定端口
            ChannelFuture f = bootstrap.bind(12345).sync();
            if (f.isSuccess()) {
                logger.info("服务器启动成功");
            }

            //关闭端口
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("错误信息: {}"+e.getMessage());
            e.printStackTrace();
        }finally {
            boos.shutdownGracefully();
            work.shutdownGracefully();
        }


    }


}
