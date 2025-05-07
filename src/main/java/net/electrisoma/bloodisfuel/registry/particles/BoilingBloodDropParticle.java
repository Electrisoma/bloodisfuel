package net.electrisoma.bloodisfuel.registry.particles;

import net.minecraft.util.Mth;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;


public class BoilingBloodDropParticle extends TextureSheetParticle {

    private final SpriteSet sprites;
    private int onGroundTime;
    public static final float particleSizeMin = 0.8F;
    public static final float particleSizeMax = 1.0F;

    protected BoilingBloodDropParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {

        super(level, x, y, z,
                0.0D,
                0.0D,
                0.0D
        );

        this.friction = 0.96F;
        this.gravity = 0;
        this.speedUpWhenYMotionIsBlocked = true;
        this.sprites = sprites;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        float scale = particleSizeMin + random.nextFloat() * (particleSizeMax - particleSizeMin);
        this.quadSize *= scale;
        this.lifetime = 100 + random.nextInt(40);
        this.lifetime = Math.max(this.lifetime, 1);
        this.setSpriteFromAge(sprites);
        this.hasPhysics = true;
    }

    public float getQuadSize(float f) {

        return this.quadSize
                * Mth.clamp(((float) this.age + f)
                / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {

        if (this.age < this.lifetime * 0.25F) this.gravity = 0;
        else {
            this.gravity = 1F;
            if (onGround) onGroundTime++;
        }

        int sprite = this.onGround ? 1 : 0;
        this.setSprite(sprites.get(sprite, 1));

        if (onGroundTime > 5) {
            this.remove();
            this.level.addParticle(ParticleTypes.SMOKE.getType(), x, y, z,
                    0,
                    0,
                    0
            );
        }

        super.tick();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public int getLightColor(float partialTicks) {
        return 240;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {

            return new BoilingBloodDropParticle(worldIn, x, y, z, spriteSet);
        }
    }
}