import javax.swing.*;


public class FloppyBird {

    public static void main(String[] args)throws Exception {
        
    
        JFrame Myframe = new JFrame("Floppy Bird");
        Myframe.setVisible(true);
        Myframe.setSize(360,640);
        Myframe.setLocationRelativeTo(null);
        Myframe.setResizable(false);
        Myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GamePanel Mygame = new GamePanel();
        Myframe.add(Mygame);
        Myframe.pack();
        Mygame.requestFocus();
        Myframe.setVisible(true);

    }
}
