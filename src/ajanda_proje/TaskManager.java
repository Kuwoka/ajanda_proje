package ajanda_proje;


import java.util.List;
import java.util.Scanner;

public class TaskManager {

    private DatabaseHelper dbHelper;

    public TaskManager() {
        this.dbHelper = new DatabaseHelper();
    }

    public void addTask() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Görev adı: ");
        String taskName = scanner.nextLine();

        System.out.print("Tarih (GG/AA/YYYY): ");
        String tarih = scanner.nextLine();

        System.out.print("Durum (Tamamlandı/Beklemede): ");
        String durum = scanner.nextLine();

        Task task = new Task(0, taskName, tarih, durum);
        dbHelper.addTaskToDatabase(task);

        System.out.println("Görev başarıyla eklendi!");
    }

    public void listTasks() {
        List<Task> tasks = dbHelper.getAllTasksFromDatabase();

        if (tasks.isEmpty()) {
            System.out.println("Hiç görev bulunamadı.");
        } else {
            System.out.println("\nTüm Görevler:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    public void updateTask() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Güncellemek istediğiniz görevin ID'si: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Task task = dbHelper.findTaskById(id);

        if (task == null) {
            System.out.println("Bu ID'ye sahip bir görev bulunamadı.");
            return;
        }

        System.out.print("Yeni görev adı (boş bırakılırsa değişmez): ");
        String taskName = scanner.nextLine();
        if (!taskName.isEmpty()) {
            task.setTaskname(taskName);
        }

        System.out.print("Yeni tarih (YYYY-MM-DD, boş bırakılırsa değişmez): ");
        String tarih = scanner.nextLine();
        if (!tarih.isEmpty()) {
            task.setTarih(tarih);
        }

        System.out.print("Yeni durum (boş bırakılırsa değişmez): ");
        String durum = scanner.nextLine();
        if (!durum.isEmpty()) {
            task.setDurum(durum);
        }

        dbHelper.updateTaskInDatabase(task);
        System.out.println("Görev başarıyla güncellendi!");
    }

    public void deleteTask() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Silmek istediğiniz görevin ID'si: ");
        int id = scanner.nextInt();

        Task task = dbHelper.findTaskById(id);

        if (task == null) {
            System.out.println("Bu ID'ye sahip bir görev bulunamadı.");
            return;
        }

        dbHelper.deleteTaskFromDatabase(id);
        System.out.println("Görev başarıyla silindi!");
    }
    
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nKişisel Ajanda Yönetimi:");
            System.out.println("1. Görev Ekle");
            System.out.println("2. Tüm Görevleri Listele");
            System.out.println("3. Görev Güncelle");
            System.out.println("4. Görev Sil");
            System.out.println("5. Çıkış");

            System.out.print("Seçiminiz: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    listTasks();
                    break;
                case 3:
                    updateTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    System.out.println("Çıkış yapılıyor...");
                    return;
              
                default:
                    System.out.println("Geçersiz seçim. Tekrar deneyin.");
            }
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.run();
    }
}

