// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class UpperFeederSubsystem extends SubsystemBase {

    TalonSRX feederMotor = new WPI_TalonSRX(Ids.FEEDER_UPPER_TALON);

    /** Creates a new FeederSubsystem. */
    public UpperFeederSubsystem() {
        feederMotor.configFactoryDefault();
    }

    public void setSpeed(double speed) {
        feederMotor.set(ControlMode.PercentOutput, speed);
    }
}
