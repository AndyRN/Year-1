package main;

public class PendtableRecord {

  public static final int PACKETSIZE = 16;
  public int loggedin, pending, delay;
  public char[] packet = new char[PACKETSIZE];
  // Declares the variables required

  public PendtableRecord() {
    loggedin = 0;
    pending = 0;
    delay = 1000;
    clearPacket();
  } // PendtableRecord()
  // Constructor used for the instances of this class.

  public void setLoggedin(int logIn) {
    loggedin = logIn;
  } // setLoggedin()
  // Setter for changing the loggedin variable.

  public int getLoggedin() {
    return loggedin;
  } // getLoggedin()
  // Getter for obtaining the loggedin variable.

  public void setPending(int pendValue) {
    pending = pendValue;
  } // setPending()
  // Setter for changing the pending variable.

  public int getPending() {
    return pending;
  } // getPending()
  // Getter for obtaining the pending variable.

  public int decPending() {
    pending--;
    return pending;
  } // decPending()
  // Decrements the pending value for a record in the pendtable.

  public void setDelay(int delayValue) {
    delay = delayValue;
  } // setDelay()
  // Setter for changing the delay variable.

  public int getDelay() {
    return delay;
  } // getDelay()
  // Getter for obtaining the delay variable.

  public int decDelay() {
    delay--;
    return delay;
  } // decDelay()
  // Decrements the delay of a record in the pendtable.

  public void clearPacket() {
    for (int i = 0; i < packet.length; i++) {
      packet[i] = ' ';
    } // clearPacket()
  }
  // Clears the packet in the record specified in the pendtable.

  public void setPacket(char[] newPacket) {
    for (int i = 0; i < packet.length; i++) {
      packet[i] = newPacket[i];
    }
  } // setPacket()
  // Puts a packet into a record in the pendtable.

  public char[] getPacket() {
    return packet;
  } // getPacket()
  // Obtains the packet from the pendtable for a specified record.
} // PendtableRecord