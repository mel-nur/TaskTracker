package com.mel.TaskTracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

//id: Görev için benzersiz bir tanımlayıcı
//
//descriptionGörevin kısa bir açıklaması
//
//status: Görevin durumu ( todo, in-progress, done)
//
//createdAtGörevin oluşturulduğu tarih ve saat
//
//updatedAtGörevin en son güncellendiği tarih ve saat
public class Task {
    private Long id;
    private String description;
    private TaskStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public Task() {}

    public Task(Long id, String description){
        this.id = id;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String toString() {
        return String.format("[%d] %-10s | %-12s | Oluşturuldu: %s | Güncellendi: %s",
                id,
                status.getDisplayName(),
                truncate(description, 30),
                createdAt != null ? createdAt.toString().replace("T", " ").substring(0, 16) : "-",
                updatedAt != null ? updatedAt.toString().replace("T", " ").substring(0, 16) : "-");

    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() > maxLen ? text.substring(0, maxLen - 3) + "..." : text;

    }
}
