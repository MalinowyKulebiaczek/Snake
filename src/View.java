import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JOptionPane;
/**
 * Klasa, ktora wyswietla kazdy element gry
 * @author Rafal Wiercioch
 *
 */
public class View extends JFrame{
	Model theModel;
	Menu menu;
	GameView game;
	SettingMenu settingMenu;
	YouLostAllert loseAllertBox;
	/**Konstruktor klasy View. 
	 * Tworzy wszystkie obecne w programie menu, tzn.: menu poczatkowe, widok gry, menu ustawien oraz okienko informujace o przegranej.
	 * @throws IOException
	 * */
	public View(Model theModel) throws IOException {
		this.theModel=theModel;
		this.menu=new Menu();
		this.game=new GameView();
		this.settingMenu=new SettingMenu();
		this.loseAllertBox=new YouLostAllert();
	}
	/**Klasa reprezentujaca Menu poczatkowe.
	 * Sklada sie ono z okna, na ktorym wyswietlany jest obiekt panel klasy Pane
	 * @see Pane
	 */
	public class Menu extends JPanel{
		JFrame menuWindow;
		Pane panel;
		public Menu() throws IOException {
			this.menuWindow=new JFrame();
			this.panel=new Pane();			
			menuWindow.setSize(Model.SIZE_X,Model.SIZE_Y);
			Toolkit tk=Toolkit.getDefaultToolkit();
			Dimension dim = tk.getScreenSize();
			int xPos=(dim.width/2)-(this.menuWindow.getWidth()/2);
			int yPos=(dim.height/2)-(this.menuWindow.getHeight()/2);
			this.menuWindow.setLocation(xPos, yPos);
			menuWindow.add(panel, BorderLayout.CENTER);
			pack();
			menuWindow.setTitle("SNAKE");
			menuWindow.setVisible(true);
			menuWindow.setResizable(false);
		}
		/**
		 * 
		 * Klasa, ktora reprezentuje panel znajdujacy sie w menu glownym.
		 * Panel sklada sie z obrazu zawierajacego tytul oraz dwoch przyciskow: startButton, ktory wlacza rozgrywke oraz settingsButton, dzieki ktoremu mozna przejsc do menu ustawien. 
		 *
		 */
		class Pane extends JPanel{
			BufferedImage backImg;
			JButton startButton;
			JButton settingsButton;
			public Pane() throws IOException {
				this.startButton=new JButton("Play");
				this.settingsButton=new JButton("Settings");
				this.backImg=ImageIO.read(this.getClass().getResource("/backgr2.png"));
				this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				this.setSize(Model.SIZE_X,Model.SIZE_Y);
				
				//title image
				BufferedImage myPicture = ImageIO.read(this.getClass().getResource("/New Project.png"));
				ImageClass titleImg=new ImageClass(myPicture, (this.getWidth())/2-(myPicture.getWidth())/2-75, 0);
				titleImg.setMaximumSize(new Dimension(350,125));
				
				Dimension buttonSize=new Dimension(100,50);
				startButton.setMaximumSize(buttonSize);
				settingsButton.setMaximumSize(buttonSize);
				
				this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
				this.add(titleImg);
				this.add(Box.createRigidArea(new Dimension(0,40)));
				this.add(startButton);
				this.add(Box.createRigidArea(new Dimension(0,40)));
				this.add(settingsButton);
				
				startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				titleImg.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				pack();
				this.setVisible(true);
			}
			/**Funkcja rysujaca obraz jako tlo panelu*/
			 @Override 
			public void paintComponent(Graphics g){
		    	Graphics2D g2d=(Graphics2D) g;
		    	super.paintComponent(g);
		    	g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    	g2d.drawImage(backImg,0,0,this.getWidth(),this.getHeight(),this);
			}
		}
	}
	/**
	 * Klasa uzyta do wyswietlania obrazow 
	 */
	class ImageClass extends JComponent{
	    /**Koordynaty wyswietlanego obrazu*/
		int x,y;
	    BufferedImage myImage;
	    
	    public ImageClass(BufferedImage img, int x, int y) {
	    	this.myImage=img;
	    	this.x=x;
	    	this.y=y;
	    }
	    /**Funkcja wyswietlajaca obraz w miejscu o wspolrzednych x, y*/
	    @Override 
	    public void paint(Graphics g){
	    	Graphics2D g2d=(Graphics2D) g;
	    	g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    	g2d.drawImage(myImage,x,y,this);
	    }
	}
	/**
	 * Klasa reprezentujaca menu rozgrywki.
	 * Menu rozgrywki sklada sie z panelu JSplitPane podzielonego na dwie czesci.
	 * Czesc lewa to panel gry, na ktorym widac weza poruszajacego sie po planszy.
	 * Czesc prawa to panel, na ktorym znajduje sie wynik gracza, przycisk wracajacy do menu glownego oraz napis informujacy, ze po wcisnieciu "P" zostanie wlaczona pauza.
	 * 
	 */
	public class GameView extends JFrame{
		public static final int SIZE_X=500;
		public static final int SIZE_Y=453;
		
		JSplitPane splitPane;
		/**Panel, na ktorym wyswietlana jest gra*/
		JPanel gamePanel;
		/**Panel, na ktorym wyswietlany jest przycisk powrotu oraz informacja na temat wyniku*/
		JPanel panel_02;
		/**Reprezentuje wynik*/
		JLabel scoreLabel;
		/**Przycisk powrotu do menu glownego*/
		JButton returnToMenuButton;
		public GameView(){
			this.splitPane=new JSplitPane();
			this.returnToMenuButton=new JButton("Menu");
			this.gamePanel=new MyPanel();
			this.panel_02=new JPanel();
			this.scoreLabel=new JLabel("Score: 0");
			JLabel pauseLabel=new JLabel("P - PAUSE");
			scoreLabel.setFont(new Font("Sans Serif", Font.BOLD, 26));
			pauseLabel.setFont(new Font("Sans Serif", Font.BOLD, 18));
			
			Dimension minimumSizeOfPanelGry=new Dimension(SIZE_X-100, SIZE_Y);
			Dimension maxSizeOfPanelGry=new Dimension(SIZE_X-100, SIZE_Y);
			Dimension minimumSizeOfPanel02=new Dimension(SIZE_X-300, SIZE_Y);
			
			gamePanel.setBackground(Color.BLACK);
			panel_02.setLayout(new BoxLayout(panel_02, BoxLayout.PAGE_AXIS));
			this.splitPane.setPreferredSize(new Dimension((int)1.2*SIZE_X+100,SIZE_Y));
			
			getContentPane().setLayout(new GridLayout());
			getContentPane().add(splitPane);
			
			splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPane.setDividerLocation(SIZE_X-100);
			splitPane.setLeftComponent(gamePanel);
			splitPane.setRightComponent(panel_02);
			gamePanel.setMinimumSize(minimumSizeOfPanelGry);
			gamePanel.setMaximumSize(maxSizeOfPanelGry);
			panel_02.setMinimumSize(minimumSizeOfPanel02);
			
					
			panel_02.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
			panel_02.add(Box.createRigidArea(new Dimension(0,40)));
			panel_02.add(scoreLabel);
			panel_02.add(Box.createRigidArea(new Dimension(0,100)));
			panel_02.add(returnToMenuButton);
			panel_02.add(Box.createRigidArea(new Dimension(0,100)));
			panel_02.add(pauseLabel);
			returnToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			pauseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			Toolkit tk=Toolkit.getDefaultToolkit();
			Dimension dim = tk.getScreenSize();
			pack();
			int xPos=(dim.width/2)-(this.splitPane.getWidth()/2);
			int yPos=(dim.height/2)-(this.splitPane.getHeight()/2);
			this.setLocation(xPos, yPos);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("SNAKE");
			this.gamePanel.setFocusable(true);
			this.setResizable(false);
			pack();
			
		}
	}
	/**Klasa reprezentujaca panel, na ktorym wyswietlany jest postep rozgrywki, tj. mapa z jablkiem oraz wszystkimi segmentami weza*/
	private class MyPanel extends JPanel{
		public void paintComponent(Graphics g) {
			int i;
			super.paintComponent(g);
			Graphics2D graph2=(Graphics2D)g;
			graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			Shape drawRect = new RoundRectangle2D.Double(theModel.snake.getSnakePoints().get(0).getX(), theModel.snake.getSnakePoints().get(0).getY(), 25, 25, 50, 50);
			
			g.setColor(Color.RED);
			graph2.fill(new RoundRectangle2D.Double(theModel.getApple().getX(), theModel.getApple().getY(), 25, 25, 30, 30));
			
			g.setColor(Color.BLUE);
			for(i=1;i<theModel.snake.getSnakePoints().size();i++) {
				graph2.fill(new RoundRectangle2D.Double(theModel.snake.getSnakePoints().get(i).getX(), theModel.snake.getSnakePoints().get(i).getY(), 25, 25, 30, 30));
			}
			
			graph2.setColor(Color.GREEN);
			graph2.fill(drawRect);	
		}
	}
	/**Metoda aktualizujaca napis wyswietlajacy wynik*/
	public void changeScoreLabel() {
		this.game.scoreLabel.setText("Score: "+theModel.getScore());
	}
	/**Klasa reprezentujaca menu ustawien. 
	 * Menu sklada sie z grupy przyciskow za pomoca, ktorych mozna wybrac poziom trudnosci, przycisku powrotu do menu glownego oraz checkboxa decydujacego o przenikalnosci scian*/
	public class SettingMenu{
		JFrame menuWindow;
		JToggleButton intLevelButton;
		JToggleButton beginnerLevelButton;
		JToggleButton expertLevelButton;
		JButton returnToMenuButton;
		JCheckBox wallCheckBox;
		public SettingMenu() {
			this.menuWindow=new JFrame();
			this.beginnerLevelButton=new JToggleButton("Beginner");
			this.intLevelButton=new JToggleButton("Intermediate");
			this.expertLevelButton=new JToggleButton("Expert");
			this.returnToMenuButton=new JButton("Menu");
			this.wallCheckBox=new JCheckBox("Snake can't go through walls");
			ButtonGroup buttonGroup=new ButtonGroup();
			buttonGroup.add(beginnerLevelButton);
			buttonGroup.add(intLevelButton);
			buttonGroup.add(expertLevelButton);
			
			JLabel levelLabel=new JLabel("Level");
			JLabel wallLabel=new JLabel("Go-through Walls");
			levelLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));
			wallLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));
			JPanel pane=new JPanel();
			JPanel levelPane=new JPanel();			
			
			Toolkit tk=Toolkit.getDefaultToolkit();
			Dimension dim = tk.getScreenSize();
			menuWindow.setSize(Model.SIZE_X,Model.SIZE_Y);
			int xPos=(dim.width/2)-(this.menuWindow.getWidth()/2);
			int yPos=(dim.height/2)-(this.menuWindow.getHeight()/2);
			this.menuWindow.setLocation(xPos, yPos);
			
			
			levelPane.setLayout(new GridLayout(1,3));
			levelPane.add(beginnerLevelButton);
			levelPane.add(intLevelButton);
			levelPane.add(expertLevelButton);
			levelPane.setMaximumSize(new Dimension(340,50));
			
			
			pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
			pane.setSize(Model.SIZE_X,Model.SIZE_Y);
			pane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
			pane.add(Box.createRigidArea(new Dimension(0,40)));
			pane.add(levelLabel);
			pane.add(Box.createRigidArea(new Dimension(0,5)));
			pane.add(levelPane);
			pane.add(Box.createRigidArea(new Dimension(0,40)));
			pane.add(wallLabel);
			pane.add(Box.createRigidArea(new Dimension(0,5)));
			pane.add(wallCheckBox);
			pane.add(Box.createRigidArea(new Dimension(0,40)));
			pane.add(returnToMenuButton);
			levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);;
			levelPane.setAlignmentX(Component.CENTER_ALIGNMENT);
			wallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			wallCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
			returnToMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			pack();
			menuWindow.add(pane, BorderLayout.CENTER);
			menuWindow.setVisible(false);	
		}
	}

	/**Klasa wykorzystana do stworzenia okienka z wiadomoscia o przegranej*/
	public class YouLostAllert{

	    public void showBox(){
	        JOptionPane.showMessageDialog(null, "You lost", "LOOSER", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
}
