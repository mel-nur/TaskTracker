package com.mel.TaskTracker.cli;

import com.mel.TaskTracker.model.Task;
import com.mel.TaskTracker.model.TaskStatus;
import com.mel.TaskTracker.service.TaskService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskCLI {

    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_CYAN   = "\u001B[36m";
    private static final String ANSI_BOLD   = "\u001B[1m";

    private final TaskService taskService;

    public TaskCLI(TaskService taskService) {
        this.taskService = taskService;
    }

    public void run(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0].toLowerCase();

        try {
            switch (command) {
                case "add"             -> handleAdd(args);
                case "update"          -> handleUpdate(args);
                case "delete"          -> handleDelete(args);
                case "mark-in-progress"-> handleMarkInProgress(args);
                case "mark-done"       -> handleMarkDone(args);
                case "list"            -> handleList(args);
                case "help", "--help", "-h" -> printHelp();
                default -> {
                    printError("Bilinmeyen komut: '" + command + "'");
                    printHelp();
                }
            }
        } catch (NumberFormatException e) {
            printError("Geçersiz ID formatı. ID bir sayı olmalıdır.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }


    private void handleAdd(String[] args) {
        requireArgs(args, 2, "Kullanım: add \"<açıklama>\"");
        String description = args[1];
        Task task = taskService.addTask(description);
        printSuccess("Görev başarıyla eklendi (ID: " + task.getId() + ")");
    }

    private void handleUpdate(String[] args) {
        requireArgs(args, 3, "Kullanım: update <id> \"<yeni açıklama>\"");
        Long id = parseLong(args[1]);
        String description = args[2];
        Task task = taskService.updateTask(id, description);
        printSuccess("Görev güncellendi (ID: " + task.getId() + ")");
    }

    private void handleDelete(String[] args) {
        requireArgs(args, 2, "Kullanım: delete <id>");
        Long id = parseLong(args[1]);
        taskService.deleteTask(id);
        printSuccess("Görev silindi (ID: " + id + ")");
    }

    private void handleMarkInProgress(String[] args) {
        requireArgs(args, 2, "Kullanım: mark-in-progress <id>");
        Long id = parseLong(args[1]);
        Task task = taskService.markInProgress(id);
        printSuccess("Görev 'Devam Ediyor' olarak işaretlendi (ID: " + task.getId() + ")");
    }

    private void handleMarkDone(String[] args) {
        requireArgs(args, 2, "Kullanım: mark-done <id>");
        Long id = parseLong(args[1]);
        Task task = taskService.markDone(id);
        printSuccess("Görev 'Tamamlandı' olarak işaretlendi (ID: " + task.getId() + ")");
    }

    private void handleList(String[] args) {
        List<Task> tasks;
        String filterLabel;

        if (args.length >= 2) {
            String filter = args[1].toLowerCase();
            TaskStatus status = switch (filter) {
                case "done"        -> TaskStatus.DONE;
                case "todo"        -> TaskStatus.TODO;
                case "in-progress" -> TaskStatus.IN_PROGRESS;
                default -> throw new IllegalArgumentException(
                        "Geçersiz filtre: '" + filter + "'. Geçerli değerler: done, todo, in-progress");
            };
            tasks = taskService.listByStatus(status);
            filterLabel = status.getDisplayName();
        } else {
            tasks = taskService.listAll();
            filterLabel = "Tümü";
        }

        printTaskList(tasks, filterLabel);
    }


    private void printTaskList(List<Task> tasks, String label) {
        System.out.println();
        System.out.println(ANSI_BOLD + ANSI_CYAN + "═══ Görevler [" + label + "] ══════════════════════════════" + ANSI_RESET);

        if (tasks.isEmpty()) {
            System.out.println(ANSI_YELLOW + "  (Bu filtreyle eşleşen görev bulunamadı)" + ANSI_RESET);
        } else {
            System.out.println(ANSI_BOLD +
                    String.format("  %-4s %-12s %-30s %-17s %-17s",
                            "ID", "DURUM", "AÇIKLAMA", "OLUŞTURULDU", "GÜNCELLENDİ")
                    + ANSI_RESET);
            System.out.println("  " + "─".repeat(84));

            for (Task task : tasks) {
                String statusColor = switch (task.getStatus()) {
                    case DONE        -> ANSI_GREEN;
                    case IN_PROGRESS -> ANSI_YELLOW;
                    case TODO        -> ANSI_RESET;
                };

                System.out.println(statusColor + String.format("  %-4d %-12s %-30s %-17s %-17s",
                        task.getId(),
                        task.getStatus().getDisplayName(),
                        truncate(task.getDescription(), 29),
                        formatDate(task.getCreatedAt()),
                        formatDate(task.getUpdatedAt()))
                        + ANSI_RESET);
            }
        }

        System.out.println("  Toplam: " + tasks.size() + " görev");
        System.out.println();
    }

    private void printSuccess(String message) {
        System.out.println(ANSI_GREEN + "✓ " + message + ANSI_RESET);
    }

    private void printError(String message) {
        System.err.println(ANSI_RED + "✗ Hata: " + message + ANSI_RESET);
    }

    private void printHelp() {
        System.out.println();
        System.out.println(ANSI_BOLD + ANSI_CYAN + "╔══════════════════════════════════════╗");
        System.out.println("║      Task Tracker CLI - Komutlar     ║");
        System.out.println("╚══════════════════════════════════════╝" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BOLD + "  GÖREV YÖNETİMİ:" + ANSI_RESET);
        System.out.println("  add \"<açıklama>\"              Yeni görev ekle");
        System.out.println("  update <id> \"<açıklama>\"     Görevi güncelle");
        System.out.println("  delete <id>                   Görevi sil");
        System.out.println();
        System.out.println(ANSI_BOLD + "  DURUM GÜNCELLEME:" + ANSI_RESET);
        System.out.println("  mark-in-progress <id>         Devam ediyor olarak işaretle");
        System.out.println("  mark-done <id>                Tamamlandı olarak işaretle");
        System.out.println();
        System.out.println(ANSI_BOLD + "  LİSTELEME:" + ANSI_RESET);
        System.out.println("  list                          Tüm görevleri listele");
        System.out.println("  list todo                     Yapılacakları listele");
        System.out.println("  list in-progress              Devam edenleri listele");
        System.out.println("  list done                     Tamamlananları listele");
        System.out.println();
        System.out.println(ANSI_BOLD + "  ÖRNEK KULLANIM:" + ANSI_RESET);
        System.out.println("  java -jar task-tracker.jar add \"Market alışverişi yap\"");
        System.out.println("  java -jar task-tracker.jar mark-in-progress 1");
        System.out.println("  java -jar task-tracker.jar list");
        System.out.println();
    }


    private void requireArgs(String[] args, int minCount, String usage) {
        if (args.length < minCount) {
            throw new IllegalArgumentException("Yetersiz argüman. " + usage);
        }
    }

    private Long parseLong(String value) {
        try {
            long id = Long.parseLong(value);
            if (id <= 0) throw new NumberFormatException();
            return id;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Geçersiz ID: '" + value + "'. Pozitif bir sayı giriniz.");
        }
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max - 2) + ".." : text;
    }

    private String formatDate(java.time.LocalDateTime dt) {
        if (dt == null) return "-";
        return dt.toString().replace("T", " ").substring(0, 16);
    }
}
