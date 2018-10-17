package nl.tudelft.jpacman.ui

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util
import java.util.Map


/**
  * A key listener based on a set of keyCode-action pairs.
  *
  * @author Jeroen Roosen 
  */
class PacKeyListener private[ui](/**
                                   * The mappings of keyCode to action.
                                   */
                                 val mappings: util.Map[Integer, Action])

/**
  * Create a new key listener based on a set of keyCode-action pairs.
  *
  * @param keyMappings The mappings of keyCode to action.
  */
  extends KeyListener {
  assert(mappings != null)

  override def keyPressed(event: KeyEvent): Unit = {
    assert(event != null)
    val action = mappings.get(event.getKeyCode)
    if (action != null) action()
  }

  override def keyTyped(event: KeyEvent): Unit = {
    // do nothing
  }

  override def keyReleased(event: KeyEvent): Unit = {
  }
}
