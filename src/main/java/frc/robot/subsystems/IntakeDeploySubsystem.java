// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Ids;
import frc.robot.RobotLifecycleCallbacks;

public class IntakeDeploySubsystem extends SubsystemBase implements RobotLifecycleCallbacks {
    DoubleSolenoid solenoid1 = new DoubleSolenoid(Ids.PCM_ID, PneumaticsModuleType.CTREPCM,
            Ids.INTAKE_SOLENOID1_FORWARD,
            Ids.INTAKE_SOLENOID1_REVERSE);
    Compressor compressor = new Compressor(Ids.PCM_ID, PneumaticsModuleType.CTREPCM);

    /** Creates a new IntakeDeploySubsystem. */
    public IntakeDeploySubsystem() {
        setSolenoid(false);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setSolenoid(boolean position) {
        if (position) {
            solenoid1.set(Value.kForward);
        } else {
            solenoid1.set(Value.kReverse);
        }
    }

    private void compressorStart() {
        compressor.enableDigital();
    }

    private void compressorStop() {
        compressor.disable();
    }

    @Override
    public void autonomousInit() {
        compressorStart();
    }

    @Override
    public void teleopInit() {
        compressorStart();
    }

    @Override
    public void disabledInit() {
        compressorStop();
    }
}
