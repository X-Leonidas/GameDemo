package cn.xy.herostort.model;

/**
 * @author XiangYu
 * @create2021-02-22-19:02
 */
public class User {
    public User() {
    }

    /**
     * 用户Id
     */
    private int userId;

    /**
     * 英雄形象
     */
    private String heroAvatar;



    /**
     * 血量
     */
    private int currHp;


    private final MoveState moveState  = new MoveState();


    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public MoveState getMoveState() {
        return moveState;
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
