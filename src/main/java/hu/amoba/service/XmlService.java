package hu.amoba.service;

import hu.amoba.model.Board;
import hu.amoba.model.GameState;
import hu.amoba.model.Player;
import hu.amoba.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlService {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlService.class);
    private static final String DEFAULT_XML_FILE = "game_save.xml";

    public void saveGameToXml(GameState gameState, String filename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("GameState");
            doc.appendChild(rootElement);

            Element playerName = doc.createElement("PlayerName");
            playerName.appendChild(doc.createTextNode(gameState.getPlayerName()));
            rootElement.appendChild(playerName);

            Element board = doc.createElement("Board");
            rootElement.appendChild(board);

            Element rows = doc.createElement("Rows");
            rows.appendChild(doc.createTextNode(String.valueOf(gameState.getBoard().getRows())));
            board.appendChild(rows);

            Element cols = doc.createElement("Cols");
            cols.appendChild(doc.createTextNode(String.valueOf(gameState.getBoard().getCols())));
            board.appendChild(cols);

            Element currentPlayer = doc.createElement("CurrentPlayer");
            currentPlayer.appendChild(doc.createTextNode(gameState.getCurrentPlayer().name()));
            rootElement.appendChild(currentPlayer);

            Element moves = doc.createElement("Moves");
            rootElement.appendChild(moves);

            for (Position pos : gameState.getBoard().getOccupiedPositions()) {
                Element move = doc.createElement("Move");

                Element row = doc.createElement("Row");
                row.appendChild(doc.createTextNode(String.valueOf(pos.getRow())));
                move.appendChild(row);

                Element col = doc.createElement("Col");
                col.appendChild(doc.createTextNode(String.valueOf(pos.getCol())));
                move.appendChild(col);

                Element player = doc.createElement("Player");
                player.appendChild(doc.createTextNode(String.valueOf(gameState.getBoard().getPlayerAt(pos).getSymbol())));
                move.appendChild(player);

                moves.appendChild(move);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

            LOGGER.info("Game saved to XML file: {}", filename);

        } catch (Exception e) {
            LOGGER.error("Error saving game to XML: {}", e.getMessage());
        }
    }

    public GameState loadGameFromXml(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            LOGGER.info("No XML save file found: {}", filename);
            return null;
        }

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();

            String playerName = doc.getElementsByTagName("PlayerName").item(0).getTextContent();
            int rows = Integer.parseInt(doc.getElementsByTagName("Rows").item(0).getTextContent());
            int cols = Integer.parseInt(doc.getElementsByTagName("Cols").item(0).getTextContent());
            String currentPlayerStr = doc.getElementsByTagName("CurrentPlayer").item(0).getTextContent();

            Board board = new Board(rows, cols);
            GameState gameState = new GameState(board, playerName);

            NodeList moveList = doc.getElementsByTagName("Move");
            for (int i = 0; i < moveList.getLength(); i++) {
                Node moveNode = moveList.item(i);
                if (moveNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element moveElement = (Element) moveNode;
                    int row = Integer.parseInt(moveElement.getElementsByTagName("Row").item(0).getTextContent());
                    int col = Integer.parseInt(moveElement.getElementsByTagName("Col").item(0).getTextContent());
                    char symbol = moveElement.getElementsByTagName("Player").item(0).getTextContent().charAt(0);

                    Player player = symbol == 'X' ? Player.HUMAN : Player.COMPUTER;
                    board.placeSymbol(new Position(row, col), player);
                }
            }

            if ("COMPUTER".equals(currentPlayerStr)) {
                gameState.switchPlayer();
            }

            LOGGER.info("Game loaded from XML file: {}", filename);
            return gameState;

        } catch (Exception e) {
            LOGGER.error("Error loading game from XML: {}", e.getMessage());
            return null;
        }
    }

    public String getDefaultXmlFile() {
        return DEFAULT_XML_FILE;
    }
}