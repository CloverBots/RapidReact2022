// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ids;

public class FeederSubsystem extends SubsystemBase {

    TalonSRX upperFeeder = new WPI_TalonSRX(Ids.FEEDER_UPPER_TALON);
    TalonSRX lowerFeeder = new WPI_TalonSRX(Ids.FEEDER_LOWER_TALON);

    DigitalInput upperSensor = new DigitalInput(Ids.FEEDER_UPPER_SENSOR);
    DigitalInput lowerSensor = new DigitalInput(Ids.FEEDER_LOWER_SENSOR);

    /** Creates a new FeederSubsystem. */
    public FeederSubsystem() {
        upperFeeder.configFactoryDefault();
        lowerFeeder.configFactoryDefault();

        lowerFeeder.setInverted(true);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setUpperFeederSpeed(double speed) {
        upperFeeder.set(ControlMode.PercentOutput, speed);
    }

    public void setLowerFeederSpeed(double speed) {
        lowerFeeder.set(ControlMode.PercentOutput, speed);
    }

    public boolean getUpperSensor() {
        return upperSensor.get();
    }

    public boolean getLowerSensor() {
        return lowerSensor.get();
    }

    public boolean loadUpper(double speed) {
        if (!getUpperSensor()) {
            setUpperFeederSpeed(speed);
        } else {
            setUpperFeederSpeed(0);
        }
        return getUpperSensor();
    }

    public boolean loadLower(double speed) {
        //run lower until lower is hit but ignore if upper is not hit yet
        if (!getLowerSensor() || !getUpperSensor()) {
            setLowerFeederSpeed(speed);
        } else {
            setLowerFeederSpeed(0);
        }
        return getLowerSensor();
    }
}
