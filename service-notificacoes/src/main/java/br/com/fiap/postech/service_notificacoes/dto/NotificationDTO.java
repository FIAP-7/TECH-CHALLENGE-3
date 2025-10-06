package br.com.fiap.postech.service_notificacoes.dto;

import java.time.LocalDateTime;

public record NotificationDTO(
        String notificationId,
        String title,
        String message,
        LocalDateTime sentAt
) {}
