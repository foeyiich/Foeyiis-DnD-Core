package me.foeyii.fdndcore.manager.dice;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceNotation {

    private DiceNotation() {
    }

    public static final String TEMPLATE = "<dice_count>d<dice_sides>";
    public static final String COUNT_PLACEHOLDER = "<dice_count>";
    public static final String SIDES_PLACEHOLDER = "<dice_sides>";

    private static Pattern cachedPattern;
    private static boolean countFirstInTemplate;

    static {
        updatePattern();
    }

    private static void updatePattern() {
        countFirstInTemplate = TEMPLATE.indexOf(COUNT_PLACEHOLDER) < TEMPLATE.indexOf(SIDES_PLACEHOLDER);

        String regex = Pattern.quote(TEMPLATE)
                .replace(COUNT_PLACEHOLDER, "\\E(\\d+)\\Q")
                .replace(SIDES_PLACEHOLDER, "\\E(\\d+)\\Q");

        cachedPattern = Pattern.compile("^" + regex + "([+-]\\d+)?$");
    }

    public static boolean isValid(String notation) {
        if (notation == null) return false;
        return cachedPattern.matcher(notation).matches();
    }

    public static @NotNull Dice parse(@NotNull String notation) throws InvalidDiceNotationException {
        if (notation.isEmpty())
            throw new InvalidDiceNotationException("Notation is empty!");
        Matcher matcher = cachedPattern.matcher(notation);

        if (!isValid(notation)) {
            throw new InvalidDiceNotationException(notation);
        }

        if (matcher.find()) {
            int val1 = Integer.parseInt(matcher.group(1));
            int val2 = Integer.parseInt(matcher.group(2));

            int count = countFirstInTemplate ? val1 : val2;
            int sides = countFirstInTemplate ? val2 : val1;

            int modifier = 0;
            if (matcher.group(3) != null) {
                modifier = Integer.parseInt(matcher.group(3).replace("+", ""));
            }

            return Dice.of(count, sides, modifier);
        }

        throw new InvalidDiceNotationException("No match found for: " + notation);
    }

    public static @NotNull String stringify(int count, int sides, int modifier) {
        String result = TEMPLATE
                .replace(COUNT_PLACEHOLDER, String.valueOf(count))
                .replace(SIDES_PLACEHOLDER, String.valueOf(sides));
        if (modifier != 0) {
            String sign = (modifier > 0) ? "+" : "";
            result += sign + modifier;
        }

        return result;
    }

    public static @NotNull String stringify(int count, int sides) {
        return stringify(count, sides, 0);
    }

    public static @NotNull String stringify(Dice dice) {
        return stringify(dice.getCount(), dice.getSides(), dice.getModifier());
    }

}