package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.VisionTargetTracker;
import frc.robot.VisionTargetTracker.LedMode;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends CommandBase {

    private static final double SHOOTER_SPEED = 0.5; // TODO Find shooter speed

    private final ShooterSubsystem shooterSubsystem;
    private final VisionTargetTracker visionTarget;

    /** Creates a new ShootBall. */
    public ShooterCommand(ShooterSubsystem shooterSubsystem, VisionTargetTracker visionTarget) {
        this.shooterSubsystem = shooterSubsystem;
        this.visionTarget = visionTarget;

        //By adding requirement to the shooterSubsystem, WPI will ensure that if another
        // system is using the motors, this command will supersede control of the motor.
        // This prevents multiple commands from trying to control the same motor.
        addRequirements(shooterSubsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        visionTarget.setLedMode(LedMode.FORCE_ON);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (visionTarget.isValid()) {
            // Distance is in inches
            double distance = visionTarget.computeTargetDistance();
            //System.out.println(distance);

            // Dummy formula for testing
            if (distance > 100) {
                shooterSubsystem.startShooter(SHOOTER_SPEED);
            } else {
                shooterSubsystem.stop();
            }
        } else {
            shooterSubsystem.stop();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        visionTarget.setLedMode(LedMode.FORCE_OFF);
        shooterSubsystem.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
