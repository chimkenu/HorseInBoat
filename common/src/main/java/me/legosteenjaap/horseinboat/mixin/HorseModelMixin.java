package me.legosteenjaap.horseinboat.mixin;

import com.mojang.math.Constants;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseModel.class)
public abstract class HorseModelMixin<T extends AbstractHorse> extends AgeableListModel<T> {

    float legHindRollRotBoat = Constants.PI * -0.7f;
    float legHindyRotRotBoat = Constants.PI * 0.1f;
    float legHindYBoat = 19.25f;
    float legHindZBoat = -1f;

    float legFrontRollRotBoat = Constants.PI * -0.25f;
    float legFrontyRotRotBoat = Constants.PI * 0f;
    float legFrontYBoat = 0.25f;
    float legFrontZBoat = -1.75f;

    float bodyRotBoat = Constants.PI * -0.5f;
    float bodyYBoat = 15.25f;
    float bodyZBoat = -1f;

    float headYBoat = -1.75f;
    float headZBoat = 0.5f;
    float headYRotBoat = Constants.PI * 0f;
    float headXRotBoat = 0.5235988f;

    boolean updatedToNormalModel;

    @Shadow @Final
    private ModelPart rightFrontLeg;
    @Shadow @Final
    private ModelPart leftFrontLeg;
    @Shadow @Final
    private ModelPart rightHindLeg;
    @Shadow @Final
    private ModelPart leftHindLeg;
    @Shadow @Final
    private ModelPart rightHindBabyLeg;
    @Shadow @Final
    private ModelPart leftHindBabyLeg;
    @Shadow @Final
    protected ModelPart body;
    @Shadow @Final
    protected ModelPart headParts;
    @Shadow @Final
    private ModelPart tail;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;FFFFF)V", at = @At("RETURN"))
    public void setupAnim(T horse, float f, float g, float h, float headyRot, float bodyyRot, CallbackInfo ci) {
        Boat boat = null;
        if (!horse.isBaby() && horse.isPassenger() && horse.getVehicle() instanceof Boat) boat = (Boat) horse.getVehicle();
        if (boat != null && boat.getPassengers().size() == 2) {
            this.body.y =  bodyYBoat;
        }
    }

    @Inject(method = "prepareMobModel(Lnet/minecraft/world/entity/animal/horse/AbstractHorse;FFF)V", at = @At("RETURN"))
    public void prepareMobModel(T abstractHorse, float f, float g, float h, CallbackInfo ci) {
        Boat boat = null;
        if (!abstractHorse.isBaby() && abstractHorse.isPassenger() && abstractHorse.getVehicle() instanceof Boat) boat = (Boat)abstractHorse.getVehicle();
        if (!abstractHorse.isBaby() && boat != null && (boat.getMaxPassengers() == 1 || boat.getPassengers().size() == 2)) {
            //HEAD
            this.headParts.y = headYBoat;
            this.headParts.z = headZBoat;

            //BODY
            this.body.y = bodyYBoat;
            this.body.z = bodyZBoat;
            this.body.xRot = bodyRotBoat;

            //HIND LEG
            this.leftHindLeg.xRot = legHindRollRotBoat;
            this.rightHindLeg.xRot = legHindRollRotBoat;
            if (!(boat.getMaxPassengers() == 1) && boat.getPassengers().get(0) instanceof AbstractHorse && boat.getPassengers().get(1) instanceof AbstractHorse) {
                this.leftHindLeg.yRot = -legHindyRotRotBoat * 0.25f;;
                this.rightHindLeg.yRot = legHindyRotRotBoat * 0.25f;;
            } else {
                this.leftHindLeg.yRot = -legHindyRotRotBoat;
                this.rightHindLeg.yRot = legHindyRotRotBoat;
            }
            leftHindLeg.y = legHindYBoat;
            rightHindLeg.y = legHindYBoat;
            leftHindLeg.z = legHindZBoat;
            rightHindLeg.z = legHindZBoat;

            //FRONT LEG
            this.leftFrontLeg.xRot = legFrontRollRotBoat;
            this.rightFrontLeg.xRot = legFrontRollRotBoat;
            this.leftFrontLeg.yRot = -legFrontyRotRotBoat;
            this.rightFrontLeg.yRot = legFrontyRotRotBoat;
            leftFrontLeg.y = legFrontYBoat;
            rightFrontLeg.y = legFrontYBoat;
            leftFrontLeg.z = legFrontZBoat;
            rightFrontLeg.z = legFrontZBoat;

            //TAIL
            this.tail.visible = false;

            updatedToNormalModel = false;
        } else if (!updatedToNormalModel) {
            //HEAD
            this.headParts.y = 4.0f;
            this.headParts.z = -12.0f;

            //BODY
            this.body.y = 0.0f;
            this.body.z = 5.0f;

            //HIND LEG
            this.leftHindLeg.yRot = 0;
            this.rightHindLeg.yRot = 0;
            leftHindLeg.y = leftHindBabyLeg.y = 14.0f;
            rightHindLeg.y = rightHindBabyLeg.y = 14.0f;
            leftHindLeg.z = leftHindBabyLeg.z = 7.0f;
            rightHindLeg.z = rightHindBabyLeg.z = 7.0f;

            //FRONT LEG
            this.leftFrontLeg.yRot = 0;
            this.rightFrontLeg.yRot = 0;
            leftFrontLeg.y = 14.0f;
            rightFrontLeg.y = 14.0f;
            leftFrontLeg.z = -10.0f;
            rightFrontLeg.z = -10.0f;

            //TAIL
            this.tail.visible = true;

            updatedToNormalModel = true;
        }
        if (!abstractHorse.isBaby() && boat != null) {
            //Disables all head animations while in a boat
            this.headParts.xRot = headXRotBoat;
            this.headParts.yRot = headYRotBoat;
        }
    }

}
