/*
import javax.swing.JFrame;
import java.awt.event.MouseMotionListener;
import java.awt.event.*;
*/
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Random;
import java.io.*;

/*
マウスイベントを受け取るには...
① implements MouseMotionListener をする
→ implements したので mouseDragged関数とmouseMoved関数をオーバーライドする
② addMouseMotionListener(MouseTestのインスタンス)をする

① implements MouseListener をする
→ implements したので mouseClicked関数,mouseEntered関数,mouseExited関数,mousePressed関数,mouseReleased関数をオーバーライドする
② addMouseListener(MouseTestのインスタンス)をする
*/

public class SimpleDraw extends JFrame implements ActionListener, MouseMotionListener, MouseListener, ChangeListener{
  DrawPanel panel;
  JFileChooser fileChooser;
  int oldx, oldy, newx, newy;
  JPanel panelToolbar;
  JPanel panelCurrentColor;
  JPanel panelBtn;
  JPanel panelSlider;
  JSlider slider;
  String currentPenType = "Normal";
  Boolean writing = false;
  String stampString = null, ch = null;
  int choiceNum = 0;
  String path;
  int korokoroCount = 0, i = 0;
  int copyx1, copyy1, copyx2, copyy2, pastex, pastey;
  Boolean not_twice = true;
  Boolean first = true;

  SimpleDraw(){ //コンストラクタ
    this.setTitle("お絵かきプログラム");
    this.setSize(1000, 800);
    this.addMouseMotionListener(this);
    this.addMouseListener(this);
    panel = new DrawPanel();

    panelToolbar = new JPanel();
    panelCurrentColor = new JPanel();
    panelBtn = new JPanel();
    panelToolbar.setLayout(new FlowLayout());
    panelBtn.setLayout(new FlowLayout());
    panelToolbar.setBackground(new Color(233,218,203));
    panelBtn.setBackground(new Color(233,218,203));
    panelCurrentColor.setPreferredSize(new Dimension(100, 50));
    panelCurrentColor.setBackground(panel.getCurrentColor());
    /*
		for (int i=1; i<=3; i++) {
			JButton btn = new JButton("NORTH" + i);
			btn.addActionListener(this);
			panelBtn.add(btn);
		}
    */
    JLabel function1 = new JLabel("　 現在の機能");
    //function1.setOpaque(true);
    //function1.setPreferredSize(new Dimension(100, 25));
    //function1.setHorizontalAlignment(JLabel.CENTER);
    JLabel function2 = new JLabel("　　" + currentPenType);
    //function2.setOpaque(true);
    //function2.setPreferredSize(new Dimension(100, 25));
    //function2.setHorizontalAlignment(JLabel.CENTER);
    JPanel panelFunction = new JPanel();
    panelFunction.setPreferredSize(new Dimension(100, 40));
    panelFunction.setLayout(new BoxLayout(panelFunction, BoxLayout.Y_AXIS));
    panelFunction.add(function1);
    panelFunction.add(function2);
    panelToolbar.add(panelFunction);

    ImageIcon iconColorPalette = new ImageIcon("images/ColorPalette.png");
    JButton btnColorPalette = new JButton("色の選択");
    btnColorPalette.setIcon(iconColorPalette);
    btnColorPalette.setActionCommand("ColorPalette"); //紐付ける
    btnColorPalette.addActionListener(this);
    panelBtn.add(btnColorPalette);

    ImageIcon iconPen2 = new ImageIcon("images/Pen.png");
    JButton btnPen2 = new JButton("ペン");
    btnPen2.setIcon(iconPen2);
    btnPen2.setActionCommand("Normal"); //紐付ける
    btnPen2.addActionListener(this);
    panelBtn.add(btnPen2);

    ImageIcon iconErase2 = new ImageIcon("images/Erase.png");
    JButton btnErase2 = new JButton("消しゴム");
    btnErase2.setIcon(iconErase2);
    btnErase2.setActionCommand("EraserPen"); //紐付ける
    btnErase2.addActionListener(this);
    panelBtn.add(btnErase2);

    this.initSlider();

    ImageIcon iconBack = new ImageIcon("images/Back.png");
    JButton btnBack = new JButton("戻る");
    btnBack.setIcon(iconBack);
    btnBack.setActionCommand("Back"); //紐付ける
    btnBack.addActionListener(this);
    panelBtn.add(btnBack);


    panelToolbar.add(panelCurrentColor);
    panelToolbar.add(panelBtn);
    this.getContentPane().add(panel);
    this.getContentPane().add(panelToolbar,BorderLayout.SOUTH); //パネル自体を東西南北に指定する
    this.initMenu(); //メニューバーをつける
    this.setVisible(true);
    //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowClosing());
  }


  private void addMenuItem(JMenu targetMenu, String itemName, String actionName, ActionListener listener) {
    ImageIcon icon = new ImageIcon("images/" + actionName + ".png");
    JMenuItem menuItem = new JMenuItem(itemName);
    menuItem.setIcon(icon);
    menuItem.setActionCommand(actionName);
    menuItem.addActionListener(listener);
    targetMenu.add(menuItem);
  }
  private void initMenu(){
    JMenuBar menubar = new JMenuBar();

    ImageIcon iconFile = new ImageIcon("images/File.png");
    JMenu menuFile = new JMenu("ファイル");
    menuFile.setIcon(iconFile);
    this.addMenuItem(menuFile,"新規作成","New",this);
    this.addMenuItem(menuFile,"開く","Open",this);
    this.addMenuItem(menuFile,"保存する","Save",this);
    menubar.add(menuFile);
    this.setJMenuBar(menubar);

    ImageIcon iconPen = new ImageIcon("images/Pen.png");
    JMenu menuPen = new JMenu("ペンの設定");
    menuPen.setIcon(iconPen);
    ImageIcon iconColor = new ImageIcon("images/Color.png");
    JMenu menuColor = new JMenu ("色");
    menuColor.setIcon(iconColor);
    this.addMenuItem(menuColor,"黒","Black",this);
    this.addMenuItem(menuColor,"青","Blue",this);
    this.addMenuItem(menuColor,"黃","Yellow",this);
    this.addMenuItem(menuColor,"緑","Green",this);
    this.addMenuItem(menuColor,"赤","Red",this);
    this.addMenuItem(menuColor,"その他の色","ColorPalette",this);
    this.addMenuItem(menuColor,"ランダム","Random",this);
    menuPen.add(menuColor);
    ImageIcon iconWidth = new ImageIcon("images/Width.png");
    JMenu menuWidth = new JMenu("太さ");
    menuWidth.setIcon(iconWidth);
    this.addMenuItem(menuWidth, "  1", "width1", this);
    this.addMenuItem(menuWidth, "  5", "width5", this);
    this.addMenuItem(menuWidth, " 10", "width10", this);
    this.addMenuItem(menuWidth, " 20", "width20", this);
    menuPen.add(menuWidth);
    menubar.add(menuPen);
    ImageIcon iconType = new ImageIcon("images/Type.png");
    JMenu menuType = new JMenu("種類");
    menuType.setIcon(iconType);
    this.addMenuItem(menuType, "普通", "Normal", this);
    this.addMenuItem(menuType, "ドット", "Dot", this);
    this.addMenuItem(menuType, "筆", "Brush", this);
    this.addMenuItem(menuType, "レインボーペン", "Rainbow", this);
    menuPen.add(menuType);
    ImageIcon iconKorokoro = new ImageIcon("images/Korokoro.png");
    JMenu menuKorokoro = new JMenu("コロコロペン");
    menuKorokoro.setIcon(iconKorokoro);
    this.addMenuItem(menuKorokoro, "花びら", "Spring", this);
    this.addMenuItem(menuKorokoro, "葉っぱ", "Summer", this);
    this.addMenuItem(menuKorokoro, "落ち葉", "Fall", this);
    this.addMenuItem(menuKorokoro, "雪の結晶", "Winter", this);
    this.addMenuItem(menuKorokoro, "文字", "StringKorokoro", this);
    this.addMenuItem(menuKorokoro, "画像から選択する", "PictureKorokoro", this);
    menuPen.add(menuKorokoro);
    this.setJMenuBar(menubar);

    ImageIcon iconInsert = new ImageIcon("images/Insert.png");
    JMenu menuInsert = new JMenu("挿入");
    menuInsert.setIcon(iconInsert);
    ImageIcon iconShape = new ImageIcon("images/Shape.png");
    JMenu menuShape = new JMenu("図形");
    menuShape.setIcon(iconShape);
    this.addMenuItem(menuShape,"直線","StraightLine",this);
    this.addMenuItem(menuShape,"四角形","Rectangle",this);
    this.addMenuItem(menuShape,"四角形(塗りつぶし)","FillRectangle",this);
    this.addMenuItem(menuShape,"楕円","Oval",this);
    this.addMenuItem(menuShape,"楕円(塗りつぶし)","FillOval",this);
    menuInsert.add(menuShape);
    ImageIcon iconStamp = new ImageIcon("images/Stamp.png");
    JMenu menuStamp = new JMenu("スタンプ");
    menuStamp.setIcon(iconStamp);
    this.addMenuItem(menuStamp, "ハート", "Heart", this);
    this.addMenuItem(menuStamp, "星", "Star", this);
    this.addMenuItem(menuStamp, "画像から選択する", "Picture", this);
    menuInsert.add(menuStamp);
    menubar.add(menuInsert);
    this.addMenuItem(menuInsert,"文字","stampString",this);
    menubar.add(menuInsert);
    this.setJMenuBar(menubar);

    ImageIcon iconTool = new ImageIcon("images/Tool.png");
    JMenu menuTool = new JMenu("ツール");
    menuTool.setIcon(iconTool);
    this.addMenuItem(menuTool,"コピー","Copy",this);
    this.addMenuItem(menuTool,"カット","Cut",this);
    this.addMenuItem(menuTool,"ペースト","Paste",this);
    this.addMenuItem(menuTool,"スポイト","Dropper",this);
    this.addMenuItem(menuTool,"塗りつぶし","Fill",this);
    menubar.add(menuTool);
    this.setJMenuBar(menubar);

    ImageIcon iconClear = new ImageIcon("images/AllClear.png");
    JMenu menuClear = new JMenu("全消去");
    menuClear.setIcon(iconClear);
    this.addMenuItem(menuClear, "全消去", "AllClear", this);
    menubar.add(menuClear);
    this.setJMenuBar(menubar);
  }

  private void initSlider() {
    panelSlider = new JPanel();
    panelSlider.setBackground(Color.white); //new Color(233,218,203)
    slider = new JSlider(0,30,5);
    slider.setPaintTicks(true);
    slider.setMinorTickSpacing(5);
    slider.setMajorTickSpacing(10);
    slider.addChangeListener(this);
    slider.setPaintLabels(true);
    panelSlider.add(slider);
    panelBtn.add(panelSlider);
  }



  /* オーバーライド */
  public void actionPerformed(ActionEvent e){
    String command = e.getActionCommand();
    if(command != null){
      int ret;
      if(command == "New"){ //新規作成
        ret = JOptionPane.showConfirmDialog(null, "新規作成する前に保存しますか？", "新規作成", JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE);
        if(ret == JOptionPane.YES_OPTION){
          int returnVal_new = fileChooser.showSaveDialog(this);
          if (returnVal_new == JFileChooser.APPROVE_OPTION) {
            panel.saveFile(fileChooser.getSelectedFile());
          }
          panel.AllClear();
        }
        else{
          panel.AllClear();
        }
      }
      else if(command == "Open"){ //開く
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          panel.openFile(fileChooser.getSelectedFile());
        }
      }
      else if(command == "Save"){ //保存する
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          panel.saveFile(fileChooser.getSelectedFile());
        }
      }


      else if(command == "Black"){
        panel.setPenColor(Color.black);
        panelCurrentColor.setBackground(panel.getCurrentColor());
        panelCurrentColor.repaint();
      }
      else if(command == "Blue"){
        panel.setPenColor(Color.blue);
        panelCurrentColor.setBackground(panel.getCurrentColor());
        panelCurrentColor.repaint();
      }
      else if(command == "Yellow"){
        panel.setPenColor(Color.yellow);
        panelCurrentColor.setBackground(panel.getCurrentColor());
        panelCurrentColor.repaint();
      }
      else if(command == "Green"){
        panel.setPenColor(Color.green);
        panelCurrentColor.setBackground(panel.getCurrentColor());
        panelCurrentColor.repaint();
      }
      else if(command == "Red"){
        panel.setPenColor(Color.red);
        panelCurrentColor.setBackground(panel.getCurrentColor());
        panelCurrentColor.repaint();
      }
      else if(command == "ColorPalette"){ //その他の色
        JColorChooser colorchooser = new JColorChooser();
        Color color = colorchooser.showDialog(this,"choose a color",panel.getCurrentColor()); //パレットの初期値を現在の色にする
        //button.setBackground(color);
        //button.setForeground(color); //ボタンの文字の色が変わる
        panel.setPenColor(color);
        panelCurrentColor.setBackground(panel.getCurrentColor());
        panelCurrentColor.repaint();
      }
      else if(command == "Random"){ //ランダム
        Random r = new Random();
        panel.setPenColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        panelCurrentColor.setBackground(panel.getCurrentColor());
        panelCurrentColor.repaint();
      }

      else if(command == "width1"){
        panel.setPenWidth(1.0f);
      }
      else if(command == "width5"){
        panel.setPenWidth(5.0f);
      }
      else if(command == "width10"){
        panel.setPenWidth(10.0f);
      }
      else if(command == "width20"){
        panel.setPenWidth(20.0f);
      }

      else if(command == "Normal"){
        currentPenType = "Normal";
      }
      else if(command == "Dot"){
        currentPenType = "Dot";
      }
      else if(command == "Brush"){
        currentPenType = "Brush";
        panel.setPenWidth(20);
      }
      else if(command == "Rainbow"){
        currentPenType = "Rainbow";
      }

      else if(command == "Spring"){
        currentPenType = "Spring";
      }
      else if(command == "Summer"){
        currentPenType = "Summer";
      }
      else if(command == "Fall"){
        currentPenType = "Fall";
      }
      else if(command == "Winter"){
        currentPenType = "Winter";
      }
      else if(command == "StringKorokoro"){
        currentPenType = "StringKorokoro";
        ch = JOptionPane.showInputDialog(null, "文字を入力してください", "文字入力", JOptionPane.PLAIN_MESSAGE);

        if(ch == null || ch.length() == 0) {
          JOptionPane.showMessageDialog(this, "正しい文字入力がされませんでした", "エラー", JOptionPane.WARNING_MESSAGE);
        }
        else{ //正しく文字入力がされた場合
          JFrame frame2 = new JFrame();
          String choice[] = {"普通","太字","斜体"};
          choiceNum = JOptionPane.showOptionDialog(frame2, "フォントの種類を選んでください", "フォントの種類", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choice, choice[0]);
          if(choiceNum == -1){
            JOptionPane.showMessageDialog(this, "キャンセルされました", "キャンセル", JOptionPane.WARNING_MESSAGE);
          }
        }
      }
      else if(command == "PictureKorokoro"){
        currentPenType = "PictureKorokoro";
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          path = fileChooser.getSelectedFile().getPath(); //選択した画像の絶対パス
          //System.out.println(fileChooser.getSelectedFile().getPath());
        }
      }


      else if(command == "StraightLine"){
        currentPenType = "StraightLine";
      }
      else if(command == "Rectangle"){
        currentPenType = "Rectangle";
      }
      else if(command == "FillRectangle"){
        currentPenType = "FillRectangle";
      }
      else if(command == "Oval"){
        currentPenType = "Oval";
      }
      else if(command == "FillOval"){
        currentPenType = "FillOval";
      }

      else if(command == "Heart"){
        currentPenType = "Heart";
      }
      else if(command == "Star"){
        currentPenType = "Star";
      }
      else if(command == "Picture"){
        currentPenType = "Picture";
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          path = fileChooser.getSelectedFile().getPath(); //選択した画像の絶対パス
          //System.out.println(fileChooser.getSelectedFile().getPath());
        }
      }

      else if(command == "stampString"){
        currentPenType = "stampString";
        stampString = JOptionPane.showInputDialog(null, "文字を入力してください", "文字入力", JOptionPane.PLAIN_MESSAGE);

        if(stampString == null || stampString.length() == 0) {
          JOptionPane.showMessageDialog(this, "正しい文字入力がされませんでした", "エラー", JOptionPane.WARNING_MESSAGE);
        }
        else{ //正しく文字入力がされた場合
          JFrame frame2 = new JFrame();
          String choice[] = {"普通","太字","斜体"};
          choiceNum = JOptionPane.showOptionDialog(frame2, "フォントの種類を選んでください", "フォントの種類", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choice, choice[0]);
          if(choiceNum == -1){
            JOptionPane.showMessageDialog(this, "キャンセルされました", "キャンセル", JOptionPane.WARNING_MESSAGE);
          }
        }
      }


      else if(command == "Copy"){
        currentPenType = "Copy";
      }
      else if(command == "Cut"){
        currentPenType = "Cut";
      }
      else if(command == "Paste"){
        currentPenType = "Paste";
      }
      else if(command == "Dropper"){
        currentPenType = "Dropper";
      }
      else if(command == "Fill"){
        currentPenType = "Fill";
      }


      else if(command == "AllClear"){
        //JFrame frame = new JFrame();
        //JOptionPane.showMessageDialog(frame, "用紙を白紙に戻しますか？");
        ret = JOptionPane.showConfirmDialog(null, "用紙を白紙に戻しますか？", "最終確認", JOptionPane.YES_NO_OPTION , JOptionPane.QUESTION_MESSAGE);
        if(ret == JOptionPane.YES_OPTION){
          panel.AllClear();

          try{
            panel.prepareBack();
          }
          catch(AWTException aecp){
            aecp.printStackTrace();
          }
          catch (Exception ecp) {
            ecp.printStackTrace();
          }
        }
      }


      /* ツールバー */
      else if(command == "Pen"){ //ペン
        currentPenType = "Normal";
      }
      else if(command == "EraserPen"){ //消しゴム
        currentPenType = "Normal";
        panel.setPenColor(Color.white);
      }
      else if(command == "Back"){ //戻る
        if(not_twice) not_twice = false;
        else not_twice = true;

        try{
          if(!not_twice){
            panel.back();
          }
          else{
            JOptionPane.showMessageDialog(this, "これ以上戻れません", "エラー", JOptionPane.WARNING_MESSAGE);
          }
        }
        catch(AWTException aecp){
          aecp.printStackTrace();
        }
        catch (Exception ecp) {
          ecp.printStackTrace();
        }
      }

    }
  }

  public void mouseDragged(MouseEvent e) { //MouseMotionListenerをimplementsしたのでオーバーライドする
    not_twice = true;
    if(currentPenType.equals("Normal") || currentPenType.equals("Brush")){
      newx = e.getX();
      newy = e.getY();
      //panel.createBuffer(panel.getWidth(),panel.getHeight());
      panel.drawLine(oldx,oldy,newx,newy);
      oldx = newx;
      oldy = newy;
    }
    else if(currentPenType.equals("Dot")){
      newx = e.getX();
      newy = e.getY();
      //panel.createBuffer(panel.getWidth(),panel.getHeight());
      panel.drawDot(oldx,oldy,newx,newy);
      oldx = newx;
      oldy = newy;
    }
    else if(currentPenType.equals("Rainbow")){
      newx = e.getX();
      newy = e.getY();
      //panel.createBuffer(panel.getWidth(),panel.getHeight());
      panel.drawRainbow(oldx,oldy,newx,newy);
      panelCurrentColor.setBackground(panel.getCurrentColor());
      panelCurrentColor.repaint();
      oldx = newx;
      oldy = newy;
    }
    else if(currentPenType.equals("Spring") || currentPenType.equals("Summer") || currentPenType.equals("Fall") || currentPenType.equals("Winter") || currentPenType.equals("PictureKorokoro")){
      newx = e.getX();
      newy = e.getY();
      //panel.createBuffer(panel.getWidth(),panel.getHeight());
      if(i % 10 == 0){
        if(currentPenType.equals("Spring")){
          panel.stamp("images/Spring.png", e.getX(), e.getY());
        }
        else if(currentPenType.equals("Summer")){
          panel.stamp("images/Summer.png", e.getX(), e.getY());
        }
        else if(currentPenType.equals("Fall")){
          panel.stamp("images/Fall.png", e.getX(), e.getY());
        }
        else if(currentPenType.equals("Winter")){
          panel.stamp("images/Winter.png", e.getX(), e.getY());
        }
        else if(currentPenType.equals("PictureKorokoro")){
          panel.stamp(path, e.getX(), e.getY());
        }
      }
      i++;
      if(i >= 1000){
        i = 0;
      }
    }
    else if(currentPenType.equals("StringKorokoro")){
      newx = e.getX();
      newy = e.getY();
      //panel.createBuffer(panel.getWidth(),panel.getHeight());
      if(i % 10 == 0){
        panel.drawStringKorokoro(newx, newy, ch.charAt(korokoroCount % ch.length()), choiceNum);
        korokoroCount++;
      }

      i++;
      if(i >= 1000){
        i = 0;
      }
      if(korokoroCount >= 1000){
        korokoroCount = 1000 % ch.length();
      }
    }
  }
  public void mouseMoved(MouseEvent e) { //MouseMotionListenerをimplementsしたのでオーバーライドする
    //System.out.println("mouse moved!");
    if(currentPenType.equals("Brush") && writing == true){
      newx = e.getX();
      newy = e.getY();
      panel.drawBrush(oldx,oldy,newx,newy);
      oldx = newx;
      oldy = newy;
      if(panel.getCurrentWidth() <= 0.1){
        panel.setPenWidth(20.0f);
        writing = false;
      }
    }
  }

  public void mouseClicked(MouseEvent e){ //コンポーネント上でマウスボタンをクリック (押してから離す) したときに呼び出される
    //System.out.println("mouse clicked!");
    not_twice = true;

    if(currentPenType.equals("Heart")){
      panel.stamp("images/Heart.png", e.getX(), e.getY());
    }
    else if(currentPenType.equals("Star")){
      panel.stamp("images/Star.png", e.getX(), e.getY());
    }
    else if(currentPenType.equals("Picture")){
      panel.stamp(path, e.getX(), e.getY());
    }
    else if(currentPenType.equals("Dropper")){
      panel.pixelColor(e.getX(), e.getY());
    }
    else if(currentPenType.equals("Fill")){
      panel.fill();
    }
    else if(currentPenType.equals("stampString")){
      newx = e.getX();
      newy = e.getY();
      //panel.createBuffer(panel.getWidth(),panel.getHeight());
      panel.drawStampString(newx, newy, stampString, choiceNum);
    }
    else if(currentPenType.equals("Paste")){
      if(panel.getCopyCount() == 0){
        JOptionPane.showMessageDialog(this, "コピーされていない状態でペーストされました", "エラー", JOptionPane.WARNING_MESSAGE);
      }
      else{
        pastex = e.getX();
        pastey = e.getY();
        panel.paste(pastex, pastey);
      }
    }

    if(currentPenType.equals("Heart") || currentPenType.equals("Star") || currentPenType.equals("Picture")
    || currentPenType.equals("Fill")
    || currentPenType.equals("stampString") || currentPenType.equals("Paste")){
      try{
        panel.prepareBack();
      }
      catch(AWTException aecp){
        aecp.printStackTrace();
      }
      catch (Exception ecp) {
        ecp.printStackTrace();
      }
    }
  }
  public void mouseEntered(MouseEvent e){ //コンポーネントにマウスが入ると呼び出される
    //System.out.println("mouse entered!");
    fileChooser = new JFileChooser(); //どこか最初に実行される場所でJFileChooserのインスタンスを作っておく

    if(first){
      panel.AllClear();
      first = false;
    }

    System.out.println(panel.getBackCount());
    if(panel.getBackCount() == 0){
      try{
        panel.prepareBack();
      }
      catch(AWTException aecp){
        aecp.printStackTrace();
      }
      catch (Exception ecp) {
        ecp.printStackTrace();
      }
    }
  }
  public void mouseExited(MouseEvent e) { //コンポーネントからマウスが出ると呼び出される
    //System.out.println("mouse exited!");
  }
  public void mousePressed(MouseEvent e) { //コンポーネント上でマウスボタンが押されると呼び出される
    if(currentPenType.equals("Normal") || currentPenType.equals("Dot") || currentPenType.equals("Brush") || currentPenType.equals("Rainbow")
    || currentPenType.equals("StraightLine") || currentPenType.equals("Rectangle") || currentPenType.equals("FillRectangle") || currentPenType.equals("Oval") || currentPenType.equals("FillOval")){
      //一筆書きを解消する
      oldx = e.getX();
      oldy = e.getY();
    }
    else if(currentPenType.equals("StringKorokoro")){
      korokoroCount = 0;
    }
    else if(currentPenType.equals("Copy") || currentPenType.equals("Cut")){
      copyx1 = e.getX();
      copyy1 = e.getY();
    }
  }
  public void mouseReleased(MouseEvent e) { //コンポーネント上でマウスボタンが離されると呼び出される
    //System.out.println("mouse released!");
    if(currentPenType.equals("Rectangle")){
      newx = e.getX();
      newy = e.getY();
      panel.drawRectangle(oldx,oldy,newx,newy);
    }
    else if(currentPenType.equals("FillRectangle")){
      newx = e.getX();
      newy = e.getY();
      panel.drawFillRectangle(oldx,oldy,newx,newy);
    }
    else if(currentPenType.equals("Oval")){
      newx = e.getX();
      newy = e.getY();
      panel.drawOval(oldx,oldy,newx,newy);
    }
    else if(currentPenType.equals("FillOval")){
      newx = e.getX();
      newy = e.getY();
      panel.drawFillOval(oldx,oldy,newx,newy);
    }
    else if(currentPenType.equals("StraightLine")){
      newx = e.getX();
      newy = e.getY();
      panel.drawLine(oldx,oldy,newx,newy);
    }
    else if(currentPenType.equals("Brush")){
      writing = true;
      //一筆書きを解消する
      oldx = e.getX();
      oldy = e.getY();
    }
    else if(currentPenType.equals("Copy")){
      copyx2 = e.getX();
      copyy2 = e.getY();

      try{
        panel.copy(copyx1, copyy1, copyx2, copyy2);
      }
      catch(AWTException aecp){
        aecp.printStackTrace();
      }
      catch (Exception ecp) {
        ecp.printStackTrace();
      }
    }
    else if(currentPenType.equals("Cut")){
      copyx2 = e.getX();
      copyy2 = e.getY();

      try{
        panel.copy(copyx1, copyy1, copyx2, copyy2);
        panel.cut(copyx1, copyy1, copyx2, copyy2); //copyした部分を白い四角形で塗りつぶす
      }
      catch(AWTException aecp){
        aecp.printStackTrace();
      }
      catch (Exception ecp) {
        ecp.printStackTrace();
      }
    }

    try{
      panel.prepareBack();
    }
    catch(AWTException aecp){
      aecp.printStackTrace();
    }
    catch (Exception ecp) {
      ecp.printStackTrace();
    }
  }

  public void stateChanged(ChangeEvent e) {
    panel.setPenWidth(slider.getValue());
  }

  class WindowClosing extends WindowAdapter{
    public void windowClosing(WindowEvent e) {
      int ans = JOptionPane.showConfirmDialog(SimpleDraw.this, "本当に終了しますか?");
      //System.out.println(ans);
      if(ans == JOptionPane.YES_OPTION) {
        //System.out.println("プログラムによる終了処理の実行");
        if(panel.getCopyCount() != 0){
          File copyFile = new File("copy" + panel.getCopyCount() + ".png");
          //System.out.println("copy" + panel.getCopyCount() + ".png");
          copyFile.delete();
        }
        if(panel.getBackCount() != 0){
          File backFile1 = new File("back" + (panel.getBackCount()-1) + ".png");
          File backFile2 = new File("back" + panel.getBackCount() + ".png");
          if(backFile1.exists()){
            backFile1.delete();
          }
          if(backFile2.exists()){
            backFile2.delete();
          }

        }
        System.exit(0);
      }
    }
  }


  public static void main(String[] args) {
    SimpleDraw frame = new SimpleDraw();
  }

}
