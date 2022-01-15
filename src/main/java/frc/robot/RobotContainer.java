// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.commands.DriveFromControllerCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LiftObserver;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.LiftSubsystemDummy;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    private final XboxController driverController = new XboxController(Ids.CONTROLLER_DRIVE_PORT);

    private final DriveSubsystem driveSubsystem = new DriveSubsystem();
    
    // For now, we don't want a working LifSubsystem. So, we are replacing the real LiftSubsystem
    // with a dummy class that says the lift is always down. TF

    //private final LiftSubsystem liftSubsystem = new LiftSubsystem();
    private final LiftObserver liftSubsystem = new LiftSubsystemDummy();

    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final ShooterCommand shooterCommand = new ShooterCommand(shooterSubsystem);

    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    private final IntakeCommand intakeCommand = new IntakeCommand(intakeSubsystem, driverController::getRightTriggerAxis);

    private final DriveFromControllerCommand driveFromController = new DriveFromControllerCommand(
            driveSubsystem,
            liftSubsystem,
            driverController::getLeftY,
            driverController::getRightX);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        driveSubsystem.setDefaultCommand(driveFromController);

        intakeSubsystem.setDefaultCommand(intakeCommand);

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be
     * created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
     * it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        JoystickButton shootButton = new JoystickButton(driverController, XboxController.Button.kY.value);
        shootButton.whileHeld(shooterCommand);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An ExampleCommand will run in autonomous
        return null;
    }
}
