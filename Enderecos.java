import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Enderecos extends Pessoa{
    private int cep;
    private String logadouro;
    private int numero;
    private String complemento;
    private String tipopEndereco;
    private int VerificadorTipo;

    public void setCep(int cep) {this.cep = cep;}

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

    public void setVerificadorTipo(int verificadorTipo) {
        VerificadorTipo = verificadorTipo;
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
            Pessoa confirma = new Pessoa();
            System.out.println("Insira o Cep: ");
            setCep(scanner.nextInt());
            confirma.Confirmar();

            System.out.println("Insira o endereço (sem o número da casa): ");
            setLogadouro(scanner.nextLine());
            confirma.Confirmar();

            System.out.println("Insira o número da casa: ");
            setNumero(scanner.nextInt());
            confirma.Confirmar();

            System.out.println("Insira o complemento de seu endereço. Obs não é obrigatório: ");
            setComplemento(scanner.nextLine());
            confirma.Confirmar();

            System.out.println("Insira o tipo de seu endereço(Comercial, Residencia, Entrega e correspondência): ");
            String VerificarTipoEndereco = scanner.nextLine().toLowerCase();
            confirma.Confirmar();
            if (VerificarTipoEndereco  == "comercial" && VerificarTipoEndereco  == "residencial" && VerificarTipoEndereco  == "entrega" && VerificarTipoEndereco  == "correspondência"){
                setTipopEndereco(VerificarTipoEndereco);
                setVerificadorTipo(1);
            }

            try (FileWriter fileWriter = new FileWriter("EnderecoOutput.txt");
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write("Cliente Id: "+getId_pessoa() +"; CEP: " + cep + "; Logadouro: " + logadouro + "; Número: " + numero + "; Complemento: " + complemento + "; Tipo: " + tipopEndereco + ";");
                bufferedWriter.newLine();
            } catch (IOException e) {
                System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
            }

        }
    }
    }


