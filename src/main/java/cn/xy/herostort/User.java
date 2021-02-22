package cn.xy.herostort;

/**
 * @author XiangYu
 * @create2021-02-22-19:02
 */
public class User {


    private int userId;


    private String heroAvatar;

    public User() {
    }

    public User(int userId, String heroAvatar) {
        this.userId = userId;
        this.heroAvatar = heroAvatar;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getHeroAvatar() {
        return heroAvatar;
    }

    public void setHeroAvatar(String heroAvatar) {
        this.heroAvatar = heroAvatar;
    }
}
