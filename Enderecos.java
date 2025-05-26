import java.util.Scanner;

public class Enderecos {
    private int cep;
    private String logadouro;
    private int numero;
    private String complemento;
    private String tipopEndereco;

    public void setCep(int cep) {
        this.cep = cep;
    }

    public void setLogadouro(String logadouro) {
        this.logadouro = logadouro;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public void setTipopEndereco(String tipopEndereco) {
        this.tipopEndereco = tipopEndereco;
    }

    public int getCep() {
        return cep;
    }

    public String getLogadouro() {
        return logadouro;
    }

    public int getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getTipopEndereco() {
        return tipopEndereco;
    }

    public void CadastroEndereco(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Quantos endereços você precisa inserir? ");
        int escolha = scanner.nextInt();

        for (int i = 0; i < escolha; i++){

        }

    }
}
