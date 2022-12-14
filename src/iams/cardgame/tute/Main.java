
package iams.cardgame.tute;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.JFrame;
import static javax.swing.ScrollPaneConstants.*;


import iams.cardgame.tute.tr.Translator;
import iams.ui.GraphicsPanel;

public class Main extends GraphicsPanel
{
    Languages lg = new Languages();

    final public Translator tr = Translator.get(lg.getDefaultLanguage());
    
    private static final Font FONT = new Font("Times New Roman", Font.BOLD, 28); //$NON-NLS-1$
    
    private static final Font SMALL_FONT = new Font("Times New Roman", Font.BOLD, 22); //$NON-NLS-1$
    
    private static Color BACKGROUND_COLOR = new Color(13, 98, 69);
    
    public static final int BOARD_WIDTH = 700, BOARD_HEIGHT = 700;

    private final TuteController controller = new TuteController(this);
    
    final public TuteGame game = new TuteGame();
    
    final private HumanPlayer humanPlayer = new HumanPlayer(this.game, this.tr);
    
    private final TuteGameUI gameUI = new TuteGameUI(this.tr, this.game, this.controller, this.humanPlayer);

    private final GameMouseListener mouseListener = new GameMouseListener(this, this.game, this.gameUI, this.humanPlayer);

    public Main()
    {
        this.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentShown(ComponentEvent e)
            {
                this.componentResized(e);
            }
            
            @Override
            public void componentResized(ComponentEvent e)
            {
                Main.this.initializeBoundingBox(new Rectangle2D.Double(0, 0, BOARD_WIDTH, BOARD_HEIGHT));
            }
        });
        
        this.addMouseListener(this.mouseListener);
        this.addMouseMotionListener(this.mouseListener);
    }
    
    @Override
    protected void paint(Graphics2D g2, AffineTransform tx2)
    {
        this.mouseListener.mouseMoved();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2.setFont(FONT);
        FontMetrics fm = g2.getFontMetrics();
        
        g2.transform(tx2);
        
        g2.setColor(Color.yellow);
        
        if (this.gameUI.getPlayer1Points() >= 0)
        {
            String pointsString = this.tr.getPlayerPointsString(this.gameUI.getPlayer1Points());
            g2.drawString(pointsString, 150 - fm.stringWidth(pointsString), BOARD_HEIGHT - 100);
        }

        if (this.gameUI.getPlayer2Points() >= 0)
        {
            String pointsString = this.tr.getPlayerPointsString(this.gameUI.getPlayer2Points());
            g2.drawString(pointsString, 150 - fm.stringWidth(pointsString), 100 + fm.getMaxAscent());
        }

        g2.setColor(Color.white);

        String cardsRemain = this.gameUI.refreshCardsRemain(String.valueOf(this.game.getDeck().size()));
        g2.drawString(cardsRemain, 0 - fm.stringWidth(cardsRemain), 330 + fm.getMaxAscent());
        
        String notification = this.gameUI.getNotification();
        
        if (notification != null && !notification.isEmpty())
        {
            g2.drawString(notification, BOARD_WIDTH / 2 + Card.HEIGHT, BOARD_HEIGHT / 2 + fm.getMaxAscent() / 2);
        }
        
        FontMetrics fmPointsTurn = g2.getFontMetrics();

        g2.setFont(new Font("Times New Roman", Font.BOLD, 14));
        g2.setColor(Color.white);
        
        String pointsTurnPlayer1 = this.tr.getCardsOverdueText(String.valueOf(this.game.getPlayer1Baza().size() / 2));
        g2.drawString(pointsTurnPlayer1, 875 - fmPointsTurn.stringWidth(pointsTurnPlayer1), 645 + fmPointsTurn.getMaxAscent());
        
        String pointsTurnPlayer2 = this.tr.getCardsOverdueText(String.valueOf(this.game.getPlayer2Baza().size() / 2));
        g2.drawString(pointsTurnPlayer2, 875 - fmPointsTurn.stringWidth(pointsTurnPlayer2), 10 + fmPointsTurn.getMaxAscent());

        g2.setFont(SMALL_FONT);
        FontMetrics fm2 = g2.getFontMetrics();

        g2.setColor(new Color(152, 251, 152));

        String gameMessage = this.tr.getPlayerGamesString(
                this.game.getPlayer1Games(), 
                this.game.getPlayer2Games());
        
        g2.drawString(gameMessage, 
                BOARD_WIDTH / 2 - fm.stringWidth(gameMessage) / 2, 
                5 + fm.getMaxAscent());

        if (this.humanPlayer.player1Message != null && !this.humanPlayer.player1Message.isEmpty())
        {
            g2.drawString(this.humanPlayer.player1Message, 
                    BOARD_WIDTH / 2 - fm2.stringWidth(this.humanPlayer.player1Message) / 2, 
                    BOARD_HEIGHT - fm2.getMaxAscent());
        }

        Card[] cardRasters = this.game.getCardRasters().toArray(new Card[0]);
        
        Arrays.sort(cardRasters, Card.PAINT_COMPARATOR);
        
        for (Card card : cardRasters)
            card.draw(g2, tx2, this);
    }

    static public void restartGame(JFrame frame) {
        frame.dispose();
        try {
            main(null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static public void main(String[] args) throws IOException
    {
        final Languages lg = new Languages();
        final Translator tr = Translator.get(lg.getDefaultLanguage());

        Path path = Paths.get("iconhq.png");

        Path pathIcon = path.toRealPath();

        ImageIcon appIcon = new ImageIcon(pathIcon.toString());

        JFrame frame = new JFrame(tr.getWindowTitle());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(new Main());
        frame.setLocationByPlatform(true);
        
        //JLabel lab1 = new JLabel("U", JLabel.LEFT);
        //frame.setLayout(new FlowLayout()); 
        //frame.add(lab1);

        JMenuBar menuBar = new JMenuBar();

        JMenu options = new JMenu(tr.getMenuItemNames("OPTIONS"));
        JMenuItem restart = new JMenuItem(tr.getMenuItemNames("RESTART"));
        JMenuItem exit = new JMenuItem(tr.getMenuItemNames("QUIT"));

        JMenu game = new JMenu(tr.getMenuItemNames("GAME"));
        JMenuItem rules = new JMenuItem(tr.getMenuItemNames("RULES"));
        JMenu languages = new JMenu(tr.getMenuItemNames("LANGUAGES"));
        JMenuItem english = new JMenuItem(tr.getMenuItemNames("ENGLISH"));
        JMenuItem portuguese = new JMenuItem(tr.getMenuItemNames("PORTUGUESE"));
        JMenuItem spanish = new JMenuItem(tr.getMenuItemNames("SPANISH"));
        
        JMenu colorBackground = new JMenu(tr.getMenuItemNames("COLORBACKGROUND"));
        JMenuItem backgroundGreen = new JMenuItem(tr.getMenuItemNames("GREEN"));
        JMenuItem backgroundRed = new JMenuItem(tr.getMenuItemNames("RED"));
        JMenuItem backgroundBlue = new JMenuItem(tr.getMenuItemNames("BLUE"));

        menuBar.add(game);
        menuBar.add(options);

        options.add(restart);
        options.add(exit);
        options.add(colorBackground);

        game.add(rules);
        game.add(languages);
        languages.add(english);
        languages.add(portuguese);
        languages.add(spanish);
        colorBackground.add(backgroundGreen);
        colorBackground.add(backgroundRed);
        colorBackground.add(backgroundBlue);
        
        backgroundGreen.addActionListener(e -> {
    		BACKGROUND_COLOR = new Color(13, 98, 69);
            restartGame(frame);
        });

        backgroundRed.addActionListener(e -> {
    		BACKGROUND_COLOR = new Color(92, 0, 27);
            restartGame(frame);
        });

        backgroundBlue.addActionListener(e -> {
    		BACKGROUND_COLOR = new Color(0, 53, 95);
            restartGame(frame);
        });

        rules.addActionListener(e -> {
        	try {
				RulesController.ShowRules(tr, appIcon);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        });

        restart.addActionListener(e -> restartGame(frame));

        exit.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        english.addActionListener(e -> {
            lg.setDefaultLanguage("EN");
            restartGame(frame);
        });

        portuguese.addActionListener(e -> {
            lg.setDefaultLanguage("PT");
            restartGame(frame);
        });

        spanish.addActionListener(e -> {
            lg.setDefaultLanguage("SP");
            restartGame(frame);
        });

    	try {
			RulesController.ShowRules(tr, appIcon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
        
    }
}
