package ajanda_proje;

public class Task {

	private int id;
	private String taskname;
	private String tarih;
	private String durum;
	
	public Task(int id, String taskname, String tarih, String durum) {
		this.id = id;
		this.taskname = taskname;
		this.tarih = tarih;
		this.durum = durum;
				
	}

	public int getid() {
		return id;
	}

	public void setid(int id) {
		this.id = id;
	}

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getTarih() {
		return tarih;
	}

	public void setTarih(String tarih) {
		this.tarih = tarih;
	}

	public String getDurum() {
		return durum;
	}

	public void setDurum(String durum) {
		this.durum = durum;
	}
	
}
