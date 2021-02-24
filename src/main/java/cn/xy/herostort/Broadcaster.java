package cn.xy.herostort;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author XiangYu
 * @create2021-02-24-22:07
 */
public final class Broadcaster {

    /**
     * 信道组, 注意这里一定要用 static,
     * 否则无法实现群发
     */
    static private final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    private  Broadcaster(){

    }

    public static void addChannel(Channel channel){
        _channelGroup.add(channel);
    }



    public static void removeChannel(Channel channel){
        _channelGroup.remove(channel);
    }


    public static void broadcast(Object o){
        _channelGroup.writeAndFlush(o);
    }

}
