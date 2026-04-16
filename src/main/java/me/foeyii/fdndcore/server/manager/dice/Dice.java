package me.foeyii.fdndcore.server.manager.dice;

import lombok.Getter;
import me.foeyii.fdndcore.FoeyiisDnDCore;
import net.minecraft.util.RandomSource;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dice object that has a rollCount and Sides.
 * <p>
 * Use {@link #of(String)} or {@link #of(int, int)} instead.
 * </p>
 */
public final class Dice {

    // Count of dices (first number)
    @Getter
    private final int count;

    // Count of dice's sides (last number)
    @Getter
    private final int sides;

    private static final HashMap<String, Dice> dices = new HashMap<>();

    public static final int MAX_DICE_COUNTS = 10;
    public static final int MIN_DICE_COUNTS = 0;
    public static final int MAX_DICE_SIDES = 20;
    public static final int MIN_DICE_SIDES = 0;

    private static final Pattern FORMATTED_DICE_PATTERN = Pattern.compile("^(\\d+)d(\\d+)$");

    private Dice(int count, int sides) {
        this.count = Math.clamp(count, MIN_DICE_COUNTS, MAX_DICE_COUNTS);
        this.sides = Math.clamp(sides, MIN_DICE_SIDES, MAX_DICE_SIDES);
    }

    public record ParseResult(int count, int sides, boolean success) {
    }

    public static ParseResult parseFormatted(String formattedDice) {
        Matcher matcher = FORMATTED_DICE_PATTERN.matcher(formattedDice);

        if (matcher.find()) {
            try {
                int count = Math.clamp(Integer.parseInt(matcher.group(1)), MIN_DICE_COUNTS, MAX_DICE_COUNTS);
                int sides = Math.clamp(Integer.parseInt(matcher.group(2)), MIN_DICE_SIDES, MAX_DICE_SIDES);
                return new ParseResult(count, sides, true);
            } catch (NumberFormatException e) {
                FoeyiisDnDCore.getLOGGER().error("Invalid formatted dice '{}'. Returning 1d6", formattedDice);
                return new ParseResult(1, 6, false);
            }
        }

        return new ParseResult(1, 6, false);
    }

    public static Dice of(int count, int sides) {
        String formattedDice = count + "d" + sides;
        if (dices.containsKey(formattedDice))
            return dices.get(count + "d" + sides);
        Dice dice = new Dice(count, sides);
        dices.put(formattedDice, dice);
        return dice;
    }

    public static Dice of(String formattedDice) {
        ParseResult parseResult = parseFormatted(formattedDice);
        return of(parseResult.count(), parseResult.sides());
    }

    public static Dice calculateFromInt(int base) {
        if (base <= 0) return Dice.of("0d0");
        int rollCount = (int) Math.ceil(base / 20.0f);
        rollCount = Math.clamp(rollCount, MIN_DICE_COUNTS, MAX_DICE_COUNTS);
        int sides = base / rollCount;
        sides = Math.clamp(sides, MIN_DICE_SIDES, MAX_DICE_SIDES);
        return Dice.of(rollCount, sides);
    }

    public static Dice plus(Dice dice1, Dice dice2) {
        return calculateFromInt(dice1.getMaxRoll() + dice2.getMaxRoll());
    }

    public static Dice minus(Dice dice1, Dice dice2) {
        return calculateFromInt(dice1.getMaxRoll() - dice2.getMaxRoll());
    }

    public int getMinRoll() {
        return count;
    }

    public int getMaxRoll() {
        return count * sides;
    }

    public int roll(net.minecraft.util.RandomSource random) {
        if (sides <= 0 || count <= 0) return 0;
        int total = 0;
        for (int i = 0; i < count; i++) {
            total += random.nextInt(sides) + 1;
        }
        return total;
    }

    public int roll() {
        return roll(RandomSource.create());
    }

    @Override
    public String toString() {
        return count + "d" + sides;
    }

    public static int clamp(int value) {
        return Math.clamp(value, MIN_DICE_SIDES, MAX_DICE_SIDES);
    }

}
