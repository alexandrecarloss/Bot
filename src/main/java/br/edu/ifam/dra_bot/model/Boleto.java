package br.edu.ifam.dra_bot.model;

public class Boleto {


    private String codigoBarras;
    private String instituicaoRecebedora;
    private float valor;

    public Boleto() {
    }

    public Boleto(String codigoBarras, String instituicaoRecebedora, float valor) {
        this.codigoBarras = codigoBarras;
        this.instituicaoRecebedora = instituicaoRecebedora;
        this.valor = valor;
    }


    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getInstituicaoRecebedora() {
        return instituicaoRecebedora;
    }

    public void setInstituicaoRecebedora(String instituicaoRecebedora) {
        this.instituicaoRecebedora = instituicaoRecebedora;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
