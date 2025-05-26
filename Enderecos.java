import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public void CadastroEndereco() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Quantos endereços você precisa inserir? ");
        int escolha = scanner.nextInt();

        for (int i = 0; i < escolha; i++) {
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

            System.out.println("Insira o complemento de seuendereço. Obs não é obrigatório: ");
            setComplemento(scanner.nextLine());
            confirma.Confirmar();

            System.out.println("Insira o tipo de seu endereço(Comercial, Residencia, Entrega, correspondência e etc): ");
            setTipopEndereco(scanner.nextLine());
            confirma.Confirmar();

            ImprimirEndereco();
        }
    }

    public void ImprimirEndereco() {
        try (FileWriter fileWriter = new FileWriter("Output.txt");
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("CEP: " + cep + "; Logadouro: " + logadouro + "; Número: " + numero + "; Complemento: " + complemento + "; Tipo: " + tipopEndereco + ";");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    public void AtualizarEndereco() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o Id que deseja alterar o endereço:");
        int idBusca = scanner.nextInt();
        scanner.nextLine();

        boolean encontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader("Output.txt"))) {
            StringBuilder novoConteudo = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Id: " + idBusca + ";")) {
                    encontrado = true;
                    System.out.println("Endereço encontrato: \nCEP: " + cep + ";\n Logadouro: " + logadouro + ";\n Número: " + numero + "; \n Complemento: " + complemento + ";\n Tipo: " + tipopEndereco + ";");
                    System.out.println("O que deseja alterar?");

                }
            }

            if (!encontrado) {
                System.out.println("Nenhum endereço encontrado atrelado ao" + idBusca + ".");
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar cadastro: " + e.getMessage());
        }
        }
    }



