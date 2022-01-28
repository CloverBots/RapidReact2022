package frc.robot;

public final class Ids {
    public static final int CONTROLLER_DRIVE_PORT = 0;
    public static final int CONTROLLER_OPERATOR_PORT = 1;

    public static final int DRIVE_LEFT_LEAD_DEVICE = 3;
    public static final int DRIVE_LEFT_FOLLOW_DEVICE = 4;
    public static final int DRIVE_RIGHT_LEAD_DEVICE = 1;
    public static final int DRIVE_RIGHT_FOLLOW_DEVICE = 2;

    public static final int LIFT_WINCH_DEVICE = 5;
    public static final int LIFT_SOLENOID_FORWARD = 1;
    public static final int LIFT_SOLENOID_REVERSE = 1;

    public static final int RATCHET_SOLENOID_FORWARD = 3;
    public static final int RATCHET_SOLENOID_REVERSE = 7;

    // Shooter motor ID
    public static final int SHOOTER_LEAD_DEVICE = 14; //TODO check proper port number
    public static final int SHOOTER_FOLLOW_DEVICE_1 = 15;
    public static final int SHOOTER_FOLLOW_DEVICE_2 = 10;
    public static final int SHOOTER_FOLLOW_DEVICE_3 = 11;

    public static final int INTAKE_LEAD_DEVICE = 13; //TODO proper port number

    // pneumatics ids
    public static final int TEST_PCM_ID = 1;
    public static final int TEST_SOLENOID1_FORWARD = 2;
    public static final int TEST_SOLENOID1_REVERSE = 3;

    // Test shooter motor ID
    public static final int TEST_SHOOTER_LEAD_DEVICE = 16; //TODO fix clashing with other shooter ports
    public static final int TEST_SHOOTER_FOLLOW_DEVICE_1 = 17;//temporarily changed from 14 and 15 to 16 and 17 in order to test code


}
