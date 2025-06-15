//package net.electrisoma.bloodisfuel.compat.kubejs;
//
//import dev.latvian.mods.kubejs.event.EventJS;
//import net.electrisoma.bloodisfuel.api.equipment.syringe.SyringeFluidType;
//import net.minecraft.resources.ResourceLocation;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//
//public class SyringeFluidTypeEventJS extends EventJS {
//    private final Map<ResourceLocation, SyringeFluidType> types = new LinkedHashMap<>();
//
//    public SyringeFluidTypeBuilderJS add(String id) {
//        ResourceLocation resourceId = new ResourceLocation(id);
//        return new SyringeFluidTypeBuilderJS(type -> {
//            types.put(resourceId, type);
//        });
//    }
//
//    public Map<ResourceLocation, SyringeFluidType> getTypes() {
//        return types;
//    }
//}
