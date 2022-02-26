package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LiftObserver;

public class DriveFromControllerCommand extends CommandBase {
    private static final double LIFT_UP_MAX_OUTPUT = 0.25;

    private static final String SLOW_FORWARD_RATIO_KEY = "Slow foward ratio";
    private static final String SLOW_FOWARD_CURVE_KEY = "Slow foward curve";
    private static final String SLOW_ROTATION_RATIO_KEY = "Slow rotation ratio";
    private static final String SLOW_ROTATION_CURVE_KEY = "Slow rotation curve";
    private static final String DEFAULT_FORWARD_RATIO_KEY = "Default foward ratio";
    private static final String DEFAULT_FOWARD_CURVE_KEY = "Default foward curve";
    private static final String DEFAULT_ROTATION_RATIO_KEY = "Default rotation ratio";
    private static final String DEFAULT_ROTATION_CURVE_KEY = "Default rotation curve";

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

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        SmartDashboard.putNumber(SLOW_FORWARD_RATIO_KEY, .3);
        SmartDashboard.putNumber(SLOW_FOWARD_CURVE_KEY, 1.5);
        SmartDashboard.putNumber(SLOW_ROTATION_RATIO_KEY, .3);
        SmartDashboard.putNumber(SLOW_ROTATION_CURVE_KEY, 2);

        SmartDashboard.putNumber(DEFAULT_FORWARD_RATIO_KEY, .8);
        SmartDashboard.putNumber(DEFAULT_FOWARD_CURVE_KEY, 1.5);
        SmartDashboard.putNumber(DEFAULT_ROTATION_RATIO_KEY, .8);
        SmartDashboard.putNumber(DEFAULT_ROTATION_CURVE_KEY, 2);
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
            forwardRatio = SmartDashboard.getNumber(SLOW_FORWARD_RATIO_KEY, 0);
            forwardCurve = SmartDashboard.getNumber(SLOW_FOWARD_CURVE_KEY, 0);
            rotationRatio = SmartDashboard.getNumber(SLOW_ROTATION_RATIO_KEY, 0);
            rotationCurve = SmartDashboard.getNumber(SLOW_ROTATION_CURVE_KEY, 0);
        } else {
            forwardRatio = SmartDashboard.getNumber(DEFAULT_FORWARD_RATIO_KEY, 0);
            forwardCurve = SmartDashboard.getNumber(DEFAULT_FOWARD_CURVE_KEY, 0);
            rotationRatio = SmartDashboard.getNumber(DEFAULT_ROTATION_RATIO_KEY, 0);
            rotationCurve = SmartDashboard.getNumber(DEFAULT_ROTATION_CURVE_KEY, 0);
        }

        driveSubsystem.arcadeDrive(
                    -computeInputCurve(forwardRatio * forward.getAsDouble(), forwardCurve),
                    computeInputCurve(rotationRatio * rotation.getAsDouble(), rotationCurve));
    }

    private double computeInputCurve(double rawInput, double power) {
        var sign = rawInput < 0 ? 1 : -1;
        return Math.pow(Math.abs(rawInput), power) * sign;
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
