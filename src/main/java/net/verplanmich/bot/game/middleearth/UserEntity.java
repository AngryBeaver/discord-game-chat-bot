package net.verplanmich.bot.game.middleearth;

public class UserEntity {

    private String name;
    private int might;
    private int wisdom;
    private int agility;
    private int spirit;
    private int wit;
    private String race;
    private int fear;
    private int damage;
    private int inspiration;
    private int fearLimit;
    private int damageLimit;
    private int inspirationlimit;
    private String text;

    public UserEntity(String name, String race, int might,int wisdom,int agility,int spirit,int wit,int fearLimit, int damageLimit, int inspirationlimit, String text){
        this.name = name;
        this.race = race;
        this.might = might;
        this.wisdom = wisdom;
        this.agility = agility;
        this.spirit = spirit;
        this.wit = wit;
        this.fearLimit = fearLimit;
        this.damageLimit = damageLimit;
        this.inspirationlimit = inspirationlimit;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMight() {
        return might;
    }

    public void setMight(int might) {
        this.might = might;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getSpirit() {
        return spirit;
    }

    public void setSpirit(int spirit) {
        this.spirit = spirit;
    }

    public int getWit() {
        return wit;
    }

    public void setWit(int wit) {
        this.wit = wit;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public int getFear() {
        return fear;
    }

    public void setFear(int fear) {
        this.fear = fear;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getInspiration() {
        return inspiration;
    }

    public void setInspiration(int inspiration) {
        this.inspiration = inspiration;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFearLimit() {
        return fearLimit;
    }

    public void setFearLimit(int fearLimit) {
        this.fearLimit = fearLimit;
    }

    public int getDamageLimit() {
        return damageLimit;
    }

    public void setDamageLimit(int damageLimit) {
        this.damageLimit = damageLimit;
    }

    public int getInspirationlimit() {
        return inspirationlimit;
    }

    public void setInspirationlimit(int inspirationlimit) {
        this.inspirationlimit = inspirationlimit;
    }
}
