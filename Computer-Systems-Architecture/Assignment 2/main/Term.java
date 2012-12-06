package main;

import java.awt.*;
import java.awt.event.*;

public class Term extends Frame implements KeyListener, WindowListener, ActionListener {

  private static final long serialVersionUID = 1L;
  private Frame f;
  private TextArea slate;
  // Declares a Frame and Text Area to be utilised in the Term constructor.
  private char lastChar = 0;
  private boolean kbhit;
  // Declares a char for the last character typed in, and a boolean to see if a key has been pressed.

  /**
   * Function - Constructor to enable a terminal to be made from the Term class
   */
  public Term() {
    kbhit = false;
    // Sets it to false to stop errors occuring with null chars.
    f = new Frame("My JavaLAN");
    slate = new TextArea();
    // Instantiates a new Frame f, and a new Text Area slate.
    slate.addKeyListener(this);
    slate.setEditable(false);
    // Adds a keyboard listener to the text area, and makes it editable.
    f.addWindowListener(this);
    f.add(slate);
    // Adds a window listener for all window events, and adds the text area.
    f.setSize(500, 900);
    f.setVisible(true);
    // Sets the size and enables visibility.
    slate.setBackground(Color.lightGray);
    slate.setForeground(Color.blue);
    slate.setFont(new Font("Arial", Font.BOLD, 11));
    // Sets the back and foreground colours, along with the font.
  } // Term()

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  @Override
  /**
   * @param e - Event that shows when a key has been typed, and stores the key.
   * Function - To return when a key has been typed by the user. Enables debug mode.
   */
  public void keyTyped(KeyEvent e) {
    lastChar = e.getKeyChar(); // This gets the char from kbd.
    if (lastChar == '`') {
      DebugDisplay.setVisible();
    } else {
      kbhit = true;
    }
  }

  // Following methods handle window events.
  public void windowActivated(WindowEvent e) {
  }

  public void windowDeactivated(WindowEvent e) {
  }

  public void windowDeiconified(WindowEvent e) {
  }

  public void windowIconified(WindowEvent e) {
  }

  public void windowClosed(WindowEvent e) {
  }

  public void windowClosing(WindowEvent e) {
    dispose();
    System.exit(0);
  }
  // Event to process when the terminal is closed.

  public void windowOpened(WindowEvent e) {
  }

  public void windowOpening(WindowEvent e) {
  }

  public void actionPerformed(ActionEvent event) {
  }

  public void print(String s) {
    slate.append(s);
  }

  public void println(String s) {
    print(s + "\n");
  }
  // Print methods to display characters to the screen.

  /**
   * @param ch - Character passed in to display to the terminal.
   * Function - Displays a character to the terminal.
   */
  public void putChar(char ch) {
    StringBuffer str = new StringBuffer(" ");
    str.setCharAt(0, ch);
    slate.append(str.toString());
  }
  
  /**
   * @return kbhit - Returns True/False if the keyboard was hit or not.
   */
  public boolean getkbhit() {
    return kbhit;
  }

/**
   * @return lastChar - The character last input by the user.
   * Function - Returns the last character inputted by the user, if one was inputted.
   */
  public char getChar() {
    if (kbhit == true) {
      kbhit = false;
      return lastChar;
    } else {
      return 0;
    } //if
  }//getChar()
}// Term