package net.electrisoma.bloodisfuel.registry.particles;

import net.minecraft.util.Mth;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;


public class BloodDropParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private int onGroundTime;

    public static final float SIZE_MIN = 0.8F;
    public static final float SIZE_MAX = 1.0F;

    protected BloodDropParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.sprites = sprites;

        this.friction = 0.96F;
        this.gravity = 0;
        this.speedUpWhenYMotionIsBlocked = true;
        this.hasPhysics = true;

        // Set scale
        float scale = SIZE_MIN + random.nextFloat() * (SIZE_MAX - SIZE_MIN);
        this.quadSize *= scale;

        // Lifetime range: 100–139 ticks
        this.lifetime = Math.max(100 + random.nextInt(40), 1);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        // Delay gravity for a short time
        if (age < lifetime * 0.25F) gravity = 0;
        else {
            gravity = 1F;
            if (onGround) onGroundTime++;
        }

        // Sprite swap based on ground contact
        this.setSprite(sprites.get(onGround ? 1 : 0, 1));

        if (onGroundTime > 5) remove();

        super.tick();
    }

    @Override
    public float getQuadSize(float partialTick) {
        float progress = ((float) age + partialTick) / (float) lifetime;
        return quadSize * Mth.clamp(progress * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new BloodDropParticle(world, x, y, z, spriteSet);
        }
    }
}
