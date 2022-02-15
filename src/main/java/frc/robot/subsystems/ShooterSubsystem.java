package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class ShooterSubsystem extends SubsystemBase {

    private static final double WHEEL_DIAMETER_METERS = 0.1524;
    private static final double ENCODER_POSITION_CONVERSION_FACTOR = 0.1 * WHEEL_DIAMETER_METERS * Math.PI;
    private static final double ENCODER_VELOCITY_CONVERSION_FACTOR = ENCODER_POSITION_CONVERSION_FACTOR * 60.0;

    private final CANSparkMax shooterLeadMotor = new CANSparkMax(Ids.SHOOTER_LEAD_DEVICE, MotorType.kBrushless);
    private final CANSparkMax shooterFollowMotor1 = new CANSparkMax(Ids.SHOOTER_FOLLOW_DEVICE_1, MotorType.kBrushless);
    private final MotorControllerGroup shooterMotors = new MotorControllerGroup(shooterLeadMotor, shooterFollowMotor1);

    private final RelativeEncoder encoder = shooterLeadMotor.getEncoder();

    private SparkMaxPIDController pidController;

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    /** Creates a new Shooter. */
    public ShooterSubsystem() {
        configureEncoder(encoder);
        pidController = shooterLeadMotor.getPIDController();
        kP = 6e-5;
        kI = 0;
        kD = 0;
        kIz = 0;
        kFF = 0.000015;
        kMaxOutput = 1;
        kMinOutput = -1;
        maxRPM = 5700;

        // set PID coefficients
        pidController.setP(kP);
        pidController.setI(kI);
        pidController.setD(kD);
        pidController.setIZone(kIz);
        pidController.setFF(kFF);
        pidController.setOutputRange(kMinOutput, kMaxOutput);

        // display PID coefficients on SmartDashboard
        SmartDashboard.putNumber("P Gain", kP);
        SmartDashboard.putNumber("I Gain", kI);
        SmartDashboard.putNumber("D Gain", kD);
        SmartDashboard.putNumber("I Zone", kIz);
        SmartDashboard.putNumber("Feed Forward", kFF);
        SmartDashboard.putNumber("Max Output", kMaxOutput);
        SmartDashboard.putNumber("Min Output", kMinOutput);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void startShooter(double speed) {
        shooterMotors.set(speed);

    }

    public void stop() {
        shooterMotors.set(0);
    }

    private void configureEncoder(RelativeEncoder encoder) {
        encoder.setPositionConversionFactor(ENCODER_POSITION_CONVERSION_FACTOR);
        encoder.setVelocityConversionFactor(ENCODER_VELOCITY_CONVERSION_FACTOR);
    }

    public void setShooterRPM(double rpm) {
        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);
        double iz = SmartDashboard.getNumber("I Zone", 0);
        double ff = SmartDashboard.getNumber("Feed Forward", 0);
        double max = SmartDashboard.getNumber("Max Output", 0);
        double min = SmartDashboard.getNumber("Min Output", 0);

        // if PID coefficients on SmartDashboard have changed, write new values to
        // controller
        if ((p != kP)) {
            pidController.setP(p);
            kP = p;
        }
        if ((i != kI)) {
            pidController.setI(i);
            kI = i;
        }
        if ((d != kD)) {
            pidController.setD(d);
            kD = d;
        }
        if ((iz != kIz)) {
            pidController.setIZone(iz);
            kIz = iz;
        }
        if ((ff != kFF)) {
            pidController.setFF(ff);
            kFF = ff;
        }
        if ((max != kMaxOutput) || (min != kMinOutput)) {
            pidController.setOutputRange(min, max);
            kMinOutput = min;
            kMaxOutput = max;
        }

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

        SmartDashboard.putNumber("ProcessVariable", encoder.getVelocity());
    }
}
