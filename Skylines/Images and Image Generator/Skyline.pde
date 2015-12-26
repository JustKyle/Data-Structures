int[] skyline;

void setup(){
  //initialize screen
  size(1920,1080);
  //read in data
  String[] data = loadStrings("data3.txt");
  skyline = int(split(data[0],','));
}

void draw(){
  background(255);
  stroke(0);
  fill(0);
  //place rectangles
  int currentLeft = 0;
  int currentHeight = height;
  int furthest = skyline[skyline.length-2];
  for(int i = 2; i < skyline.length; i+=2){
     currentLeft = skyline[i-2]*width/furthest;
     currentHeight = (height - (skyline[i - 1])*height/30);
     rect(currentLeft, currentHeight, (skyline[i] - skyline[i-2])*width/furthest, (skyline[i-1])*height/30);
  }
  save("dataset1.jpg");
}