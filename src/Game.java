import java.util.*;



import java.awt.Point;
import java.io.IOException;
public class Game {
	public static void main(String[] args) throws InterruptedException, IOException{
		Model theModel=new Model();
		View theView=new View(theModel);
        Controller theController = new Controller(theView, theModel);    
	}
}
