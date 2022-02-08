package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LiftObserver;

public class DriveFromControllerCommand extends CommandBase {
    private static final double LIFT_UP_MAX_OUTPUT = 0.25;

    private final DriveSubsystem driveSubsystem;
    private final LiftObserver liftObserver;
    private final DoubleSupplier forward;
    private final DoubleSupplier rotation;

    /**
     * Constructs a new {@link DriveFromControllerCommand} instance.
     * @param driveSubsystem The drive subsystem to control.
     * @param liftObserver Used to read information about the lift subsystem.
     * @param forward Used to read the forward input.
     * @param rotation Used to read the rotation input.
     */
    public DriveFromControllerCommand(
            DriveSubsystem driveSubsystem,
            LiftObserver liftObserver,
            DoubleSupplier forward,
            DoubleSupplier rotation) {
        this.driveSubsystem = driveSubsystem;
        this.liftObserver = liftObserver;
        this.forward = forward;
        this.rotation = rotation;

        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        updateMaximumOutput();
        driveSubsystem.arcadeDrive(forward.getAsDouble(), -rotation.getAsDouble());
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
