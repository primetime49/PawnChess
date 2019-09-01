import java.*;
import java.awt.*;
import java.io.Serializable;

public class Pawn{
    
    private String nama;
    private Image pion;
    private boolean eat;
    private String lokasi;
    
    public Pawn(String nama, Image pion) {
        this.nama = nama;
        this.pion = pion;
    }
    
    public String getNama() {
        return this.nama;
    }
    
    public Image getImage() {
        return this.pion;
    }
	
	public boolean isBlack() {
		if (nama.substring(0,1).equals("0")) {
			return true;
		}
		else {
			return false;
		}
	}
    
    public void setEaten() {
        this.eat = true;
    }
    
    public boolean isEaten() {
        return this.eat;
    }
    
    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
    
    public String getLokasi() {
        return this.lokasi;
    }
    
}