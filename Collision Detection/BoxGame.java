import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;

public class BoxGame {

  private Partical partical;
  private Box box;
  private  Displayer displayer;
 
  public static void main(String[] args){
  	BoxGame game = new BoxGame();
  	
  	game.setValues();
  }
  private void setValues(){
  	 float[] speed = {40 * 3,-75 * 3};
  	float[] position = {50,30};
  	partical = new Partical(speed,10,position);
  	box =  new Box(600);
  	displayer =   new Displayer();
  	update(1f/30f);
  }
  private void update(float dt){
  	//updating position
   float[] newParticalPosition = {partical.position[0] + partical.speed[0] * dt,partical.position[1] + partical.speed[1] * dt};
   partical.position = newParticalPosition;
   
   //updating speed & checking for collisions
   float[] point1 = {partical.position[0] + partical.points[0][0],partical.position[1] + partical.points[0][1]};
   float[] point2 = {partical.position[1] + partical.points[1][0],partical.position[1] + partical.points[1][1]};
   float[] point3 = {partical.position[0] + partical.points[2][0],partical.position[1] + partical.points[2][1]};
   float[] point4 = {partical.position[0] + partical.points[3][0],partical.position[1] + partical.points[3][1]};
   if(box.topBorderCheckCollison(point2) || box.buttonBorderCheckCollison(point4))
   	 partical.speed[1] *= -1;

   	if(box.leftBorderCheckCollison(point3) || box.rightBorderCheckCollison(point1))
   	 partical.speed[0] *= -1;
    
    int[] particalPos = {(int)partical.position[0],(int)partical.position[1]};
    displayer.displayData((int)partical.radius,particalPos,(int)box.size);
    try{
    Thread.currentThread().sleep((long)(dt * 1000));
    }catch(Exception e){System.out.println(e);}
    update(dt);
  }

 public class Displayer extends JFrame{
    private int circleRaduis;
    private int[] circlePosition;
    private int boxSize;
    public void displayData(int circleRaduis,int[] circlePosition,int boxSize){
    	this.circleRaduis = circleRaduis;
        this.circlePosition = circlePosition;
        this.boxSize = boxSize;
        setTitle("Box Game");
        setSize(boxSize + 100, boxSize + 100);
        setVisible(true);
        repaint();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    public void paint(Graphics g) {
    	super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRect(50,50, boxSize, boxSize);
        g2d.drawOval(circlePosition[0] + boxSize / 2 + 50,circlePosition[1] + boxSize / 2 + 50,circleRaduis,circleRaduis);
    }
 }
 public class Box{

  public float size;

  public Box(float size){
  	this.size = size;
  }

  //these methods are used to check for collisions
  public boolean topBorderCheckCollison(float[] point){

   boolean isCollided = false;

   if(point[1] > size / 2)
   isCollided = true;
   
   return isCollided;
  }

  public boolean buttonBorderCheckCollison(float[] point){

   boolean isCollided = false;

   if(point[1] < -size / 2)
   isCollided = true;
   
   return isCollided;
  }

  public boolean leftBorderCheckCollison(float[] point){

   boolean isCollided = false;

   if(point[0] < -size / 2)
   isCollided = true;
   
   return isCollided;
  }

  public boolean rightBorderCheckCollison(float[] point){

   boolean isCollided = false;

   if(point[0] > size / 2)
   isCollided = true;
   
   return isCollided;
  }
 }


 public class Partical{
 	public float[] speed;
 	public float radius;
    public float[] position;
    //these are some local positions from the edge of the partical we use it for collision detection
    public float[][] points;

    public Partical(float[] speed,float radius,float[] position){
    	//setting the default values of the speed, radius & position
    	this.speed = speed;
    	this.radius = radius;
    	this.position =  position;
        //setting the points positions
        float[][] points = {{radius,0},{0,radius},{-radius,0},{0,-radius}};
        this.points = points;
    }
  }
}