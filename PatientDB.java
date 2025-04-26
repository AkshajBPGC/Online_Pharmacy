import java.io.Serializable;

class PatientDB implements Serializable
{
    private Patient[] patientDB;
    private int numPatients;
    
    public PatientDB() {
        this.patientDB = new Patient[100]; // Initial capacity
        this.numPatients = 0;
    }
    
    public void addPatient(Patient patient) {
        if (numPatients >= patientDB.length) {
            // Expand the array if needed
            Patient[] newPatientDB = new Patient[patientDB.length * 2];
            System.arraycopy(patientDB, 0, newPatientDB, 0, patientDB.length);
            patientDB = newPatientDB;
        }
        patientDB[numPatients++] = patient;
    }
    
    public Patient getPatientById(int id) {
        for (int i = 0; i < numPatients; i++) {
            if (patientDB[i].getId() == id) {
                return patientDB[i];
            }
        }
        return null;
    }
    
    public Patient getPatientByUsername(String username) {
        for (int i = 0; i < numPatients; i++) {
            if (patientDB[i].getUsername().equals(username)) {
                return patientDB[i];
            }
        }
        return null;
    }
    
    public Patient[] getAllPatients() {
        Patient[] allPatients = new Patient[numPatients];
        System.arraycopy(patientDB, 0, allPatients, 0, numPatients);
        return allPatients;
    }
    
    public int getNumPatients() {
        return numPatients;
    }
}