package org.emmanuel.chewallet.dtos.payments.webHookDto;

public record MercadoPagoWebhookDto(
        String action,
        String api_version,
        DataDto data,
        String date_created,
        String id,
        boolean live_mode,
        String type,
        Long user_id
) {
    public record DataDto(
            String id
    ) {
    }

}
