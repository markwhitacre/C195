package Model;

import Utils.MasterList;

import java.sql.Timestamp;


/**
 * The type Appointment.
 */
public class Appointment {

    private int apptID;
    private String apptTitle;
    private String apptDesc;
    private String apptLocation;
    private int apptContact;
    private String apptType;
    private Timestamp apptStart;
    private Timestamp apptEnd;
    private int apptCustomer;
    private int apptUser;


    /**
     * Instantiates a new Appointment.
     *
     * @param apptID       the appointment ID
     * @param apptTitle    the appointment title
     * @param apptDesc     the appointment description
     * @param apptLocation the appointment location
     * @param apptType     the appointment type
     * @param apptStart    the appointment starting time
     * @param apptEnd      the appointment ending time
     * @param apptContact  the appointment contact
     * @param apptCustomer the appointment customer
     * @param apptUser     the current user
     */
    public Appointment(int apptID, String apptTitle, String apptDesc, String apptLocation,
                       String apptType, Timestamp apptStart, Timestamp apptEnd, int apptContact, int apptCustomer,
                       int apptUser) {
        this.apptID = apptID;
        this.apptTitle = apptTitle;
        this.apptDesc = apptDesc;
        this.apptLocation = apptLocation;
        this.apptContact = apptContact;
        this.apptType = apptType;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptCustomer = apptCustomer;
        this.apptUser = apptUser;
    }

    /**
     * Gets appointment id.
     *
     * @return the id
     */
    public int getApptID() {
        return apptID;
    }

    /**
     * Gets appointment title.
     *
     * @return the appointment title
     */
    public String getApptTitle() {
        return apptTitle;
    }

    /**
     * Gets appointment description.
     *
     * @return the appointment description
     */
    public String getApptDesc() {
        return apptDesc;
    }

    /**
     * Gets appointment location.
     *
     * @return the appointment location
     */
    public String getApptLocation() {
        return apptLocation;
    }

    /**
     * Gets appointment contact.
     *
     * @return the appointment contact
     */
    public int getApptContact() {
        return apptContact;
    }

    /**
     * Gets appointment type.
     *
     * @return the appointment type
     */
    public String getApptType() {
        return apptType;
    }

    /**
     * Gets the appointment start time.
     *
     * @return the appt start
     */
    public Timestamp getApptStart() {
        return apptStart;
    }

    /**
     * Gets appt end.
     *
     * @return the appt end
     */
    public Timestamp getApptEnd() {
        return apptEnd;
    }

    /**
     * Gets appointment customer.
     *
     * @return the appointment customer
     */
    public int getApptCustomer() {
        return apptCustomer;
    }

    /**
     * Gets appointment user.
     *
     * @return the appointment user
     */
    public int getApptUser() {
        return apptUser;
    }

    /**
     * Updates the appointment as an instance, instead of updating each attribute separately.
     *
     * @param index the appointment ID
     * @param appt  the appointment to be updated.
     */
    public static void updateAppt(int index, Appointment appt){
        for (int i = 0; i < MasterList.getAppointments().size(); i++){
            if (index == MasterList.getAppointments().get(i).getApptID()){
                MasterList.appointments.set(i, appt);
            }
        }
    }
}
