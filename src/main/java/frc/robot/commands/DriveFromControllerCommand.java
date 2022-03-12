package frc.robot.commands;

import java.util.function.DoubleSupplier;

import org.ejml.sparse.ComputePermutation;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LiftObserver;

public class DriveFromControllerCommand extends CommandBase {
    private static final double LIFT_UP_MAX_OUTPUT = 0.25;

    private static final double SLOW_FORWARD_RATIO = .2;
    private static final double SLOW_FORWARD_CURVE = 1.5;
    private static final double SLOW_ROTATION_RATIO = .3;
    private static final double SLOW_ROTATION_CURVE = 2;

    private static final double DEFAULT_FOWARD_RATIO = .7;
    private static final double DEFAULT_FORWARD_CURVE = 1.5;
    private static final double DEFAULT_ROTATION_RATIO = .6;
    private static final double DEFAULT_ROTATION_CURVE = 2;

    private final DriveSubsystem driveSubsystem;
    private final LiftObserver liftObserver;
    private final DoubleSupplier forward;
    private final DoubleSupplier rotation;
    private final DoubleSupplier slowModeTrigger;

    /**
     * Constructs a new {@link DriveFromControllerCommand} instance.
     * 
     * @param driveSubsystem The drive subsystem to control.
     * @param liftObserver   Used to read information about the lift subsystem.
     * @param forward        Used to read the forward input.
     * @param rotation       Used to read the rotation input.
     */
    public DriveFromControllerCommand(
            DriveSubsystem driveSubsystem,
            LiftObserver liftObserver,
            DoubleSupplier forward,
            DoubleSupplier rotation,
            DoubleSupplier slowModeTrigger) {
        this.driveSubsystem = driveSubsystem;
        this.liftObserver = liftObserver;
        this.forward = forward;
        this.rotation = rotation;
        this.slowModeTrigger = slowModeTrigger;

        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        updateMaximumOutput();

        //set dummy values if something fails
        double forwardRatio = .6;
        double forwardCurve = 1.5;
        double rotationRatio = .6;
        double rotationCurve = 2;

        if (slowModeTrigger.getAsDouble() > .3) {
            forwardRatio = SLOW_FORWARD_RATIO;
            forwardCurve = SLOW_FORWARD_CURVE;
            rotationRatio = SLOW_ROTATION_RATIO;
            rotationCurve = SLOW_ROTATION_CURVE;
        } else {
            forwardRatio = DEFAULT_FOWARD_RATIO;
            forwardCurve = DEFAULT_FORWARD_CURVE;
            rotationRatio = DEFAULT_ROTATION_RATIO;
            rotationCurve = DEFAULT_ROTATION_CURVE;
        }

        driveSubsystem.arcadeDrive(
                    -computeInputCurve(forwardRatio * forward.getAsDouble(), forwardCurve),
                    computeInputCurve(rotationRatio * rotation.getAsDouble(), rotationCurve));
    }

    //math calculation
    private double computeInputCurve(double rawInput, double power) {
        var sign = rawInput < 0 ? 1 : -1;
        return Math.pow(Math.abs(rawInput), power) * sign;
    }

    //computing using an array of predefined numbers
    private double computeInputCurveOne(double rawInput, double power){
        double computedPower = 0;

        int [] powerPercentage = new int [20]; //tune values as needed
        powerPercentage[0] = 1;
        powerPercentage[1] = 2;
        powerPercentage[2] = 3;
        powerPercentage[3] = 5;
        powerPercentage[4] = 8;
        powerPercentage[5] = 15;
        powerPercentage[6] = 25;
        powerPercentage[7] = 30;
        powerPercentage[8] = 35;
        powerPercentage[9] = 40;
        powerPercentage[10] = 46;
        powerPercentage[11] = 57;
        powerPercentage[12] = 60;
        powerPercentage[13] = 68;
        powerPercentage[14] = 75;
        powerPercentage[15] = 79;
        powerPercentage[16] = 81;
        powerPercentage[17] = 86;
        powerPercentage[18] = 95;
        powerPercentage[19] = 100;

        if (0 < rawInput && rawInput <= .05) {
                computedPower = powerPercentage[0];
            }
            else if (.05 < rawInput && rawInput <= .1) {
                computedPower = powerPercentage[1];
            }
            else if (.1 < rawInput && rawInput <= .15){
                computedPower = powerPercentage[2];
            }
            else if (.15 < rawInput && rawInput <= .2) {
                computedPower = powerPercentage[3];
            }
            else if (.2 < rawInput && rawInput <= .25) {
                computedPower = powerPercentage[4];
            }
            else if (.25 < rawInput && rawInput <= .3) {
                computedPower = powerPercentage[5];
            }
            else if (.3 < rawInput && rawInput <= .35) {
                computedPower = powerPercentage[6];
            }
            else if (.35 < rawInput && rawInput <= .4) {
                computedPower = powerPercentage[7];
            }
            else if (.4 < rawInput && rawInput <= .45) {
                computedPower = powerPercentage[8];
            }
            else if (.45 < rawInput && rawInput <= .5) {
                computedPower = powerPercentage[9];
            }
            else if (.5 < rawInput && rawInput <= .55) {
                computedPower = powerPercentage[10];
            }
            else if (.55 < rawInput && rawInput <= .6) {
                computedPower = powerPercentage[11];
            }
            else if (.6 < rawInput && rawInput <= .65) {
                computedPower = powerPercentage[12];
            }
            else if (.65 < rawInput && rawInput <= .7) {
                computedPower = powerPercentage[13];
            }
            else if (.7 < rawInput && rawInput <= .75) {
                computedPower = powerPercentage[14];
            }
            else if (.75 < rawInput && rawInput <= .8) {
                computedPower = powerPercentage[15];
            }
            else if (.8 < rawInput && rawInput <= .85) {
                computedPower = powerPercentage[16];
            }
            else if (.85 < rawInput && rawInput <= .9) {
                computedPower = powerPercentage[17];
            }
            else if (.9 < rawInput && rawInput <= .95) {
                computedPower = powerPercentage[18];
            }
            else if (.95 < rawInput && rawInput <= 1) {
                computedPower = powerPercentage[19];
            }
            return computedPower;
        }
    

    /**
     * Updates the maximum output of the drive system depending on whether
     * the lift is in its "UP" or "DOWN" positions.
     */
    private void updateMaximumOutput() {
        var liftPosition = liftObserver.getLiftPosition();

        switch (liftPosition) {
            case DOWN:
                // TODO: Update for new DriveTrain Code
                // driveSubsystem.resetMaxOutput();
                break;
            case UP:
                // driveSubsystem.setMaxOutput(LIFT_UP_MAX_OUTPUT);
                break;
            default:
                System.err.println("Unknown lift position '" + liftPosition + "'.");
                break;
        }
    }
}
