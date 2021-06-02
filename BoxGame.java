import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;

public class BoxGame {

  private List<Partical> particals = new ArrayList<Partical>();
  private Box box;
  private  Displayer displayer;
 
  public static void main(String[] args){
  	BoxGame game = new BoxGame();
  	
  	game.setValues();
  }
  private void setValues(){
  	for(int i = 0; i < 120;i++){
  	 float[] speed = {5 * i / 10,-20 * i / 10}; 	
  	float[] position = {i * .8f, i * .8f};
  	Random rand = new Random();
    Partical partical = new Partical(speed,(int)(60f * rand.nextFloat()),position);
  	particals.add(partical);
  }
    box =  new Box(600);
  	displayer =   new Displayer();
  	update(1f/30f);
  }
  private void update(float dt){
  	//updating position
   int[][] positions = new int[200][2];
   int[] circlesRaduis = new int[200];
   for(int i = 0;i < particals.size();i++){
   Partical partical = particals.get(i);
   float[] newParticalPosition = {partical.position[0] + partical.speed[0] * dt,partical.position[1] + partical.speed[1] * dt};
   partical.position = newParticalPosition;
   
   //updating speed & checking for collisions
   float[] point1 = {partical.position[0] + partical.points[0][0],partical.position[1] + partical.points[0][1]};
   float[] point2 = {partical.position[1] + partical.points[1][0],partical.position[1] + partical.points[1][1]};
   float[] point3 = {partical.position[0] + partical.points[2][0],partical.position[1] + partical.points[2][1]};
   float[] point4 = {partical.position[0] + partical.points[3][0],partical.position[1] + partical.points[3][1]};
   if(box.topBorderCheckCollison(point2) || box.buttonBorderCheckCollison(point4))
   	 partical.speed[1] *= -.95;

   	if(box.leftBorderCheckCollison(point3) || box.rightBorderCheckCollison(point1))
   	 partical.speed[0] *= -.95;
    
    int[] particalPos = {(int)partical.position[0],(int)partical.position[1]};
    positions[i] = particalPos;
    circlesRaduis[i] = (int)partical.radius;
 }
 displayer.displayData(circlesRaduis,positions,(int)box.size);
    try{
    Thread.currentThread().sleep((long)(dt * 1000));
    }catch(Exception e){System.out.println(e);}
    update(dt);
  }

 public class Displayer extends JFrame{
    private int[] circlesRaduis;
    private int[][] circlePositions;
    private int boxSize;
    
    public void displayData(int[] circlesRaduis,int[][] circlePositions,int boxSize){
    	this.circlesRaduis = circlesRaduis;
        this.circlePositions = circlePositions;
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
        for(int i = 0; i < circlesRaduis.length;i++){
       g2d.setColor(Color.red);
       g2d.drawOval(circlePositions[i][0] + boxSize / 2 + 50,circlePositions[i][1] + boxSize / 2 + 50,circlesRaduis[i],circlesRaduis[i]);
    }
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
  public class Node {
  	public float[] position;
  	public float size;
    public List<Partical> particals = new ArrayList<Partical>();

  	public Node(float[] position,float size){
  		this.position = position;
  		this.size = size;
  	}
  	public void isAPointInsideMe(Partical partical){
  		float[] point = partical.position;
  		//checking if a point is inside the node
  		if(Math.abs(point[0] - position[0]) <= size / 2 && Math.abs(point[1] - position[1]) <= size / 2)
  			particals.add(partical);
  	}
  	//checking for collision between particals
  	public void checkForCollisions(){
  		int j = 0;
  		while(j < particals.size() * particals.size() / 2){
  			for(Partical partical : particals){
  				for(Partical partical2 : particals){
  					if(Math.sqrt(Math.pow(partical.position[0] - partical2.position[1],2) + Math.pow(partical2.position[1] - partical.position[1],2)) < partical2.radius + partical.radius){
  						partical2.speed[0] *= -1;
  						partical2.speed[1] *= -1;
  						partical.speed[0] *= -1;
  						partical.speed[1] *= -1;
  					}
  					j++;
  				}
  			}
  		}
  	}
  }

  public class Grid{
   public List<Node> nodes = new ArrayList<Node>();
   public Grid(float size,float nodeNum){
   	//setting up the grid
   	for(int i = 0;i < (int)nodeNum;i++){
   		for(int j = 0;i < (int)nodeNum;j++){
   		float[] nodePos = {i * size / nodeNum,j * size / nodeNum};
   		Node node =  new Node(nodePos,size / nodeNum);
   		nodes.add(node);
   	}
   }
  }

 }
}