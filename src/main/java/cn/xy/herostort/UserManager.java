package cn.xy.herostort;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XiangYu
 * @create2021-02-24-22:13
 */
public class UserManager {


    /**
     * 用户字典
     */
    static private final Map<Integer, User> _userMap = new HashMap<>();




    public static void addUser(User user){
        _userMap.putIfAbsent(user.getUserId(),user);
    }


    public  static  void removeUserBuUserId(Integer userId){
        _userMap.remove(userId);
    }



    public static Collection<User> listUser(){
        return _userMap.values();
    }


}
