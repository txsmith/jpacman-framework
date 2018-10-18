package nl.tudelft.jpacman.level

import java.io.{BufferedReader, IOException, InputStream, InputStreamReader}
import java.util

import cats.data.{Nested, Writer}
import nl.tudelft.jpacman.PacmanConfigurationException
import nl.tudelft.jpacman.board.{BoardFactory, Square}
import nl.tudelft.jpacman.npc.Ghost
import cats.syntax.monad._
import cats.syntax.functor._
import cats.data.Nested._
import cats.data.Writer._
import cats.instances.list._
import cats.instances.tuple._
import cats.syntax.traverse._

import scala.io.Source


/**
  * Parses the text representation of the board into an actual level.
  *
  * <ul>
  * <li>Supported characters:
  * <li>' ' (space) an empty square.
  * <li>'#' (bracket) a wall.
  * <li>'.' (period) a square with a pellet.
  * <li>'P' (capital P) a starting square for players.
  * <li>'G' (capital G) a square with a ghost.
  * </ul>
  *
  * @param map
  * The text representation of the board, with map[x][y]
  * representing the square at position x,y.
  * @return The level as represented by this text.
  */
class MapParser(val levelCreator: LevelFactory,
                val boardCreator: BoardFactory) {

  type Info = (List[Ghost], List[Square])

  def parseMap(map: List[List[Char]]): Level = {
    val result: Writer[Info, Nested[List, List, Square]] = Nested(map).traverse(c => addSquare(c))
    val ((ghosts, startPos), grid) = result.run

    new Level(boardCreator.createBoard(grid.value), ghosts, startPos, null)
  }

  /**
    * Adds a square to the grid based on a given character. These
    * character come from the map files and describe the type
    * of square.
    *
    * @param c
    * Character describing the square type.
    */
  protected def addSquare(c: Char): Writer[Info, Square] = c match {
    case ' ' => Writer.value(boardCreator.createGround)
    case '#' => Writer.value(boardCreator.createWall)
    case '.' =>
      val pelletSquare = boardCreator.createGround
      levelCreator.createPellet.occupy(pelletSquare)
      Writer.value(pelletSquare)
    case 'G' =>
      val ghost = levelCreator.createGhost
      val ghostSquare = makeGhostSquare(ghost)
      Writer((List(ghost), Nil), ghostSquare)
    case 'P' =>
      val playerSquare = boardCreator.createGround
      Writer((Nil, List(playerSquare)), playerSquare)
    case _ =>
      throw new PacmanConfigurationException(s"Invalid character $c")
  }


  /**
    * creates a Square with the specified ghost on it
    * and appends the placed ghost into the ghost list.
    *
    * @param ghosts all the ghosts in the level so far, the new ghost will be appended
    * @param ghost  the newly created ghost to be placed
    * @return a square with the ghost on it.
    */
  protected def makeGhostSquare(ghost: Ghost): Square = {
    val ghostSquare = boardCreator.createGround
    ghost.occupy(ghostSquare)
    ghostSquare
  }

  /**
    * Parses the list of strings into a 2-dimensional character array and
    * passes it on to {@link #parseMap(char[][])}.
    *
    * @param text
    * The plain text, with every entry in the list being a equally
    * sized row of squares on the board and the first element being
    * the top row.
    * @return The level as represented by the text.
    * @throws PacmanConfigurationException If text lines are not properly formatted.
    */
  def parseMapList(text: List[String]): Level = {
    parseMap(text.map(_.toList).transpose)
  }

  /**
    * Check the correctness of the map lines in the text.
    *
    * @param text Map to be checked
    * @throws PacmanConfigurationException if map is not OK.
    */
  private def checkMapFormat(text: List[String]): Unit = {
    if (text == null) throw new PacmanConfigurationException("Input text cannot be null.")
    if (text.isEmpty) throw new PacmanConfigurationException("Input text must consist of at least 1 row.")
    val width = text.head.length
    if (width == 0) throw new PacmanConfigurationException("Input text lines cannot be empty.")
    for (line <- text) {
      if (line.length != width) throw new PacmanConfigurationException("Input text lines are not of equal width.")
    }
  }

  /**
    * Parses the provided input stream as a character stream and passes it
    * result to {@link #parseMap(List)}.
    *
    * @param source
    * The input stream that will be read.
    * @return The parsed level as represented by the text on the input stream.
    * @throws IOException
    * when the source could not be read.
    */
  @throws[IOException]
  def parseMap(source: InputStream): Level =
    parseMapList(Source.fromInputStream(source).getLines().toList)

  /**
    * Parses the provided input stream as a character stream and passes it
    * result to {@link #parseMap(List)}.
    *
    * @param mapName
    * Name of a resource that will be read.
    * @return The parsed level as represented by the text on the input stream.
    * @throws IOException
    * when the resource could not be read.
    */
  @throws[IOException]
  def parseMap(mapName: String): Level = {
    val boardStream = classOf[MapParser].getResourceAsStream(mapName)
    try {
      if (boardStream == null) throw new PacmanConfigurationException("Could not get resource for: " + mapName)
      parseMap(boardStream)
    } finally if (boardStream != null) boardStream.close()
  }
}
