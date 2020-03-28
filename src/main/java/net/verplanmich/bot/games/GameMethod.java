package net.verplanmich.bot.games;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GameMethod {
    public GameMethodType type() default GameMethodType.Image;
}
