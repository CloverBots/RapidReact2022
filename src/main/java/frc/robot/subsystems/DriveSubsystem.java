package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
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
    public static final double ENCODER_TICKS_PER_ROTATION = 4096;

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


    private final CANSparkMax leftLeadMotor = new CANSparkMax(Ids.DRIVE_LEFT_LEAD_DEVICE, MotorType.kBrushless);
    private final CANSparkMax rightLeadMotor = new CANSparkMax(Ids.DRIVE_RIGHT_LEAD_DEVICE, MotorType.kBrushless);
    private final CANSparkMax leftFollowMotor = new CANSparkMax(Ids.DRIVE_LEFT_FOLLOW_DEVICE, MotorType.kBrushless);
    private final CANSparkMax rightFollowMotor = new CANSparkMax(Ids.DRIVE_RIGHT_FOLLOW_DEVICE, MotorType.kBrushless);

    private final RelativeEncoder leftEncoder = leftLeadMotor.getEncoder();
    private final RelativeEncoder rightEncoder = rightLeadMotor.getEncoder();

    private final MotorControllerGroup leftMotors = new MotorControllerGroup(leftLeadMotor, leftFollowMotor);
    private final MotorControllerGroup rightMotors = new MotorControllerGroup(rightLeadMotor, rightFollowMotor);

    private final DifferentialDrive differentialDrive = new DifferentialDrive(leftMotors, rightMotors);
    private final DifferentialDriveOdometry differentialDriveOdometry;

    public final NavXGyro navXGyro = new NavXGyro();

    /**
     * Constructs a new {@link DriveSubsystem} instance.
     */
    public DriveSubsystem() {
        differentialDriveOdometry = new DifferentialDriveOdometry(getRotation());

        limePidController.setSetpoint(LIME_PID_DEFAULT_SETPOINT);

        configureEncoder(leftEncoder);
        configureEncoder(rightEncoder);

        rightMotors.setInverted(true);

        // Temporarily turning saftey off to allow the motors to run without constant
        // updates
        // differentialDrive.setSafetyEnabled(false);
    }

    @Override
    public void periodic() {
        differentialDriveOdometry.update(
                getRotation(),
                leftEncoder.getPosition(),
                rightEncoder.getPosition());

        SmartDashboard.putNumber("heading", navXGyro.getHeading());
    }

    /**
     * Sets the maximum drive output.
     * 
     * @param maxOutput The maximum drive output, from a -1 to 1 range.
     */
    public void setMaxOutput(double maxOutput) {
        differentialDrive.setMaxOutput(maxOutput);
    }

    /**
     * Resets the maximum drive output to its default value (1.0).
     */
    public void resetMaxOutput() {
        differentialDrive.setMaxOutput(1.0);
    }

    /**
     * Updates the drive output given arcade drive values.
     * 
     * @param forward The forward value, from a -1 to 1 range.
     * @param rotate  The rotational value, from a -1 to 1 range.
     */
    public void arcadeDrive(double forward, double rotate) {
        differentialDrive.arcadeDrive(forward, rotate);
    }

    // differentialDrive requires constant updates, so we are manually setting the
    // motor speeds to test autonomous
    public void autoDrive(double forward, double rotate) { // TODO: Investigate why arcadeDrive isn't working, but
                                                           // autoDrive is.
        leftLeadMotor.set(forward - rotate);
        rightLeadMotor.set(forward - rotate);
    }

    /**
     * Updates the drive output given tank drive values.
     * 
     * @param leftVoltage  The voltage to apply to the left motors.
     * @param rightVoltage The voltage to apply to the right motors.
     */
    public void tankDrive(double leftVoltage, double rightVoltage) {
        leftMotors.setVoltage(leftVoltage);
        rightMotors.setVoltage(rightVoltage);
        differentialDrive.feed();
    }

    // Get the current position of the left encoder.
    // Currently used to get the left encoder for driving by distance, but may be changed to include right
    public double getLeftEncoderPosition() {
        return leftEncoder.getPosition();
    }

    // Get the current position of the right encoder.
    // Currently used to get the left encoder for driving by distance, but may be changed to include right
    public double getRightEncoderPosition() {
        return rightEncoder.getPosition();
    }

    // Get the average position of left and right encoders. 
    public double getAverageEncoderPosition() {
        return (rightEncoder.getPosition()+leftEncoder.getPosition())/2;
    }

    private void configureEncoder(RelativeEncoder encoder) {
        encoder.setPositionConversionFactor(ENCODER_POSITION_CONVERSION_FACTOR);
        encoder.setVelocityConversionFactor(ENCODER_VELOCITY_CONVERSION_FACTOR);
    }

    private Rotation2d getRotation() {
        return Rotation2d.fromDegrees(navXGyro.getHeading() % 360.0);
    }

    @Override
    public void autonomousInit() {
        // Disable the differentialDrive safety during autonomous
        // differentialDrive requires constant feeding of motor inputs when safety is
        // enabled.
        differentialDrive.setSafetyEnabled(false);
    }

    @Override
    public void teleopInit() {
        // Re-enable safety for teleop
        differentialDrive.setSafetyEnabled(true);
    }

    @Override
    public void disabledInit() {
        // TODO Auto-generated method stub
    }
}
