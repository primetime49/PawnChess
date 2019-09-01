import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class KotakCatur extends JButton{
    
    private String nama;
    private Pawn pawn;
    private boolean isPawn = false;
    private final ImageIcon kosong = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
    
    public KotakCatur() {
        super();
        this.nama = null;
        this.pawn = null;
    }
    
    public Pawn getPawn() {
        return this.pawn;
    }
    
    public void setPawn(Pawn pawn) {
        super.setIcon(new ImageIcon(pawn.getImage()));
        this.pawn = pawn;
        isPawn = true;
        this.pawn.setLokasi(this.nama);
    }
    
    public void removePawn() {
        super.setIcon(kosong);
        //this.pawn.setLokasi("");
        this.pawn = null;
        isPawn = false;
    }
    
    public String getNama() {
        return this.nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public boolean isPawned() {
        return isPawn;
    }
    
    public boolean isBlackSq() {
        if ((Integer.parseInt(nama.substring(0,1)) % 2) == 0) {
            if ((Integer.parseInt(nama.substring(2,3)) % 2) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            if ((Integer.parseInt(nama.substring(2,3)) % 2) == 0) {
                return true;
            } else {
                return false;
            }
        }
    }
    
}
    
    