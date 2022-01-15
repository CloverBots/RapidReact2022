package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends CommandBase {
  IntakeSubsystem intakeSubsystem;
  DoubleSupplier speed;

  /** Creates a new ShootBall. */
  public IntakeCommand(IntakeSubsystem intakeSubsystem, DoubleSupplier speed) {
    this.intakeSubsystem = intakeSubsystem;
    this.speed = speed;
    addRequirements(intakeSubsystem);

    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intakeSubsystem.startIntake(speed.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}