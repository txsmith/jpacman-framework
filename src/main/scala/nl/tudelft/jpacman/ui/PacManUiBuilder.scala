package nl.tudelft.jpacman.ui

import java.util
import java.util.{HashMap, LinkedHashMap, Map}

import nl.tudelft.jpacman.game.Game
import nl.tudelft.jpacman.ui.ScorePanel.ScoreFormatter


/**
  * Builder for the JPac-Man UI.
  *
  * @author Jeroen Roosen 
  */
object PacManUiBuilder {
  /**
    * Caption for the default stop button.
    */
  private val STOP_CAPTION = "Stop"
  /**
    * Caption for the default start button.
    */
  private val START_CAPTION = "Start"
}

/**
  * Creates a new Pac-Man UI builder without any mapped keys or buttons.
  */
class PacManUiBuilder() {
  this.defaultButtons = false
  /**
    * Map of buttons and their actions.
    */
  final private var buttons = new util.LinkedHashMap[String, Action]
  /**
    * Map of key events and their actions.
    */
  final private var keyMappings = new util.HashMap[Integer, Action]
  /**
    * <code>true</code> iff this UI has the default buttons.
    */
  private var defaultButtons = false
  /**
    * Way to format the score.
    */
  private var scoreFormatter: ScoreFormatter = null

  /**
    * Creates a new Pac-Man UI with the set keys and buttons.
    *
    * @param game
    * The game to build the UI for.
    * @return A new Pac-Man UI with the set keys and buttons.
    */
  def build(game: Game): PacManUI = {
    assert(game != null)
    if (defaultButtons) {
      addStartButton(game)
      addStopButton(game)
    }
    new PacManUI(game, buttons, keyMappings, scoreFormatter)
  }

  /**
    * Adds a button with the caption {@value #STOP_CAPTION} that stops the
    * game.
    *
    * @param game
    * The game to stop.
    */
  private def addStopButton(game: Game): Unit = {
    assert(game != null)
    buttons.put(PacManUiBuilder.STOP_CAPTION, game.stop)
  }

  /**
    * Adds a button with the caption {@value #START_CAPTION} that starts the
    * game.
    *
    * @param game
    * The game to start.
    */
  private def addStartButton(game: Game): Unit = {
    assert(game != null)
    buttons.put(PacManUiBuilder.START_CAPTION, game.start)
  }

  /**
    * Adds a key listener to the UI.
    *
    * @param keyCode
    * The key code of the key as used by { @link java.awt.event.KeyEvent}.
    * @param action
    * The action to perform when the key is pressed.
    * @return The builder.
    */
  def addKey(keyCode: Integer, action: Action): PacManUiBuilder = {
    assert(keyCode != null)
    assert(action != null)
    keyMappings.put(keyCode, action)
    this
  }

  /**
    * Adds a button to the UI.
    *
    * @param caption
    * The caption of the button.
    * @param action
    * The action to execute when the button is clicked.
    * @return The builder.
    */
  def addButton(caption: String, action: Action): PacManUiBuilder = {
    assert(caption != null)
    assert(!caption.isEmpty)
    assert(action != null)
    buttons.put(caption, action)
    this
  }

  /**
    * Adds a start and stop button to the UI. The actual actions for these
    * buttons will be added upon building the UI.
    *
    * @return The builder.
    */
  def withDefaultButtons: PacManUiBuilder = {
    defaultButtons = true
    buttons.put(PacManUiBuilder.START_CAPTION, null)
    buttons.put(PacManUiBuilder.STOP_CAPTION, null)
    this
  }

  /**
    * Provide formatter for the score.
    *
    * @param scoreFormatter
    * The score formatter to be used.
    * @return The builder.
    */
  def withScoreFormatter(scoreFormatter: ScorePanel.ScoreFormatter): PacManUiBuilder = {
    this.scoreFormatter = scoreFormatter
    this
  }
}
