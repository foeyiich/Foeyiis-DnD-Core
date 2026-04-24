package me.foeyii.fdndcore.dice;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DiceNotation {

    private DiceNotation() { /* Utility class */ }

    public static final String TEMPLATE = "<dice_count>d<dice_sides>";
    private static final String P_COUNT = "<dice_count>";
    private static final String P_SIDES = "<dice_sides>";

    private static final Pattern CACHED_PATTERN;
    private static final boolean COUNT_FIRST;

    static {
        COUNT_FIRST = TEMPLATE.indexOf(P_COUNT) < TEMPLATE.indexOf(P_SIDES);

        String regex = Pattern.quote(TEMPLATE)
                .replace(P_COUNT, "\\E(\\d+)\\Q")
                .replace(P_SIDES, "\\E(\\d+)\\Q");

        CACHED_PATTERN = Pattern.compile("^" + regex + "([+-]\\d+)?$");
    }

    public static @NotNull Dice parse(@NotNull String notation) {
        if (notation.isEmpty()) return Dice.EMPTY;

        Matcher matcher = CACHED_PATTERN.matcher(notation);
        if (matcher.find()) {
            try {
                int v1 = Integer.parseInt(matcher.group(1));
                int v2 = Integer.parseInt(matcher.group(2));

                int mod = (matcher.group(3) != null)
                        ? Integer.parseInt(matcher.group(3).replace("+", ""))
                        : 0;

                int count = COUNT_FIRST ? v1 : v2;
                int sides = COUNT_FIRST ? v2 : v1;

                return Dice.of(count, sides, mod);
            } catch (Exception e) {
                e.printStackTrace();
                return Dice.EMPTY;
            }
        }
        return Dice.EMPTY;
    }

    public static @NotNull String stringify(int count, int sides, int modifier) {
        if (count > 0 && sides > 0) {
            String res = TEMPLATE
                    .replace(P_COUNT, String.valueOf(count))
                    .replace(P_SIDES, String.valueOf(sides));

            if (modifier != 0) {
                res += (modifier > 0 ? "+" : "") + modifier;
            }
            return res;
        }

        if (modifier == 0) return "0";

        return (modifier > 0 ? "+" : "") + modifier;
    }

    public static @NotNull String stringify(int count, int sides) {
        return stringify(count, sides, 0);
    }

    public static @NotNull String stringify(@NotNull Dice dice) {
        return stringify(dice.count(), dice.sides(), dice.modifier());
    }

}