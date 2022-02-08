package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;
import frc.robot.NavXGyro;
import frc.robot.RobotLifecycleCallbacks;

public class DriveSubsystem extends SubsystemBase implements RobotLifecycleCallbacks {

    private static final double LIME_PID_P = 0.09;
    private static final double LIME_PID_I = 0.0;
    private static final double LIME_PID_D = 0.0;
    private static final double LIME_PID_DEFAULT_SETPOINT = -3.3;

    private static final double DRIVESTRAIGHT_PID_P = 0.05;
    private static final double DRIVESTRAIGHT_PID_I = 0.0;
    private static final double DRIVESTRAIGHT_PID_D = 0.06;

    private static final double DRIVEROTATE_PID_P = 0.01;
    private static final double DRIVEROTATE_PID_I = 0.00;
    private static final double DRIVEROTATE_PID_D = 0.007;

    public static final double WHEEL_DIAMETER_METERS = 0.1524;
    public static final double ENCODER_POSITION_CONVERSION_FACTOR = 0.1 * WHEEL_DIAMETER_METERS * Math.PI;
    public static final double ENCODER_VELOCITY_CONVERSION_FACTOR = ENCODER_POSITION_CONVERSION_FACTOR * 60.0;
    public static final double ENCODER_TICKS_PER_ROTATION = 2048;

    private final PIDController limePidController = new PIDController(
            LIME_PID_P,
            LIME_PID_I,
            LIME_PID_D);

    public final PIDController driveStraightPidController = new PIDController(
            DRIVESTRAIGHT_PID_P,
            DRIVESTRAIGHT_PID_I,
            DRIVESTRAIGHT_PID_D);

    public final PIDController driveRotatePidController = new PIDController(
            DRIVEROTATE_PID_P,
            DRIVEROTATE_PID_I,
            DRIVEROTATE_PID_D);

    private final TalonFX leftLeadMotor = new TalonFX(Ids.DRIVE_LEFT_LEAD_DEVICE);
    private final TalonFX rightLeadMotor = new TalonFX(Ids.DRIVE_RIGHT_LEAD_DEVICE);
    private final TalonFX leftFollowMotor = new TalonFX(Ids.DRIVE_LEFT_FOLLOW_DEVICE);
    private final TalonFX rightFollowMotor = new TalonFX(Ids.DRIVE_RIGHT_FOLLOW_DEVICE);

    private static final int kPIDLoopIdx = 0;
    private static final int kTimeoutMs = 30;

    public final NavXGyro navXGyro = new NavXGyro();

    /**
     * Constructs a new {@link DriveSubsystem} instance.
     */
    public DriveSubsystem() {
        leftFollowMotor.follow(leftLeadMotor);
        rightFollowMotor.follow(rightLeadMotor);

        leftLeadMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor,
        kPIDLoopIdx,
        kTimeoutMs);
        rightLeadMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor,
        kPIDLoopIdx,
        kTimeoutMs);

        limePidController.setSetpoint(LIME_PID_DEFAULT_SETPOINT);

        // Temporarily turning saftey off to allow the motors to run without constant
        // updates
        // differentialDrive.setSafetyEnabled(false);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("heading", navXGyro.getHeading());
    }

    /**
     * Updates the drive output given arcade drive values.
     * 
     * @param forward The forward value, from a -1 to 1 range.
     * @param rotate  The rotational value, from a -1 to 1 range.
     */
    public void arcadeDrive(double forward, double rotate) {
        leftLeadMotor.set(TalonFXControlMode.PercentOutput, forward - rotate);
        rightLeadMotor.set(TalonFXControlMode.PercentOutput, forward - rotate);
    }

    // differentialDrive requires constant updates, so we are manually setting the
    // motor speeds to test autonomous
    public void autoDrive(double forward, double rotate) { // TODO: Investigate why arcadeDrive isn't working, but
                                                           // autoDrive is.
        leftLeadMotor.set(TalonFXControlMode.PercentOutput, forward - rotate);
        rightLeadMotor.set(TalonFXControlMode.PercentOutput, forward - rotate);
    }

    // Get the current position of the left encoder.
    // Currently used to get the left encoder for driving by distance, but may be changed to include right
    public double getLeftEncoderPosition() {
        return leftLeadMotor.getSelectedSensorPosition() / ENCODER_TICKS_PER_ROTATION * ENCODER_POSITION_CONVERSION_FACTOR;
    }

    // Get the current position of the right encoder.
    // Currently used to get the left encoder for driving by distance, but may be changed to include right
    public double getRightEncoderPosition() {
        return rightLeadMotor.getSelectedSensorPosition() / ENCODER_TICKS_PER_ROTATION * ENCODER_POSITION_CONVERSION_FACTOR;
    }

    // Get the average position of left and right encoders. 
    public double getAverageEncoderPosition() {
        return (getLeftEncoderPosition()+getRightEncoderPosition())/2;
    }

    private Rotation2d getRotation() {
        return Rotation2d.fromDegrees(navXGyro.getHeading() % 360.0);
    }

    @Override
    public void autonomousInit() {
        // Disable the differentialDrive safety during autonomous
        // differentialDrive requires constant feeding of motor inputs when safety is
        // enabled.
        // differentialDrive.setSafetyEnabled(false);
    }

    @Override
    public void teleopInit() {
        // Re-enable safety for teleop
        // differentialDrive.setSafetyEnabled(true);
    }

    @Override
    public void disabledInit() {
        // TODO Auto-generated method stub
    }
}
