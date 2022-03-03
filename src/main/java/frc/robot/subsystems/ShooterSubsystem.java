package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class ShooterSubsystem extends SubsystemBase {

    private static final double WHEEL_DIAMETER_METERS = 0.1524;
    private static final double ENCODER_POSITION_CONVERSION_FACTOR = 1;//0.1 * WHEEL_DIAMETER_METERS * Math.PI;
    private static final double ENCODER_VELOCITY_CONVERSION_FACTOR = 1;//ENCODER_POSITION_CONVERSION_FACTOR * 60.0;

    private static final double SHOOTER_P = 8e-5;
    private static final double SHOOTER_I = 0;
    private static final double SHOOTER_D = 0;
    private static final double SHOOTER_Iz = 0;
    private static final double SHOOTER_FF = 0.00017;
    private static final double SHOOTER_MAX_OUTPUT = 1;
    private static final double SHOOTER_MIN_OUTPUT = -1;
    private static final double MAX_RPM = 5700;

    private static final String SHOOT_HIGH_KEY = "Shoot high rpm";
    private static final String SHOOT_LOW_KEY = "Shoot low rpm";

    private static final double DEFAULT_HIGH_SPEED = 4000;
    private static final double DEFAULT_LOW_SPEED = 1800;

    private final CANSparkMax shooterLeadMotor = new CANSparkMax(Ids.SHOOTER_LEAD_DEVICE, MotorType.kBrushless);
    private final CANSparkMax shooterFollowMotor1 = new CANSparkMax(Ids.SHOOTER_FOLLOW_DEVICE_1, MotorType.kBrushless);
    // private final MotorControllerGroup shooterMotors = new MotorControllerGroup(shooterLeadMotor, shooterFollowMotor1);

    private final RelativeEncoder encoder = shooterLeadMotor.getEncoder();

    private SparkMaxPIDController pidController;

    /** Creates a new Shooter. */
    public ShooterSubsystem() {
        // shooterFollowMotor1.setInverted(true);
        shooterFollowMotor1.follow(shooterLeadMotor, true);

        configureEncoder(encoder);
        pidController = shooterLeadMotor.getPIDController();

        // set PID coefficients
        pidController.setP(SHOOTER_P);
        pidController.setI(SHOOTER_I);
        pidController.setD(SHOOTER_D);
        pidController.setIZone(SHOOTER_Iz);
        pidController.setFF(SHOOTER_FF);
        pidController.setOutputRange(SHOOTER_MIN_OUTPUT, SHOOTER_MAX_OUTPUT);

        SmartDashboard.putNumber(SHOOT_HIGH_KEY, DEFAULT_HIGH_SPEED);
        SmartDashboard.putNumber(SHOOT_LOW_KEY, DEFAULT_LOW_SPEED);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void stop() {
        pidController.setReference(0, CANSparkMax.ControlType.kVelocity);
    }

    private void configureEncoder(RelativeEncoder encoder) {
        encoder.setPositionConversionFactor(ENCODER_POSITION_CONVERSION_FACTOR);
        encoder.setVelocityConversionFactor(ENCODER_VELOCITY_CONVERSION_FACTOR);
    }

    public void setShooterRPM(double rpm) {

        /**
         * PIDController objects are commanded to a set point using the
         * SetReference() method.
         * 
         * The first parameter is the value of the set point, whose units vary
         * depending on the control type set in the second parameter.
         * 
         * The second parameter is the control type can be set to one of four
         * parameters:
         * com.revrobotics.CANSparkMax.ControlType.kDutyCycle
         * com.revrobotics.CANSparkMax.ControlType.kPosition
         * com.revrobotics.CANSparkMax.ControlType.kVelocity
         * com.revrobotics.CANSparkMax.ControlType.kVoltage
         */

        pidController.setReference(rpm, CANSparkMax.ControlType.kVelocity);

        // SmartDashboard.putNumber("ProcessVariable", encoder.getVelocity());
    }

    public void setHighGoalRPM(){
        double rpm = SmartDashboard.getNumber(SHOOT_HIGH_KEY, 0);
        setShooterRPM(rpm);
    }

    public void setLowGoalRPM(){
        double rpm = SmartDashboard.getNumber(SHOOT_LOW_KEY, 0);
        setShooterRPM(rpm);
    }
}
