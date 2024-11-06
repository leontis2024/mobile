package com.aula.leontis;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.aula.leontis.activities.TelaInfoObra;

import com.aula.leontis.models.obra.Obra;
import com.aula.leontis.services.ObraService;
import com.aula.leontis.utilities.MetodosAux;

import java.util.List;
import java.util.Random;public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MetodosAux aux = new MetodosAux();
        aux.agendarNotificacaoHoraEmHora(context);
        // Chama o método para buscar todas as obras
        ObraService obraService = new ObraService();
        obraService.buscarTodasobras(context, new ObraService.ObraCallback() {
            @Override
            public void onObrasReceived(List<Obra> obras) {
                if (!obras.isEmpty()) {
                    // Seleciona uma obra aleatoriamente
                    Random random = new Random();
                    Obra obraSelecionada = obras.get(random.nextInt(obras.size()));

                    // Cria a notificação com a obra selecionada
                    criarNotificacao(context, obraSelecionada);
                }
            }
        });
    }

    // Método que cria e exibe a notificação
    private void criarNotificacao(Context context, Obra obra) {
        String channelId = "channel_id";
        String channelName = "channel_name";
        NotificationManager manager = context.getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel(channelId);
            if (channel == null) {
                channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
        }

        // Intent para abrir a activity com detalhes da obra selecionada
        Intent intent = new Intent(context, TelaInfoObra.class);
        intent.putExtra("id", obra.getId()); // Passa o ID da obra para a activity
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Criar a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo_clara)
                .setContentTitle("Obra do dia!")
                .setContentText("Confira a obra " + obra.getNomeObra() + " selecionada para hoje!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
