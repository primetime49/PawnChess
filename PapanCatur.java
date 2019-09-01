import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class PapanCatur extends JPanel{
    
    private KotakCatur[][] squares;
    private Pawn[][] Pawns = new Pawn[2][8];
    
    public PapanCatur(LayoutManager layout) {
        super(layout);
        squares = new KotakCatur[8][8];
    }
    
    public void setPawn(int warna, int urutan, Pawn put) {
        Pawns[warna][urutan] = put;
    }
    
    public Pawn getPawn(int warna, int urutan) {
        return Pawns[warna][urutan];
    }
    
    public int getSqSize() {
        return this.squares.length;
    }
    
    public void setKotak(int ii, int jj, KotakCatur b) {
        squares[ii][jj] = b;
    }
    
    public KotakCatur getKotak(int ii, int jj) {
        return squares[ii][jj];
    }
    
    public void addToPanel(int ii, int jj) {
        super.add(squares[ii][jj]);
    }
    
    public void configKotak(ActionListener lis, int ii, int jj) {
        squares[ii][jj].addActionListener(lis);
        squares[ii][jj].setNama(""+ii+","+jj);
        squares[ii][jj].setActionCommand(ii+","+jj);
    }
    
    public KotakCatur getLurus(int row, int kolom, int jalan) {
        if (jalan == 1) {
            return squares[row-1][kolom];
        } else {
            return squares[row+1][kolom];
        }
    }
    
    public KotakCatur getLeft(int row, int kolom, int jalan) {
        if (jalan == 1) {
            if (squares[row][kolom].getNama().substring(2,3).equals("0")) {
                return squares[row-1][kolom+1];
            }
            else {
                return squares[row-1][kolom-1];
            }
        }
        else {
            if (squares[row][kolom].getNama().substring(2,3).equals("0")) {
                return squares[row+1][kolom+1];
            }
            else {
                return squares[row+1][kolom-1];
            }
        }
    }
    
    public KotakCatur getRight(int row, int kolom, int jalan) {
        if (jalan == 1) {
            if (squares[row][kolom].getNama().substring(2,3).equals("7")) {
                return squares[row-1][kolom-1];
            }
            else {
                return squares[row-1][kolom+1];
            }
        }
        else {
            if (squares[row][kolom].getNama().substring(2,3).equals("7")) {
                return squares[row+1][kolom-1];
            }
            else {
                return squares[row+1][kolom+1];
            }
        }
    }
    
    public boolean pawnValid(int jalan, KotakCatur chosen) {
        for (int jj = 0; jj < 8; jj++) {
            if (Pawns[jalan][jj].getNama().equals(chosen.getPawn().getNama())) {
                return true;
            }
        }
        return false;
    }
    
    public int[] colorCheck(int jalan, KotakCatur chosen) {
        if (pawnValid(jalan, chosen)) {
            int[] hasil = new int[2];
            String one = chosen.getNama();
            hasil[0] = Integer.parseInt(one.substring(0,1));
            hasil[1] = Integer.parseInt(one.substring(2,3));
            return hasil;
        } else {
            int[] hasil = new int[1];
            hasil[0] = 99;
            return hasil;
        }
    }
    
    public boolean canMove(int row, int kolom, int jalan) {
        if (jalan == 0) {
            //Assign variable kemungkinan kotak berikutnya
            KotakCatur bawah = this.getLurus(row,kolom,jalan);
            KotakCatur bawahkiri = this.getLeft(row,kolom,jalan);
            KotakCatur bawahkanan = this.getRight(row,kolom,jalan);
            
            //Jika tujuan jalan pion valid
            if ((!bawah.isPawned()) || (bawahkiri.isPawned() && !bawahkiri.getPawn().isBlack()) || (bawahkanan.isPawned() && !bawahkanan.getPawn().isBlack())) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            //Assign variable kemungkinan kotak berikutnya
            KotakCatur atas = this.getLurus(row,kolom,jalan);
            KotakCatur ataskiri = this.getLeft(row,kolom,jalan);
            KotakCatur ataskanan = this.getRight(row,kolom,jalan);
            
            //Jika tujuan jalan pion valid
            if ((!atas.isPawned()) || (ataskiri.isPawned() && ataskiri.getPawn().isBlack()) || (ataskanan.isPawned() && ataskanan.getPawn().isBlack())) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    public boolean checkWin(int jalan) {
        String finish = "";
        if (jalan == 0) {
            finish = "7";
        } else {
            finish = "0";
        }
        for (int i = 0; i < 8; i++) {
            if (Pawns[jalan][i].getLokasi().substring(0,1).equals(finish)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean validMove(int row, int kolom, KotakCatur chosen, int warna) {
        int next = 0;
        boolean colr = false;
        if (warna == 0) {
            next = row+1;
            colr = (chosen.isPawned() && !chosen.getPawn().isBlack());
        } else {
            next = row-1;
            colr = (chosen.isPawned() && chosen.getPawn().isBlack());
        }
        if (Integer.parseInt(chosen.getNama().substring(0,1)) == next) {
            if (Integer.parseInt(chosen.getNama().substring(2,3)) == kolom) {
                if (!chosen.isPawned()) {
                    return true;
                }
            }
            else if ((Integer.parseInt(chosen.getNama().substring(2,3)) == kolom+1) || (Integer.parseInt(chosen.getNama().substring(2,3)) == kolom-1)) {
                if (colr) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void move(KotakCatur awal, KotakCatur akhir) {
        if (akhir.isPawned()) {
            akhir.getPawn().setEaten();
        }
        System.out.println("Move: "+awal.getNama()+" to "+akhir.getNama());
        akhir.setPawn(awal.getPawn());
        awal.removePawn();
        
    }
    
}