package me.foeyii.fdndcore.manager.dice;

import lombok.Getter;
import me.foeyii.fdndcore.FoeyiisDnDCore;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Dice {

    public static final boolean SIDES_MUST_BE_EVEN = true;
    public static final Dice DEFAULT_DICE = new Dice(1, 6);
    private static final HashMap<String, Dice> CACHED_DICE = new HashMap<>();

    @Getter
    private final int count;

    @Getter
    private final int sides;

    @Getter
    private final int modifier;

    public static final int MAX_DICE_COUNTS = 10;
    public static final int MIN_DICE_COUNTS = 0;
    public static final int MAX_DICE_SIDES = 20;
    public static final int MIN_DICE_SIDES = 0;

    private Dice(int count, int sides, int modifier) throws IllegalDiceSidesException {
        if (SIDES_MUST_BE_EVEN && sides % 2 != 0) {
            throw new IllegalDiceSidesException("Invalid sides for Dice: SIDES MUST BE EVEN!");
        }
        this.count = clampCounts(count);
        this.sides = clampSides(sides);
        this.modifier = modifier;
    }

    private Dice(int count, int sides) throws IllegalDiceSidesException {
        this(count, sides, 0);
    }

    public static @NotNull Dice of(int count, int sides) {
        String diceNotation = DiceNotation.stringify(count, sides);
        if (CACHED_DICE.containsKey(diceNotation))
            return CACHED_DICE.get(diceNotation);

        final Dice dice;
        try {
            dice = new Dice(count, sides);
        } catch (IllegalDiceSidesException e) {
            FoeyiisDnDCore.getLOGGER().error("Invalid dice notation '{}': Dice sides must be even!", diceNotation);
            return DEFAULT_DICE;
        }
        CACHED_DICE.put(diceNotation, dice);
        return dice;
    }

    public static @NotNull Dice of(int count, int sides, int modifier) {
        return new Dice(count, sides, modifier);
    }

    public static @NotNull Dice of(String notation) {
        Dice dice;
        try {
            dice = DiceNotation.parse(notation);
        } catch (IllegalDiceSidesException e) {
            dice = DEFAULT_DICE;
            FoeyiisDnDCore.getLOGGER().error("Invalid dice notation '{}'", notation);
        }
        return dice;
    }

    public static @NotNull Dice generateFromInt(int base) {
        if (base <= 0) return of(0, 0);
        if (base < 2) return of(0, 0, base);

        int count = (int) Math.ceil(base / 20.0f);
        count = Math.clamp(count, 1, MAX_DICE_COUNTS);

        int rawSides = base / count;
        int sides = (SIDES_MUST_BE_EVEN && rawSides % 2 != 0) ? rawSides - 1 : rawSides;
        sides = Math.clamp(sides, 0, MAX_DICE_SIDES);

        int diff = base - (count * sides);
        return of(count, sides, diff);
    }

    public int getMinRoll() {
        return count;
    }

    public int getMaxRoll() {
        return count * sides;
    }

    public int roll(net.minecraft.util.RandomSource random) {
        if (sides <= 0 || count <= 0) return 0;
        if (count == 1 && sides == 1) return 1;
        int total = 0;
        for (int i = 0; i < count; i++) {
            total += random.nextInt(sides) + 1;
        }
        return total + modifier;
    }

    public int roll() {
        return roll(RandomSource.create());
    }

    public Dice getPureDice() {
        return of(count, sides, 0);
    }

    public static int clampSides(int value) {
        return Math.clamp(value, MIN_DICE_SIDES, MAX_DICE_SIDES);
    }

    public static int clampCounts(int value) {
        return Math.clamp(value, MIN_DICE_COUNTS, MAX_DICE_COUNTS);
    }

    @Override
    public String toString() {
        return DiceNotation.stringify(this);
    }
}
