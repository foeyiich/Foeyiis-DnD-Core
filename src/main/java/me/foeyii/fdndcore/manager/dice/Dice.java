package me.foeyii.fdndcore.manager.dice;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import me.foeyii.fdndcore.DnDCore;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public record Dice(int count, int sides, int modifier) {

    public static final Codec<Dice> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("count").forGetter(Dice::count),
                    Codec.INT.fieldOf("sides").forGetter(Dice::sides),
                    Codec.INT.optionalFieldOf("modifier", 0).forGetter(Dice::modifier)
            ).apply(instance, Dice::of)
    );

    private static class CACHE {
        private static final Long2ObjectMap<Dice> cacheMap = new Long2ObjectOpenHashMap<>();

        private static long getKey(int count, int sides, int modifier) {
            return ((long) (count & 0xFF) << 24) |
                    ((long) (sides & 0xFF) << 16) |
                    (modifier & 0xFFFF);
        }
    }

    public static final Dice FULL = Dice.of(1, 20, 0);
    public static final Dice EMPTY = Dice.of(0, 0, 0);

    public static final boolean SIDES_MUST_BE_EVEN = true;
    public static final int MAX_DICE_COUNTS = 10;
    public static final int MAX_DICE_SIDES = 20;

    public Dice {
        if (count == 0)
            sides = 0;
        if (sides == 0)
            count = 0;

        count = Math.clamp(count, 0, MAX_DICE_COUNTS);
        sides = Math.clamp(sides, 0, MAX_DICE_SIDES);

        if (SIDES_MUST_BE_EVEN && sides % 2 != 0) {
            int oldSides = sides;
            sides++;
            DnDCore.LOGGER.warn("Invalid sides for Dice ({}). returning with {} sides", oldSides, sides);
        }
    }

    public Dice(int count, int sides) {
        this(count, sides, 0);
    }

    public static @NotNull Dice of(int count, int sides, int modifier) {
        return CACHE.cacheMap.computeIfAbsent(CACHE.getKey(count, sides, modifier), k -> new Dice(
                Math.clamp(count, 0, MAX_DICE_COUNTS),
                Math.clamp(sides, 0, MAX_DICE_SIDES),
                modifier
        ));
    }

    public static Dice of(int count, int sides) {
        return of(count, sides, 0);
    }

    public int roll(RandomSource random) {
        if (sides <= 0 || count <= 0) return modifier;

        int total = 0;
        for (int i = 0; i < count; i++) {
            total += random.nextInt(sides) + 1;
        }
        return total + modifier;
    }

    public int roll() {
        return roll(RandomSource.create());
    }

    public static @NotNull Dice from(int base) {
        if (base <= 0) return Dice.EMPTY;
        if (base == 1) return Dice.of(0, 0, 1);

        int count = Math.clamp(
                (int) Math.ceil((float) base / MAX_DICE_SIDES),
                1,
                MAX_DICE_COUNTS
        );

        int sides = Math.clamp(base / count, 2, MAX_DICE_SIDES);
        int modifier = 0;

        int maxRoll = count * sides;
        if (base - maxRoll > 0)
            modifier = base - maxRoll;

        return Dice.of(count, sides, modifier);
    }


    public boolean isEmpty() {
        return count == 0 && sides == 0;
    }

    public int getMaxRoll() {
        return (count * sides) + modifier;
    }

    public int getMinRoll() {
        return count + modifier;
    }

    @Override
    public @NotNull String toString() {
        return DiceNotation.stringify(this);
    }

}