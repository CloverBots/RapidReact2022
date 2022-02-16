package frc.robot;

import edu.wpi.first.wpilibj.I2C.Port;

public final class Ids {
    public static final int CONTROLLER_DRIVE_PORT = 0;
    public static final int CONTROLLER_OPERATOR_PORT = 1;

    public static final int DRIVE_LEFT_LEAD_DEVICE = 12;
    public static final int DRIVE_LEFT_FOLLOW_DEVICE = 10;
    public static final int DRIVE_RIGHT_LEAD_DEVICE = 14;
    public static final int DRIVE_RIGHT_FOLLOW_DEVICE = 13;

    public static final int LIFT_WINCH_DEVICE0 = 8;
    public static final int LIFT_WINCH_DEVICE1 = 9;
    public static final int LIFT_SOLENOID_FORWARD = 1;
    public static final int LIFT_SOLENOID_REVERSE = 1;

    public static final int RATCHET_SOLENOID_FORWARD = 3;
    public static final int RATCHET_SOLENOID_REVERSE = 7;

    // Shooter motor ID
    public static final int SHOOTER_LEAD_DEVICE = 11; //TODO check proper port number
    public static final int SHOOTER_FOLLOW_DEVICE_1 = 16;

    public static final int INTAKE_LEAD_DEVICE = 20; //TODO proper port number
    public static final int INTAKE_SOLENOID1_FORWARD = 2;
    public static final int INTAKE_SOLENOID1_REVERSE = 3;

    // pneumatics ids
    public static final int PCM_ID = 1;
    public static final int TEST_SOLENOID1_FORWARD = 4;  //TODO change to correct value
    public static final int TEST_SOLENOID1_REVERSE = 5; //TODO change to correct value

    //NavX Gyro
    public static final Port AHRS_PORT_ID = Port.kMXP;

    //feeder ids
    public static final int FEEDER_UPPER_TALON = 15;
    public static final int FEEDER_LOWER_TALON = 18;
    public static final int FEEDER_UPPER_SENSOR = 21;
    public static final int FEEDER_LOWER_SENSOR = 22;
}
