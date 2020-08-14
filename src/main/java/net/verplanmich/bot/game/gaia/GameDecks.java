package net.verplanmich.bot.game.gaia;

import net.verplanmich.bot.game.GameResult;
import net.verplanmich.bot.game.GameResultException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.verplanmich.bot.game.gaia.Gaia.EVENT_EXPLORED;
import static net.verplanmich.bot.game.gaia.Gaia.MAP_KEY_EXPLORED;

public class GameDecks {

    private List<String> scores = new ArrayList(Arrays.asList("score-1","score-2","score-3","score-4","score-5","score-5","score-6","score-6","score-7","score-7"));

    private List<String> finals = new ArrayList(Arrays.asList("final-1","final-2","final-3","final-4","final-5","final-6"));


    private Map<String, UserEntity> avatars = new HashMap();

    {
        avatars.put("ambas", new UserEntity().setAvatar("ambas").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(2).setTech(Arrays.asList(0, 1, 0, 0, 0, 0)).setColor("brown"));
        avatars.put("taklons", new UserEntity().setAvatar("taklons").setMight1(2).setMight2(4).setBrainstone1(1).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 0, 0)).setColor("brown"));
        avatars.put("baltak", new UserEntity().setAvatar("baltak").setMight1(2).setMight2(2).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(0).setTech(Arrays.asList(0, 0, 0, 1, 0, 0)).setColor("orange"));
        avatars.put("geoden", new UserEntity().setAvatar("geoden").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(6).setQic(1).setTech(Arrays.asList(1, 0, 0, 0, 0, 0)).setColor("orange"));
        avatars.put("bescod", new UserEntity().setAvatar("bescod").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(1).setOre(4).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 0, 0)).setColor("grey"));
        avatars.put("firaks", new UserEntity().setAvatar("firaks").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(2).setOre(3).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 0, 0)).setColor("grey"));
        avatars.put("gleen", new UserEntity().setAvatar("gleen").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(1).setTech(Arrays.asList(0, 1, 0, 0, 0, 0)).setColor("yellow"));
        avatars.put("xenos", new UserEntity().setAvatar("xenos").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(2).setTech(Arrays.asList(0, 0, 1, 0, 0, 0)).setColor("yellow"));
        avatars.put("hadsch-halla", new UserEntity().setAvatar("hadsch-halla").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 1, 0)).setColor("red"));
        avatars.put("ivits", new UserEntity().setAvatar("ivits").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 0, 0)).setColor("red"));
        avatars.put("lantida", new UserEntity().setAvatar("lantida").setMight1(4).setMight2(0).setVictory(10).setCredit(13).setKnowledge(3).setOre(4).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 0, 0)).setColor("blue"));
        avatars.put("terraner", new UserEntity().setAvatar("terraner").setMight1(4).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(4).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 1, 0)).setColor("blue"));
        avatars.put("itar", new UserEntity().setAvatar("itar").setMight1(4).setMight2(4).setVictory(10).setCredit(15).setKnowledge(3).setOre(5).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 0, 0)).setColor("white"));
        avatars.put("nevla", new UserEntity().setAvatar("nevla").setMight1(2).setMight2(4).setVictory(10).setCredit(15).setKnowledge(2).setOre(4).setQic(1).setTech(Arrays.asList(0, 0, 0, 0, 0, 1)).setColor("white"));
    }

    private List<String> advTechs = new ArrayList(Arrays.asList(
            "tech-adv-1", "tech-adv-2", "tech-adv-3", "tech-adv-4", "tech-adv-5", "tech-adv-6", "tech-adv-7", "tech-adv-8", "tech-adv-9", "tech-adv-10", "tech-adv-11", "tech-adv-12", "tech-adv-13", "tech-adv-14", "tech-adv-15"
    ));

    private List<String> defaultTechs = new ArrayList(Arrays.asList(
            "tech-1", "tech-2", "tech-3", "tech-4", "tech-5", "tech-6", "tech-7", "tech-8", "tech-9"
    ));

    private List<Tech> alliances = new ArrayList(
            Arrays.asList(
                    new Tech(1,"alliance-front-1"),
                    new Tech(4,"alliance-front-2"),
                    new Tech(4,"alliance-front-3"),
                    new Tech(4,"alliance-front-4"),
                    new Tech(4,"alliance-front-5"),
                    new Tech(4,"alliance-front-6"),
                    new Tech(4,"alliance-front-7")
                    )
    );

    private List<String> roundBooster = new ArrayList(Arrays.asList(
            "round-booster-1", "round-booster-2", "round-booster-3", "round-booster-4", "round-booster-5", "round-booster-6", "round-booster-7", "round-booster-8", "round-booster-9", "round-booster-10"
    ));

    private Map<String, List<Tech>> techs = new HashMap();

    public class Tech {
        Tech(int amount, String cardId) {
            this.amount = amount;
            this.cardId = cardId;
        }

        int amount;
        String cardId;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }
    }

    public Map<String,Map<String,Map<String,String>>> map = new HashMap();

    public void explore(String exploreString, UserEntity userEntity){
        String[] parts = exploreString.split(",");
        String sector = parts[0];
        String field = parts[1];
        String type = "";
        if(parts.length == 3) {
            type = parts[2];
        }
        if(!map.containsKey(sector)){
            map.put(sector, new HashMap());
        }
        if(!map.get(sector).containsKey(field)){
            map.get(sector).put(field, new HashMap());
        }
        String previousType = map.get(sector).get(field).get(userEntity.getColor());
        if(previousType == null){
            previousType = "";
        }
        adjustTypeForUser(previousType,type,userEntity);
        map.get(sector).get(field).remove(userEntity.getColor());
        if(!type.equals("")){
            map.get(sector).get(field).put(userEntity.getColor(),type);
        }
    }

    private void adjustTypeForUser(String previousType, String currentType, UserEntity userEntity){
        if(currentType.equals("mine")){
            if(userEntity.getMines().size() > 0){
                userEntity.getMines().remove(0);
            }else{
                throw new GameResultException(new GameResult().addEvent(EVENT_EXPLORED)
                        .set(MAP_KEY_EXPLORED, getMap())
                );

            }
        }
        if(currentType.equals("trade")){
            if(userEntity.getTrades().size() > 0){
                userEntity.getTrades().remove(0);
            }else{
                throw new GameResultException(new GameResult().addEvent(EVENT_EXPLORED)
                        .set(MAP_KEY_EXPLORED, getMap())
                );
            }
        }
        if(currentType.equals("laboratory")){
            if(userEntity.getLaboratories().size() > 0){
                userEntity.getLaboratories().remove(0);
            }else{
                throw new GameResultException(new GameResult().addEvent(EVENT_EXPLORED)
                        .set(MAP_KEY_EXPLORED, getMap())
                );
            }
        }
        if(currentType.equals("gaia")){
            if(userEntity.getTerraformers().size() > 0){
                userEntity.getTerraformers().remove(0);
            }else{
                throw new GameResultException(new GameResult().addEvent(EVENT_EXPLORED)
                        .set(MAP_KEY_EXPLORED, getMap())
                );
            }
        }
        if(currentType.equals("station")){
            if(!userEntity.isStation()){
                userEntity.setStation(true);
            }else{
                throw new GameResultException(new GameResult().addEvent(EVENT_EXPLORED)
                        .set(MAP_KEY_EXPLORED, getMap())
                );
            }
        }
        if(currentType.equals("academie")){
            if(!userEntity.isAcademy1()){
                userEntity.setAcademy1(true);
            }else if(!userEntity.isAcademy2()) {
                userEntity.setAcademy2(true);
            }else{
                throw new GameResultException(new GameResult().addEvent(EVENT_EXPLORED)
                        .set(MAP_KEY_EXPLORED, getMap())
                );
            }
        }

        if(previousType.equals("mine")){
            if(userEntity.getMines().size() < 8){
                userEntity.getMines().add("");
            }
        }
        if(previousType.equals("trade")){
            if(userEntity.getTrades().size() < 4){
                userEntity.getTrades().add("");
            }
        }
        if(previousType.equals("laboratory")){
            if(userEntity.getLaboratories().size() < 3){
                userEntity.getLaboratories().add("");
            }
        }
        if(previousType.equals("gaia")){
            if(userEntity.getTerraformers().size() < 3){
                userEntity.getTerraformers().add("");
            }
        }
        if(previousType.equals("station")){
            if(userEntity.isStation()){
                userEntity.setStation(false);
            }
        }
        if(previousType.equals("academie")){
            if(userEntity.isAcademy1()){
                userEntity.setAcademy1(false);
            }else if(userEntity.isAcademy2()) {
                userEntity.setAcademy2(false);
            }
        }

    }

    public Map<String,Map<String,Map<String,String>>> getMap(){
        return map;
    }

    public List<String> gameRoundBooster = new ArrayList();

    public List<String> score = new ArrayList();
    public List<String> endScore = new ArrayList();


    public GameDecks() {
        Collections.shuffle(scores);
        score =  scores.subList(0,6);
        Collections.shuffle(finals);
        endScore = finals.subList(0,2);
        Collections.shuffle(roundBooster);
        gameRoundBooster.add(roundBooster.remove(0));
        gameRoundBooster.add(roundBooster.remove(0));
        gameRoundBooster.add(roundBooster.remove(0));
    }

    public List<List<String>> getSectors(int players){
        List<List<String>> sectors = new ArrayList();
        if(players <3) {
            sectors.add(Arrays.asList("1", "5-2"));
            sectors.add(Arrays.asList("2", "3", "6-2"));
            sectors.add(Arrays.asList("4", "7-2"));
            return sectors;
        }
        sectors.add(Arrays.asList("10","1","5"));
        sectors.add(Arrays.asList("9","2","3","6"));
        sectors.add(Arrays.asList("8","4","7"));
        return sectors;
    }

    public void startGame(int amount) {
        Collections.shuffle(advTechs);
        Collections.shuffle(defaultTechs);
        List<Tech> list = new ArrayList<>();
        list.add(new Tech(amount, defaultTechs.remove(0)));
        list.add(new Tech(amount, defaultTechs.remove(0)));
        list.add(new Tech(amount, defaultTechs.remove(0)));
        list.add(new Tech(amount, defaultTechs.remove(0)));
        list.add(new Tech(amount, defaultTechs.remove(0)));
        list.add(new Tech(amount, defaultTechs.remove(0)));
        techs.put("basic", list);
        list = new ArrayList<>();
        list.add(new Tech(amount, defaultTechs.remove(0)));
        list.add(new Tech(amount, defaultTechs.remove(0)));
        list.add(new Tech(amount, defaultTechs.remove(0)));
        techs.put("global", list);
        list = new ArrayList<>();
        list.add(new Tech(1, advTechs.remove(0)));
        list.add(new Tech(1, advTechs.remove(0)));
        list.add(new Tech(1, advTechs.remove(0)));
        list.add(new Tech(1, advTechs.remove(0)));
        list.add(new Tech(1, advTechs.remove(0)));
        list.add(new Tech(1, advTechs.remove(0)));
        techs.put("adv", list);
    }


    public synchronized UserEntity join(String avatar) {
        if (avatars.containsKey(avatar)) {
            UserEntity userEntity = avatars.get(avatar);
            avatars.entrySet().removeIf(entry -> entry.getValue().getColor().equals(userEntity.getColor()));
            gameRoundBooster.add(roundBooster.remove(0));
            return userEntity;
        }
        throw new GameResultException("avatar " + avatar + "no longer avaiable");
    }

    public Set<String> getAvatars() {
        return avatars.keySet();
    }

    public Map<String, List<Tech>> getTechs() {
        return techs;
    }

    public void getTech(String cardId) {
        AtomicBoolean found = new AtomicBoolean();
        techs.values().forEach(type -> {
            type.forEach(tech -> {
                if (tech.cardId.equals(cardId) && tech.amount > 0) {
                    tech.amount--;
                    found.set(true);
                }
            });
        });
        if(!found.get()) {
            throw new GameResultException("tech " + cardId + " no longer avaiable");
        }
    }

    public List<Tech> getAlliances() {
        return alliances;
    }

    public void getAlliance(String alliance) {
        Optional<Tech> optionalAlliance =
                alliances.stream().filter(a -> a.getCardId().equals(alliance) && a.getAmount() > 0).findFirst();
        if (!optionalAlliance.isPresent()) {
            throw new GameResultException("alliance " + alliance + " no longer avaiable");
        }
        Tech tech = optionalAlliance.get();
        int amount = tech.getAmount() - 1;
        tech.setAmount(amount);
    }


}
