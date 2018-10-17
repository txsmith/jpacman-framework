package nl.tudelft.jpacman.npc

import nl.tudelft.jpacman.board.Direction
import nl.tudelft.jpacman.board.Square
import nl.tudelft.jpacman.board.JpacmanUnit
import nl.tudelft.jpacman.level.Player
import nl.tudelft.jpacman.npc.Ghost
import nl.tudelft.jpacman.sprite.Sprite
import java.util
import java.util.Optional

/**
  * <p>
  * An implementation of the classic Pac-Man ghost Inky.
  * </p>
  * <b>AI:</b> Inky has the most complicated AI of all. Inky considers two things: Blinky's
  * location, and the location two grid spaces ahead of Pac-Man. Inky draws a
  * line from Blinky to the spot that is two squares in front of Pac-Man and
  * extends that line twice as far. Therefore, if Inky is alongside Blinky
  * when they are behind Pac-Man, Inky will usually follow Blinky the whole
  * time. But if Inky is in front of Pac-Man when Blinky is far behind him,
  * Inky tends to want to move away from Pac-Man (in reality, to a point very
  * far ahead of Pac-Man). Inky is affected by a similar targeting bug that
  * affects Speedy. When Pac-Man is moving or facing up, the spot Inky uses to
  * draw the line is two squares above and left of Pac-Man.
  * <p>
  * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
  * </p>
  *
  * @author Jeroen Roosen
  */
object Inky {
  private val SQUARES_AHEAD = 2
  /**
    * The variation in intervals, this makes the ghosts look more dynamic and
    * less predictable.
    */
  private val INTERVAL_VARIATION = 50
  /**
    * The base movement interval.
    */
  private val MOVE_INTERVAL = 250
}

class Inky(val spriteMap: util.Map[Direction, Sprite])

/**
  * Creates a new "Inky".
  *
  * @param spriteMap The sprites for this ghost.
  */
  extends Ghost(spriteMap, Inky.MOVE_INTERVAL, Inky.INTERVAL_VARIATION) {
  /**
    * {@inheritDoc }
    *
    * <p>
    * Inky has the most complicated AI of all. Inky considers two things: Blinky's
    * location, and the location two grid spaces ahead of Pac-Man. Inky
    * draws a line from Blinky to the spot that is two squares in front of
    * Pac-Man and extends that line twice as far. Therefore, if Inky is
    * alongside Blinky when they are behind Pac-Man, Inky will usually
    * follow Blinky the whole time. But if Inky is in front of Pac-Man when
    * Blinky is far behind him, Inky tends to want to move away from Pac-Man
    * (in reality, to a point very far ahead of Pac-Man). Inky is affected
    * by a similar targeting bug that affects Speedy. When Pac-Man is moving or
    * facing up, the spot Inky uses to draw the line is two squares above
    * and left of Pac-Man.
    * </p>
    *
    * <p>
    * <b>Implementation:</b>
    * To actually implement this in jpacman we have the following approximation:
    * first determine the square of Blinky (A) and the square 2
    * squares away from Pac-Man (B). Then determine the shortest path from A to
    * B regardless of terrain and walk that same path from B. This is the
    * destination.
    * </p>
    */
  override def nextAiMove: Option[Direction] = {
    assert(hasSquare)
    val blinky: JpacmanUnit = Navigation.findNearest(classOf[Blinky], getSquare)
    val player: JpacmanUnit = Navigation.findNearest(classOf[Player], getSquare)
    if (blinky == null || player == null) return Option.empty
    assert(player.hasSquare)
    val playerDestination: Square = player.squaresAheadOf(Inky.SQUARES_AHEAD)
    val firstHalf: util.List[Direction] = Navigation.shortestPath(blinky.getSquare, playerDestination, null)
    if (firstHalf == null) return Option.empty
    val destination: Square = followPath(firstHalf, playerDestination)
    val path: util.List[Direction] = Navigation.shortestPath(getSquare, destination, this)
    if (path != null && !path.isEmpty) return Option(path.get(0))
    Option.empty
  }

  private def followPath(directions: util.List[Direction], start: Square) = {
    var destination: Square = start
    import scala.collection.JavaConversions._
    for (d <- directions) {
      destination = destination.getSquareAt(d)
    }
    destination
  }
}