import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

/**Klasa kontrolujaca komunikacje pomiedzy klasa wyswietlajaca, a klasa wykonujaca obliczenia*/
public class Controller {
	private View theView;
	private Model theModel;
	/**Timer wykorzystany do odswiezania stanu gry*/
	Timer timer;
	/**Zmienna sygnalizujaca czy jestesmy w menu glownym, czy w menu rozgrywki.
	 * Przyjmuje wartosc 0 w menu oraz 1 podczas rozgrywki.*/
	private int gameState=0;//0-menu, 1-game
	/**Zmienna sluzaca za flage, mowiaca o tym czy to poczatkowy przebieg gry*/
	private int start=0;
	/**Flaga wskazujaca czy trwa rozgrywka czy wlaczona jest pauza.
	 * Przyjmuje true, gdy gra jest zatrzymana oraz false, gdy trwa rozgrywka*/
	private boolean pause=true;
	/**Konstruktor klasy kontrolujacej: dodaje nasluchiwaczy zdarzen do wszystkich komponentow gry*/
	public Controller(View theView, Model theModel) {
		this.theView=theView;
		this.theModel=theModel;
		this.theView.game.gamePanel.addKeyListener(new ListenForKeys());
		this.theView.menu.panel.startButton.addActionListener(new ListenForButton());
		this.theView.game.returnToMenuButton.addActionListener(new ListenForMenuButton());
		this.theView.menu.panel.settingsButton.addActionListener(new ListenForSettingsButton());
		this.theView.settingMenu.returnToMenuButton.addActionListener(new ListenForMenuInSettingsButton());
		this.theView.settingMenu.beginnerLevelButton.addActionListener(new ListenForLevelButton());
		this.theView.settingMenu.intLevelButton.addActionListener(new ListenForLevelButton());
		this.theView.settingMenu.expertLevelButton.addActionListener(new ListenForLevelButton());
		this.theView.settingMenu.wallCheckBox.addItemListener(new ListenForWallCheckbox());
	}
	int getGameState() {
		return this.gameState;
	}
	void setGameState(int x) {
		this.gameState=x;
	}
	int getStart() {
		return this.start;
	}
	void setStart(int s) {
		this.start=s;
	}
	/**Klasa zajmujaca sie nasluchiwaniem przyciskow klawiatury*/
	public class ListenForKeys implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
		}


		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * Funkcja, ktora zmienia kierunek weza w zaleznosci od uzytego przycisku, a takze zatrzymuje lub wlacza timer po nacisnieciu litery "P".
		 * Funckja ta rowniez wlacza timer przy pierwszym przebiegu gry niezaleznie od wcisnietego przycisku.
		 * @see RefreshTask
		 */
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(pause==false || (pause==true && start==0)) {		//zmieniaj kierunek ruchu tylko gdy nie ma pauzy i gra juz ruszyla
			
				if(e.getKeyCode()==KeyEvent.VK_UP && theModel.snake.getPrevMoveDirection()!="down") theModel.snake.setMoveDirection("up");
				if(e.getKeyCode()==KeyEvent.VK_DOWN && theModel.snake.getPrevMoveDirection()!="up") theModel.snake.setMoveDirection("down");
				if(e.getKeyCode()==KeyEvent.VK_LEFT && theModel.snake.getPrevMoveDirection()!="right") theModel.snake.setMoveDirection("left");
				if(e.getKeyCode()==KeyEvent.VK_RIGHT && theModel.snake.getPrevMoveDirection()!="left") theModel.snake.setMoveDirection("right");
			}
			if(e.getKeyCode()==KeyEvent.VK_P && start!=0) {
				if(pause==true) {
					pause=false;
					timer=new Timer();
					timer.schedule(new RefreshTask(),0, theModel.getLevelTime());
				}
				else {
					pause=true;
					timer.cancel();
				}
			}
			if(gameState==1 && start==0) {
				timer=new Timer();
				timer.schedule(new RefreshTask(),0, theModel.getLevelTime());
				start=1;
				pause=false;
			}
		}
		
	}
	/**
	 * Klasa reprezentujaca nasluchiwacza przycisku Play w menu glownym.
	 */
	public class ListenForButton implements ActionListener{
		/**
		 * Wlacza menu gry oraz ustawia fokus na panel gry.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			setGameState(1);
			setStart(0);
			theView.game.setVisible(true);
			theView.game.gamePanel.setFocusable(true);
			theView.game.gamePanel.requestFocus();
			theView.game.panel_02.setFocusable(false);
			theView.menu.menuWindow.setVisible(false);
			theView.settingMenu.menuWindow.setVisible(false);
		}
		
	}
	/**
	 *Klasa nasluchiwacza przycisku powrotu do menu glownego w menu rozgrywki. 
	 */
	public class ListenForMenuButton implements ActionListener{
		/**
		 *Wylacza okno gry i wlacza okno menu glownego.
		 *Zatrzymuje takze stoper oraz restartuje segmenty weza oraz wynik.
		 *@see Model.Snake#refreshSnake()
		 *@see Controller#start
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			setGameState(0);
			theView.game.setVisible(false);
			theView.menu.menuWindow.setVisible(true);
			theView.settingMenu.menuWindow.setVisible(false);
			if (start==1) timer.cancel();
			theModel.setScore(0);
			theModel.snake.refreshSnake();
			theModel.snake.setMoveDirection("right");
			theView.changeScoreLabel();
			theView.game.revalidate();
			theView.game.repaint();
		}
		
	}
	/**
	 * Klasa reprezentujaca nasluchiwacza przycisku prowadzacego do menu ustawien.
	 */
	public class ListenForSettingsButton implements ActionListener{
		/**
		 * Wlacza menu ustawien oraz wylacza menu glowne
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			theView.game.setVisible(false);
			theView.menu.menuWindow.setVisible(false);
			theView.settingMenu.menuWindow.setVisible(true);
			
		}
		
	}
	/**
	 * Klasa nasluchiwacza powrotu do menu glownego z menu ustawien. 
	 */
	public class ListenForMenuInSettingsButton implements ActionListener{
		/**
		 * Funkcja, ktora wlacza okno menu glownego oraz wylacza okno menu ustawien.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			theView.game.setVisible(false);
			theView.settingMenu.menuWindow.setVisible(false);
			theView.menu.menuWindow.setVisible(true);
			
			
		}
		
	}
	/**
	 *Klasa nasluchiwacza przycisku odpowiadajacego za wybor poziomu gry
	 */
	public class ListenForLevelButton implements ActionListener{
		/**
		 * Funkcja ustawia odpowiedni czas odswiezania w zaleznosci od nacisnietego przycisku.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			if(e.getSource()==theView.settingMenu.beginnerLevelButton) theModel.setLevelTime(250);
			if(e.getSource()==theView.settingMenu.intLevelButton) theModel.setLevelTime(175);
			if(e.getSource()==theView.settingMenu.expertLevelButton) theModel.setLevelTime(100);
		}
		
	}
	/**
	 *Klasa reprezentujaca nasluchiwacza checkboxa w menu ustawien
	 */
	public class ListenForWallCheckbox implements ItemListener{
		/**
		 * Funkcja ustawia przenikalnosc scian przy zmianie stanu checkboxa. Zaznaczone okienko oznacza, ze waz moze przenikac przez sciany.
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getStateChange()==ItemEvent.SELECTED) {
				theModel.setGoThroughWalls(true);
				theView.settingMenu.wallCheckBox.setText("Snake is now able to go through walls");
			}
			else {
				theModel.setGoThroughWalls(false);
				theView.settingMenu.wallCheckBox.setText("Snake can't go through walls");
			}
		}
		
	}
	/**
	 * Klasa reprezentujaca zadanie wykonywane przez Timer z kazdym odswiezeniem
	 */
	class RefreshTask extends TimerTask{	//zeby plynniej chodzilo mozna zrobic nowego taska, ktory bedzie sie wyswietlal z czestotliwoscia np. 5 razy wieksza i 5 razy mniej bedzie dodawal do przemieszczenia
		/**
		 * Funkcja, ktora zajmuje sie rozgrywka
		 * <p>
		 * 				<ol>
		 * 				<li>zatrzymuje gre w momencie przegranej (po sygnale z klasy Model)
		 * 				<li>wywoluje metode moveSnake() z klasy Model;
		 * 				<li>aktualizuje obiekt klasy View</ol>
		 * 				Gdy waz najedzie na jablko:
		 * 						<ol>
		 * 						<li>wywoluje z klasy Model metody: setAppleRandom(), incrementScore(), addSnakePoint()
		 * 						<li>wywoluje z klasy View metode changeScoreLabel()</ol>
		 * 				
		 *@see Model#gameOver()
		 *@see Model#moveSnake()
		 *@see Model#setAppleRandom()
		 *@see Model#incrementScore()
		 *@see Model#addSnakePoint()
		 *@see View#changeScoreLabel()
		 *
		 */
		@Override
		public void run() {
			if(theModel.gameOver()) {
				System.out.println("You Lost");
				theView.loseAllertBox.showBox();
				timer.cancel();
				return;
			}
			if(theModel.getApple().getX()==theModel.snake.getSnakePoints().get(0).getX() &&
					theModel.getApple().getY()==theModel.snake.getSnakePoints().get(0).getY()) {
				theModel.setAppleRandom();
				theModel.incrementScore();
				theModel.addSnakePoint();
				theView.changeScoreLabel();
			}
			theModel.moveSnake();
			theView.game.gamePanel.revalidate();
			theView.game.gamePanel.repaint();
			
		}
		
	}
}
