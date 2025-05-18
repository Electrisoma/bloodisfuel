package net.electrisoma.bloodisfuel.registry.particles;

import net.minecraft.util.Mth;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;


public class BoilingBloodDropParticle extends TextureSheetParticle {

    private static final float SIZE_MIN = 0.8F;
    private static final float SIZE_MAX = 1.0F;

    private final SpriteSet sprites;
    private int onGroundTime;

    protected BoilingBloodDropParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, 0.0D, 0.0D, 0.0D);
        this.sprites = sprites;

        this.friction = 0.96F;
        this.gravity = 0F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.hasPhysics = true;

        this.xd = 0;
        this.yd = 0;
        this.zd = 0;

        float scale = SIZE_MIN + random.nextFloat() * (SIZE_MAX - SIZE_MIN);
        this.quadSize *= scale;

        this.lifetime = Math.max(100 + random.nextInt(40), 1);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        if (this.age < this.lifetime * 0.25F) this.gravity = 0F;
        else {
            this.gravity = 1F;
            if (onGround) onGroundTime++;
        }

        this.setSprite(sprites.get(this.onGround ? 1 : 0, 1));
        if (onGroundTime > 5) {
            this.remove();
            level.addParticle(ParticleTypes.SMOKE.getType(), x, y, z, 0, 0, 0);
        }

        super.tick();
    }

    @Override
    public float getQuadSize(float partialTicks) {
        float progress = ((float) this.age + partialTicks) / (float) this.lifetime * 32.0F;
        return this.quadSize * Mth.clamp(progress, 0.0F, 1.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
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
            return new BoilingBloodDropParticle(world, x, y, z, spriteSet);
        }
    }
}
