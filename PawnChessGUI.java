//Source: https://stackoverflow.com/questions/21142686/making-a-robust-resizable-swing-chess-gui with modifications

import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;

public class PawnChessGUI {

    AI smart = new AI();
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private Image[] PawnImages = new Image[2];
    private PapanCatur chessBoard;
    private static final String COLS = "ABCDEFGH";
    private boolean pawnClicked;
    public static final int BLACK = 0, WHITE = 1;
    private KotakCatur asalnya;
    private int warna;
    private int row;
    private int kolom;
    private int jalan = 1;
    private int limit;
    private Random random = new Random();
    private int rand = random.nextInt(2);

    PawnChessGUI() {
        initializeGui();
    }

    public final void initializeGui() {
        // create the images for the chess pieces
        createImages();

        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("Hard") {

            public void actionPerformed(ActionEvent e) {
                setupNewGame();
                limit = 1;
                randomStart();
            }
        };
        tools.add(newGameAction);
        Action newGameAction2 = new AbstractAction("KiloHard") {

            public void actionPerformed(ActionEvent e) {
                setupNewGame();
                limit = 2;
                randomStart();
            }
        };
        tools.add(newGameAction2);
        Action newGameAction3 = new AbstractAction("MegaHard") {

            public void actionPerformed(ActionEvent e) {
                setupNewGame();
                limit = 3;
                //System.out.println(rand);
                randomStart();
            }
        };
        tools.add(newGameAction3);
        
        Action newGameAction4 = new AbstractAction("GigaHard") {

            public void actionPerformed(ActionEvent e) {
                setupNewGame();
                limit = 4;
                randomStart();
            }
        };
        tools.add(newGameAction4);
        
        Action newGameAction5 = new AbstractAction("TeraHard") {

            public void actionPerformed(ActionEvent e) {
                setupNewGame();
                limit = 5;
                randomStart();
            }
        };
        tools.add(newGameAction5);
        
        ActionListener kotakListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                
                System.out.println("Clicked Square ID: "+actionEvent.getActionCommand());
                KotakCatur chosen = (KotakCatur) actionEvent.getSource();
                
                if (!pawnClicked) {
                    if (chosen.isPawned()) {
                        asalnya = (KotakCatur) actionEvent.getSource();
                        
                        if (chessBoard.colorCheck(jalan, chosen)[0] != 99) {
                            int[] pos = chessBoard.colorCheck(jalan, chosen);
                            row = pos[0];
                            kolom = pos[1];
                            pawnClicked = true;
                        }
                        
                        if (pawnClicked) {
                            if (chessBoard.canMove(row,kolom,jalan)) {
                                asalnya.setBackground(Color.GREEN);
                                warna = jalan;
                                if (jalan == 0) {
                                    jalan = 1;
                                } else {
                                    jalan = 0;
                                }
                            }
                            else {
                                pawnClicked = false;
                            }
                        }
                        
                        System.out.println("Valid pawn color? "+pawnClicked);
                        System.out.println("Pawn ID: "+chosen.getPawn().getNama());
                        System.out.println("Pawn Location: "+chosen.getPawn().getLokasi());
                    }
                }
                else {
                    if (chosen.getNama().equals(asalnya.getNama())) {
                        if (asalnya.isBlackSq()) {
                            asalnya.setBackground(Color.BLACK);
                        } else {
                            asalnya.setBackground(Color.WHITE);
                        }
                        jalan = 1;
                        pawnClicked = false;
                    }
                    else if (chessBoard.validMove(row,kolom,chosen,warna)) {
                        chessBoard.move(asalnya,chosen);
                        if (asalnya.isBlackSq()) {
                            asalnya.setBackground(Color.BLACK);
                        } else {
                            asalnya.setBackground(Color.WHITE);
                        }
                        /*if (chosen.getNama().substring(0,1).equals(finish)) {
                            JOptionPane.showMessageDialog(null, "selesai", "done", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }*/
                        pawnClicked = false;
                        if (chosen.getNama().substring(0,1).equals("0")) {
                            JOptionPane.showMessageDialog(null, "CHAMPION", "Result", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }
                        
                        moveAI();
                        checkUserStuck();
                    }
                }
            }
        };
        
        gui.add(new JLabel("?"), BorderLayout.LINE_START);

        chessBoard = new PapanCatur(new GridLayout(0, 9)) {

            /**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstaint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };
        chessBoard.setBorder(new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
                ));
        // Set the BG to be ochre
        Color ochre = new Color(204,119,34);
        chessBoard.setBackground(ochre);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(ochre);
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        // create the chess board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int ii = 0; ii < chessBoard.getSqSize(); ii++) {
            for (int jj = 0; jj < chessBoard.getSqSize(); jj++) {
                KotakCatur b = new KotakCatur();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1)
                        //) {
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }
                chessBoard.setKotak(ii,jj,b);
            }
        }

        /*
         * fill the chess board
         */
        chessBoard.add(new JLabel(""));
        // fill the top row
        for (int ii = 0; ii < 8; ii++) {
            chessBoard.add(
                    new JLabel(COLS.substring(ii, ii + 1),
                    SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                switch (jj) {
                    case 0:
                        chessBoard.add(new JLabel("" + (9-(ii + 1)),
                                SwingConstants.CENTER));
                    default:
                        chessBoard.addToPanel(ii,jj);
                }
            }
        }
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                chessBoard.configKotak(kotakListener,ii,jj);
            }
        }
        
    }
    
    public void checkUserStuck() {
        boolean isStuck = true;
        for (int i = 0; i < 8; i++) {
            Pawn user = chessBoard.getPawn(1,i);
            if (!user.isEaten()) {
                int userow = Integer.parseInt(user.getLokasi().substring(0,1));
                int userkol = Integer.parseInt(user.getLokasi().substring(2,3));
                if (chessBoard.canMove(userow,userkol,jalan)) {
                    isStuck = false;
                }
            }
        }
        if (isStuck) {
            JOptionPane.showMessageDialog(null, "You are stuck", "Result", JOptionPane.INFORMATION_MESSAGE);
            moveAI();
        }
    }
    
    public void randomStart() {
        if (rand == 1) {
            JOptionPane.showMessageDialog(null, "AI goes first", "Random Start", JOptionPane.INFORMATION_MESSAGE);
            moveAI();
        } else {
            JOptionPane.showMessageDialog(null, "You go first", "Random Start", JOptionPane.INFORMATION_MESSAGE);
        }
    }
        
    
    public void moveAI() {
        GameRule gamerule = new GameRule(chessBoard);
        MinimaxScore hasil = smart.minimax('b', gamerule, 0, limit, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println("AI move ID: "+hasil.movement[1]+" to "+hasil.movement[0]);
        System.out.println("AI best move score: "+hasil.score);
        int bar = hasil.movement[0]/8;
        int kol = hasil.movement[0]%8;
        int ba = hasil.movement[1]/8;
        int ko = hasil.movement[1]%8;
        if (hasil.movement[1] == 0 && hasil.movement[0] == 0) {
            JOptionPane.showMessageDialog(null, "AI is stuck", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            chessBoard.move(chessBoard.getKotak(ba,ko),chessBoard.getKotak(bar,kol));
            if (bar == 7) {
                JOptionPane.showMessageDialog(null, "LOSER", "Result", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
        jalan = 1;
    }

    public final JComponent getGui() {
        return gui;
    }

    private final void createImages() {
        try {
            //URL url = new URL("http://i.stack.imgur.com/memI0.png");
            File fileku = new File("memI0.png");
            BufferedImage bi = ImageIO.read(fileku);
            for (int ii = 0; ii < 2; ii++) {
                PawnImages[ii] = bi.getSubimage(
                        320, ii * 64, 64, 64);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Initializes the icons of the initial chess board piece places
     */
    private final void setupNewGame() {
        
        // set up the black pieces
        for (int ii = 0; ii < 8; ii++) {
            String nama = "" + 0 + ii;
            Pawn black = new Pawn(nama, PawnImages[0]);
            KotakCatur curr = chessBoard.getKotak(0,ii);
            curr.setPawn(black);
            chessBoard.setKotak(0,ii,curr);
            chessBoard.setPawn(0,ii,black);
            //Pawns[0][ii] = black;
            
        }
        // set up the white pieces
        for (int ii = 0; ii < 8; ii++) {
            String nama = "" + 1 + ii;
            Pawn white = new Pawn(nama, PawnImages[1]);
            KotakCatur curr = chessBoard.getKotak(7,ii);
            curr.setPawn(white);
            chessBoard.setKotak(7,ii,curr);
            chessBoard.setPawn(1,ii,white);
            //Pawns[1][ii] = white;
        }
    }
    
    public static void main(String[] args) {
        Runnable r = new Runnable() {

            public void run() {
                PawnChessGUI cg = new PawnChessGUI();

                JFrame f = new JFrame("ChessChamp");
                f.add(cg.getGui());
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See https://stackoverflow.com/a/7143398/418556 for demo.
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
                
            }
            
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
        
    }
}