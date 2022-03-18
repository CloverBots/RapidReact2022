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
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.commands.AlignHighCommand;
import frc.robot.commands.AutoDelayedTaxiCommand;
import frc.robot.commands.AutoHighGoalTaxiCommand;
import frc.robot.commands.AutoLowGoalTaxiCommand;
import frc.robot.commands.AutoTwoBallCommand;
import frc.robot.commands.DriveFromControllerCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.LiftCommand;
import frc.robot.commands.LiftHookCommand;
import frc.robot.commands.SpinShooterHighCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeDeploySubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LiftSubsystem;
import frc.robot.subsystems.LowerFeederSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.UpperFeederSubsystem;
import frc.robot.subsystems.LiftHookSubsystem;

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
    private static final double CAMERA_HEIGHT = 43.7; // on test robot
    private static final double CAMERA_PITCH = 22.0;

    private final VisionConfiguration visionConfiguration = new VisionConfiguration(
            VISION_TARGET_HEIGHT,
            CAMERA_HEIGHT,
            CAMERA_PITCH);

    private final VisionTargetTracker visionTargetTracker = new VisionTargetTracker(visionConfiguration);

    private final XboxController driverController = new XboxController(Ids.CONTROLLER_DRIVE_PORT);
    private final XboxController operatorController = new XboxController(Ids.CONTROLLER_OPERATOR_PORT);

    private final DriveSubsystem driveSubsystem = new DriveSubsystem();

    private final LiftSubsystem liftSubsystem = new LiftSubsystem();
    private final LiftHookSubsystem liftHookSubsystem = new LiftHookSubsystem();

    private final IntakeDeploySubsystem intakeDeploySubsystem = new IntakeDeploySubsystem();
    private final IntakeSubsystem intakeSubsystem;
    private final UpperFeederSubsystem upperFeederSubsystem = new UpperFeederSubsystem();
    private final LowerFeederSubsystem lowerFeederSubsystem = new LowerFeederSubsystem();

    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();

    private final AlignHighCommand alignHighCommand = new AlignHighCommand(driveSubsystem, driverController::getLeftY, visionTargetTracker);
  
    private final SpinShooterHighCommand spinShooterHighCommand = new SpinShooterHighCommand(shooterSubsystem, visionTargetTracker);
    private final Command lowShootCommand = new RunCommand(()-> {
        shooterSubsystem.setTeleOpLowGoalRPM();
    }, shooterSubsystem);
    private final Command highShootCommand = new RunCommand(()-> {
        shooterSubsystem.setTeleOpHighGoalRPM();
    }, shooterSubsystem);
    private final Command stopShooterCommand = new InstantCommand(() -> {
        shooterSubsystem.setShooterRPM(0);
    }, shooterSubsystem);

    // By passing in the driverController right trigger to the intakeCommand, the
    // controller value will
    // automatically be fed into the intakeCommand as the speed value.

    private final IntakeCommand intakeInCommand;
    // Use an InstantCommand since all we need is to run a
    // intakeSubsystem.startIntake method and don't
    // need a whole command class for this.
    private final Command reverseIntakeCommand;
    private final Command stopIntakeCommand;

    private final Command feedShooterCommand = new InstantCommand(() -> {
        upperFeederSubsystem.setSpeed(1);
        lowerFeederSubsystem.setSpeed(1);
    }, upperFeederSubsystem, lowerFeederSubsystem);

    private final Command stopFeedShooterCommand = new InstantCommand(() -> {
        upperFeederSubsystem.setSpeed(0);
        lowerFeederSubsystem.setSpeed(0);
    }, upperFeederSubsystem, lowerFeederSubsystem);

    private final Command upperFeederInCommand = new InstantCommand(()-> {
        upperFeederSubsystem.setSpeed(1);
    }, upperFeederSubsystem);

    private final Command upperFeederOutCommand = new InstantCommand(() -> {
        upperFeederSubsystem.setSpeed(-1);
    }, upperFeederSubsystem);

    private final Command stopUpperFeederCommand = new InstantCommand(()-> {
        upperFeederSubsystem.setSpeed(0);
    }, upperFeederSubsystem);

    private Command intakeDeployCommand = new InstantCommand(() -> intakeDeploySubsystem.setSolenoid(true),
            intakeDeploySubsystem);
    private Command intakeRetractCommand = new InstantCommand(() -> intakeDeploySubsystem.setSolenoid(false),
            intakeDeploySubsystem);

    private LiftHookCommand liftHookCommand = new LiftHookCommand(liftHookSubsystem,
            operatorController::getLeftTriggerAxis);

    private final LiftCommand liftCommand = new LiftCommand(liftSubsystem, operatorController::getLeftTriggerAxis,
            operatorController::getLeftY);

    private final DriveFromControllerCommand driveFromController = new DriveFromControllerCommand(
            driveSubsystem,
            liftSubsystem,
            driverController::getLeftY,
            driverController::getRightX, 
            driverController::getLeftTriggerAxis);

    // lifecyclecallbacks used when special cases are needed for autonomous and
    // teleop
    private final RobotLifecycleCallbacks[] robotLifecycleCallbacks = new RobotLifecycleCallbacks[] {
            driveSubsystem,
            intakeDeploySubsystem,
            liftHookSubsystem
    };

    private final SendableChooser<Command> chooser = new SendableChooser<>();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        intakeSubsystem = new IntakeSubsystem(intakeDeploySubsystem);

        intakeInCommand = new IntakeCommand(intakeSubsystem, lowerFeederSubsystem,
            1, operatorController::getRightTriggerAxis);

        reverseIntakeCommand = new InstantCommand(()-> {
            intakeSubsystem.startIntake(-1);
            lowerFeederSubsystem.setSpeed(-1);
        }, intakeSubsystem, lowerFeederSubsystem);

        stopIntakeCommand = new InstantCommand(()-> {
            intakeSubsystem.stop();
            lowerFeederSubsystem.setSpeed(0);
        }, intakeSubsystem, lowerFeederSubsystem);

        

        driveSubsystem.setDefaultCommand(driveFromController);
        liftSubsystem.setDefaultCommand(liftCommand);

        // Add choices to the chooser
        // chooser.addOption("Autonomous One", new AutonomousOne(driveSubsystem));
        // chooser.setDefaultOption("Autonomous Left Middle", new AutonomousLeftMiddleCommand(
        //         driveSubsystem,
        //         intakeSubsystem,
        //         intakeDeploySubsystem,
        //         lowerFeederSubsystem,
        //         upperFeederSubsystem,
        //         shooterSubsystem,
        //         visionTargetTracker));
        chooser.setDefaultOption("Delayed Taxi", new AutoDelayedTaxiCommand(driveSubsystem));
        chooser.addOption("Low Taxi", new AutoLowGoalTaxiCommand(
                driveSubsystem, 
                intakeSubsystem, 
                intakeDeploySubsystem, 
                lowerFeederSubsystem, 
                upperFeederSubsystem, 
                shooterSubsystem, 
                visionTargetTracker));
        chooser.addOption("High Taxi", new AutoHighGoalTaxiCommand(
                driveSubsystem,
                intakeSubsystem,
                intakeDeploySubsystem, 
                lowerFeederSubsystem, 
                upperFeederSubsystem, 
                shooterSubsystem, 
                visionTargetTracker));
        chooser.addOption("Two Ball", new AutoTwoBallCommand(driveSubsystem,
                intakeSubsystem,
                intakeDeploySubsystem,
                lowerFeederSubsystem,
                upperFeederSubsystem,
                shooterSubsystem,
                visionTargetTracker));
        // Add chooser to the SmartDashboard
        SmartDashboard.putData("Autonomous Selection", chooser);
        SmartDashboard.putNumber("AutoWaitTime", 0);

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

        JoystickButton feedShooterButton = new JoystickButton(operatorController, XboxController.Button.kY.value);
        feedShooterButton.whileHeld(feedShooterCommand);
        feedShooterButton.whenReleased(stopFeedShooterCommand);

        JoystickButton feedUpperButton = new JoystickButton(operatorController, XboxController.Button.kB.value);
        feedUpperButton.whileHeld(upperFeederInCommand);
        feedUpperButton.whenReleased(stopFeedShooterCommand);

        JoystickTrigger aimTrigger = new JoystickTrigger(driverController, 3);
        aimTrigger.whileHeld(alignHighCommand);
        // aimTrigger.whileHeld(spinShooterHighCommand);
        aimTrigger.whenReleased(stopShooterCommand);
      
        POVButton dPadDownButton = new POVButton(operatorController, 180);
        dPadDownButton.whileHeld(lowShootCommand, false);
        dPadDownButton.whenReleased(stopShooterCommand);

        POVButton dPadDownLeftButton = new POVButton(operatorController, 135);
        dPadDownLeftButton.whileHeld(lowShootCommand, false);
        dPadDownLeftButton.whenReleased(stopShooterCommand);

        POVButton dPadDownRightButton = new POVButton(operatorController, 225);
        dPadDownRightButton.whileHeld(lowShootCommand, false);
        dPadDownRightButton.whenReleased(stopShooterCommand);

        POVButton dPadUpButton = new POVButton(operatorController, 0);
        dPadUpButton.whileHeld(highShootCommand, false);
        dPadUpButton.whenReleased(stopShooterCommand);

        POVButton dPadUpLeftButton = new POVButton(operatorController, 315);
        dPadUpLeftButton.whileHeld(highShootCommand, false);
        dPadUpLeftButton.whenReleased(stopShooterCommand);

        POVButton dPadUpRightButton = new POVButton(operatorController, 45);
        dPadUpRightButton.whileHeld(highShootCommand, false);
        dPadUpRightButton.whenReleased(stopShooterCommand);

        JoystickButton intakeDeployButton = new JoystickButton(operatorController,
                XboxController.Button.kA.value);
        intakeDeployButton.whenPressed(intakeDeployCommand);
        intakeDeployButton.whenReleased(intakeRetractCommand);

        JoystickButton liftHookDeployButton = new JoystickButton(operatorController, 
                XboxController.Button.kX.value);
        liftHookDeployButton.whenPressed(liftHookCommand);

        // right trigger is axis id 3
        JoystickTrigger startIntakeTrigger = new JoystickTrigger(operatorController, 3);
        startIntakeTrigger.whileHeld(intakeInCommand);

        JoystickButton reverseUpperFeederButton = new JoystickButton(operatorController,
                XboxController.Button.kBack.value);
        reverseUpperFeederButton.whileHeld(upperFeederOutCommand);
        reverseUpperFeederButton.whenReleased(stopUpperFeederCommand);

        JoystickButton reverseIntakeButton = new JoystickButton(operatorController, XboxController.Button.kStart.value);
        reverseIntakeButton.whileHeld(reverseIntakeCommand);
        reverseIntakeButton.whenReleased(stopIntakeCommand);
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
