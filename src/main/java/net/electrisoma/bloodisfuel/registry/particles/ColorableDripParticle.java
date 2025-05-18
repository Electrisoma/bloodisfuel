package net.electrisoma.bloodisfuel.registry.particles;

import net.electrisoma.bloodisfuel.api.data.ColorableDripParticleData;

import net.minecraft.util.Mth;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;


/**
 * A particle that drips and supports custom RGB coloring.
 */
public class ColorableDripParticle extends TextureSheetParticle {
    private static final float SIZE_MIN = 0.6F;
    private static final float SIZE_MAX = 1.0F;
    private static final int MIN_LIFETIME = 1;
    private static final int MAX_LIFETIME = 75;
    private static final int LIFETIME_VARIANCE = 50;
    private static final int GROUND_TICKS_BEFORE_REMOVAL = 5;

    private final SpriteSet sprites;
    private int onGroundTime = 0;
    private final float floatTime;

    protected ColorableDripParticle(ClientLevel level, double x, double y, double z,
                                    double red, double green, double blue,
                                    SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;

        this.friction = 0.96F;
        this.gravity = 0.0F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.hasPhysics = true;

        this.rCol = (float) red;
        this.gCol = (float) green;
        this.bCol = (float) blue;

        float scale = SIZE_MIN + level.random.nextFloat() * (SIZE_MAX - SIZE_MIN);
        this.quadSize *= scale;
        this.lifetime = Mth.clamp(MIN_LIFETIME + level.random.nextInt(LIFETIME_VARIANCE), 1, MAX_LIFETIME);
        this.floatTime = 0.1F + level.random.nextFloat() * 0.4F;

        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        if (age > lifetime * floatTime) gravity = 1.0F;
        if (onGround) onGroundTime++;
        if (onGroundTime > GROUND_TICKS_BEFORE_REMOVAL) remove();
        this.setSprite(sprites.get(onGround ? 1 : 0, 1));

        super.tick();
    }

    @Override
    public float getQuadSize(float partialTick) {
        float progress = ((float) age + partialTick) / (float) lifetime;
        return quadSize * Mth.clamp(progress * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<ColorableDripParticleData> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(ColorableDripParticleData data, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new ColorableDripParticle(
                    level, x, y, z,
                    data.r(), data.g(), data.b(),
                    spriteSet
            );
        }
    }
}