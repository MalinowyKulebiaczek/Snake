import java.awt.Point;
import java.util.*;
/**
 * Klasa zajmujaca sie wykonywaniem wszystkich niezbednych do działania programu obliczen
 * @author rafaw
 *
 */
public class Model {
	Snake snake;
	/**Zmienna przechowujaca zdobyte przez uzytkownika punkty*/
	private int score=0;
	/**Punkt reprezentujacy jablko na mapie*/
	private Point apple;
	/**Zmienna ktora reprezentuje liczbe milisekund, po ktorej odswiezany jest obraz i pozycja weza.
	 * Czlon "level" wystepuje tu dlatego, ze im mniejsza jest ta zmienna, tym wiekszy poziom trudnosci*/
	private int levelTime=250;
	/**Flaga wskazujaca czy waz moze przenikac przez sciany*/
	private boolean gothroughWalls=false;
	/**Zmienna uzyta do skalowania mapy oraz komponentow wzdluz osi poziomej*/
	static final int SIZE_X=500;
	/**Wysokosc mapy*/
	static final int SIZE_Y=500;
	/**Klasa reprezentujaca weza*/
	public class Snake{
		/**Lista, w ktorej przechowywane sa wszystkie segmenty weza*/
		private LinkedList<Point> snakePoints;
		/**Kierunek, w ktorym obecnie porusza sie waz*/
		private String moveDirection="right";
		/**Kierunek, w ktorym ruch wczesniej poruszal sie waz.
		 * Wykorzystany do tego, aby waz nie mogl "zawrocic" w siebie
		 */
		private String prevMoveDirection="right";
		/**Konstruktor weza - tworzy 3 pierwsze segmenty*/
		public Snake(){
			LinkedList<Point> snakePoints = new LinkedList<Point>();
			snakePoints.add(new Point(SIZE_X/2,SIZE_Y/2));
			snakePoints.add(new Point(SIZE_X/2-SIZE_X/20,SIZE_Y/2));
			snakePoints.add(new Point(SIZE_X/2-2*(SIZE_X/20),SIZE_Y/2));
			this.snakePoints=snakePoints;
		}
		void setMoveDirection(String dir) {
			this.moveDirection=dir;
		}
		LinkedList<Point> getSnakePoints() {
			return this.snakePoints;
		}
		String getMoveDirection() {
			return this.moveDirection;
		}
		void setPrevMoveDirection(String dir) {
			this.prevMoveDirection=dir;
		}
		String getPrevMoveDirection() {
			return this.prevMoveDirection;
		}
		/**Funkcja przywracajaca weza do stanu poczatkowego, wykorzystywana do tego, zeby zresetowac weza za kazdym razem, gdy wchodzimy w nowa gre*/
		void refreshSnake() {
			
			snakePoints.clear();
			snakePoints.add(new Point(SIZE_X/2,SIZE_Y/2));
			snakePoints.add(new Point(SIZE_X/2-SIZE_X/20,SIZE_Y/2));
			snakePoints.add(new Point(SIZE_X/2-2*(SIZE_X/20),SIZE_Y/2));
			
		}
	}
	/**Konstruktor modelu.
	 * Tworzy weza oraz jablko i ustawia poczatkowe koordynaty jablka.
	 */
	public Model() {
		this.snake= new Snake();
		this.apple=new Point();
		setAppleRandom();
	}
	/**
	 * Ustawia wspolrzedne jablka w losowym miejscu na mapie, sprawdzajac przy tym czy jablko nie zostalo ustawione na polu zajetym przez weza.
	 * Jesli tak jest, tworzy nowe losowe wspolrzedne dla jablka dopoki nie trafi na wolne miejsce.
	 */
	void setAppleRandom() {
		
		int x,y, max=15, min=0,i;
		boolean appleIsOnSnake=false;
		do {
			Random randX = new Random();
			Random randY = new Random();
			appleIsOnSnake=false;
			x=randX.nextInt((max - min))+min;
			y=randY.nextInt((max-min))+min;
			
			for(i=0;i<snake.snakePoints.size();i++) {
				if(x*25==snake.snakePoints.get(i).getX() && y*25==snake.snakePoints.get(i).getY()) {
					appleIsOnSnake=true;
				}
			}
		}while(appleIsOnSnake);
		this.apple.setLocation((int)x*25,(int)y*25);
	}
	Point getApple() {
		return this.apple;
	}
	void addSnakePoint() {
		Point p=new Point();
		this.snake.snakePoints.add(p);
	}
	/**
	 * Funkcja zwracajaca true, jesli waz wejdzie w siebie lub gdy wejdzie w sciane w odpowiednim trybie.
	 * @see Model#gothroughWalls
	 */
	boolean gameOver() {
		int i;
		if(!gothroughWalls) {
			if(snake.snakePoints.get(0).getX()<0 || snake.snakePoints.get(0).getX()>=400) return true;
			if(snake.snakePoints.get(0).getY()<0 || snake.snakePoints.get(0).getY()>=450) return true;
		}
		for(i=1;i<snake.snakePoints.size();i++) {
			if(snake.snakePoints.get(0).getX()==snake.snakePoints.get(i).getX() && snake.snakePoints.get(0).getY()==snake.snakePoints.get(i).getY()) return true;	
		}
		return false;
	}
	/**
	 * Funkcja przemieszczajaca wszystkie segmenty weza w zaleznosci od kierunku.
	 * Kazdemu segmentowi poza pierwszym przypisywane są wspolrzedne poprzednika.
	 */
	void moveSnake(){
		int i;
		Point p=new Point();
		Point p0=new Point();
		Point temp_p=new Point();
		int hopDistance=SIZE_X/20;
		int newX=(int)snake.snakePoints.get(0).getX();
		int newY=(int)snake.snakePoints.get(0).getY();
		temp_p.setLocation(newX, newY);
		p.setLocation(newX, newY);
		
		switch(snake.getMoveDirection()) {
			case "right": 	if(newX>=375 && gothroughWalls) newX=0;
							else newX=newX+hopDistance;
							break;
			case "left":	if(newX<=0 && gothroughWalls) newX=375;
							else newX=newX-hopDistance;
							break;
			case "up":		if(newY<=0 && gothroughWalls) newY=425;
							else newY=newY-hopDistance;
							break;
			case "down":	if(newY>=425 && gothroughWalls) newY=0;
							else newY=newY+hopDistance;
							break;
		}	
			
		for(i=1;i<this.snake.snakePoints.size();i++) {
			temp_p=this.snake.snakePoints.get(i);
			this.snake.snakePoints.set(i, p);
			p=temp_p;
		}
		p0.setLocation(newX,newY);
		this.snake.snakePoints.set(0, p0);
		this.snake.setPrevMoveDirection(this.snake.getMoveDirection());
	}
	void setScore(int score) {
		this.score=score;
	}
	int getScore() {
		return this.score;
	}
	void incrementScore() {
		this.score++;
	}
	void setLevelTime(int l) {
		this.levelTime=l;
	}
	int getLevelTime() {
		return levelTime;
	}
	void setGoThroughWalls(boolean b) {
		this.gothroughWalls=b;
	}
	boolean getGoThroughWalls() {
		return this.gothroughWalls;
	}
}
