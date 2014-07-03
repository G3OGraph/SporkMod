package in.parapengu.spork.scoreboard;

import in.parapengu.spork.util.Chars;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScoreboardObjective {

	public Chars character();

	public Chars complete();

}
