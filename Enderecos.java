import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Enderecos extends Pessoa{
    private int cep;
    private String logadouro;
    private int numero;
    private String complemento;
    private String tipoEndereco;
    private int VerificadorTipoEndereco;
    protected int VinculoIdpessoaEndereco;

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

    public void setTipoEndereco(String tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public void setVerificadorTipoEndereco(int verificadorTipoEndereco) {
        VerificadorTipoEndereco = verificadorTipoEndereco;
    }

    public void setVinculoIdpessoaEndereco(int vinculoIdpessoaEndereco) {
        VinculoIdpessoaEndereco = vinculoIdpessoaEndereco;
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

    public String getTipoEndereco() {
        return tipoEndereco;
    }

    public void CadastroEndereco(){
        System.out.println("Quantos endereços você precisa inserir? ");
        int escolha = scanner.nextInt();

        for (int i = 0; i < escolha; i++){
            Pessoa confirma = new Pessoa();
            int  VerificadorCep = 0;
            do {
                System.out.println("Insira o"+ (i+1) + "° Cep: ");
                int NumCep = scanner.nextInt();
                scanner.nextLine();
                confirma.Confirmar();
                if (NumCep > 9999999 && NumCep <= 99999999){
                    setCep(NumCep);
                    VerificadorCep = 1;
                }
                else{System.out.println("Cep incorreto, tente de novo. ");}
            }
            while(VerificadorCep != 1);


            System.out.println("Insira o"+ (i+1) +"° endereço (sem o número da casa): ");
            setLogadouro(scanner.nextLine());
            confirma.Confirmar();

            System.out.println("Insira o "+ (i+1) + "° número da casa: ");
            setNumero(scanner.nextInt());
            scanner.nextLine();
            confirma.Confirmar();

            System.out.println("Insira o "+ (i+1) + "° complemento de seu endereço. Obs não é obrigatório: ");
            setComplemento(scanner.nextLine());
            confirma.Confirmar();

            do {
                System.out.println("Insira o tipo de seu "+ (i+1) + "°endereço(Comercial, Residencial, Entrega e correspondência): ");
                String VerificarTipoEndereco = scanner.nextLine().toLowerCase();
                confirma.Confirmar();
                if (VerificarTipoEndereco.contains("comercial")  || VerificarTipoEndereco.contains("residencial")  || VerificarTipoEndereco.contains("entrega")  || VerificarTipoEndereco.contains("correspondência")){
                    setTipoEndereco(VerificarTipoEndereco);
                    setVerificadorTipoEndereco(1);
                }
                else{
                    System.out.println("Tipo de endereço inserido incorretamente, Tente de novo.");}
            }
            while(VerificadorTipoEndereco != 1);
            ImprimirCadastro();
        }
        GravarCadastroLog();

    }
    @Override
    public void ImprimirCadastro(){
        try (FileWriter fileWriter = new FileWriter("EnderecoOutput.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("Cliente Id: "+ VinculoIdpessoaEndereco +"; CEP: " + cep + "; Logadouro: " + logadouro + "; Número: " + numero + "; Complemento: " + complemento + "; Tipo: " + tipoEndereco + ";");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
    @Override
    public void GravarCadastroLog(){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("Endereço "+ tipoEndereco + " Cadastrado com sucesso, verifique o arquivo EnderecoOutput para visualizar seu cadastro. ");
        try (FileWriter fileWriter = new FileWriter("Log.txt", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("["+ timestamp + "] " + "usuário admin cadastrou endereço "  + tipoEndereco + " no banco de dados.");
            bufferedWriter.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }
}


