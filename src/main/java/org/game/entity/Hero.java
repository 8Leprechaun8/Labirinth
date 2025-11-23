package org.game.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "hero")
public class Hero extends BattleField {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;

    @Column(name = "up_", nullable = false)
    private Boolean isUp;

    @Column(name = "down_", nullable = false)
    private Boolean isDown;

    @Column(name = "left_", nullable = false)
    private Boolean isLeft;

    @Column(name = "right_", nullable = false)
    private Boolean isRight;

    @Column(name = "cadr", nullable = false)
    private Integer cadr;

    @Column(name = "active_flag", nullable = false)
    private Boolean active;

    @Column(name = "player_flag", nullable = false)
    private Boolean player;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public UUID getId() {
        return id;
    }

    public Integer getY() {
        return y;
    }

    public Integer getX() {
        return x;
    }

    public User getUser() {
        return user;
    }

    public Integer getCadr() {
        return cadr;
    }

    public Boolean getActive() {
        return active;
    }

    public Boolean getDown() {
        return isDown;
    }

    public Boolean getLeft() {
        return isLeft;
    }

    public Boolean getRight() {
        return isRight;
    }

    public Boolean getUp() {
        return isUp;
    }

    public Boolean getPlayer() {
        return player;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPlayer(Boolean player) {
        this.player = player;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCadr(Integer cadr) {
        this.cadr = cadr;
    }

    public void setDown(Boolean down) {
        isDown = down;
    }

    public void setLeft(Boolean left) {
        isLeft = left;
    }

    public void setRight(Boolean right) {
        isRight = right;
    }

    public void setUp(Boolean up) {
        isUp = up;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
