// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.TestShooterNeoSubsystem;

public class TestShooterNeoCommand extends CommandBase {
  private final TestShooterNeoSubsystem testShooterNeoSubsystem;

  /** Creates a new TestShooterNeoCommand. */
  public TestShooterNeoCommand(TestShooterNeoSubsystem testShooterNeoSubsystem) {
    this.testShooterNeoSubsystem = testShooterNeoSubsystem;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(testShooterNeoSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putNumber("rpm", 0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double rpm = SmartDashboard.getNumber("rpm", 0);
    testShooterNeoSubsystem.setShooterRPM(rpm);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
