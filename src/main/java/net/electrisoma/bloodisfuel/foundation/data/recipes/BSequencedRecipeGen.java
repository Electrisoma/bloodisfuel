package net.electrisoma.bloodisfuel.foundation.data.recipes;

import net.electrisoma.bloodisfuel.BloodIsFuel;
import net.electrisoma.bloodisfuel.registry.BItems;

import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;

import net.minecraft.data.PackOutput;

import java.util.function.UnaryOperator;


@SuppressWarnings("unused")
public class BSequencedRecipeGen extends BRecipeProvider {
    GeneratedRecipe

    SYRINGE_BLADE = create("syringe_blade", b -> b
            .require(I.ironSword())
            .transitionTo(BItems.INCOMPLETE_SYRINGE_BLADE.get())
            .addOutput(BItems.SYRINGE_BLADE.get(), 1)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.bottle()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.copperSheet()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.electronTube()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.precisionMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.brassNugget()))
            .addStep(PressingRecipe::new, rb -> rb)),
    SYRINGE_GUN = create("syringe_gun", b -> b
            .require(I.crossbow())
            .transitionTo(BItems.INCOMPLETE_SYRINGE_GUN.get())
            .addOutput(BItems.SYRINGE_GUN.get(), 1)
            .loops(1)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.bottle()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.copperSheet()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.electronTube()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.precisionMechanism()))
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(I.brassSheet()))
            .addStep(PressingRecipe::new, rb -> rb))

    ;

    public BSequencedRecipeGen(PackOutput p_i48262_1_) {
        super(p_i48262_1_);
    }

    protected BRecipeProvider.GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        BRecipeProvider.GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(BloodIsFuel.asResource(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public String getName() {
        return BloodIsFuel.NAME + "'s Sequenced Assembly Recipes";
    }
}
