// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestShooterSubsystem extends SubsystemBase {

    private class Gains {
        public final double kP;
        public final double kI;
        public final double kD;
        public final double kF;
        public final int kIzone;
        public final double kPeakOutput;

        public Gains(double _kP, double _kI, double _kD, double _kF, int _kIzone, double _kPeakOutput) {
            kP = _kP;
            kI = _kI;
            kD = _kD;
            kF = _kF;
            kIzone = _kIzone;
            kPeakOutput = _kPeakOutput;
        }
    }

    /**
     * Which PID slot to pull gains from. Starting 2018, you can choose from
     * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
     * configuration.
     */
    public static final int kSlotIdx = 0;

    /**
     * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
     * now we just want the primary one.
     */
    public static final int kPIDLoopIdx = 0;

    /**
     * Set to zero to skip waiting for confirmation, set to nonzero to wait and
     * report to DS if action fails.
     */
    public static final int kTimeoutMs = 30;

    /**
     * PID Gains may have to be adjusted based on the responsiveness of control
     * loop.
     * kF: 1023 represents output value to Talon at 100%, 7200 represents Velocity
     * units at 100% output
     * 
     * kP kI kD kF Iz PeakOut
     */
    public final Gains kGains_Velocit = new Gains(0.25, 0.001, 20, 1023.0 / 7200.0, 300, 1.00);

    TalonSRX talonLead = new WPI_TalonSRX(1);
    TalonSRX talonFollow = new WPI_TalonSRX(2);

    /** Creates a new TestShooterSubsystem. */
    public TestShooterSubsystem() {
        talonLead.configFactoryDefault();
        /* Config sensor used for Primary PID [Velocity] */
        talonLead.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
                kPIDLoopIdx,
                kTimeoutMs);

        /**
         * Phase sensor accordingly.
         * Positive Sensor Reading should match Green (blinking) Leds on Talon
         */
        talonLead.setSensorPhase(false);

        /* Config the peak and nominal outputs */
        talonLead.configNominalOutputForward(0, kTimeoutMs);
        talonLead.configNominalOutputReverse(0, kTimeoutMs);
        talonLead.configPeakOutputForward(1, kTimeoutMs);
        talonLead.configPeakOutputReverse(-1, kTimeoutMs);

        /* Config the Velocity closed loop gains in slot0 */
        talonLead.config_kF(kPIDLoopIdx, kGains_Velocit.kF, kTimeoutMs);
        talonLead.config_kP(kPIDLoopIdx, kGains_Velocit.kP, kTimeoutMs);
        talonLead.config_kI(kPIDLoopIdx, kGains_Velocit.kI, kTimeoutMs);
        talonLead.config_kD(kPIDLoopIdx, kGains_Velocit.kD, kTimeoutMs);

        talonFollow.follow(talonLead);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setShooterRPM(double rpm) {
        // 4096 encoder ticks per revolution / 100 ms / minute
        double targetVelocity_UnitsPer100ms = rpm * 4096 / 600;

        talonLead.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);

        SmartDashboard.putNumber("current rpm", talonLead.getSelectedSensorVelocity() / 4096 * 600);
    }
}
