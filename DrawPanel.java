import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.lang.*;
import javax.swing.*;
/*
import java.awt.Graphics2D;
import java.awt.Color;
*/

public class DrawPanel extends JPanel {
  BufferedImage bufferImage = null;
	Graphics2D bufferGraphics = null;
  Color currentColor = Color.black;
  Float currentWidth = 5.0f;
  int HSB = 0;
  int copyCount = 0, backCount = 0;
  Boolean Back = false;

  public void createBuffer(int width, int height) {
    //バッファ用のImageとGraphicsを用意する
		bufferImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_BGR);
		bufferGraphics = bufferImage.createGraphics(); //getGraphicsと似ているが、戻り値がGraphics2D。
		bufferGraphics.setBackground(Color.white);
		bufferGraphics.clearRect(0, 0, width, height); //バッファクリア
	}

  public void setPenColor(Color newColor){ //ペンの色を変える
    currentColor = newColor;
  }
  public void setPenWidth(float newWidth){ //ペンの太さを変える
    currentWidth = newWidth;
  }

  public Color getCurrentColor(){ //現在のペンの色を返す
    return currentColor;
  }
  public Float getCurrentWidth(){ //現在のペンの太さを返す
    return currentWidth;
  }

  public void drawLine(int x1, int y1, int x2, int y2){ //線を描く
    /*
    Graphics2D g = (Graphics2D)this.getGraphics();
    g.setColor(currentColor);
    g.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 45;
    y1 -= distance;
    y2 -= distance;

    g.drawLine(x1, y1, x2, y2);
    */
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    bufferGraphics.drawLine(x1, y1, x2, y2); //バッファに描画する
		repaint(); //再描画するためpaintComponentを呼び出す。
  }
  public void paintComponent(Graphics g) { //バッファを表示する
		super.paintComponent(g); //他に描画するものがあるかもしれないので親を呼んでおく
		if(null != bufferImage) g.drawImage(bufferImage, 0, 0, this); //バッファを表示する
	}

  public void drawDot(int x1, int y1, int x2, int y2){ //点線を描く
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    float dot[] = {1.0f, 100.0f*currentWidth};
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dot, 0.0f)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    bufferGraphics.drawLine(x1, y1, x2, y2); //バッファに描画する
		repaint(); //再描画するためpaintComponentを呼び出す。
  }
  public void drawBrush(int x1, int y1, int x2, int y2){ //線を描く
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    if(currentWidth > 0){
      bufferGraphics.drawLine(x1, y1, x2, y2); //バッファに描画する
      currentWidth -= 2.0f;
    }
		repaint(); //再描画するためpaintComponentを呼び出す。
  }
  public void drawRainbow(int x1, int y1, int x2, int y2){ //線を描く
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor = Color.getHSBColor(HSB/256.0f, 1.0f, 1.0f));
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    bufferGraphics.drawLine(x1, y1, x2, y2); //バッファに描画する
		repaint(); //再描画するためpaintComponentを呼び出す。

    if(HSB < 255){
      HSB++;
    }
    else if(HSB == 255){
      HSB = 0;
    }
  }

  public void drawStampString(int x, int y, String str, int num){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    if(num == 0){
      bufferGraphics.setFont(new Font("SansSerif", Font.PLAIN, Math.round(3.0f * currentWidth)));
    }
    else if(num == 1){
      bufferGraphics.setFont(new Font("SansSerif", Font.BOLD, Math.round(3.0f * currentWidth)));
    }
    else if(num == 2){
      bufferGraphics.setFont(new Font("SansSerif", Font.ITALIC, Math.round(3.0f * currentWidth)));
    }
    //bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y -= distance;
    FontMetrics fm = ((Graphics)bufferGraphics).getFontMetrics();
		Rectangle rectText = fm.getStringBounds(str, ((Graphics)bufferGraphics)).getBounds();
		x = x - rectText.width/2;
		y = y - rectText.height/2 + fm.getMaxAscent();

    ((Graphics)bufferGraphics).drawString(str, x, y); //バッファに描画する
		repaint(); //再描画するためpaintComponentを呼び出す。
  }
  public void drawStringKorokoro(int x, int y, char ch, int num){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    String str = String.valueOf(ch);
    bufferGraphics.setColor(currentColor);
    if(num == 0){
      bufferGraphics.setFont(new Font("SansSerif", Font.PLAIN, Math.round(3.0f * currentWidth)));
    }
    else if(num == 1){
      bufferGraphics.setFont(new Font("SansSerif", Font.BOLD, Math.round(3.0f * currentWidth)));
    }
    else if(num == 2){
      bufferGraphics.setFont(new Font("SansSerif", Font.ITALIC, Math.round(3.0f * currentWidth)));
    }
    //bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y -= distance;
    FontMetrics fm = ((Graphics)bufferGraphics).getFontMetrics();
		Rectangle rectText = fm.getStringBounds(str, ((Graphics)bufferGraphics)).getBounds();
		x = x - rectText.width/2;
		y = y - rectText.height/2 + fm.getMaxAscent();

    ((Graphics)bufferGraphics).drawString(str, x, y); //バッファに描画する
		repaint(); //再描画するためpaintComponentを呼び出す。
  }

  public void drawRectangle(int x1, int y1, int x2, int y2){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    ((Graphics)bufferGraphics).drawRect(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1)); //バッファに描画する
    repaint(); //再描画するためpaintComponentを呼び出す。
  }
  /*
  public void drawRubberRectangle(int x1, int y1, int x2, int y2){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    ((Graphics)bufferGraphics).drawRect(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1)); //バッファに描画する
    repaint(); //再描画するためpaintComponentを呼び出す。
    ((Graphics)bufferGraphics).clearRect(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1));
    repaint();
  }
  */
  public void drawFillRectangle(int x1, int y1, int x2, int y2){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    ((Graphics)bufferGraphics).fillRect(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1)); //バッファに描画する
    repaint(); //再描画するためpaintComponentを呼び出す。
  }
  public void drawOval(int x1, int y1, int x2, int y2){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    ((Graphics)bufferGraphics).drawOval(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1)); //バッファに描画する
    repaint(); //再描画するためpaintComponentを呼び出す。
  }
  public void drawFillOval(int x1, int y1, int x2, int y2){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(currentColor);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    ((Graphics)bufferGraphics).fillOval(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1)); //バッファに描画する
    repaint(); //再描画するためpaintComponentを呼び出す。
  }

  public void stamp(String path, int x, int y){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y -= distance;

    BufferedImage stamp;
		try {
			stamp = ImageIO.read(new File(path));
		}
    catch(Exception e){
			System.out.println("Error: reading file = " + path);
			return;
		}

		bufferGraphics.drawImage(stamp, x, y, this);
		repaint(); //画像を表示するためにpaintComponentを呼ぶ
  }

  public void pixelColor(int x, int y){
    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y -= distance;

    currentColor = new Color(bufferImage.getRGB(x, y));
    bufferGraphics.setColor(currentColor);
    System.out.println("R:" + currentColor.getRed());
    System.out.println("G:" + currentColor.getGreen());
    System.out.println("B:" + currentColor.getBlue());
  }
/*
  public void paste(int x1, int y1, int x2, int y2, int x, int y){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;
    y -= distance;

    bufferGraphics.copyArea(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1), x-x1, y-y1);
    repaint(); //再描画するためpaintComponentを呼び出す。
  }*/
  public void copy(int x1, int y1, int x2, int y2)throws AWTException, IOException{
    if(copyCount != 0){
      File copyFile = new File("copy" + copyCount + ".png");
      copyFile.delete();
    }
    copyCount++;

    Robot robot = new Robot();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    BufferedImage screenshot = robot.createScreenCapture(
    new Rectangle(x1, y1+25, Math.abs(x2-x1), Math.abs(y2-y1)));
    ImageIO.write(screenshot, "PNG", new File("copy" + copyCount + ".png"));
  }
  public void cut(int x1, int y1, int x2, int y2){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    bufferGraphics.setColor(Color.white);
    bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER)); //太さがcurrentWidthの線を描く 線の両端は丸くする

    //マウスポインターと描かれる線の位置のずれを修正する
    int distance = 75;
    y1 -= distance;
    y2 -= distance;

    ((Graphics)bufferGraphics).fillRect(x1, y1, Math.abs(x2-x1), Math.abs(y2-y1)); //バッファに描画する
    repaint(); //再描画するためpaintComponentを呼び出す。
  }
  public void paste(int x, int y){
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }

    this.stamp("copy" + copyCount + ".png", x, y);
  }
  public int getCopyCount(){
    return copyCount;
  }

  public void prepareBack()throws AWTException, IOException{ //Backするために保存しておくスクショをとる
    /*
    if(backCount != 0 && backCount != 1){
      File backFile = new File("back" + (backCount-1) + ".png");
      backFile.delete();
      System.out.println(backFile);
    }
    backCount++;
    */
    File backFile = new File("back" + (backCount-1) + ".png");
    if(backFile.exists()){
      if(Back){
        backFile.renameTo(new File("back" + (backCount+1) + ".png"));
        System.out.println("back" + (backCount-1) + ".pngをback" + (backCount+1) + ".pngに変更しました");
      }
      else{
        backFile.delete();
        System.out.println(backFile + "を削除しました");
      }
    }
    backCount++;

    if(!Back){
      this.saveFile(new File("back" + backCount + ".png"));
      System.out.println("back" + backCount + ".png" + "のスクショをとりました");
    }
  }
  public void back()throws AWTException, IOException{
    Back = true;
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }
    if(backCount-1 >= 0){
      this.stamp("back" + (backCount-1) + ".png", 0, 75);
      System.out.println("back" + (backCount-1) + ".png" + "を貼り付けました");
      this.prepareBack();
    }
    Back = false;
  }
  public int getBackCount(){
    return backCount;
  }

  public void AllClear(){ //全消去
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }

    //Graphics g = this.getGraphics();
    Dimension size = this.getSize();
    bufferGraphics.setColor(Color.white);
    ((Graphics)bufferGraphics).fillRect(0, 0, (int)size.getWidth() - 1, (int)size.getHeight() - 1);
    repaint(); //画像を表示するためにpaintComponentを呼ぶ
  }
  public void fill(){ //塗りつぶし
    if(null == bufferGraphics) { //バッファが作られていなかったら
		 	this.createBuffer(this.getWidth(),this.getHeight());  //バッファをまだ作ってなければ作る
    }

    //Graphics g = this.getGraphics();
    Dimension size = this.getSize();
    bufferGraphics.setColor(currentColor);
    ((Graphics)bufferGraphics).fillRect(0, 0, (int)size.getWidth() - 1, (int)size.getHeight() - 1);
    repaint(); //画像を表示するためにpaintComponentを呼ぶ
  }

  public void openFile(File file2open){
    BufferedImage pictureImage;
		try {
			pictureImage = ImageIO.read(file2open);
		}
    catch(Exception e){
			System.out.println("Error: reading file="+file2open.getName());
			return;
		}
		//画像に合わせたサイズでbufferImageとbufferGraphicsを作りなおして画像を読み込む
		//ImageIO.readの戻り値をbufferImageに代入するのでは駄目みたいです。
		this.createBuffer(pictureImage.getWidth(),pictureImage.getHeight());
		bufferGraphics.drawImage(pictureImage,0,0,this);
		repaint(); //画像を表示するためにpaintComponentを呼ぶ
  }
  public void saveFile(File file2save) {
    try {
      ImageIO.write(bufferImage, "png", file2save);
    }
    catch (Exception e) {
      System.out.println("Error: writing file="+file2save.getName());
      return;
    }
  }

}
