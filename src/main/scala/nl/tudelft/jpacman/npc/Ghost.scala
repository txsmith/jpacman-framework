package nl.tudelft.jpacman.npc

import nl.tudelft.jpacman.board.Direction
import nl.tudelft.jpacman.board.Square
import nl.tudelft.jpacman.board.JpacmanUnit
import nl.tudelft.jpacman.sprite.Sprite
import java.util
import java.util.{ArrayList, List, Map, Optional, Random}

import monix.reactive.Observable
import nl.tudelft.jpacman.game.Game

import concurrent.duration._


/**
  * A non-player unit.
  *
  * @author Jeroen Roosen
  */
abstract class Ghost protected(/**
                                 * The sprite map, one sprite for each direction.
                                 */
                               val sprites: util.Map[Direction, Sprite],

                               /**
                                 * The base move interval of the ghost.
                                 */
                               val moveInterval: Int,

                               /**
                                 * The random variation added to the {@link #moveInterval}.
                                 */
                               val intervalVariation: Int)

/**
  * Creates a new ghost.
  *
  * @param spriteMap         The sprites for every direction.
  * @param moveInterval      The base interval of movement.
  * @param intervalVariation The variation of the interval.
  */
  extends JpacmanUnit {

  def ghostEvents(): Observable[Game => Game] = {
    Observable
      .interval(getInterval milliseconds)
      .map {_ =>
        def stateChange(game: Game): Game = {
          game.getLevel.move(this, nextMove)
          game
        }
        stateChange
      }
  }
  /**
    * Calculates the next move for this unit and returns the direction to move
    * in.
    * <p>
    * Precondition: The NPC occupies a square (hasSquare() holds).
    *
    * @return The direction to move in, or <code>null</code> if no move could
    *         be devised.
    */
  def nextMove: Direction = nextAiMove.getOrElse(randomMove)

  /**
    * Tries to calculate a move based on the behaviour of the npc.
    *
    * @return an optional containing the move or empty if the current state of the game
    *         makes the ai move impossible
    */
  def nextAiMove: Option[Direction]

  override def getSprite: Sprite = sprites.get(getDirection)

  /**
    * The time that should be taken between moves.
    *
    * @return The suggested delay between moves in milliseconds.
    */
  def getInterval: Long = this.moveInterval + new Random().nextInt(this.intervalVariation)

  /**
    * Determines a possible move in a random direction.
    *
    * @return A direction in which the ghost can move, or <code>null</code> if
    *         the ghost is shut in by inaccessible squares.
    */
  protected def randomMove: Direction = {
    val square: Square = getSquare
    val directions = new util.ArrayList[Direction]
    for (direction <- Direction.values) {
      if (square.getSquareAt(direction).isAccessibleTo(this)) directions.add(direction)
    }
    if (directions.isEmpty) return null
    val i: Int = new Random().nextInt(directions.size)
    directions.get(i)
  }
}
