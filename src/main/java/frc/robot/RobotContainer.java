// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AimHighCommand;
import frc.robot.commands.AutonomousLeftMiddleCommand;
import frc.robot.commands.AutonomousOne;
import frc.robot.commands.DriveFromControllerCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.IntakeCommand.IntakeConfig;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.LiftCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;
import frc.robot.subsystems.UpperFeederSubsystem;
import frc.robot.subsystems.IntakeDeploySubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

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

    private final VisionTargetTracker visionTargetTracker = new VisionTargetTracker(visionConfiguration);

    private final XboxController driverController = new XboxController(Ids.CONTROLLER_DRIVE_PORT);
    private final XboxController operatorController = new XboxController(Ids.CONTROLLER_OPERATOR_PORT);

    private final DriveSubsystem driveSubsystem = new DriveSubsystem();

    // For now, we don't want a working LifSubsystem. So, we are replacing the real
    // LiftSubsystem with a dummy class that says the lift is always down.

    private final LiftSubsystem liftSubsystem = new LiftSubsystem();
    // private final LiftObserver liftSubsystem = new LiftSubsystemDummy();

    private final IntakeDeploySubsystem intakeDeploySubsystem = new IntakeDeploySubsystem();
    private final IntakeSubsystem intakeSubsystem;
    private final UpperFeederSubsystem upperFeederSubsystem = new UpperFeederSubsystem();
    private final LowerFeederSubsystem lowerFeederSubsystem = new LowerFeederSubsystem();

    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final ShooterCommand shooterCommand = new ShooterCommand(shooterSubsystem,
            visionTargetTracker);

    private final AimHighCommand aimHighCommand = new AimHighCommand(driveSubsystem, visionTargetTracker);

    // By passing in the driverController right trigger to the intakeCommand, the
    // controller value will
    // automatically be fed into the intakeCommand as the speed value.

    private final IntakeCommand intakeInCommand;
    // Use an InstantCommand since all we need is to run a
    // intakeSubsystem.startIntake method and don't
    // need a whole command class for this.
    private final Command upperFeederOutCommand;

    private final Command feedShooterCommand = new InstantCommand(() -> {
        upperFeederSubsystem.setSpeed(1);
        lowerFeederSubsystem.setSpeed(1);
    }, upperFeederSubsystem, lowerFeederSubsystem);

    private final Command stopFeedShooterCommand = new InstantCommand(() -> {
        upperFeederSubsystem.setSpeed(0);
        lowerFeederSubsystem.setSpeed(0);
    }, upperFeederSubsystem, lowerFeederSubsystem);

    private Command intakeDeployCommand = new InstantCommand(() -> intakeDeploySubsystem.setSolenoid(true),
            intakeDeploySubsystem);
    private Command intakeRetractCommand = new InstantCommand(() -> intakeDeploySubsystem.setSolenoid(false),
            intakeDeploySubsystem);

    private final LiftCommand liftCommand = new LiftCommand(liftSubsystem, operatorController::getLeftTriggerAxis,
            operatorController::getLeftY);

    private final DriveFromControllerCommand driveFromController = new DriveFromControllerCommand(
            driveSubsystem,
            liftSubsystem,
            driverController::getLeftY,
            driverController::getRightX);

    // lifecyclecallbacks used when special cases are needed for autonomous and
    // teleop
    private final RobotLifecycleCallbacks[] robotLifecycleCallbacks = new RobotLifecycleCallbacks[] {
            driveSubsystem,
            intakeDeploySubsystem
    };

    private final SendableChooser<Command> chooser = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        intakeSubsystem = new IntakeSubsystem(intakeDeploySubsystem);

        intakeInCommand = new IntakeCommand(intakeSubsystem, lowerFeederSubsystem,
                IntakeConfig.TWO_BALLS, 1, operatorController::getRightTriggerAxis);

        upperFeederOutCommand = new InstantCommand(() -> {
            upperFeederSubsystem.setSpeed(-1);
        }, upperFeederSubsystem);

        driveSubsystem.setDefaultCommand(driveFromController);
        liftSubsystem.setDefaultCommand(liftCommand);

        // TODO: Enable when ready (it doesn't work consistantly with no motors
        // connected)

        // Add choices to the chooser
        chooser.addOption("Autonomous One", new AutonomousOne(driveSubsystem));
        chooser.setDefaultOption("Autonomous Left Middle", new AutonomousLeftMiddleCommand(
                driveSubsystem,
                intakeSubsystem,
                intakeDeploySubsystem,
                lowerFeederSubsystem,
                upperFeederSubsystem,
                shooterSubsystem,
                visionTargetTracker));
        // Add chooser to the SmartDashboard
        SmartDashboard.putData("Autonomous One", chooser);
        SmartDashboard.putData("Autonomous Left Middle", chooser);

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
        JoystickButton shootButton = new JoystickButton(operatorController, XboxController.Button.kB.value);
        shootButton.whileHeld(shooterCommand);

        JoystickButton feedShooterButton = new JoystickButton(operatorController, XboxController.Button.kY.value);
        feedShooterButton.whileHeld(feedShooterCommand);
        feedShooterButton.whenReleased(stopFeedShooterCommand);

        JoystickButton aimButton = new JoystickButton(driverController, XboxController.Button.kB.value);
        aimButton.whileHeld(aimHighCommand);

        JoystickButton intakeDeployButton = new JoystickButton(operatorController,
                XboxController.Button.kA.value);
        intakeDeployButton.whenPressed(intakeDeployCommand);
        intakeDeployButton.whenReleased(intakeRetractCommand);

        //right trigger is axis id 3
        JoystickTrigger startIntakeTrigger = new JoystickTrigger(operatorController, 3);
        startIntakeTrigger.whileHeld(intakeInCommand)
;
        JoystickButton reverseUpperFeederButton = new JoystickButton(operatorController,
                XboxController.Button.kBack.value);
        reverseUpperFeederButton.whileHeld(upperFeederOutCommand);
        reverseUpperFeederButton.whenReleased(stopFeedShooterCommand);
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
