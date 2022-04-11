package project;
import java.util.Queue;
import java.util.Scanner;
import java.util.Vector;
import java.util.LinkedList;

class Cordinate{
    int r, c;
    public  Cordinate(){

    }
    public Cordinate(int r, int c){
        this.r = r;
        this.c = c;
    }
}
class Node{
    int r, c;
    Vector<Cordinate> shortestPath=new Vector<Cordinate>();
    public Node(int r, int c){
        this.r = r;
        this.c = c;
        shortestPath.add(new Cordinate(r,c));
    }
    public Node(Vector<Cordinate> path,int r, int c){
        this.r = r;
        this.c = c;
        shortestPath.addAll(path);
        shortestPath.add(new Cordinate(r,c));
    }
}
public class projectmaze {

    static Scanner in=new Scanner(System.in);

    static int mazeSize=12,mr=mazeSize+1,mc=mazeSize*2+1;
    static char randomMaze[][]=new char[mr][mc];

    static int cells[][]=new int [mazeSize][mazeSize];


    static int path_r[]={0,-1,0,1};
    static int path_c[]={-1,0,1,0};

    static void initializeMaze(){
        for(int i=0;i<mr;i++){
            for(int j=0;j<mc;j++){
                randomMaze[i][j]=(j%2==0)?((i!=0)?'|':' '):'_';
            }
        }

//        for(int i=0;i<mazeSize;i++){
//            for(int j=0;j<mazeSize;j++){
//                cells[i][j]=i*mazeSize+j;
//            }
//        }

    }
    static void printMaze(){
        for(int i=0;i<mr;i++){
            for(int j=0;j<mc;j++){
                System.out.print(randomMaze[i][j]);
            }
            System.out.println();
        }

    }
    static void printCells(){
        for(int i=0;i<mazeSize;i++){
            for(int j=0;j<mazeSize;j++){
                System.out.printf("%-3d ",cells[i][j]);
            }
            System.out.println();
        }

    }

    static Cordinate getBetweenWallCordinate(Cordinate cell_a, Cordinate cell_b){
        if(cell_a.r ==cell_b.r){
            if(cell_a.c <cell_b.c){
                return new Cordinate(cell_a.r +1,(cell_a.c +1)*2);
            }
            else{
                return new Cordinate(cell_b.r +1,(cell_b.c +1)*2);
            }
        }
        else{
            if(cell_a.r <cell_b.r){
                return new Cordinate(cell_a.r +1,(cell_a.c +1)*2-1);
            }
            else{
                return new Cordinate(cell_b.r +1,(cell_b.c +1)*2-1);
            }
        }

    }
    static boolean canGo(Cordinate src_cell, Cordinate dest_cell){
        Cordinate wall=getBetweenWallCordinate(src_cell,dest_cell);
        return randomMaze[wall.r][wall.c]==' ';
    }
    static void visitAllConnectedCells(int r, int c, boolean isVisited[][],int setNumber){
        if(isVisited[r][c]) return;

        isVisited[r][c]=true;
        cells[r][c]=setNumber;
        for(int i=0;i<path_r.length;i++){
           int new_r=r+path_r[i];
           int new_c=c+path_c[i];
           if(new_r>=0 && new_r<mazeSize && new_c>=0 && new_c<mazeSize){
              if(canGo(new Cordinate(r,c),new Cordinate(new_r,new_c))){
                  visitAllConnectedCells(new_r,new_c,isVisited,setNumber);
              }
           }
        }
    }
    static int getNumberOfSet(){
       int numberOfSet=0;
       boolean isVisited[][]=new boolean[mazeSize][mazeSize];

       for(int i=0;i<mazeSize;i++)
           for(int j=0;j<mazeSize;j++)
               isVisited[i][j]=false;

       for(int i=0;i<mazeSize;i++){
           for(int j=0;j<mazeSize;j++){
               if(!isVisited[i][j]){
                   visitAllConnectedCells(i,j,isVisited,numberOfSet);
                   numberOfSet++;
               }
           }
       }
       return numberOfSet;
    }

    static Vector<Cordinate> getShortestPathBetweenTwoCells(Cordinate src,Cordinate dest){
        boolean isVisited[][]=new boolean[mazeSize][mazeSize];

        for(int i=0;i<mazeSize;i++)
            for(int j=0;j<mazeSize;j++)
                isVisited[i][j]=false;
        Vector<Cordinate> shortestPath=new Vector<Cordinate>();

        Queue<Node> queue= new LinkedList();

        queue.add(new Node(src.r,src.c));
        isVisited[src.r][src.c]=true;

        while (!queue.isEmpty()){
            Node node=queue.remove();
            if(node.r==dest.r && node.c==dest.c) {
                shortestPath=node.shortestPath;
                break;
            }
            for(int i=0;i<path_r.length;i++){
                int new_r=node.r+path_r[i];
                int new_c=node.c+path_c[i];
                if(new_r>=0 && new_r<mazeSize && new_c>=0 && new_c<mazeSize){
                    if(!isVisited[new_r][new_c] && canGo(new Cordinate(node.r,node.c),new Cordinate(new_r,new_c))){
                        isVisited[new_r][new_c]=true;
                        queue.add(new Node(node.shortestPath,new_r,new_c));
                    }
                }
            }
        }

        return shortestPath;
    }

    static Cordinate nextRandomWall(){
        int r = (int) (Math.random()*(mr-1)+1);
        int c = (int) (Math.random()*(mc-2)+1);
        return new Cordinate(r,c);
    }
    static void printStatus(String msg){
        System.out.println("<------------------"+msg+"--------------->");
    }
    static int takeInput(int min, int max){
        int tmp=in.nextInt();
        while(tmp<0 || tmp>=max){
            System.out.print("Invalid input, try again: ");
            tmp=in.nextInt();
        }
        return  tmp;
    }
    public static void main(String args[]) throws Exception {
        initializeMaze();

        printStatus("Initial Maze");
        printMaze();

        int round=1;

        int numberOfSet=getNumberOfSet();

        printStatus("Initial Sets");
        printCells();

        while(numberOfSet!=1){
            printStatus("Round "+(round++));
            System.out.println("Current Total Number of Sets: "+numberOfSet);
            Cordinate wall=nextRandomWall();
            while(wall.r ==mr-1 && wall.c%2 != 0 || randomMaze[wall.r][wall.c]==' '){
                wall=nextRandomWall();
            }
            if(wall.r == 0 || wall.c == 0 || wall.c == mc-1 || randomMaze[wall.r][wall.c]==' '){
                throw new Exception("DD");
            }

            System.out.printf("Current Wall: (%d,%d) = %c\n",wall.r,wall.c,randomMaze[wall.r][wall.c]);
            int r1,r2,c1,c2;
            if(randomMaze[wall.r][wall.c]=='|'){
                r1 = wall.r-1; c1 = wall.c/2-1;
                r2= r1; c2 = c1+1;
            }
//            else if(randomMaze[wall.r][wall.c]=='_'){
            else{
                r1=wall.r-1; c1=wall.c/2;
                r2=r1+1; c2=c1;
            }
            if(cells[r1][c1]!=cells[r2][c2]){
                randomMaze[wall.r][wall.c]=' ';
                numberOfSet = getNumberOfSet();
            }
            printStatus("Current Maze");
            printMaze();
            printStatus("Current Sets");
            printCells();
            System.out.println();
        }
        printStatus("Final Maze");
        printMaze();
        printStatus("Final Sets");
        printCells();

        System.out.println("Enter source and destination row(0-11) and column(0-11) number to print shortest path.");
        while(true){
            Cordinate src=new Cordinate();
            Cordinate dest= new Cordinate();
            System.out.print("Enter Source's 0 based row column number( Ex. 0 0): ");
            src.r=takeInput(0,12);
            src.c=takeInput(0,12);

            System.out.println("Entered source row,column: "+src.r+","+src.c);

            System.out.print("Enter Destination's 0 based row column number( Ex. 11 11): ");
            dest.r=takeInput(0,12);
            dest.c=takeInput(0,12);

            System.out.println("Entered source row,column: "+dest.r+","+dest.c);

            Vector<Cordinate> shortestPath=getShortestPathBetweenTwoCells(src,dest);
            System.out.println("Shortest Path is: ");
            for(Cordinate node: shortestPath){
                System.out.printf("(%d,%d)",node.r,node.c);
            }
            System.out.println();
        }
    }
}
