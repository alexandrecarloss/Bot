package br.edu.ifam.dra_bot;

// Importações necessárias para o funcionamento do bot
import br.edu.ifam.dra_bot.model.Boleto;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe principal do bot que estende TelegramLongPollingBot
 * e implementa as funcionalidades básicas de um bot Telegram.
 */
public class DraBot extends TelegramLongPollingBot {

    private List<Boleto> boletos = new ArrayList<>();
    private boolean aguardandoCodigoBarras = false;
    private boolean aguardandoInstituicao = false;
    private boolean aguardandoValor = false;
    private Boleto boletoParcial = new Boleto();

    /**
     * Retorna o nome do bot definido em DadosBot.
     * @return Nome do bot.
     */
    @Override
    public String getBotUsername(){
        return DadosBot.BOT_USER_NAME;
    }

    /**
     * Retorna o token do bot definido em DadosBot.
     * @return Token do bot.
     */
    @Override
    public String getBotToken(){
        return DadosBot.BOT_TOKEN;
    }

    /**
     * Método auxiliar para obter a data atual no formato "dd/MM/yyyy".
     * @return String contendo a data atual.
     */
    private String getData(){
        // Define o formato da data
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Retorna a data formatada
        return "A data atual é: " + sdf.format(new Date());
    }

    /**
     * Método responsável por gerar a resposta para o usuário com base no comando recebido.
     * @param update Objeto Update recebido do Telegram.
     * @return Objeto SendMessage com a resposta.
     */
    private SendMessage processaBoleto(Update update, String chatId) {
        String textoMensagem = update.getMessage().getText();

        if (aguardandoCodigoBarras) {
            boletoParcial.setCodigoBarras(textoMensagem);
            aguardandoCodigoBarras = false;
            aguardandoInstituicao = true;
            return SendMessage.builder().text("Agora, informe a instituição recebedora do boleto").chatId(chatId).build();
        } else if (aguardandoInstituicao) {
            boletoParcial.setInstituicaoRecebedora(textoMensagem);
            aguardandoInstituicao = false;
            aguardandoValor = true;
            return SendMessage.builder().text("Por fim, informe o valor do boleto").chatId(chatId).build();
        } else if (aguardandoValor) {
            try {
                boletoParcial.setValor(Float.parseFloat(textoMensagem.replace(",",".")));
                boletos.add(boletoParcial);

                System.out.println("Código de Barras: " + boletoParcial.getCodigoBarras());
                System.out.println("Instituição: " + boletoParcial.getInstituicaoRecebedora());
                System.out.println("Valor: " + boletoParcial.getValor());

                boletoParcial = new Boleto();
                aguardandoValor = false;

                return SendMessage.builder().text("Boleto cadastrado com sucesso!").chatId(chatId).build();
            } catch (Exception e) {
                e.printStackTrace();
                return SendMessage.builder().text("Valor inválido! Por favor, informe um valor numérico para o boleto").chatId(chatId).build();
            }
        } else {
            aguardandoCodigoBarras = true;
            return SendMessage.builder().text("Por favor, informe o código de barras do boleto").chatId(chatId).build();
        }
    }

    //TODO: Implementar método para retornar o valor total de boletos
    public String valorTotalBoletos(){

       return "";
    }

    public SendMessage responder(Update update) {
        // Obtém o texto da mensagem e converte para minúsculas
        String textoMensagem = update.getMessage().getText().toLowerCase();
        System.out.println("A mensagem enviada é: " + textoMensagem);
        // Obtém o ID do chat
        String chatId = update.getMessage().getChatId().toString();
        System.out.println("O chatId é: " + chatId);

        // Verifica se estamos aguardando entrada de dados do boleto
        if (aguardandoCodigoBarras || aguardandoInstituicao || aguardandoValor) {
            return processaBoleto(update, chatId);
        }

        // Variável para armazenar a resposta
        String resposta = "";

        // Verifica o comando recebido e define a resposta
        if (textoMensagem.startsWith("/data")) {
            // Retorna a data atual
            resposta = getData();

        } else if (textoMensagem.startsWith("/cadastroboleto")) {

            return processaBoleto(update, chatId);

        } else if (textoMensagem.startsWith("/help")) {
            // Retorna os comandos disponíveis
            resposta = "Utilize um dos comandos:\n /data\n /cadastroBoleto \n /valorTotal";

        } else if (textoMensagem.startsWith("/start")) {
            // Mensagem de boas-vindas
            resposta = "Olá! Sou o Bot DRA 1.\n" +
                    "Sou um exemplo de aplicação de Bot no Telegram com Java.\n" +
                    "No momento tenho duas funções implementadas:.\n" +
                    "Para ver a data atual, digite /data.\n" +
                    "Para cadastrar um boleto para controle, digite /cadastroBoleto \n" +
                    "Para saber o valor total dos boletos, digite /valorTotal";
        } else {
            // Mensagem padrão para comandos desconhecidos
            resposta = "Não entendi!\nDigite /help para ver os comandos disponíveis.";
        }

        // Retorna o objeto SendMessage configurado
        return SendMessage.builder().text(resposta).chatId(chatId).build();
    }


    /**
     * Método que é chamado automaticamente quando o bot recebe uma mensagem.
     * @param update Objeto Update contendo os dados da mensagem recebida.
     */
    public void onUpdateReceived(Update update) {
        // Verifica se a mensagem contém texto
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Gera a resposta com base no comando
            SendMessage mensagem = responder(update);

            try {
                // Envia a mensagem de resposta
                execute(mensagem);
            } catch (TelegramApiException e) {
                // Trata possíveis exceções durante o envio da mensagem
                e.printStackTrace();
            }
        }
    }
}
