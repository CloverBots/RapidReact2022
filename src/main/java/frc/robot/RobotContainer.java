// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AutonomousOne;
import frc.robot.commands.DriveFromControllerCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.TestShooterCommand;
import frc.robot.commands.TestShooterNeoCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LiftObserver;
import frc.robot.subsystems.LiftSubsystemDummy;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TestShooterNeoSubsystem;
import frc.robot.subsystems.TestShooterSubsystem;

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
    private static final double VISION_TARGET_HEIGHT = 78.5; // on test robot
    private static final double CAMERA_HEIGHT = 55.75; // on test robot
    private static final double CAMERA_PITCH = -3.0;

    private final VisionConfiguration visionConfiguration = new VisionConfiguration(
            VISION_TARGET_HEIGHT,
            CAMERA_HEIGHT,
            CAMERA_PITCH);

    private final VisionTargetTracker visionTarget = new VisionTargetTracker(visionConfiguration);

    private final XboxController driverController = new XboxController(Ids.CONTROLLER_DRIVE_PORT);

    private final DriveSubsystem driveSubsystem = new DriveSubsystem();

    // For now, we don't want a working LifSubsystem. So, we are replacing the real
    // LiftSubsystem
    // with a dummy class that says the lift is always down. TF

    // private final LiftSubsystem liftSubsystem = new LiftSubsystem();
    private final LiftObserver liftSubsystem = new LiftSubsystemDummy();

    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final ShooterCommand shooterCommand = new ShooterCommand(shooterSubsystem, visionTarget);

    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();

    private final TestShooterSubsystem testShooterSubsystem = new TestShooterSubsystem();
    private final TestShooterCommand testShooterCommand = new TestShooterCommand(testShooterSubsystem);

    private final TestShooterNeoSubsystem testShooterNeoSubsystem = new TestShooterNeoSubsystem();
    private final TestShooterNeoCommand testShooterNeoCommand = new TestShooterNeoCommand(testShooterNeoSubsystem);

    // By passing in the driverController right trigger to the intakeCommand, the
    // controller value will
    // automatically be fed into the intakeCommand as the speed value.
    private final IntakeCommand intakeCommand = new IntakeCommand(intakeSubsystem,
            driverController::getRightTriggerAxis);

    private final DriveFromControllerCommand driveFromController = new DriveFromControllerCommand(
            driveSubsystem,
            liftSubsystem,
            driverController::getLeftY,
            driverController::getRightX);

    // lifecyclecallbacks used when special cases are needed for autonomous and
    // teleop
    private final RobotLifecycleCallbacks[] robotLifecycleCallbacks = new RobotLifecycleCallbacks[] {
            driveSubsystem
    };

    private final SendableChooser<Command> chooser = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        driveSubsystem.setDefaultCommand(driveFromController);
        testShooterSubsystem.setDefaultCommand(testShooterCommand);
        testShooterNeoSubsystem.setDefaultCommand(testShooterNeoCommand);

        // TODO: Enable when ready (it doesn't work consistantly with no motors
        // connected)
        intakeSubsystem.setDefaultCommand(intakeCommand);

        // Add choices to the chooser
        chooser.setDefaultOption("Autonomous One", new AutonomousOne(driveSubsystem));
        // Add chooser to the SmartDashboard
        SmartDashboard.putData("Automous", chooser);

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
        return chooser.getSelected();
    }

    public RobotLifecycleCallbacks[] getLifecycleCallbacks() {
        return robotLifecycleCallbacks;
    }
}
