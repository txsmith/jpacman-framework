package nl.tudelft.jpacman.level

import java.util.Optional

import nl.tudelft.jpacman.board.Board
import nl.tudelft.jpacman.board.Direction
import nl.tudelft.jpacman.board.Square
import nl.tudelft.jpacman.npc.Ghost
import nl.tudelft.jpacman.npc.GhostColor
import nl.tudelft.jpacman.npc.GhostFactory
import nl.tudelft.jpacman.sprite.PacManSprites
import nl.tudelft.jpacman.sprite.Sprite


/**
  * Factory that creates levels and units.
  *
  * @author Jeroen Roosen
  */
object LevelFactory {
  private val GHOSTS = 4
  private val BLINKY = 0
  private val INKY = 1
  private val PINKY = 2
  private val CLYDE = 3
  /**
    * The default value of a pellet.
    */
  private val PELLET_VALUE = 10
}

class LevelFactory(val sprites: PacManSprites,
                   val ghostFact: GhostFactory) {
  this.ghostIndex = -1
  /**
    * Used to cycle through the various ghost types.
    */
  private var ghostIndex = 0

  /**
    * Creates a new level from the provided data.
    *
    * @param board
    * The board with all ghosts and pellets occupying their squares.
    * @param ghosts
    * A list of all ghosts on the board.
    * @param startPositions
    * A list of squares from which players may start the game.
    * @return A new level for the board.
    */
  def createLevel(board: Board, ghosts: List[Ghost], startPositions: List[Square]): Level = { // We'll adopt the simple collision map for now.
    val collisionMap = new PlayerCollisions
    new Level(board, ghosts, startPositions, collisionMap)
  }

  /**
    * Creates a new ghost.
    *
    * @return The new ghost.
    */
  private[level] def createGhost = {
    ghostIndex += 1
    ghostIndex %= LevelFactory.GHOSTS
    ghostIndex match {
      case LevelFactory.BLINKY =>
        ghostFact.createBlinky
      case LevelFactory.INKY =>
        ghostFact.createInky
      case LevelFactory.PINKY =>
        ghostFact.createPinky
      case LevelFactory.CLYDE =>
        ghostFact.createClyde
      case _ =>
        throw new RuntimeException("lol waddaya doin")
    }
  }

  /**
    * Creates a new pellet.
    *
    * @return The new pellet.
    */
  def createPellet = new Pellet(LevelFactory.PELLET_VALUE, sprites.getPelletSprite)
}
