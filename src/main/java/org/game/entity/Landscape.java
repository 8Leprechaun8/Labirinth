package org.game.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "landscape")
public class Landscape extends BattleField {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "i", nullable = false)
    private Integer i;

    @Column(name = "j", nullable = false)
    private Integer j;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LandscapeType type;

    @Column(name = "timer_for_bomb")
    private Integer timerForBomb = 8;

    @Column(name = "size_for_bomb")
    private Integer sizeForBomb = 3;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Integer getI() {
        return i;
    }

    public Integer getJ() {
        return j;
    }

    public Integer getSizeForBomb() {
        return sizeForBomb;
    }

    public Integer getTimerForBomb() {
        return timerForBomb;
    }

    public LandscapeType getType() {
        return type;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public void setJ(Integer j) {
        this.j = j;
    }

    public void setSizeForBomb(Integer sizeForBomb) {
        this.sizeForBomb = sizeForBomb;
    }

    public void setTimerForBomb(Integer timerForBomb) {
        this.timerForBomb = timerForBomb;
    }

    public void setType(LandscapeType type) {
        this.type = type;
    }
}
