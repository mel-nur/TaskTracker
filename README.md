# 🚀 Task Tracker CLI

Bu proje, komut satırı üzerinden görevlerinizi yönetmenizi sağlayan basit ve etkili bir CLI (Command Line Interface) uygulamasıdır. Java ve Spring Boot ekosisteminin gücüyle geliştirilmiş olup, verileri yerel bir `tasks.json` dosyasında saklar.

## 🛠 Özellikler

* **Görev Ekleme:** Yeni görevler oluşturun.
* **Güncelleme & Silme:** Mevcut görevlerin açıklamalarını değiştirin veya görevleri silin.
* **Durum Yönetimi:** Görevleri `todo`, `in-progress` veya `done` olarak işaretleyin.
* **Filtreleme:** Tüm görevleri veya sadece belirli bir durumdaki (örn. tamamlananlar) görevleri listeleyin.
* **JSON Tabanlı Depolama:** Verileriniz kalıcı olarak yerel dosya sisteminde tutulur.

## 📋 Gereksinimler

* **Java 17** veya üzeri bir sürüm.
* **Maven** (Bağımlılık yönetimi için).

## 🚀 Kurulum ve Çalıştırma

1.  **Projeyi Klonlayın:**
    ```bash
    git clone https://github.com/mel-nur/TaskTracker.git
    cd TaskTracker
    ```

2.  **Projeyi Derleyin:**
    ```bash
    ./mvnw clean package
    ```

3.  **Uygulamayı Çalıştırın:**
    *(Örnek komutlar)*
    ```bash
    # Yeni görev ekle
    java -jar target/task-tracker.jar add "Market alışverişi yap"

    # Görevleri listele
    java -jar target/task-tracker.jar list

    # Görevi güncelleniyor olarak işaretle
    java -jar target/task-tracker.jar mark-in-progress 1
    ```

## 💻 Kullanım Örnekleri

| Komut | Açıklama |
| :--- | :--- |
| `add "Açıklama"` | Listeye yeni bir görev ekler. |
| `update <id> "Yeni Açıklama"` | Belirtilen ID'ye sahip görevi günceller. |
| `delete <id>` | Görevi listeden siler. |
| `list` | Tüm görevleri listeler. |
| `list done` | Sadece tamamlanmış görevleri gösterir. |

## 🏗 Teknik Detaylar

* **Framework:** Spring Boot
* **Veri Formatı:** JSON
* **Kütüphaneler:** Jackson (JSON serileştirme için)

---
*Bu proje bir Roadmap.sh projesidir.*
