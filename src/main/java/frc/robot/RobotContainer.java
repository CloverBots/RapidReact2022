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
import frc.robot.subsystems.FeederSubsystem;
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
//     private final LiftObserver liftSubsystem = new LiftSubsystemDummy();

    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    private final FeederSubsystem feederSubsystem = new FeederSubsystem();

    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final ShooterCommand shooterCommand = new ShooterCommand(shooterSubsystem, feederSubsystem,
            visionTargetTracker);

    private final AimHighCommand aimHighCommand = new AimHighCommand(driveSubsystem, visionTargetTracker);

    // By passing in the driverController right trigger to the intakeCommand, the
    // controller value will
    // automatically be fed into the intakeCommand as the speed value.

    private final IntakeCommand intakeInCommand = new IntakeCommand(intakeSubsystem, feederSubsystem,
            IntakeConfig.TWO_BALLS, 1);
    // Use an InstantCommand since all we need is to run a intakeSubsystem.startIntake method and don't
    // need a whole command class for this.
    private final Command intakeOutCommand = new InstantCommand(() -> {
        intakeSubsystem.startIntake(0);
        feederSubsystem.setUpperFeederSpeed(-1);
        feederSubsystem.setLowerFeederSpeed(0);
    }, intakeSubsystem, feederSubsystem);

    private final Command feedShooterCommand = new InstantCommand(() -> {
            feederSubsystem.setUpperFeederSpeed(1);
            feederSubsystem.setLowerFeederSpeed(1);
    }, feederSubsystem);

    private final Command stopFeedShooterCommand = new InstantCommand(() -> {
        feederSubsystem.setUpperFeederSpeed(0);
        feederSubsystem.setLowerFeederSpeed(0);
    }, feederSubsystem);

    private final IntakeDeploySubsystem intakeDeploySubsystem = new IntakeDeploySubsystem();
    private Command intakeDeployCommand = new InstantCommand(() -> intakeDeploySubsystem.setSolenoid(true),
            intakeDeploySubsystem);
    private Command intakeRetractCommand = new InstantCommand(() -> intakeDeploySubsystem.setSolenoid(false),
            intakeDeploySubsystem);

    private final LiftCommand liftCommand = new LiftCommand(liftSubsystem, operatorController::getRightTriggerAxis, operatorController::getLeftY);

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
                feederSubsystem,
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

        JoystickButton startIntakeButton = new JoystickButton(operatorController,
                XboxController.Button.kX.value);
        startIntakeButton.whileHeld(intakeInCommand);

        JoystickButton reverseIntakeButton = new JoystickButton(operatorController,
                XboxController.Button.kBack.value);
        reverseIntakeButton.whileHeld(intakeOutCommand);
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
